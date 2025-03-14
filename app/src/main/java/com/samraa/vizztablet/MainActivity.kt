package com.samraa.vizztablet

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.samraa.data.persistence.SessionManager
import com.samraa.vizztablet.databinding.ActivityMainBinding
import com.samraa.vizztablet.ui.base.BaseActivity
import com.samraa.vizztablet.utils.extension.findNavHostNavController

class MainActivity : BaseActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val navController by lazy(LazyThreadSafetyMode.NONE) {
        findNavHostNavController(R.id.nav_container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.lifecycleOwner = this@MainActivity
        checkLoginState()
        val window = window
        window.statusBarColor = getColor(R.color.white) // Ensure this matches your intended color

        // Ensure light status bar icons if needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        }


        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                Log.d("Device_Token", task.result.toString())
                val token = task.result
                SessionManager.firebaseToken = token
            })
    }

    private fun checkLoginState() {
        val isLoggedIn = SessionManager.loggedIn
        if (isLoggedIn) {
            navController.navigate(R.id.homeFragment)
        } else {
            navController.navigate(R.id.loginFragment)
        }
    }

}