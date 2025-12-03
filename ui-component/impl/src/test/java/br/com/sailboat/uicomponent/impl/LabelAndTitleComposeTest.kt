package br.com.sailboat.uicomponent.impl

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import br.com.sailboat.uicomponent.impl.alarm.AlarmItem
import br.com.sailboat.uicomponent.impl.label.LabelItem
import br.com.sailboat.uicomponent.impl.label.LabelValueItem
import br.com.sailboat.uicomponent.impl.theme.TodozyTheme
import br.com.sailboat.uicomponent.impl.title.TitleItem
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

@Ignore("Compose UI tests require Android runtime; ignored on JVM unit target")
class LabelAndTitleComposeTest {
    @get:Rule val composeRule = createComposeRule()

    @Test
    fun label_and_value_render_text() {
        composeRule.setContent {
            TodozyTheme {
                LabelValueItem(label = "Priority", value = "High")
            }
        }

        composeRule.onNodeWithText("PRIORITY").assertIsDisplayed()
        composeRule.onNodeWithText("High").assertIsDisplayed()
    }

    @Test
    fun title_renders_centered_text() {
        composeRule.setContent {
            TodozyTheme {
                TitleItem(text = "Task Details")
            }
        }

        composeRule.onNodeWithText("Task Details").assertIsDisplayed()
    }

    @Test
    fun alarm_item_shows_date_time_and_repeat() {
        composeRule.setContent {
            TodozyTheme {
                AlarmItem(
                    date = "Today",
                    time = "15:30",
                    repeatDescription = "Every day",
                )
            }
        }

        composeRule.onNodeWithText("Today").assertIsDisplayed()
        composeRule.onNodeWithText("15:30").assertIsDisplayed()
        composeRule.onNodeWithText("Every day").assertIsDisplayed()
    }

    @Test
    fun label_item_uppercases_text() {
        composeRule.setContent {
            TodozyTheme {
                LabelItem(text = "due date")
            }
        }

        composeRule.onNodeWithText("DUE DATE").assertIsDisplayed()
    }
}
