package br.com.sailboat.uicomponent.impl.viewholder

import android.view.ViewGroup
import androidx.compose.material.Surface
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.uicomponent.impl.label.LabelItem
import br.com.sailboat.uicomponent.impl.theme.TodozyTheme
import br.com.sailboat.uicomponent.model.LabelUiModel

class LabelViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    ComposeView(parent.context).apply {
        layoutParams =
            RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT,
            )
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
    },
) {
    private val composeView get() = itemView as ComposeView

    fun bind(item: LabelUiModel) {
        composeView.setContent {
            TodozyTheme {
                Surface {
                    LabelItem(text = item.label)
                }
            }
        }
    }
}
