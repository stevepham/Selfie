package com.ht117.selfie.ext

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentViewBindingDelegate<VB: ViewBinding>(
    private val fragment: Fragment,
    val viewBindingFactory: (View) -> VB
): ReadOnlyProperty<Fragment, VB> {
    private var binding: VB? = null

    init {
        fragment.lifecycleScope.launch {
            fragment.lifecycle.repeatOnLifecycle(Lifecycle.State.DESTROYED) {
                binding = null
            }
        }
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
        if (binding != null && binding!!.root === thisRef.view) {
            return binding!!
        }

        val view = thisRef.view
            ?: throw IllegalStateException("Fragment view is null")
        return viewBindingFactory(view).also { this.binding = it }
    }
}

fun <VB: ViewBinding> Fragment.viewBinding(factory: (View) -> VB) =
    FragmentViewBindingDelegate(this, factory)
