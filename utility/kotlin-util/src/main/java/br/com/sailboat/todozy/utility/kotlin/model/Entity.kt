package br.com.sailboat.todozy.utility.kotlin.model

abstract class Entity {
    abstract var id: Long

    companion object {
        const val NO_ID: Long = -1
    }

    fun hasNoId() = id == NO_ID
}