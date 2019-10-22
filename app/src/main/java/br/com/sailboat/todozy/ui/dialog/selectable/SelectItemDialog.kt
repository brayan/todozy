package br.com.sailboat.todozy.ui.dialog.selectable

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.ui.base.BaseDialogFragment
import kotlinx.android.synthetic.main.dlg_reycler.*

class SelectItemDialog(private val callback: Callback) : BaseDialogFragment(), SelectableItemAdapter.Callback {

    override lateinit var selectableItems: List<SelectableItem>
    override var selectedItem: SelectableItem? = null

    private var items: List<SelectableItem>? = null

    interface Callback {
        fun onClickItem(item: SelectableItem)
    }


    companion object {
        fun show(manager: FragmentManager, title: String, items: List<SelectableItem>,
                 selectedItem: SelectableItem?, callback: Callback) {
            val dialog = SelectItemDialog(callback)
            dialog.items = items
            dialog.selectedItem = selectedItem
            dialog.title = title
            dialog.show(manager, SelectItemDialog::class.java.name)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(activity, R.layout.dlg_reycler, null)
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.adapter = SelectableItemAdapter(this)
        updateViews()
        return buildDialog(view)
    }

    override fun onClickItem(position: Int) {
        callback.onClickItem(items!![position])
        dismiss()
    }

    private fun updateViews() {
        if (title?.isNotEmpty() == true) {
            dlg_recycler__tv__title.text = title
            dlg_recycler__tv__title.visibility = View.VISIBLE
        } else {
            dlg_recycler__tv__title.visibility = View.GONE
        }

        recycler.adapter?.notifyDataSetChanged()
    }

    private fun buildDialog(view: View): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        builder.setView(view)

        return builder.create()
    }




}