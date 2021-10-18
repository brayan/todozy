package br.com.sailboat.todozy.features.tasks.presentation.history

import android.content.Context
import br.com.sailboat.todozy.core.presentation.dialog.selectable.DateFilterTaskHistorySelectableItem

// TODO: Add unit tests
// TODO: Repalce this use case by a service
class GetDateFilterNameView(private val context: Context) : GetDateFilterNameViewUseCase {

    override operator fun invoke(dateRangeType: DateFilterTaskHistorySelectableItem): String {
        return context.getString(dateRangeType.getName())
    }

}