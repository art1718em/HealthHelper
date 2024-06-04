package com.example.healthhelper.ui.screens

sealed class Screen(val route: String) {

    data object SignInScreen : Screen("sign_in_screen")
    data object SignUpScreen : Screen("sign_up_screen")

    data object AnalysisScreen : Screen("analysis_screen")
    data object AnalysisDetailsScreen : Screen("analysis_details_screen")
    data object EditAnalysisScreen : Screen("edit_analysis_screen")
    data object AddAnalysisScreen : Screen("add_analysis_screen")

    data object AppointmentScreen : Screen("appointment_screen")
    data object AddAppointmentScreen : Screen("add_appointment_screen")
    data object AppointmentDetailsScreen : Screen("appointment_details_screen")
    data object EditAppointmentScreen : Screen("edit_appointment_screen")

    data object DiaryScreen : Screen("diary_screen")
    data object DiaryEntryDetailsScreen : Screen("diary_entry_details_screen")
    data object EditDiaryEntryScreen : Screen("edit_diary_entry_screen")
    data object AddDiaryEntryScreen : Screen("add_diary_entry_screen")

    data object AccountScreen : Screen("account_screen")
    data object ChangePasswordScreen : Screen("change_password_screen")

    data object SplashScreen : Screen("splash_screen")

}