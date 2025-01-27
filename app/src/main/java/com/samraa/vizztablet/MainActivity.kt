package com.samraa.vizztablet

import android.os.Bundle
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
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                val token = task.result
                SessionManager.firebaseToken = token
            })
    }

}