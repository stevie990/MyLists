package com.sserra.mylists.lists

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.firebase.ui.auth.AuthUI
import com.sserra.mylists.R
import com.sserra.mylists.data.MyList

import com.sserra.mylists.databinding.FragmentListsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ListsFragment : Fragment() {

    companion object {
        const val TAG = "ListsFragment"
    }

    private val viewModel: ListsViewModel by viewModels()

    private lateinit var viewDataBinding: FragmentListsBinding
    private lateinit var listAdapter: ListsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.lists_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                this.signOut()
                true
            }
        }

        return super.onOptionsItemSelected(item)
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

    private fun setupNavigation() {
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
                AddListDialogFragment().show(
                    childFragmentManager,
                    AddListDialogFragment.TAG.toString()
                )
            }
        }
    }

    private fun navigateToItemsList(list: MyList) {
        val action = ListsFragmentDirections.actionListsFragmentToItemsFragment(
            list.id.toString(),
            list.title.toString()
        )

        findNavController().navigate(action)
    }

    private fun navigateToLoginFragment() {
        val action = ListsFragmentDirections.actionListsFragmentToLoginFragment()
        findNavController().navigate(action)
    }

    fun addNewList(list: MyList) {
        viewModel.addNewList(list)
    }

    private fun signOut() {
        AuthUI.getInstance().signOut(requireContext()).addOnCompleteListener {
            this.navigateToLoginFragment()
        }
    }
}
