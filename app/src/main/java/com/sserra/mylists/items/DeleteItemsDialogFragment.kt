package com.sserra.mylists.items

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sserra.mylists.R
import com.sserra.mylists.databinding.DialogDeleteItemBinding

class DeleteItemsDialogFragment : DialogFragment() {

    private lateinit var viewDataBinding: DialogDeleteItemBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = MaterialAlertDialogBuilder(it)
            val inflater = requireActivity().layoutInflater

            viewDataBinding = DialogDeleteItemBinding.inflate(inflater, null, false)

            builder.setView(viewDataBinding.root)
                .setTitle(R.string.delete_item)
                .setPositiveButton(
                    R.string.delete
                ) { _, _ ->
                    onDeleteItemClicked()
                }
                .setNegativeButton(
                    R.string.cancel
                ) { _, _ ->
                    requireDialog().cancel()
                }
                .setCancelable(false)

            val dialog = builder.create()
            dialog.setCanceledOnTouchOutside(false)
            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun onDeleteItemClicked(){
        val itemFragment = parentFragment as ItemsFragment
        itemFragment.deleteSelectedItems()
    }

    companion object {
        const val TAG = R.string.delete_items_dialog
    }
}