package br.com.sailboat.todozy.core.presentation.helper

import android.content.Context
import br.com.sailboat.todozy.BuildConfig
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.presentation.model.ImageTitleDividerItemView
import br.com.sailboat.todozy.core.presentation.model.ItemView
import br.com.sailboat.todozy.core.presentation.model.LabelValueItemView
import java.util.*

class AboutHelper(private val context: Context) {

    fun getInfo(): ArrayList<ItemView> {
        val items = ArrayList<ItemView>()
        items.add(getImageWithTitle())
        items.add(getAppDescription())
        items.add(getVersion())
        items.add(getDevelopedBy())

        return items
    }

    private fun getImageWithTitle(): ImageTitleDividerItemView {
        return ImageTitleDividerItemView(imageRes = R.drawable.ic_todozy_128px,
                title = context.getString(R.string.app_full_name))
    }

    private fun getAppDescription(): ItemView {
        return LabelValueItemView(label = context.getString(R.string.label_description),
                value = context.getString(R.string.app_description))
    }

    private fun getVersion(): LabelValueItemView {
        return LabelValueItemView(label = context.getString(R.string.version),
                value = BuildConfig.VERSION_NAME)
    }

    private fun getDevelopedBy(): LabelValueItemView {
        return LabelValueItemView(label = context.getString(R.string.developed_by),
                value = "Brayan Bedritchuk")
    }

}