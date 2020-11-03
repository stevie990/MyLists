package com.sserra.mylists.framework.presentation.items

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.DividerItemDecoration
import com.sserra.mylists.R
import com.sserra.mylists.business.domain.model.Item
import com.sserra.mylists.databinding.FragmentItemsBinding
import com.sserra.mylists.framework.presentation.items.state.ItemStateEvent
import com.sserra.mylists.framework.presentation.items.state.ItemStateEvent.GetItemsEvent

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ItemsFragment : Fragment() {

    private val args: ItemsFragmentArgs by navArgs()

    private val viewModel: ItemsViewModel by viewModels()

    private var _viewDataBinding: FragmentItemsBinding? = null
    private val viewDataBinding get() = _viewDataBinding!!

    private var _listAdapter: ItemsAdapter? = null
    private val listAdapter get() = _listAdapter!!

    private var tracker: SelectionTracker<String>? = null
    private var actionMode: ActionMode? = null
    private var isInActionMode: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _viewDataBinding = FragmentItemsBinding.inflate(inflater, container, false)
            .apply {
                itemsViewmodel = viewModel
            }

        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return viewDataBinding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        this.tracker?.onSaveInstanceState(outState)
        outState.putBoolean("IsInActionMode", isInActionMode)
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
        viewModel.setStateEvent(GetItemsEvent)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        this.setupListAdapter()
        this.setupFab()
        this.setupTracker()

        if (savedInstanceState != null) {
            this.tracker?.onRestoreInstanceState(savedInstanceState)

            if (savedInstanceState.getBoolean("IsInActionMode", false)){
                actionMode = (activity as AppCompatActivity).startSupportActionMode(actionModeCallback)
                actionMode?.title = "${tracker?.selection?.size()} selected"
            }
        }

        this.tracker?.addObserver(
            object : SelectionTracker.SelectionObserver<String>() {
                override fun onSelectionChanged() {
                    val itemsSelected = tracker?.selection?.size()

                    if (itemsSelected!! > 0){
                        if (actionMode == null) {
                            actionMode = (activity as AppCompatActivity).startSupportActionMode(actionModeCallback)
                        }

                        actionMode?.title = "$itemsSelected selected"

                    } else {
                        actionMode?.finish()
                    }

//                    if (itemsSelected != null && itemsSelected > 0) {
//                        // Change title and color of action bar
//
////                        title = "$itemsSelected items selected"
////                        supportActionBar?.setBackgroundDrawable(
////                            ColorDrawable(Color.parseColor("#ef6c00")))
//                    } else {
//                        // Reset color and title to default values
//
////                        title = "RVSelection"
////                        supportActionBar?.setBackgroundDrawable(
////                            ColorDrawable(getColor(R.color.colorPrimary)))
//                    }
                }
            }
        )
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater.inflate(R.menu.items_contextual_action_bar, menu)
////        itemsMenu = menu
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.action_delete -> {
//                // Call viewmodel in order to delete selected items
//                )
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.itemsViewmodel
        if (viewModel != null) {
            _listAdapter = ItemsAdapter(viewModel)
            viewDataBinding.itemsList.apply {
                adapter = listAdapter
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun setupFab() {
        viewDataBinding.addItemFab.let {
            it.setOnClickListener {
                AddItemDialogFragment().show(
                    childFragmentManager,
                    AddItemDialogFragment.TAG.toString()
                )
                tracker?.clearSelection()
            }
        }
    }

    private fun setupTracker() {
        this.tracker = SelectionTracker.Builder(
            "selection-1",
            viewDataBinding.itemsList,
            MyItemKeyProvider(listAdapter),
            MyItemDetailsLookup(viewDataBinding.itemsList),
            StorageStrategy.createStringStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        listAdapter.setTracker(this.tracker)
    }

    fun addNewItem(item: Item) {
        val newItem = viewModel.createNewItem(item.id, item.title, item.description)
        viewModel.setStateEvent(ItemStateEvent.AddItemEvent(newItem))
    }

    fun deleteSelectedItems() {
        tracker?.selection?.let {
            viewModel.deleteItems(it)
            tracker?.clearSelection()
            actionMode = null
        }
    }

    private val actionModeCallback = object : ActionMode.Callback {

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(R.menu.items_contextual_action_bar, menu)
            isInActionMode = true
            return true
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {

            return when (item?.itemId) {
                R.id.action_delete -> {
                    DeleteItemsDialogFragment().show(childFragmentManager, DeleteItemsDialogFragment.TAG.toString())
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            isInActionMode = false
            tracker?.clearSelection()
            actionMode = null
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewDataBinding = null
        _listAdapter = null
    }

    // *** CLEAN MVI *****************************

    private fun subscribeObservers(){

        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->

            dataState.data?.let { event ->
                event.getContentIfNotHandled()?.let { itemViewState ->

                    itemViewState.items?.let {
                        viewModel.setItems(it)
                    }
                }
            }
        })
    }
}