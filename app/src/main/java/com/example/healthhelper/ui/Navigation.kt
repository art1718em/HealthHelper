package com.example.healthhelper.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.healthhelper.ui.screens.Screen
import com.example.healthhelper.ui.screens.login.SignInScreen
import com.example.healthhelper.ui.screens.login.SignUpScreen
import com.example.healthhelper.ui.viewModels.SignInScreenViewModel
import com.example.healthhelper.ui.viewModels.SignInScreenViewModel_HiltModules
import com.example.healthhelper.ui.viewModels.SignUpScreenViewModel

@Composable
fun Navigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.SignInScreen.route,
    ){
        composable(route = Screen.SignInScreen.route){
            val viewModel = hiltViewModel<SignInScreenViewModel>()
            SignInScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = Screen.SignUpScreen.route){
            val viewModel = hiltViewModel<SignUpScreenViewModel>()
            SignUpScreen(navController = navController, viewModel = viewModel)
        }
    }
}