package com.sserra.mylists.lists

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.sserra.mylists.R

import com.sserra.mylists.data.MyList
import com.sserra.mylists.databinding.DialogAddListBinding
import timber.log.Timber
import java.lang.IllegalStateException
import java.util.*
import kotlin.ClassCastException

class AddListDialogFragment : DialogFragment() {

    private lateinit var viewDataBinding: DialogAddListBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {

            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            viewDataBinding = DialogAddListBinding.inflate(inflater, null,false)

            builder.setView(viewDataBinding.root)
                .setTitle(R.string.add_list)
                .setPositiveButton(
                    R.string.add_item_button
                ) { dialog, id ->
                    onAddNewListClicked()
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

    private fun onAddNewListClicked() {
        val list = MyList(
            title =  viewDataBinding.newListTitleEditText.text.toString(),
            id = UUID.randomUUID().toString()
        )

        val listsFragment = parentFragment as ListsFragment
        listsFragment.addNewList(list)
    }

    companion object {
        const val TAG = "AddListDialog"
    }
}
