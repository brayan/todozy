package br.com.sailboat.todozy.utility.android.sqlite

import android.net.Uri

interface DatabaseJsonBackupService {
    suspend fun exportToJson(uri: Uri)
    suspend fun importFromJson(uri: Uri)
}
