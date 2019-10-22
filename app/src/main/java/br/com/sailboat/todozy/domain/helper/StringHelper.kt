package br.com.sailboat.todozy.domain.helper

object StringHelper {

    fun isNotEmpty(text: String): Boolean {
        return !isNullOrEmpty(text)
    }

    fun isNullOrEmpty(text: String?): Boolean {
        return text == null || text.trim { it <= ' ' }.isEmpty()
    }

    fun upperCaseFirstLetter(text: String): String {
        return text.substring(0, 1).toUpperCase() + text.substring(1)
    }

}
