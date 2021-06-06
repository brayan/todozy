package br.com.sailboat.todozy.core.presentation.dialog.selectable

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.presentation.base.BaseDialogFragment
import br.com.sailboat.todozy.core.presentation.helper.gone
import br.com.sailboat.todozy.core.presentation.helper.visible
import br.com.sailboat.todozy.databinding.DlgReyclerBinding
import br.com.sailboat.todozy.databinding.DlgWeekDaysSelectorBinding

class SelectItemDialog(private val callback: Callback) : BaseDialogFragment(), SelectableItemAdapter.Callback {

    override lateinit var selectableItems: List<SelectableItem>
    override var selectedItem: SelectableItem? = null

    interface Callback {
        fun onClickItem(item: SelectableItem)
    }

    private lateinit var binding: DlgReyclerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DlgReyclerBinding.inflate(inflater, container, false)
        return binding.root
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
        binding.recycler.layoutManager = LinearLayoutManager(activity)
        binding.recycler.adapter = SelectableItemAdapter(this)
        updateViews()

        return buildDialog()
    }

    override fun onClickItem(position: Int) {
        callback.onClickItem(selectableItems[position])
        dismiss()
    }

    private fun updateViews() = with(binding) {
        if (title?.isNotEmpty() == true) {
            dlgRecyclerTvTitle.text = title
            dlgRecyclerTvTitle.visible()
        } else {
            dlgRecyclerTvTitle.gone()
        }

        recycler.adapter?.notifyDataSetChanged()
    }

    private fun buildDialog(): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)

        return builder.create()
    }


}