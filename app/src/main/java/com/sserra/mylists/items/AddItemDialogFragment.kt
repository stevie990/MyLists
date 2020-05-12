package com.sserra.mylists.items

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.sserra.mylists.R

import com.sserra.mylists.data.Item
import com.sserra.mylists.databinding.DialogAddItemBinding
import java.lang.IllegalStateException

class AddItemDialogFragment(val listId: String) : DialogFragment() {

    private val viewModel: ItemsViewModel by viewModels {
        ItemsViewModelFactory()
    }

    private lateinit var viewDataBinding: DialogAddItemBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {

            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            viewDataBinding = DialogAddItemBinding.inflate(inflater, null,false)

            builder.setView(viewDataBinding.root)
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

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        viewDataBinding = DialogAddItemBinding.inflate(inflater, container, false)
//
////        viewDataBinding.addItemButton.setOnClickListener { onAddNewItemClicked() }
//
//        return viewDataBinding.root
//    }

    private fun onAddNewItemClicked() {
        val item = Item(viewDataBinding.newItemTitleEditText.text.toString())
        viewModel.addNewItem(item, listId)
//        dismiss()
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    companion object {
        const val TAG = "AddItemDialog"
    }
}
