package br.com.sailboat.todozy.feature.about.impl.presentation

import android.content.Context
import br.com.sailboat.todozy.feature.about.impl.R
import br.com.sailboat.todozy.uicomponent.model.ImageTitleDividerUiModel
import br.com.sailboat.todozy.uicomponent.model.LabelValueUiModel
import br.com.sailboat.todozy.uicomponent.model.UiModel

class AboutHelper(private val context: Context) {

    fun getInfo(): ArrayList<UiModel> {
        val items = ArrayList<UiModel>()
        items.add(getImageWithTitle())
        items.add(getAppDescription())
        items.add(getVersion())
        items.add(getDevelopedBy())

        return items
    }

    private fun getImageWithTitle(): ImageTitleDividerUiModel {
        return ImageTitleDividerUiModel(
            imageRes = R.drawable.ic_todozy_128px,
            title = context.getString(R.string.app_full_name)
        )
    }

    private fun getAppDescription(): UiModel {
        return LabelValueUiModel(
            label = context.getString(R.string.label_description),
            value = context.getString(R.string.app_description)
        )
    }

    private fun getVersion(): LabelValueUiModel {
        return LabelValueUiModel(
            label = context.getString(R.string.version),
            value = "1.5.0"
        )
    }

    private fun getDevelopedBy(): LabelValueUiModel {
        return LabelValueUiModel(
            label = context.getString(R.string.developed_by),
            value = "Brayan Bedritchuk"
        )
    }

}