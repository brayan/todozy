package br.com.sailboat.todozy.ui.dialog.color_picker

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.ui.base.BaseDialogFragment
import java.io.Serializable

class ColorPickerDialog(private val callback: Callback) : BaseDialogFragment(), ColorPickerViewHolder.Callback {

    override var currentColor: MaterialColor? = null
    private var adapter: ColorPickerAdapter? = null
    private var recyclerView: RecyclerView? = null

    interface Callback : Serializable {
        fun onClickColor(currentColor: MaterialColor)
    }

    companion object {
        fun showDialog(fm: FragmentManager, initialColor: MaterialColor, callback: Callback) {
            val dialog = ColorPickerDialog(callback)
            dialog.currentColor = initialColor
            dialog.show(fm, ColorPickerDialog::class.java.name)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(getActivity(), R.layout.dlg_color_picker, null)
        initRecyclerView(view)
        return buildDialog(view)
    }

    override fun onClickColor(adapterPosition: Int) {
        val selectedColor = MaterialColor.getColor(adapterPosition)
        callback.onClickColor(selectedColor)
        dismiss()
    }

    private fun initRecyclerView(view: View) {
        recyclerView = view.findViewById<View>(R.id.recycler) as RecyclerView
        recyclerView!!.layoutManager = GridLayoutManager(activity, 2, LinearLayoutManager.HORIZONTAL, false)
        adapter = ColorPickerAdapter(this)
        recyclerView!!.adapter = adapter
    }

    private fun buildDialog(view: View): Dialog {
        val builder = AlertDialog.Builder(getActivity()!!)
        builder.setView(view)
        return builder.create()
    }

}