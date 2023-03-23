package com.ht117.selfie.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ht117.selfie.data.State
import com.ht117.selfie.data.repo.IUserRepo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepo: IUserRepo): ViewModel() {

    private val _uiState = MutableSharedFlow<State<Boolean>>(replay = 1)
    val uiState: SharedFlow<State<Boolean>>
        get() = _uiState

    init {
        viewModelScope.launch {
            userRepo.isLoggedIn()
                .collectLatest {
                    _uiState.emit(it)
                }
        }
    }

    fun login(user: String, pwd: String) = viewModelScope.launch {
        userRepo.login(user, pwd)
            .onEach {
                _uiState.tryEmit(it)
                if (it is State.Result && it.data) {
                    userRepo.setLoggedIn()
                }
            }
            .collect()
    }
}
