plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.samraa.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }

    buildTypes {
        release {
            isMinifyEnabled = false

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    //KTX
    api(libs.androidx.core.ktx)

    //Koin
    api(libs.koin.core)
    api(libs.koin.androidx.workmanager)
    testApi(libs.koin.test)
    api(libs.koin.androidx.compose)

    //Retrofit
    api(libs.retrofit)
    api(libs.converter.gson)
    api(libs.gson)
    api(libs.logging.interceptor)
    api(libs.retrofit2.kotlin.coroutines.adapter)


    //security
    api(libs.androidx.security.crypto)

    //log
    api(libs.timber)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}