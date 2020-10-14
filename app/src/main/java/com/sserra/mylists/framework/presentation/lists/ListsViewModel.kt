package com.sserra.mylists.framework.presentation.lists

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.sserra.mylists.business.domain.model.MyList
import com.sserra.mylists.business.domain.model.MyListFactory
import com.sserra.mylists.business.domain.state.DataState
import com.sserra.mylists.business.interactors.AddList
import com.sserra.mylists.business.interactors.GetLists
import com.sserra.mylists.framework.presentation.lists.state.MyListStateEvent
import com.sserra.mylists.framework.presentation.lists.state.MyListStateEvent.GetMyListsEvent
import com.sserra.mylists.framework.presentation.lists.state.MyListViewState
import com.sserra.mylists.model.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ListsViewModel @ViewModelInject constructor(
    private val repository: Repository,
    private val myListFactory: MyListFactory,
    private val getLists: GetLists,
    private val addList: AddList,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

//    val lists = repository.getLists().asLiveData(Dispatchers.IO + viewModelScope.coroutineContext)

    private val _openList = MutableLiveData<MyList>()
    val openList: LiveData<MyList>
        get() = _openList

    fun displayList(list: MyList) {
        _openList.value = list
    }

    fun displayListComplete() {
        _openList.value = null
    }

    fun getListItemsCount(listId: String): LiveData<Int> {
        return repository.getListItemsCount(listId)
            .asLiveData(Dispatchers.IO + viewModelScope.coroutineContext)
    }

    fun addNewList(list: MyList) {
        viewModelScope.launch {
            repository.addNewList(list)
        }
    }

    fun createNewList(
        id: String,
        title: String,
        description: String
    ) = myListFactory.createList(id, title, description)

    // *** CLEAN MVI *****************************

    private val _stateEvent: MutableLiveData<MyListStateEvent> = MutableLiveData()
    private val _viewState: MutableLiveData<MyListViewState> = MutableLiveData()

    val viewState: LiveData<MyListViewState>
        get() = _viewState

    private val _dataState: MutableLiveData<DataState<MyListViewState>> = MutableLiveData()
    val dataState: LiveData<DataState<MyListViewState>>
        get() = _dataState

//            =
//        Transformations.switchMap(_stateEvent) { stateEvent ->
//            stateEvent?.let {
//                handleStateEvent(stateEvent)
//            }
//        }

//    fun setStateEvent(event: MyListStateEvent) {
//        _stateEvent.value = event
//    }


    fun setStateEvent(stateEvent: MyListStateEvent) {

//        val result = MutableLiveData<DataState<MyListViewState>>()

        when (stateEvent) {

            is GetMyListsEvent -> {

                viewModelScope.launch {
                    getLists.execute().onEach {
                        _dataState.value = it
                    }.launchIn(viewModelScope)
                }
            }

            is MyListStateEvent.AddListEvent -> {
                viewModelScope.launch {
                    addList.execute(stateEvent.list).onEach {
                        _dataState.value = it
                    }.launchIn(viewModelScope)
                }
            }

            is MyListStateEvent.None -> {
                // Do nothing
            }
        }
    }

    fun setLists(lists: LiveData<List<MyList>>){
        val updateViewState = getCurrentViewStateOrNew()
        updateViewState.lists = lists
        _viewState.value = updateViewState
    }

    private fun getCurrentViewStateOrNew(): MyListViewState {
        return viewState.value ?: MyListViewState()
    }

}