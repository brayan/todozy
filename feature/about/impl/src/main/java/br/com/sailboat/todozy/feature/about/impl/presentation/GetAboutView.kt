package br.com.sailboat.todozy.feature.about.impl.presentation

import android.content.Context
import br.com.sailboat.uicomponent.model.ImageTitleDividerUiModel
import br.com.sailboat.uicomponent.model.LabelValueUiModel
import br.com.sailboat.uicomponent.model.UiModel
import br.com.sailboat.uicomponent.impl.R as UiR

internal class GetAboutView(private val context: Context) : GetAboutViewUseCase {

    override suspend fun invoke(): Result<List<UiModel>> = runCatching {
        val items = ArrayList<UiModel>()
        items.add(getImageWithTitle())
        items.add(getAppDescription())
        items.add(getVersion())
        items.add(getDevelopedBy())

        return@runCatching items
    }

    private fun getImageWithTitle(): ImageTitleDividerUiModel {
        return ImageTitleDividerUiModel(
            imageRes = UiR.drawable.ic_todozy_128px,
            title = context.getString(UiR.string.app_full_name)
        )
    }

    private fun getAppDescription(): UiModel {
        return LabelValueUiModel(
            label = context.getString(UiR.string.label_description),
            value = context.getString(UiR.string.app_description)
        )
    }

    private fun getVersion(): LabelValueUiModel {
        return LabelValueUiModel(
            label = context.getString(UiR.string.version),
            value = "1.5.0"
        )
    }

    private fun getDevelopedBy(): LabelValueUiModel {
        return LabelValueUiModel(
            label = context.getString(UiR.string.developed_by),
            value = "Brayan Bedritchuk"
        )
    }
}
