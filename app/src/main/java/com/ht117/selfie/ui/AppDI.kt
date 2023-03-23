package com.ht117.selfie.ui

import com.ht117.selfie.ui.screen.home.HomeViewModel
import com.ht117.selfie.ui.screen.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
}
