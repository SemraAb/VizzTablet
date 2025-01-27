package com.samraa.data.di

import com.samraa.data.api.client.VizzApiClient
import org.koin.dsl.module

var appModuleApi = module {
    single { VizzApiClient.provideAuthApi() }
    single { VizzApiClient.provideHomeApi() }
}