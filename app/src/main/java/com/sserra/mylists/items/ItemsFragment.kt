package com.sserra.mylists.items

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sserra.mylists.R

import com.sserra.mylists.databinding.FragmentItemsBinding
import timber.log.Timber

class ItemsFragment : Fragment() {

    private val viewModel: ItemsViewModel by viewModels {
        ItemsViewModelFactory()
    }

    private val args: ItemsFragmentArgs by navArgs()

    private lateinit var viewDataBinding: FragmentItemsBinding
    private lateinit var listAdapter: ItemsAdapter

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        this.setupListAdapter()
        this.setupFab()

//        viewModel.items.observe(this, Observer {
//
//        })
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
        activity?.findViewById<FloatingActionButton>(R.id.add_item_fab)?.let {
            it.setOnClickListener {
                AddItemDialogFragment(listId = args.listId).show(fragmentManager!!, AddItemDialogFragment.TAG)
            }
        }
    }

}
