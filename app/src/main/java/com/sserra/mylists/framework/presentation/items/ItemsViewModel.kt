package com.sserra.mylists.framework.presentation.items

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.recyclerview.selection.Selection
import com.sserra.mylists.business.domain.model.Item
import com.sserra.mylists.business.domain.model.ItemFactory
import com.sserra.mylists.business.domain.state.DataState
import com.sserra.mylists.business.interactors.items.AddItem
import com.sserra.mylists.business.interactors.items.GetItems
import com.sserra.mylists.framework.presentation.items.state.ItemStateEvent
import com.sserra.mylists.framework.presentation.items.state.ItemViewState
import com.sserra.mylists.model.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ItemsViewModel @ViewModelInject constructor(
    private val repository: Repository,
    private val itemFactory: ItemFactory,
    private val getItems: GetItems,
    private val addItem: AddItem,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val listId: String =
        savedStateHandle["listId"] ?: throw kotlin.IllegalArgumentException("List id required")

//    val items = repository.getItems(listId).asLiveData(Dispatchers.IO + viewModelScope.coroutineContext)

//    fun addNewItem(item: Item) {
//        viewModelScope.launch {
//            repository.addNewItem(listId, item)
//        }
//    }

    fun completeItem(item: Item, completed: Boolean) {
        item.isCompleted = completed
        viewModelScope.launch {
            repository.completeItem(listId, item)
        }
    }

    fun deleteItems(selectedItems: Selection<String>) {
        viewModelScope.launch {
            repository.deleteItems(listId, selectedItems)
        }
    }

    fun createNewItem(id: String, title: String, description: String)
            = itemFactory.createSingleItem(id, title, description)

    // *** MVI logic *****************************

    private val _viewState: MutableLiveData<ItemViewState> = MutableLiveData()
    val viewState: LiveData<ItemViewState>
        get() = _viewState

    private val _dataState: MutableLiveData<DataState<ItemViewState>> = MutableLiveData()
    val dataState: LiveData<DataState<ItemViewState>>
        get() = _dataState

    fun setStateEvent(stateEvent: ItemStateEvent) {

        when (stateEvent) {

            is ItemStateEvent.GetItemsEvent -> {
                viewModelScope.launch {
                    getItems.execute(listId).onEach {
                        _dataState.value = it
                    }.launchIn(viewModelScope)
                }
            }

            is ItemStateEvent.AddItemEvent -> {
                viewModelScope.launch {
                    addItem.execute(stateEvent.item, listId).onEach {
                        _dataState.value = it
                    }.launchIn(viewModelScope)
                }
            }

            is ItemStateEvent.None -> {

            }
        }
    }

    fun setItems(items: LiveData<List<Item>>){
        val updateViewState = getCurrentViewStateOrNew()
        updateViewState.items = items
        _viewState.value = updateViewState
    }

    private fun getCurrentViewStateOrNew(): ItemViewState {
        return viewState.value ?: ItemViewState()
    }
}