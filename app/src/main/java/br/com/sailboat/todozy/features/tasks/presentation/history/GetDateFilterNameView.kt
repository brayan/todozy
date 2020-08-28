package br.com.sailboat.todozy.features.tasks.presentation.history

import android.content.Context
import br.com.sailboat.todozy.core.presentation.dialog.selectable.DateFilterTaskHistorySelectableItem

class GetDateFilterNameView(private val context: Context) {

    operator fun invoke(dateRangeType: DateFilterTaskHistorySelectableItem) = context.getString(dateRangeType.getName())

}