package com.sserra.mylists.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.firebase.auth.FirebaseAuth
import com.sserra.mylists.model.FirebaseUserLiveData

class LoginViewModel : ViewModel() {

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    // region SignIn
    private val _signIn = MutableLiveData<Boolean>()
    val signIn: LiveData<Boolean>
        get() = _signIn

    fun signIn() {
        _signIn.value = true
    }

    fun signInComplete() {
        _signIn.value = null
    }
    //    endregion

    fun isAuthenticated() = FirebaseAuth.getInstance().currentUser != null
}