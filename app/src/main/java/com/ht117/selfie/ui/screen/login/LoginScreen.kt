package com.ht117.selfie.ui.screen.login

import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ht117.selfie.R
import com.ht117.selfie.data.State
import com.ht117.selfie.databinding.FragmentLoginBinding
import com.ht117.selfie.ext.hideKeyboard
import com.ht117.selfie.ext.viewBinding
import com.ht117.selfie.ui.screen.base.BaseScreen
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginScreen: BaseScreen(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel: LoginViewModel by viewModel()

    override fun setupView() {
        super.setupView()
        binding.run {
            btnLogin.setOnClickListener {
                val user = etLogin.text.toString()
                val pwd = etPassword.text.toString()
                if (isFormValid(user, pwd)) {
                    processLogin(user, pwd)
                } else {
                    tvMessage.isVisible = true
                    tvMessage.setText(R.string.input_is_invalid)
                }
            }
        }
    }

    override fun setupLogic() {
        super.setupLogic()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collect {
                    render(it)
                }
            }
        }
    }

    private fun render(state: State<Boolean>) {
        binding.run {
            when (state) {
                is State.Loading -> {
                    btnLogin.isEnabled = false
                    loader.isVisible = true
                }

                is State.Failed -> {
                    btnLogin.isEnabled = true
                    loader.isVisible = false
                    tvMessage.isVisible = true
                    // Process error message
                }
                is State.Result -> {
                    loader.isVisible = false
                    tvMessage.isVisible = false
                    if (state.data) {
                        navigate(R.id.login_to_home)
                    }
                }
            }
        }
    }

    private fun isFormValid(user: String, pwd: String): Boolean {
        return !user.isNullOrBlank() && !pwd.isNullOrBlank()
    }

    private fun processLogin(user: String, pwd: String) {
        binding.run {
            etPassword.hideKeyboard()
            viewModel.login(user, pwd)
        }
    }
}
