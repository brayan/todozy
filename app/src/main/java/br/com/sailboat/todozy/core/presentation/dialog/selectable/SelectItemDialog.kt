package br.com.sailboat.todozy.core.presentation.dialog.selectable

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.presentation.base.BaseDialogFragment

class SelectItemDialog(private val callback: Callback) : BaseDialogFragment(), SelectableItemAdapter.Callback {

    override lateinit var selectableItems: List<SelectableItem>
    override var selectedItem: SelectableItem? = null

    interface Callback {
        fun onClickItem(item: SelectableItem)
    }


    companion object {
        fun show(manager: FragmentManager, title: String, items: List<SelectableItem>,
                 selectedItem: SelectableItem?, callback: Callback) {
            val dialog = SelectItemDialog(callback)
            dialog.selectableItems = items
            dialog.selectedItem = selectedItem
            dialog.title = title
            dialog.show(manager, SelectItemDialog::class.java.name)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(activity, R.layout.dlg_reycler, null)
        view.recycler.layoutManager = LinearLayoutManager(activity)
        view.recycler.adapter = SelectableItemAdapter(this)
        updateViews(view)

        return buildDialog(view)
    }

    override fun onClickItem(position: Int) {
        callback.onClickItem(selectableItems[position])
        dismiss()
    }

    private fun updateViews(view: View) {
        if (title?.isNotEmpty() == true) {
            view.dlg_recycler__tv__title.text = title
            view.dlg_recycler__tv__title.visibility = View.VISIBLE
        } else {
            view.dlg_recycler__tv__title.visibility = View.GONE
        }

        view.recycler.adapter?.notifyDataSetChanged()
    }

    private fun buildDialog(view: View): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        builder.setView(view)

        return builder.create()
    }


}