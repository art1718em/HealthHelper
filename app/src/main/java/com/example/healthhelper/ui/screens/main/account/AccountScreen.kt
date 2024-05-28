package com.example.healthhelper.ui.screens.main.account

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.healthhelper.R
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.ui.screens.Screen
import com.example.healthhelper.ui.viewModels.AccountScreenViewModel

@Composable
fun AccountScreen(
    navController: NavController,
    viewModel: AccountScreenViewModel,
    onNavigateToAuth: () -> Unit,
) {

    val userEmail by viewModel.userEmail.collectAsState()

    val resultOfLogOut = viewModel.resultOfLogOut.collectAsState().value

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(
            modifier = Modifier
                .height(16.dp),
        )
        Text(
            text = stringResource(id = R.string.your_profile),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
        )
        userEmail?.let {
            Text(
                text = it,
                fontSize = 24.sp,
            )
        }
        Spacer(
            modifier = Modifier
                .height(8.dp),
        )
        FilledTonalButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                navController.navigate(Screen.ChangePasswordScreen.route)
            },
        ) {
            Text(
                text = stringResource(id = R.string.change_password),
            )
        }
        Spacer(
            modifier = Modifier
                .height(8.dp),
        )
        FilledTonalButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                viewModel.logOut()
            },
        ) {
            Text(
                text = stringResource(id = R.string.log_out),
            )
        }
    }

    LaunchedEffect(resultOfLogOut) {
        when (resultOfLogOut) {
            is ResultOfRequest.Success -> {
                onNavigateToAuth()
            }

            is ResultOfRequest.Error -> {
                Toast.makeText(
                    context,
                    resultOfLogOut.errorMessage,
                    Toast.LENGTH_SHORT,
                ).show()
            }

            is ResultOfRequest.Loading -> {}
        }
    }
}