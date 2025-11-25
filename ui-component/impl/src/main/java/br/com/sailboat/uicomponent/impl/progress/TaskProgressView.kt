package br.com.sailboat.uicomponent.impl.progress

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import br.com.sailboat.todozy.domain.model.TaskProgressDay
import br.com.sailboat.todozy.domain.model.TaskProgressRange

class TaskProgressView
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : FrameLayout(context, attrs, defStyleAttr) {
        private val composeView =
            ComposeView(context).apply {
                layoutParams =
                    LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT,
                    )
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            }
        private val daysState = mutableStateOf(emptyList<TaskProgressDay>())
        private val rangeState = mutableStateOf(TaskProgressRange.LAST_YEAR)
        private val onRangeSelectedState = mutableStateOf<(TaskProgressRange) -> Unit>({})
        private val onDayClickState = mutableStateOf<((TaskProgressDay) -> Unit)?>(null)

        init {
            addView(composeView)
            composeView.setContent {
                MaterialTheme {
                    Surface {
                        TaskProgressContent(
                            days = daysState.value,
                            selectedRange = rangeState.value,
                            onRangeSelected = onRangeSelectedState.value,
                            onDayClick = onDayClickState.value,
                        )
                    }
                }
            }
        }

        fun render(
            days: List<TaskProgressDay>,
            range: TaskProgressRange,
            onRangeSelected: (TaskProgressRange) -> Unit,
            onDayClick: ((TaskProgressDay) -> Unit)? = null,
        ) {
            onRangeSelectedState.value = onRangeSelected
            onDayClickState.value = onDayClick
            daysState.value = days
            rangeState.value = range
        }
    }
