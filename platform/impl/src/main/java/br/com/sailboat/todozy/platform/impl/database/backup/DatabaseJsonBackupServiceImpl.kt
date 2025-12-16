package br.com.sailboat.todozy.platform.impl.database.backup

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.util.Base64
import br.com.sailboat.todozy.utility.android.sqlite.DatabaseJsonBackupService
import br.com.sailboat.todozy.utility.android.sqlite.DatabaseOpenHelperService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

private const val BACKUP_FORMAT = "todozy-db-backup"
private const val BACKUP_VERSION = 1
private const val JSON_TYPE_KEY = "__type"
private const val JSON_TYPE_BLOB = "blob"
private const val JSON_BLOB_BASE64_KEY = "base64"

internal class DatabaseJsonBackupServiceImpl(
    private val context: Context,
    private val databaseOpenHelperService: DatabaseOpenHelperService,
) : DatabaseJsonBackupService {
    override suspend fun exportToJson(uri: Uri) = withContext(Dispatchers.IO) {
        val database = databaseOpenHelperService.readable

        val tableNames = getUserTableNames(database)
        val tablesJson = JSONObject()

        for (tableName in tableNames) {
            val cursor = database.rawQuery("SELECT * FROM \"$tableName\"", null)
            cursor.use {
                tablesJson.put(tableName, cursorToJsonArray(cursor))
            }
        }

        val root =
            JSONObject().apply {
                put("format", BACKUP_FORMAT)
                put("version", BACKUP_VERSION)
                put("databaseVersion", database.version)
                put("exportedAtEpochMillis", System.currentTimeMillis())
                put("tables", tablesJson)
            }

        val outputStream =
            context.contentResolver.openOutputStream(uri)
                ?: error("Could not open output stream for uri=$uri")

        outputStream.use { stream ->
            stream.writer(Charsets.UTF_8).use { writer ->
                writer.write(root.toString(2))
            }
        }
    }

    override suspend fun importFromJson(uri: Uri) = withContext(Dispatchers.IO) {
        val inputStream =
            context.contentResolver.openInputStream(uri)
                ?: error("Could not open input stream for uri=$uri")

        val jsonString = inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
        val root = JSONObject(jsonString)

        val format = root.optString("format")
        require(format == BACKUP_FORMAT) { "Invalid backup format: $format" }

        val version = root.optInt("version")
        require(version == BACKUP_VERSION) { "Unsupported backup version: $version" }

        val tablesJson = root.getJSONObject("tables")

        val database = databaseOpenHelperService.writable
        val tableNames = getUserTableNames(database)
        val insertOrder = sortTablesByForeignKeys(database, tableNames)
        val deleteOrder = insertOrder.asReversed()

        database.beginTransactionNonExclusive()
        try {
            for (tableName in deleteOrder) {
                database.delete(tableName, null, null)
            }

            for (tableName in insertOrder) {
                val rows = tablesJson.optJSONArray(tableName) ?: continue
                insertRows(database, tableName, rows)
            }

            database.setTransactionSuccessful()
        } finally {
            database.endTransaction()
        }
    }

    private fun getUserTableNames(database: SQLiteDatabase): List<String> {
        val cursor =
            database.rawQuery(
                """
                SELECT name FROM sqlite_master
                WHERE type='table'
                  AND name NOT LIKE 'android_%'
                  AND name NOT LIKE 'sqlite_%'
                ORDER BY name
                """.trimIndent(),
                null,
            )

        return cursor.use {
            buildList {
                while (cursor.moveToNext()) {
                    add(cursor.getString(0))
                }
            }
        }
    }

    private fun sortTablesByForeignKeys(
        database: SQLiteDatabase,
        tableNames: List<String>,
    ): List<String> {
        val tableSet = tableNames.toSet()
        val dependencies =
            tableNames.associateWith { tableName ->
                getForeignKeyDependencies(database, tableName).filter { it in tableSet }.toSet()
            }

        return topologicalSort(dependencies)
    }

    private fun getForeignKeyDependencies(
        database: SQLiteDatabase,
        tableName: String,
    ): List<String> {
        val cursor = database.rawQuery("PRAGMA foreign_key_list(\"$tableName\")", null)

        return cursor.use {
            val referencedTableIndex = cursor.getColumnIndex("table").takeIf { it >= 0 } ?: return emptyList()
            buildList {
                while (cursor.moveToNext()) {
                    add(cursor.getString(referencedTableIndex))
                }
            }
        }
    }

    private fun topologicalSort(dependencies: Map<String, Set<String>>): List<String> {
        val pendingDependencies = dependencies.mapValues { it.value.toMutableSet() }.toMutableMap()
        val resolved: MutableList<String> = mutableListOf()
        val ready = java.util.PriorityQueue<String>()

        for ((table, deps) in pendingDependencies) {
            if (deps.isEmpty()) {
                ready.add(table)
            }
        }

        while (ready.isNotEmpty()) {
            val table = ready.remove()
            resolved.add(table)

            for ((otherTable, deps) in pendingDependencies) {
                if (deps.remove(table) && deps.isEmpty() && otherTable !in resolved) {
                    ready.add(otherTable)
                }
            }
        }

        check(resolved.size == pendingDependencies.size) {
            "Cyclic table dependencies detected: $pendingDependencies"
        }

        return resolved
    }

    private fun cursorToJsonArray(cursor: Cursor): JSONArray {
        val jsonArray = JSONArray()
        while (cursor.moveToNext()) {
            jsonArray.put(cursorRowToJsonObject(cursor))
        }
        return jsonArray
    }

    private fun cursorRowToJsonObject(cursor: Cursor): JSONObject {
        val jsonObject = JSONObject()
        for (index in 0 until cursor.columnCount) {
            val key = cursor.getColumnName(index)
            jsonObject.put(key, cursorColumnValueToJson(cursor, index))
        }
        return jsonObject
    }

    private fun cursorColumnValueToJson(
        cursor: Cursor,
        index: Int,
    ): Any {
        return when (cursor.getType(index)) {
            Cursor.FIELD_TYPE_NULL -> JSONObject.NULL
            Cursor.FIELD_TYPE_INTEGER -> cursor.getLong(index)
            Cursor.FIELD_TYPE_FLOAT -> cursor.getDouble(index)
            Cursor.FIELD_TYPE_STRING -> cursor.getString(index)
            Cursor.FIELD_TYPE_BLOB -> {
                val blob = cursor.getBlob(index)
                JSONObject()
                    .put(JSON_TYPE_KEY, JSON_TYPE_BLOB)
                    .put(JSON_BLOB_BASE64_KEY, Base64.encodeToString(blob, Base64.NO_WRAP))
            }
            else -> JSONObject.NULL
        }
    }

    private fun insertRows(
        database: SQLiteDatabase,
        tableName: String,
        rows: JSONArray,
    ) {
        for (i in 0 until rows.length()) {
            val row = rows.getJSONObject(i)
            val values = ContentValues(row.length())

            for (key in row.keys()) {
                val value = row.get(key)
                putJsonValue(values, key, value)
            }

            database.insertOrThrow(tableName, null, values)
        }
    }

    private fun putJsonValue(
        values: ContentValues,
        key: String,
        value: Any,
    ) {
        when (value) {
            JSONObject.NULL -> values.putNull(key)
            is Int -> values.put(key, value)
            is Long -> values.put(key, value)
            is Double -> values.put(key, value)
            is Float -> values.put(key, value)
            is Boolean -> values.put(key, value)
            is String -> values.put(key, value)
            is JSONObject -> putJsonObjectValue(values, key, value)
            else -> values.put(key, value.toString())
        }
    }

    private fun putJsonObjectValue(
        values: ContentValues,
        key: String,
        value: JSONObject,
    ) {
        val type = value.optString(JSON_TYPE_KEY)
        when (type) {
            JSON_TYPE_BLOB -> {
                val base64 = value.optString(JSON_BLOB_BASE64_KEY)
                val bytes = Base64.decode(base64, Base64.DEFAULT)
                values.put(key, bytes)
            }
            else -> values.put(key, value.toString())
        }
    }
}
