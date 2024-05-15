package com.example.healthhelper.ui.screens.login

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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.healthhelper.R
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.ui.screens.Screen
import com.example.healthhelper.ui.theme.md_theme_light_primary
import com.example.healthhelper.ui.viewModels.SignInScreenViewModel

@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: SignInScreenViewModel
){

    val context = LocalContext.current

    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()

    val isEmailCorrect by viewModel.isEmailCorrect.collectAsState()
    val isPasswordCorrect by viewModel.isPasswordCorrect.collectAsState()
    val emailErrorMessage by viewModel.emailErrorMessage.collectAsState()
    val passwordErrorMessage by viewModel.passwordErrorMessage.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)

    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        Text(
            text = context.getString(R.string.sign_in_account),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = context.getString(R.string.input_login_add_password),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        OutlinedTextField(
            value = email,
            maxLines = 1,
            singleLine = true,
            onValueChange = {
                viewModel.updateEmail(it, context)
            },
            modifier = Modifier
                .fillMaxWidth(),
            label = {
                Text(text = context.getString(R.string.input_email))
            },
            isError = !isEmailCorrect,
            supportingText = {
                if (isEmailCorrect)
                    Text("")
                else
                    Text(emailErrorMessage)
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            maxLines = 1,
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = {
                viewModel.updatePassword(it, context)
            },
            modifier = Modifier
                .fillMaxWidth(),
            label = {
                Text(text = context.getString(R.string.input_password))
            },
            isError = !isPasswordCorrect,
            supportingText = {
                if (isPasswordCorrect)
                    Text("")
                else
                    Text(passwordErrorMessage)
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        FilledTonalButton(
            onClick = {
                if (isEmailCorrect && isPasswordCorrect){
                    viewModel.signIn(email, password)
                }else{
                    Toast.makeText(
                        context,
                        context.getString(R.string.wrong_fields),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(text = context.getString(R.string.sign_in))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(
                modifier = Modifier
                    .weight(1f),
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                modifier = Modifier.padding(4.dp, 0.dp),
                text = context.getString(R.string.or_sign_up),
                color = MaterialTheme.colorScheme.outline
            )
            Divider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.outline
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        FilledTonalButton(
            onClick = {
                navController.navigate(Screen.SignUpScreen.route)
            },
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(text = context.getString(R.string.sign_up))
        }

        LaunchedEffect(viewModel.resultOfRequest) {
            viewModel.resultOfRequest.collect{result ->
                when(result){
                    is ResultOfRequest.Success ->
                        Toast.makeText(
                            context,
                            "Успех",
                            Toast.LENGTH_SHORT
                        ).show()
                    is ResultOfRequest.Error ->
                        Toast.makeText(
                            context,
                            result.errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    else -> {}
                }
            }
        }
    }
}