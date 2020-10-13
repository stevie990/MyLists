package com.sserra.mylists.framework.presentation.lists

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.sserra.mylists.business.domain.model.MyList
import com.sserra.mylists.business.domain.model.MyListFactory
import com.sserra.mylists.model.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ListsViewModel @ViewModelInject constructor(
    private val repository: Repository,
    private val myListFactory: MyListFactory
) : ViewModel() {

    val lists = repository.getLists().asLiveData(Dispatchers.IO + viewModelScope.coroutineContext)

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
}