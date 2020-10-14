package com.sserra.mylists.framework.presentation

import com.sserra.mylists.business.domain.state.DataState

interface DataStateListener {

    fun onDataStateChange(dataState: DataState<*>?)
}