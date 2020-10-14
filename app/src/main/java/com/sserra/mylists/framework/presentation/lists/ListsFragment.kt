package com.sserra.mylists.framework.presentation.lists

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.firebase.ui.auth.AuthUI
import com.sserra.mylists.R
import com.sserra.mylists.business.domain.model.MyList
import com.sserra.mylists.business.domain.state.DataState
import com.sserra.mylists.business.interactors.AddList

import com.sserra.mylists.databinding.FragmentListsBinding
import com.sserra.mylists.framework.presentation.DataStateListener
import com.sserra.mylists.framework.presentation.lists.state.MyListStateEvent
import com.sserra.mylists.framework.presentation.lists.state.MyListStateEvent.AddListEvent
import com.sserra.mylists.framework.presentation.lists.state.MyListStateEvent.GetMyListsEvent
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

    lateinit var dataStateHandler: DataStateListener

    private var _viewDataBinding: FragmentListsBinding? = null
    private val viewDataBinding get() = _viewDataBinding!!

    private var _listAdapter: ListsAdapter? = null
    private val listAdapter get() = _listAdapter!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewDataBinding = FragmentListsBinding.inflate(inflater, container, false)
            .apply {
                listsViewmodel = viewModel
            }

        setHasOptionsMenu(true)

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        setupListAdapter()
        setupNavigation()
        setupFab()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
        viewModel.setStateEvent(GetMyListsEvent)
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
            _listAdapter = ListsAdapter(viewModel)
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
            list.id,
            list.title
        )

        findNavController().navigate(action)
    }

    private fun navigateToLoginFragment() {
        val action = ListsFragmentDirections.actionListsFragmentToLoginFragment()
        findNavController().navigate(action)
    }

    fun addNewList(list: MyList) {
        val newList = viewModel.createNewList(list.id, list.title, list.description)
        viewModel.setStateEvent(AddListEvent(newList))
//        viewModel.addNewList(newList)
    }

    private fun signOut() {
        AuthUI.getInstance().signOut(requireContext()).addOnCompleteListener {
            this.navigateToLoginFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _listAdapter = null
        _viewDataBinding = null
    }

    // *** CLEAN MVI *****************************

    private fun subscribeObservers(){

        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->

            dataState.data?.let { event ->
                event.getContentIfNotHandled()?.let { myListViewState ->

                    myListViewState.lists?.let {
                        viewModel.setLists(it)
                    }
                }
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try{
            dataStateHandler = context as DataStateListener
        }catch(e: ClassCastException){
            println("$context must implement DataStateListener")
        }
    }
}