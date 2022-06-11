package br.com.sailboat.todozy.feature.task.history.impl.presentation

import android.content.Context
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.DateFilterTaskHistorySelectableItem

class GetDateFilterNameView(private val context: Context) : GetDateFilterNameViewUseCase {

    override operator fun invoke(dateRangeType: DateFilterTaskHistorySelectableItem): String {
        return context.getString(dateRangeType.getName()).uppercase()
    }

}