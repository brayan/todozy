package br.com.sailboat.todozy.core.presentation.model

import java.io.Serializable

interface ItemView : Serializable {
    val viewType: Int
}