package com.example.healthhelper.ui.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.healthhelper.R
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.ui.Navigation
import com.example.healthhelper.ui.viewModels.SplashScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashScreenViewModel,
) {

    val scale = remember {
        androidx.compose.animation.core.Animatable(0f)
    }

    LaunchedEffect(key1 = true) {

        val job = launch(Dispatchers.Main) {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = {
                        OvershootInterpolator(2f).getInterpolation(it)
                    },
                ),
            )
        }

        launch {
            viewModel.checkUser()
        }

        job.join()
        viewModel.resultOfCheckingUser.collect { result ->
            navigationFromSplashScreen(
                resultOfRequest = result,
                navController = navController,
                onSuccess = viewModel::startLoadingUserData,
            )
        }
    }

    LaunchedEffect(viewModel.resultOfLoadingData) {
        viewModel.resultOfLoadingData.collect { result ->
            when (result) {
                is ResultOfRequest.Success -> {
                    navController.navigate(Navigation.MAIN_ROUTE) {
                        popUpTo(Navigation.SPLASH_ROUTE)
                    }
                }

                is ResultOfRequest.Error -> {
                    navController.navigate(Navigation.MAIN_ROUTE) {
                        popUpTo(Navigation.SPLASH_ROUTE)
                    }
                }

                ResultOfRequest.Loading -> {}
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,

        ) {
        Image(
            modifier = Modifier
                .scale(scale.value),
            painter = painterResource(id = R.drawable.ic_app),
            contentDescription = stringResource(id = R.string.app_logo),
        )
    }
}

fun navigationFromSplashScreen(
    resultOfRequest: ResultOfRequest<Unit>,
    navController: NavController,
    onSuccess: () -> Unit,
) {
    when (resultOfRequest) {
        is ResultOfRequest.Success -> {
            onSuccess()
        }

        is ResultOfRequest.Error -> navController.navigate(Navigation.AUTH_ROUTE) {
            popUpTo(Navigation.SPLASH_ROUTE)
        }

        is ResultOfRequest.Loading -> {}
    }
}