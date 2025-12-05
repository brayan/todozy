@file:Suppress("ktlint:standard:indent")

package br.com.sailboat.uicomponent.impl.viewholder

import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.uicomponent.impl.skeleton.TaskSkeletonItem
import br.com.sailboat.uicomponent.impl.theme.TodozyTheme
import br.com.sailboat.uicomponent.model.TaskSkeletonUiModel

class TaskSkeletonViewHolder(
    parent: ViewGroup,
) : RecyclerView.ViewHolder(
    ComposeView(parent.context).apply {
        layoutParams =
            RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT,
            )
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            TodozyTheme {
                TaskSkeletonItem()
            }
        }
    },
) {
    fun bind(
        @Suppress("UNUSED_PARAMETER") item: TaskSkeletonUiModel,
    ) {
        // Static skeleton; no-op
    }
}
