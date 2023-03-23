package com.ht117.selfie.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ht117.selfie.data.State
import com.ht117.selfie.data.repo.IResourceRepo
import com.ht117.selfie.data.repo.IUserRepo
import com.ht117.selfie.data.source.local.MyImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class HomeState(val isLogout: Boolean,
                     val images: State<List<MyImage>>? = null)

class HomeViewModel(private val resourceRepo: IResourceRepo,
                    private val userRepo: IUserRepo): ViewModel() {

    private val _uiState = MutableSharedFlow<HomeState>()
    val uiState: SharedFlow<HomeState>
        get() = _uiState

    init {
        refresh()
    }

    fun refresh() = viewModelScope.launch {
        resourceRepo.getImages()
            .flowOn(Dispatchers.IO)
            .map {
                HomeState(false, it)
            }
            .onEach {
                _uiState.emit(it)
            }
            .collect()
    }

    fun logout() = viewModelScope.launch {
        userRepo.logout().collect {
            _uiState.emit(HomeState(isLogout = true))
        }
    }
}
