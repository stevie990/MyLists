package com.sserra.mylists.framework.presentation.lists

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sserra.mylists.R

import com.sserra.mylists.business.domain.model.MyList
import com.sserra.mylists.databinding.DialogAddListBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.lang.IllegalStateException
import java.util.*

@ExperimentalCoroutinesApi
class AddListDialogFragment : DialogFragment() {

    private lateinit var viewDataBinding: DialogAddListBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {

            val builder = MaterialAlertDialogBuilder(it)
            val inflater = requireActivity().layoutInflater

            viewDataBinding = DialogAddListBinding.inflate(inflater, null,false)

            builder.setView(viewDataBinding.root)
                .setTitle(R.string.add_list)
                .setPositiveButton(
                    R.string.create_list_button, null)
                .setNegativeButton(
                    R.string.cancel
                ) { _, _ ->
                    requireDialog().cancel()
                }
                .setCancelable(false)

            val dialog = builder.create()
            dialog.setCanceledOnTouchOutside(false)

            dialog.setOnShowListener {
                val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton.setOnClickListener {
                    if (isNameValid(viewDataBinding.etListName.text)) {
                        viewDataBinding.etListName.error = null
                        onAddNewListClicked()
                        dialog.dismiss()
                    } else {
                        viewDataBinding.etListName.error = getString(R.string.name_empty_error)
                    }
                }
            }

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun onAddNewListClicked() {
            val list = MyList(
                id = UUID.randomUUID().toString(),
                title =  viewDataBinding.etListName.text.toString(),
                description = "",
                created_at = "",
                updated_at = ""
            )

            val listsFragment = parentFragment as ListsFragment
            listsFragment.addNewList(list)
    }

    private fun isNameValid(text: Editable?): Boolean {
        return text != null && text.isNotEmpty()
    }

    companion object {
        const val TAG = R.string.add_list_dialog
    }
}
