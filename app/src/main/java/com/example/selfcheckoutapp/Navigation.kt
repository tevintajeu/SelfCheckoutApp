package com.example.selfcheckoutapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.selfcheckoutapp.pages.Home
import com.example.selfcheckoutapp.pages.Login
import com.example.selfcheckoutapp.pages.Register

@Composable
fun Navigation(modifier: Modifier = Modifier,authViewModel : AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login", builder = {
        composable("login"){
            Login(modifier,navController,authViewModel)
        }
        composable("register"){
            Register(modifier,navController,authViewModel)
        }
        composable("home"){
            Home(modifier,navController,authViewModel)
        }
    })
}


