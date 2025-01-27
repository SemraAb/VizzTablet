package com.samraa.vizztablet.ui.auth.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.samraa.data.persistence.SessionManager
import com.samraa.vizztablet.R
import com.samraa.vizztablet.databinding.FragmentLoginBinding
import com.samraa.vizztablet.ui.auth.register.RegisterFragmentDirections
import com.samraa.vizztablet.ui.base.BaseFragment
import com.samraa.vizztablet.utils.bindingAdapters.setOnSingleClickListener
import com.samraa.vizztablet.utils.extension.navigateSafely
import com.samraa.vizztablet.utils.extension.observe
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LoginFragment : BaseFragment() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentLoginBinding.inflate(
            layoutInflater
        )
    }

    override val viewModel by viewModel<LoginVM> {
        parametersOf()
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

        binding.signInButton.setOnSingleClickListener {
            viewModel.signIn()
        }

        binding.signUpTxt.setOnSingleClickListener {
            findNavController().navigateSafely(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
    }


    override fun subscribeToObservables() {
        viewModel.navigateToHome.asLiveData().observe(this) {
            findNavController().navigateSafely(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
        }
    }

}