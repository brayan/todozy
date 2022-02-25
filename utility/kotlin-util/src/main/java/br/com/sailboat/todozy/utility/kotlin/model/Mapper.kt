package br.com.sailboat.todozy.utility.kotlin.model

interface Mapper<FROM, TO> {
    fun map(from: FROM): TO
}