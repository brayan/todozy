package br.com.sailboat.todozy.data.model

import br.com.sailboat.todozy.domain.helper.EntityHelper

data class TaskData(var id: Long = EntityHelper.NO_ID,
                    var name: String?,
                    var notes: String?,
                    var insertingDate: String? = null,
                    var enabled: Boolean = false)