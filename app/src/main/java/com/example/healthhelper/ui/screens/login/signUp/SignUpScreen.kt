package com.example.healthhelper.ui.screens.login.signUp

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.healthhelper.ui.Navigation
import com.example.healthhelper.R
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.ui.screens.Screen
import com.example.healthhelper.ui.viewModels.SignUpScreenViewModel

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpScreenViewModel,
) {
    val context = LocalContext.current

    val signUpScreenUiState by viewModel.signUpScreenUiState.collectAsState()

    val resultOfRequest = viewModel.resultOfRequest.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxHeight(0.1f),
        )
        Text(
            text = stringResource(id = R.string.create_account),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(
            modifier = Modifier
                .height(8.dp),
        )
        Text(
            text = stringResource(id = R.string.input_login_add_password),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(
            modifier = Modifier
                .fillMaxHeight(0.1f),
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = signUpScreenUiState.email,
            maxLines = 1,
            singleLine = true,
            onValueChange = {
                viewModel.updateEmail(email = it)
            },
            label = {
                Text(text = stringResource(id = R.string.input_email))
            },
            isError = signUpScreenUiState.emailErrorMessage != null,
            supportingText = {
                Text(
                    text = signUpScreenUiState.emailErrorMessage?.let {
                        stringResource(id = it)
                    } ?: ""
                )
            }
        )
        Spacer(
            modifier = Modifier
                .height(8.dp),
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = signUpScreenUiState.password,
            maxLines = 1,
            singleLine = true,
            onValueChange = {
                viewModel.updatePassword(password = it)
            },
            visualTransformation = PasswordVisualTransformation(),
            label = {
                Text(text = stringResource(id = R.string.input_password))
            },
            isError = signUpScreenUiState.passwordErrorMessage != null,
            supportingText = {
                Text(
                    text = signUpScreenUiState.passwordErrorMessage?.let {
                        stringResource(id = it)
                    } ?: ""
                )
            }
        )
        Spacer(
            modifier = Modifier
                .height(8.dp),
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = signUpScreenUiState.passwordAgain,
            maxLines = 1,
            singleLine = true,
            onValueChange = {
                viewModel.updatePasswordAgain(
                    passwordAgain = it,
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            label = {
                Text(
                    text = stringResource(id = R.string.repeat_password),
                )
            },
            isError = signUpScreenUiState.passwordAgainErrorMessage != null,
            supportingText = {
                Text(
                    text = signUpScreenUiState.passwordAgainErrorMessage?.let {
                        stringResource(id = it)
                    } ?: ""
                )
            }
        )
        Spacer(
            modifier = Modifier
                .height(8.dp),
        )
        FilledTonalButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                if (signUpScreenUiState.emailErrorMessage == null
                    && signUpScreenUiState.passwordErrorMessage == null
                    && signUpScreenUiState.passwordAgainErrorMessage == null
                ) {
                    viewModel.signUp()
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.wrong_fields),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },

            ) {
            Text(
                text = stringResource(id = R.string.sign_up),
            )
        }
        Spacer(
            modifier = Modifier
                .height(8.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Divider(
                modifier = Modifier
                    .weight(1f),
                color = MaterialTheme.colorScheme.outline,
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 4.dp),
                text = stringResource(id = R.string.or_sign_in),
                color = MaterialTheme.colorScheme.outline,
            )
            Divider(
                modifier = Modifier
                    .weight(1f),
                color = MaterialTheme.colorScheme.outline,
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
                navController.navigate(Screen.SignInScreen.route)
            },
        ) {
            Text(
                text = stringResource(id = R.string.sign_in),
            )
        }

        LaunchedEffect(resultOfRequest) {
            when (resultOfRequest) {
                is ResultOfRequest.Success -> {
                    navController.navigate(Navigation.MAIN_ROUTE) {
                        popUpTo(Navigation.AUTH_ROUTE)
                    }
                }

                is ResultOfRequest.Error ->
                    Toast.makeText(
                        context,
                        resultOfRequest.errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()

                is ResultOfRequest.Loading -> {}
            }
        }
    }
}