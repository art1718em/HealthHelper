package com.example.healthhelper.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.healthhelper.R
import com.example.healthhelper.ui.screens.Screen
import com.example.healthhelper.ui.screens.SplashScreen
import com.example.healthhelper.ui.screens.login.signIn.SignInScreen
import com.example.healthhelper.ui.screens.login.signUp.SignUpScreen
import com.example.healthhelper.ui.screens.main.account.AccountScreen
import com.example.healthhelper.ui.screens.main.changePassword.ChangePasswordScreen
import com.example.healthhelper.ui.screens.main.addAnalysis.AddAnalysisScreen
import com.example.healthhelper.ui.screens.main.addAppointment.AddAppointmentScreen
import com.example.healthhelper.ui.screens.main.addDiaryEntry.AddDiaryEntryScreen
import com.example.healthhelper.ui.screens.main.analysis.AnalysisScreen
import com.example.healthhelper.ui.screens.main.analysisDetails.AnalysisDetailsScreen
import com.example.healthhelper.ui.screens.main.appointmentDetails.AppointmentDetailsScreen
import com.example.healthhelper.ui.screens.main.appointments.AppointmentsScreen
import com.example.healthhelper.ui.screens.main.diary.DiaryScreen
import com.example.healthhelper.ui.screens.main.diaryEntryDetails.DiaryEntryDetailsScreen
import com.example.healthhelper.ui.screens.main.editAnalysis.EditAnalysisScreen
import com.example.healthhelper.ui.screens.main.editAppointment.EditAppointmentScreen
import com.example.healthhelper.ui.screens.main.editDiaryEntry.EditDiaryEntryScreen
import com.example.healthhelper.ui.viewModels.AccountScreenViewModel
import com.example.healthhelper.ui.viewModels.AddAnalysisScreenViewModel
import com.example.healthhelper.ui.viewModels.AddAppointmentViewModel
import com.example.healthhelper.ui.viewModels.AddDiaryEntryScreenViewModel
import com.example.healthhelper.ui.viewModels.AnalysisDetailsScreenViewModel
import com.example.healthhelper.ui.viewModels.AnalysisScreenViewModel
import com.example.healthhelper.ui.viewModels.AppointmentDetailsScreenViewModel
import com.example.healthhelper.ui.viewModels.AppointmentsScreenViewModel
import com.example.healthhelper.ui.viewModels.ChangePasswordViewModel
import com.example.healthhelper.ui.viewModels.DiaryEntryDetailsScreenViewModel
import com.example.healthhelper.ui.viewModels.DiaryScreenViewModel
import com.example.healthhelper.ui.viewModels.EditAnalysisScreenViewModel
import com.example.healthhelper.ui.viewModels.EditAppointmentScreenViewModel
import com.example.healthhelper.ui.viewModels.EditDiaryEntryScreenViewModel
import com.example.healthhelper.ui.viewModels.SignInScreenViewModel
import com.example.healthhelper.ui.viewModels.SignUpScreenViewModel
import com.example.healthhelper.ui.viewModels.SplashScreenViewModel

object Navigation {

    const val AUTH_ROUTE = "authRoute"
    const val MAIN_ROUTE = "mainRoute"
    const val SPLASH_ROUTE = "splashRoute"

