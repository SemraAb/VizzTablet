package com.samraa.vizztablet.utils.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavHost

fun AppCompatActivity.findNavHostNavController(id: Int): NavController {
    val navHost = supportFragmentManager.findFragmentById(id) as NavHost
    return navHost.navController
}



