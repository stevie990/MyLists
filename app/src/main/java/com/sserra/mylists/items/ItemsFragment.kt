package com.sserra.mylists.items

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import com.sserra.mylists.R
import com.sserra.mylists.data.Item

import com.sserra.mylists.databinding.FragmentItemsBinding
import timber.log.Timber

class ItemsFragment : Fragment() {

    private val viewModel: ItemsViewModel by viewModels {
        ItemsViewModelFactory()
    }

    private val args: ItemsFragmentArgs by navArgs()

    private lateinit var viewDataBinding: FragmentItemsBinding
    private lateinit var listAdapter: ItemsAdapter
    private var tracker: SelectionTracker<Long>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewDataBinding = FragmentItemsBinding.inflate(inflater, container, false)
            .apply {
                itemsViewmodel = viewModel
            }

        setHasOptionsMenu(true)
        viewModel.setListId(args.listId)

        // Inflate the layout for this fragment
        return viewDataBinding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        this.tracker?.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        this.setupListAdapter()
        this.setupFab()
        this.setupTracker()

        if (savedInstanceState != null) {
            this.tracker?.onRestoreInstanceState(savedInstanceState)
        }

//        viewModel.items.observe(this, Observer {
//
//        })

        this.tracker?.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    val nItems: Int? = tracker?.selection?.size()

                    if (nItems != null && nItems > 0) {
                        // Change title and color of action bar

//                        title = "$nItems items selected"
//                        supportActionBar?.setBackgroundDrawable(
//                            ColorDrawable(Color.parseColor("#ef6c00")))
                    } else {
                        // Reset color and title to default values

//                        title = "RVSelection"
//                        supportActionBar?.setBackgroundDrawable(
//                            ColorDrawable(getColor(R.color.colorPrimary)))
                    }
                }
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.items_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                // Call viewmodel in order to delete the selected items
//                viewModel.deleteItems(tracker?.selection)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.itemsViewmodel
        if (viewModel != null) {
            listAdapter = ItemsAdapter(viewModel)
            viewDataBinding.itemsList.adapter = listAdapter
        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun setupFab() {
        viewDataBinding.addItemFab.let {
            it.setOnClickListener {
                AddItemDialogFragment().show(childFragmentManager, AddItemDialogFragment.TAG)
                tracker?.clearSelection()
            }
        }
    }

    private fun setupTracker() {
        this.tracker = SelectionTracker.Builder<Long>(
            "selection-1",
            viewDataBinding.itemsList,
            RecyclerViewIdKeyProvider(viewDataBinding.itemsList),
            MyLookup(viewDataBinding.itemsList),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        listAdapter.setTracker(this.tracker)
    }

    fun addNewItem(item: Item) {
        viewModel.addNewItem(item)
    }
}