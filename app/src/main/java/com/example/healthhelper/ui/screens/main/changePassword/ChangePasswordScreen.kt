package com.example.healthhelper.ui.screens.main.changePassword

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
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
import com.example.healthhelper.R
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.ui.screens.Screen
import com.example.healthhelper.ui.viewModels.ChangePasswordViewModel

@Composable
fun ChangePasswordScreen(
    navController: NavController,
    viewModel: ChangePasswordViewModel,
) {
    val context = LocalContext.current

    val changePasswordScreenUiState by viewModel.changePasswordScreenUiState.collectAsState()

    val resultOfChangingPassword = viewModel.resultOfChangingPassword.collectAsState().value

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
            text = stringResource(id = R.string.change_password),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(
            modifier = Modifier
                .height(8.dp),
        )
        Text(
            text = stringResource(id = R.string.confirm_old_password_and_input_new),
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
            value = changePasswordScreenUiState.oldPassword,
            maxLines = 1,
            singleLine = true,
            onValueChange = {
                viewModel.updateOldPassword(oldPassword = it)
            },
            visualTransformation = PasswordVisualTransformation(),
            label = {
                Text(text = stringResource(id = R.string.input_old_password))
            },
            isError = changePasswordScreenUiState.oldPasswordErrorMessage != null,
            supportingText = {
                Text(
                    text = changePasswordScreenUiState.oldPasswordErrorMessage?.let {
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
            value = changePasswordScreenUiState.newPassword,
            maxLines = 1,
            singleLine = true,
            onValueChange = {
                viewModel.updateNewPassword(newPassword = it)
            },
            visualTransformation = PasswordVisualTransformation(),
            label = {
                Text(text = stringResource(id = R.string.input_new_password))
            },
            isError = changePasswordScreenUiState.newPasswordErrorMessage != null,
            supportingText = {
                Text(
                    text = changePasswordScreenUiState.newPasswordErrorMessage?.let {
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
            value = changePasswordScreenUiState.newPasswordAgain,
            maxLines = 1,
            singleLine = true,
            onValueChange = {
                viewModel.updateNewPasswordAgain(
                    newPasswordAgain = it
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            label = {
                Text(
                    text = stringResource(id = R.string.repeat_password),
                )
            },
            isError = changePasswordScreenUiState.newPasswordAgainErrorMessage != null,
            supportingText = {
                Text(
                    text = changePasswordScreenUiState.newPasswordAgainErrorMessage?.let {
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
                if (changePasswordScreenUiState.oldPasswordErrorMessage == null
                    && changePasswordScreenUiState.newPasswordErrorMessage == null
                    && changePasswordScreenUiState.newPasswordAgainErrorMessage == null
                ) {
                    viewModel.changePassword()
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
                text = stringResource(id = R.string.change_password),
            )
        }
    }

    LaunchedEffect(resultOfChangingPassword) {
        when (resultOfChangingPassword) {
            is ResultOfRequest.Success -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.successful_changing_password),
                    Toast.LENGTH_SHORT
                ).show()
                navController.navigate(Screen.AccountScreen.route) {
                    popUpTo(Screen.ChangePasswordScreen.route)
                }
            }

            is ResultOfRequest.Error -> {
                Toast.makeText(
                    context,
                    resultOfChangingPassword.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is ResultOfRequest.Loading -> {}
        }
    }
}