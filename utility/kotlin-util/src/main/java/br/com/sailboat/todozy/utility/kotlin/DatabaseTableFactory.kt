package br.com.sailboat.todozy.utility.kotlin

interface DatabaseTableFactory {
    fun getTables(): List<String>
}
