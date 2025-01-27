package com.samraa.vizztablet.ui.auth.register

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import androidx.lifecycle.asLiveData
import androidx.multidex.BuildConfig
import androidx.navigation.fragment.findNavController
import com.samraa.vizztablet.databinding.FragmentRegisterBinding
import com.samraa.vizztablet.ui.base.BaseFragment
import com.samraa.vizztablet.utils.PermissionCallback
import com.samraa.vizztablet.utils.PermissionUtils
import com.samraa.vizztablet.utils.bindingAdapters.setOnSingleClickListener
import com.samraa.vizztablet.utils.extension.navigateSafely
import com.samraa.vizztablet.utils.extension.observe
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RegisterFragment : BaseFragment() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentRegisterBinding.inflate(
            layoutInflater
        )
    }

    override val viewModel by viewModel<RegisterVM> {
        parametersOf()
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                viewModel.logoImage.value = uri
                Toast.makeText(context, "Image Selected: $uri", Toast.LENGTH_SHORT).show()
                viewModel.isVisibleDone.value = true
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        return binding.root
    }

    override fun setClicks() {
        binding.signInTxt.setOnSingleClickListener {
            findNavController().popBackStack()
        }
        binding.uploadImgTxt.setOnSingleClickListener {
            requestPermissionFileUpload()
        }
        binding.singUpBtn.setOnSingleClickListener {
            viewModel.register()
        }
    }

    override fun subscribeToObservables() {
        viewModel.navigateToHome.asLiveData().observe(this) {
            findNavController().navigateSafely(RegisterFragmentDirections.actionRegisterFragmentToHomeFragment())
        }
    }

    private fun requestPermissionFileUpload() {
        PermissionUtils.requestReadExternalStoragePermission(
            requireContext(),
            object : PermissionCallback {
                override fun onPermissionRequest(granted: Boolean) {

                    if (granted) {
                        openGallery()
                    } else {
                        Toast.makeText(
                            context,
                            "Please Grant  Permission to use the app",
                            Toast.LENGTH_SHORT
                        ).show()
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                            )
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }, 2000)
                    }
                }

            })
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }
}