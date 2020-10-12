package com.sserra.mylists.framework.presentation.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.sserra.mylists.R

import com.sserra.mylists.databinding.FragmentLoginBinding
import timber.log.Timber

class LoginFragment : Fragment() {

    companion object {
        const val TAG = "LoginFragment"
        const val SIGN_IN_RESULT_CODE = 1001
    }

    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory()
    }


    private var _viewDataBinding: FragmentLoginBinding? = null
    private val viewDataBinding get() = _viewDataBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewDataBinding = FragmentLoginBinding.inflate(inflater, container, false)
            .apply {
                loginViewModel = viewModel
            }

        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return viewDataBinding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        if (viewModel.isAuthenticated()) {
            this.navigateToListFragment()
        }
//        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
//            when (authenticationState) {
//                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
//                    this.navigateToListFragment()
//                }
//            }
//        })

        viewModel.signIn.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.launchSignInFlow()
                viewModel.signInComplete()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_RESULT_CODE) {
//            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                this.navigateToListFragment()
                Timber.i("Successfully signed In")
            } else {
                Timber.i("UnSuccessfully signed In")
            }
        }
    }

    private fun launchSignInFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.Theme_MyList)
                .build(),
            SIGN_IN_RESULT_CODE
        )
    }

    private fun navigateToListFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToListsFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewDataBinding = null
    }
}
