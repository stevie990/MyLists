package com.sserra.mylists.lists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sserra.mylists.R

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
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.listsViewmodel
        if (viewModel != null) {
            listAdapter = ListsAdapter(viewModel)
            viewDataBinding.mylistsList.adapter = listAdapter
        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun setupNavigation(){
//        viewModel.openList.observe(this, Observer {
//            it?.let {
//                this.navigateToItemsList(it)
//            }
//        })
    }

    private fun setupFab() {
        activity?.findViewById<FloatingActionButton>(R.id.add_list_fab)?.let {
            it.setOnClickListener {
            }
        }
    }

    private fun navigateToItemsList(listId: String){
        val action = ListsFragmentDirections.actionListsFragmentToItemsFragment(listId)
        findNavController().navigate(action)
    }
}
