package com.sserra.mylists.lists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.sserra.mylists.data.MyList

import com.sserra.mylists.databinding.FragmentListsBinding
import timber.log.Timber

class ListsFragment : Fragment() {

    private val viewModel: ListsViewModel by viewModels {
        ListsViewModelFactory()
    }

    private lateinit var viewDataBinding: FragmentListsBinding
    private lateinit var listAdapter: ListsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = FragmentListsBinding.inflate(inflater, container, false)
            .apply {
                listsViewmodel = viewModel
            }

        setHasOptionsMenu(true)

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        this.setupListAdapter()
        this.setupNavigation()
        this.setupFab()
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.listsViewmodel
        if (viewModel != null) {
            listAdapter = ListsAdapter(viewModel)
            viewDataBinding.mylistsList.apply {
                adapter = listAdapter
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun setupNavigation(){
        viewModel.openList.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.navigateToItemsList(it)
                viewModel.displayListComplete()
            }
        })
    }

    private fun setupFab() {
        viewDataBinding.addListFab.let {
            it.setOnClickListener {
                AddListDialogFragment().show(childFragmentManager, AddListDialogFragment.TAG.toString())
            }
        }
    }

    private fun navigateToItemsList(list: MyList){
        val action = ListsFragmentDirections.actionListsFragmentToItemsFragment(list.id.toString(), list.title.toString())
        findNavController().navigate(action)
    }

    fun addNewList(list: MyList) {
        viewModel.addNewList(list)
    }
}
