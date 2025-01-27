package com.samraa.data.di

import com.samraa.data.api.repository.AuthRepo
import com.samraa.data.api.repository.HomeRepo
import org.koin.dsl.module

var appModuleRepo = module {
    factory { AuthRepo(get(), get()) }
    factory { HomeRepo(get()) }
}