    @Composable
    fun Navigation() {

        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = SPLASH_ROUTE,
        ) {
            navigation(
                startDestination = Screen.SplashScreen.route,
                route = SPLASH_ROUTE,
            ) {
                composable(
                    route = Screen.SplashScreen.route,
                ) {
                    val viewModel = hiltViewModel<SplashScreenViewModel>()
                    SplashScreen(
                        navController = navController,
                        viewModel = viewModel,
                    )
                }
            }
            navigation(
                startDestination = Screen.SignInScreen.route,
                route = AUTH_ROUTE,
            ) {
                composable(
                    route = Screen.SignInScreen.route,
                ) {
                    val viewModel = hiltViewModel<SignInScreenViewModel>()
                    SignInScreen(
                        navController = navController,
                        viewModel = viewModel,
                    )
                }
                composable(
                    route = Screen.SignUpScreen.route,
                ) {
                    val viewModel = hiltViewModel<SignUpScreenViewModel>()
                    SignUpScreen(
                        navController = navController,
                        viewModel = viewModel,
                    )
                }
            }
            composable(
                route = MAIN_ROUTE,
            ) {
                BottomNavigation(
                    authNavController = navController,
                )
            }
        }
    }

    @Composable
    fun BottomNavigation(
        authNavController: NavController,
    ) {
        val analysisNavController = rememberNavController()
        val appointmentNavController = rememberNavController()
        val diaryNavController = rememberNavController()
        val accountNavController = rememberNavController()
        val bottomItems = listOf(
            Screen.AnalysisScreen.route,
            Screen.AppointmentScreen.route,
            Screen.DiaryScreen.route,
            Screen.AccountScreen.route,
        )



        val currentTab = remember { mutableStateOf(Screen.AnalysisScreen.route) }

        Surface {
            Scaffold(
                bottomBar = {
                    BottomAppBar {
                        bottomItems.forEach { route ->
                            NavigationBarItem(
                                selected = currentTab.value == route,
                                onClick = {
                                    currentTab.value = route
                                },
                                icon = {
                                    IconOfBottomBar(route = route)
                                },
                                label = {
                                    LabelOfBottomBar(route = route)
                                },
                            )
                        }
                    }
                }
            ) {
                val paddingValues = it
                Box(Modifier.padding(paddingValues)) {
                    when (currentTab.value) {
                        Screen.AnalysisScreen.route -> {
                            AnalysisNavHost(navController = analysisNavController)
                        }
                        Screen.AppointmentScreen.route -> {
                            AppointmentNavHost(navController = appointmentNavController)
                        }
                        Screen.DiaryScreen.route -> {
                            DiaryNavHost(navController = diaryNavController)
                        }
                        Screen.AccountScreen.route -> {
                            AccountNavHost(navController = accountNavController, authNavController = authNavController)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun AnalysisNavHost(navController: NavHostController) {
        NavHost(
            navController = navController,
            startDestination = Screen.AnalysisScreen.route
        ) {
            composable(Screen.AnalysisScreen.route) {
                val analysisScreenViewModel = hiltViewModel<AnalysisScreenViewModel>()
                AnalysisScreen(navController = navController, viewModel = analysisScreenViewModel)
            }
            composable(Screen.AddAnalysisScreen.route) {
                val viewModel = hiltViewModel<AddAnalysisScreenViewModel>()
                AddAnalysisScreen(navController = navController, viewModel = viewModel, analysisScreenViewModel = hiltViewModel())
            }
            composable(Screen.AnalysisDetailsScreen.route) {
                val viewModel = hiltViewModel<AnalysisDetailsScreenViewModel>()
                AnalysisDetailsScreen(navController = navController, viewModel = viewModel)
            }
            composable(Screen.EditAnalysisScreen.route) {
                val viewModel = hiltViewModel<EditAnalysisScreenViewModel>()
                EditAnalysisScreen(navController = navController, viewModel = viewModel)
            }
        }
    }

    @Composable
    fun AppointmentNavHost(navController: NavHostController) {
        NavHost(
            navController = navController,
            startDestination = Screen.AppointmentScreen.route
        ) {
            composable(Screen.AppointmentScreen.route) {
                val appointmentScreenViewModel = hiltViewModel<AppointmentsScreenViewModel>()
                AppointmentsScreen(navController = navController, viewModel = appointmentScreenViewModel)
            }
            composable(Screen.AddAppointmentScreen.route) {
                val viewModel = hiltViewModel<AddAppointmentViewModel>()
                AddAppointmentScreen(navController = navController, viewModel = viewModel, appointmentViewModel = hiltViewModel())
            }
            composable(Screen.AppointmentDetailsScreen.route) {
                val viewModel = hiltViewModel<AppointmentDetailsScreenViewModel>()
                AppointmentDetailsScreen(navController = navController, viewModel = viewModel)
            }
            composable(Screen.EditAppointmentScreen.route) {
                val viewModel = hiltViewModel<EditAppointmentScreenViewModel>()
                EditAppointmentScreen(navController = navController, viewModel = viewModel)
            }
        }
    }

    @Composable
    fun DiaryNavHost(navController: NavHostController) {
        NavHost(
            navController = navController,
            startDestination = Screen.DiaryScreen.route
        ) {
            composable(Screen.DiaryScreen.route) {
                val diaryScreenViewModel = hiltViewModel<DiaryScreenViewModel>()
                DiaryScreen(navController = navController, viewModel = diaryScreenViewModel)
            }
            composable(Screen.AddDiaryEntryScreen.route) {
                val viewModel = hiltViewModel<AddDiaryEntryScreenViewModel>()
                AddDiaryEntryScreen(navController = navController, viewModel = viewModel, diaryScreenViewModel = hiltViewModel())
            }
            composable(Screen.DiaryEntryDetailsScreen.route) {
                val viewModel = hiltViewModel<DiaryEntryDetailsScreenViewModel>()
                DiaryEntryDetailsScreen(navController = navController, viewModel = viewModel)
            }
            composable(Screen.EditDiaryEntryScreen.route) {
                val viewModel = hiltViewModel<EditDiaryEntryScreenViewModel>()
                EditDiaryEntryScreen(navController = navController, viewModel = viewModel)
            }
        }
    }

    @Composable
    fun AccountNavHost(navController: NavHostController, authNavController: NavController) {
        NavHost(
            navController = navController,
            startDestination = Screen.AccountScreen.route
        ) {
            composable(Screen.AccountScreen.route) {
                val viewModel = hiltViewModel<AccountScreenViewModel>()
                viewModel.getUserEmail()
                AccountScreen(navController = navController, viewModel = viewModel, onNavigateToAuth = { authNavController.navigate(AUTH_ROUTE) })
            }
            composable(Screen.ChangePasswordScreen.route) {
                val viewModel = hiltViewModel<ChangePasswordViewModel>()
                ChangePasswordScreen(navController = navController, viewModel = viewModel)
            }
        }
    }


}

@Composable
fun IconOfBottomBar(route: String) {

    var idOfIcon = R.drawable.ic_analysis
    var idOfContentDescription = R.string.icon_analyzes

    when (route) {
        Screen.AppointmentScreen.route -> {
            idOfIcon = R.drawable.ic_appointment
            idOfContentDescription = R.string.icon_appointments
        }

        Screen.DiaryScreen.route -> {
            idOfIcon = R.drawable.ic_diary
            idOfContentDescription = R.string.icon_diary
        }

        Screen.AccountScreen.route -> {
            idOfIcon = R.drawable.ic_account
            idOfContentDescription = R.string.icon_account
        }
    }
    Icon(
        painter = painterResource(id = idOfIcon),
        contentDescription = stringResource(id = idOfContentDescription),
        modifier = Modifier
            .height(32.dp)
            .width(32.dp)
            .padding(bottom = 4.dp),
    )
}

@Composable
fun LabelOfBottomBar(route: String) {

    var idOfText = R.string.analyzes

    when (route) {
        Screen.AppointmentScreen.route -> idOfText = R.string.appointments
        Screen.DiaryScreen.route -> idOfText = R.string.diary
        Screen.AccountScreen.route -> idOfText = R.string.account
    }
    Text(
        text = stringResource(id = idOfText),
    )
}
