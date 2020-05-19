package com.sserra.mylists.items

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sserra.mylists.R

import com.sserra.mylists.data.Item
import com.sserra.mylists.databinding.DialogAddItemBinding
import java.lang.IllegalStateException
import java.util.*

class AddItemDialogFragment : DialogFragment() {

    private lateinit var viewDataBinding: DialogAddItemBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {

            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            viewDataBinding = DialogAddItemBinding.inflate(inflater, null, false)

            builder.setView(viewDataBinding.root)
                .setTitle(R.string.add_item)
                .setPositiveButton(
                    R.string.add_item_button
                ) { dialog, id ->
                    onAddNewItemClicked()
                }
                .setNegativeButton(
                    R.string.cancel
                ) { dialog, id ->
                    requireDialog().cancel()
                }
                .setCancelable(false)

            val dialog = builder.create()
            dialog.setCanceledOnTouchOutside(false)
            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun onAddNewItemClicked() {
        val item = Item(
            title = viewDataBinding.newItemTitleEditText.text.toString(),
            id = UUID.randomUUID().toString()
        )

        val itemFragment = parentFragment as ItemsFragment
        itemFragment.addNewItem(item)
    }

//    override fun onResume() {
//        super.onResume()
//        dialog?.window?.setLayout(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//    }

    companion object {
        const val TAG = "AddItemDialog"
    }
}
