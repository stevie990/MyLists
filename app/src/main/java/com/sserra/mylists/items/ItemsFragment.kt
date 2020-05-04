package com.sserra.mylists.items

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider

import com.sserra.mylists.databinding.FragmentItemsBinding

class ItemsFragment : Fragment() {

    private val viewModel: ItemsViewModel by viewModels {
        ItemsViewModelFactory()
    }

//    private val viewModel: ItemsViewModel by viewModels {
//        ItemsViewModelFactory()
//    }

//    private val viewModel = ViewModelProvider(this, ItemsViewModelFactory()).get(ItemsViewModel::class.java)

    private lateinit var viewDataBinding: FragmentItemsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = FragmentItemsBinding.inflate(inflater, container, false)
            .apply {
            itemsViewmodel = viewModel
        }

//        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
    }
}
