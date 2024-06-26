package com.example.healthhelper.ui

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
    private const val ANALYSIS_ROUTE = "analysisRoute"
    private const val APPOINTMENT_ROUTE = "appointmentRoute"
    private const val DIARY_ROUTE = "diaryRoute"
    private const val ACCOUNT_ROUTE = "accountRoute"

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
    private fun BottomNavigation(
        authNavController: NavController,
    ) {

        val analysisScreenViewModel = hiltViewModel<AnalysisScreenViewModel>()
        val appointmentScreenViewModel = hiltViewModel<AppointmentsScreenViewModel>()
        val diaryScreenViewModel = hiltViewModel<DiaryScreenViewModel>()

        Surface {
            val bottomItems = listOf(
                Screen.AnalysisScreen.route,
                Screen.AppointmentScreen.route,
                Screen.DiaryScreen.route,
                Screen.AccountScreen.route,
            )
            val bottomNavController = rememberNavController()

            Scaffold(
                bottomBar = {
                    BottomAppBar {
                        val currentRoute =
                            bottomNavController.currentBackStackEntryAsState().value?.destination?.route
                        bottomItems.forEach { route ->
                            NavigationBarItem(
                                selected = currentRoute == route,
                                onClick = {
                                    if (currentRoute != route) {
                                        bottomNavController.navigate(route) {
                                            popUpTo(bottomNavController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                        }
                                    }
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
                NavHost(
                    navController = bottomNavController,
                    startDestination = ANALYSIS_ROUTE,
                    modifier = Modifier.padding(it),
                ) {
                    navigation(
                        startDestination = Screen.AnalysisScreen.route,
                        route = ANALYSIS_ROUTE,
                    ) {
                        composable(
                            route = Screen.AnalysisScreen.route,
                        ) {
                            AnalysisScreen(
                                navController = bottomNavController,
                                viewModel = analysisScreenViewModel,
                            )
                        }
                        composable(
                            route = Screen.AddAnalysisScreen.route,
                        ) {
                            val viewModel = hiltViewModel<AddAnalysisScreenViewModel>()
                            AddAnalysisScreen(
                                navController = bottomNavController,
                                viewModel = viewModel,
                                analysisScreenViewModel = analysisScreenViewModel,
                            )
                        }
                        composable(
                            route = Screen.AnalysisDetailsScreen.route,
                        ) {
                            val viewModel = hiltViewModel<AnalysisDetailsScreenViewModel>()
                            AnalysisDetailsScreen(
                                navController = bottomNavController,
                                viewModel = viewModel,
                            )
                        }
                        composable(
                            route = Screen.EditAnalysisScreen.route,
                        ) {
                            val viewModel = hiltViewModel<EditAnalysisScreenViewModel>()
                            EditAnalysisScreen(
                                navController = bottomNavController,
                                viewModel = viewModel,
                            )
                        }
                    }
                    navigation(
                        startDestination = Screen.AppointmentScreen.route,
                        route = APPOINTMENT_ROUTE,
                    ) {
                        composable(
                            route = Screen.AppointmentScreen.route,
                        ) {
                            AppointmentsScreen(
                                navController = bottomNavController,
                                viewModel = appointmentScreenViewModel,
                            )
                        }
                        composable(
                            route = Screen.AddAppointmentScreen.route,
                        ) {
                            val viewModel = hiltViewModel<AddAppointmentViewModel>()
                            AddAppointmentScreen(
                                navController = bottomNavController,
                                viewModel = viewModel,
                                appointmentViewModel = appointmentScreenViewModel,
                            )
                        }
                        composable(
                            route = Screen.AppointmentDetailsScreen.route,
                        ) {
                            val viewModel = hiltViewModel<AppointmentDetailsScreenViewModel>()
                            AppointmentDetailsScreen(
                                navController = bottomNavController,
                                viewModel = viewModel,
                            )
                        }
                        composable(
                            route = Screen.EditAppointmentScreen.route,
                        ) {
                            val viewModel = hiltViewModel<EditAppointmentScreenViewModel>()
                            EditAppointmentScreen(
                                navController = bottomNavController,
                                viewModel = viewModel,
                            )
                        }

                    }
                    navigation(
                        startDestination = Screen.DiaryScreen.route,
                        route = DIARY_ROUTE,
                    ) {
                        composable(
                            route = Screen.DiaryScreen.route,
                        ) {
                            DiaryScreen(
                                navController = bottomNavController,
                                viewModel = diaryScreenViewModel,
                            )
                        }
                        composable(
                            route = Screen.AddDiaryEntryScreen.route,
                        ) {
                            val viewModel = hiltViewModel<AddDiaryEntryScreenViewModel>()
                            AddDiaryEntryScreen(
                                navController = bottomNavController,
                                viewModel = viewModel,
                                diaryScreenViewModel = diaryScreenViewModel,
                            )
                        }
                        composable(
                            route = Screen.DiaryEntryDetailsScreen.route,
                        ) {
                            val viewModel = hiltViewModel<DiaryEntryDetailsScreenViewModel>()
                            DiaryEntryDetailsScreen(
                                navController = bottomNavController,
                                viewModel = viewModel,
                            )
                        }
                        composable(
                            route = Screen.EditDiaryEntryScreen.route,
                        ) {
                            val viewModel = hiltViewModel<EditDiaryEntryScreenViewModel>()
                            EditDiaryEntryScreen(
                                navController = bottomNavController,
                                viewModel = viewModel,
                            )
                        }
                    }
                    navigation(
                        startDestination = Screen.AccountScreen.route,
                        route = ACCOUNT_ROUTE,
                    ) {
                        composable(
                            route = Screen.AccountScreen.route,
                        ) {
                            val viewModel = hiltViewModel<AccountScreenViewModel>()
                            viewModel.getUserEmail()
                            AccountScreen(
                                navController = bottomNavController,
                                viewModel = viewModel,
                                onNavigateToAuth = { authNavController.navigate(AUTH_ROUTE) }
                            )
                        }
                        composable(
                            route = Screen.ChangePasswordScreen.route,
                        ) {
                            val viewModel = hiltViewModel<ChangePasswordViewModel>()
                            ChangePasswordScreen(
                                navController = bottomNavController,
                                viewModel = viewModel,
                            )
                        }
                    }

                }
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