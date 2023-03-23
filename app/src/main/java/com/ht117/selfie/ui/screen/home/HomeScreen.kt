package com.ht117.selfie.ui.screen.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ht117.selfie.R
import com.ht117.selfie.data.State
import com.ht117.selfie.data.source.local.MyImage
import com.ht117.selfie.databinding.FragmentHomeBinding
import com.ht117.selfie.ext.viewBinding
import com.ht117.selfie.ui.adapter.PhotoAdapter
import com.ht117.selfie.ui.screen.base.BaseScreen
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeScreen: BaseScreen(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModel()
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private var adapter: PhotoAdapter? = PhotoAdapter()

    override fun setupView() {
        super.setupView()
        binding.run {
            rvPhotos.adapter = adapter
            fabTakePhoto.setOnClickListener {
                navigate(R.id.home_to_selfie)
            }
            fabLogout.setOnClickListener {
                AlertDialog.Builder(requireContext())
                    .setMessage(R.string.do_you_want_logout)
                    .setPositiveButton(R.string.ok) { dialog, _ ->
                        viewModel.logout()
                        dialog.dismiss()
                    }
                    .setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }
    }

    override fun setupLogic() {
        super.setupLogic()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.uiState.collect {
                    if (it.isLogout) {
                        navigate(R.id.home_to_login)
                    } else if (it.images != null) {
                        render(it.images)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter = null
    }

    private fun render(state: State<List<MyImage>>) {
        binding.run {
            when (state) {
                is State.Loading -> {
                    rvPhotos.isVisible = false
                    tvMessage.isVisible = false
                    loader.isVisible = true
                }
                is State.Failed -> {
                    rvPhotos.isVisible = false
                    loader.isVisible = false
                    tvMessage.isVisible = true
                    tvMessage.setText(R.string.empty_images)
                }
                is State.Result -> {
                    loader.isVisible = false
                    rvPhotos.isVisible = state.data.isNotEmpty()
                    tvMessage.isVisible = state.data.isEmpty()

                    tvMessage.setText(R.string.empty_images)
                    adapter?.dispatchData(state.data)
                }
            }
        }
    }
}
