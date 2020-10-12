package com.sserra.mylists.framework.presentation.items

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sserra.mylists.R

import com.sserra.mylists.business.domain.model.Item
import com.sserra.mylists.databinding.DialogAddItemBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.lang.IllegalStateException
import java.util.*

@ExperimentalCoroutinesApi
class AddItemDialogFragment : DialogFragment() {

    private lateinit var viewDataBinding: DialogAddItemBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {

            val builder = MaterialAlertDialogBuilder(it)
            val inflater = requireActivity().layoutInflater

            viewDataBinding = DialogAddItemBinding.inflate(inflater, null, false)

            builder.setView(viewDataBinding.root)
                .setTitle(R.string.add_item)
                .setPositiveButton(R.string.add_item_button, null)
                .setNegativeButton(R.string.cancel) { _, _ ->
                    requireDialog().cancel()
                }
                .setCancelable(false)

            val dialog = builder.create()
            dialog.setCanceledOnTouchOutside(false)

            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    if (isNameValid(viewDataBinding.etNewItemTitle.text)) {
                        viewDataBinding.etNewItemTitle.error = null
                        onAddNewItemClicked()
                        dialog.dismiss()
                    } else {
                        viewDataBinding.etNewItemTitle.error = getString(R.string.name_empty_error)
                    }
                }
            }

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun onAddNewItemClicked() {
        val item = Item(
            id = UUID.randomUUID().toString(),
            title = viewDataBinding.etNewItemTitle.text.toString(),
            description = "",
            isCompleted = false,
            created_at = "",
            updated_at = ""
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

    private fun isNameValid(text: Editable?): Boolean {
        return text != null && text.isNotEmpty()
    }

    companion object {
        const val TAG = R.string.add_item_dialog
    }
}
