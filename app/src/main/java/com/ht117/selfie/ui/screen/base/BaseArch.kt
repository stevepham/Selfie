package com.ht117.selfie.ui.screen.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.ht117.selfie.data.State

interface IView<T> {
    fun render(state: State<T>)
}

abstract class BaseScreen(@LayoutRes layoutId: Int): Fragment(layoutId) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupLogic()
    }

    open fun setupView() {}

    open fun setupLogic() {}

    fun navigate(dest: Int, bundle: Bundle? = null, option: NavOptions? = null) {
        findNavController().navigate(dest, bundle, option)
    }

    fun navigateBack() {
        findNavController().navigateUp()
    }
}
