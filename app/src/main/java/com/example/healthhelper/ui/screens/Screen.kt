package com.example.healthhelper.ui.screens

sealed class Screen(val route: String) {

    data object SignInScreen : Screen("sign_in_screen")
    data object SignUpScreen : Screen("sign_up_screen")
    data object AnalysisScreen : Screen("analysis_screen")
    data object AddAnalysisScreen : Screen("add_analysis_screen")
    data object AppointmentScreen : Screen("appointment_screen")
    data object DiaryScreen : Screen("diary_screen")
    data object AccountScreen : Screen("account_screen")
    data object ChangePasswordScreen : Screen("change_password_screen")
    data object SplashScreen : Screen("splash_screen")

}