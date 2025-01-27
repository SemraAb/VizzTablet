package com.samraa.vizztablet.di

import com.samraa.vizztablet.ui.auth.login.LoginVM
import com.samraa.vizztablet.ui.auth.register.RegisterVM
import com.samraa.vizztablet.ui.home.HomeVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

var appModule = module {
    viewModel {
        LoginVM(get())
    }

    viewModel {
        RegisterVM(get())
    }

    viewModel {
        HomeVM(get())
    }
}