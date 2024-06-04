package com.example.healthhelper.ui.screens.main.editAppointment

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.healthhelper.R
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.ui.screens.DateMaterialDialog
import com.example.healthhelper.ui.screens.Screen
import com.example.healthhelper.ui.screens.TimeMaterialDialog
import com.example.healthhelper.ui.viewModels.EditAppointmentScreenViewModel
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@Composable
fun EditAppointmentScreen(
    navController: NavController,
    viewModel: EditAppointmentScreenViewModel,
) {
    val context = LocalContext.current

    val addAppointmentUiState by viewModel.addAppointmentScreenUiState.collectAsState()

    val resultOfRequest = viewModel.resultOfEditingAppointment.collectAsState().value

    val dateDialogState = rememberMaterialDialogState()

    val timeDialogState = rememberMaterialDialogState()

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
            text = stringResource(id = R.string.add_appointment),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(
            modifier = Modifier
                .height(8.dp),
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = addAppointmentUiState.doctorSpecialty,
            maxLines = 1,
            singleLine = true,
            onValueChange = {
                viewModel.updateDoctorSpecialty(doctorSpecialty = it)
            },
            label = {
                Text(
                    text = stringResource(id = R.string.input_doctor_speciality),
                )
            },
            isError = addAppointmentUiState.doctorSpecialtyErrorMessage != null,
            supportingText = {
                Text(
                    text = addAppointmentUiState.doctorSpecialtyErrorMessage?.let {
                        stringResource(id = it)
                    } ?: ""
                )
            },
        )
        Spacer(
            modifier = Modifier
                .height(8.dp),
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = addAppointmentUiState.doctorName,
            maxLines = 1,
            singleLine = true,
            onValueChange = {
                viewModel.updateDoctorName(doctorName = it)
            },
            label = {
                Text(
                    text = stringResource(id = R.string.input_doctor_name),
                )
            },
            isError = addAppointmentUiState.doctorNameErrorMessage != null,
            supportingText = {
                Text(
                    text = addAppointmentUiState.doctorNameErrorMessage?.let {
                        stringResource(id = it)
                    } ?: ""
                )
            },
        )
        Spacer(
            modifier = Modifier
                .height(8.dp),
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = addAppointmentUiState.address,
            maxLines = 1,
            singleLine = true,
            onValueChange = {
                viewModel.updateAddress(it)
            },
            label = {
                Text(
                    text = stringResource(id = R.string.input_address),
                )
            },
            isError = addAppointmentUiState.addressErrorMessage != null,
            supportingText = {
                Text(
                    text = addAppointmentUiState.addressErrorMessage?.let {
                        stringResource(id = it)
                    } ?: ""
                )
            },
        )
        Spacer(
            modifier = Modifier
                .height(8.dp),
        )
        if (addAppointmentUiState.visited) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = addAppointmentUiState.recommendations ?: "",
                onValueChange = {
                    viewModel.updateRecommendations(it)
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.input_recommendations),
                    )
                },
                isError = addAppointmentUiState.recommendationsErrorMessage != null,
                supportingText = {
                    Text(
                        text = addAppointmentUiState.recommendationsErrorMessage?.let {
                            stringResource(id = it)
                        } ?: ""
                    )
                },
            )
            Spacer(
                modifier = Modifier
                    .height(8.dp),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = addAppointmentUiState.formattedDate,
                fontSize = 20.sp,
            )
            IconButton(
                onClick = {
                    dateDialogState.show()
                },
            ) {
                Icon(
                    modifier = Modifier
                        .padding(4.dp),
                    painter = painterResource(R.drawable.ic_calendar),
                    contentDescription = stringResource(id = R.string.icon_calendar),
                )
            }
        }
        Spacer(
            modifier = Modifier
                .height(8.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = addAppointmentUiState.formattedTime,
                fontSize = 20.sp,
            )
            IconButton(
                onClick = {
                    timeDialogState.show()
                },
            ) {
                Icon(
                    modifier = Modifier
                        .padding(4.dp),
                    painter = painterResource(R.drawable.ic_watch),
                    contentDescription = stringResource(id = R.string.icon_watch),
                )
            }
        }
        FilledTonalButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                if (addAppointmentUiState.doctorSpecialtyErrorMessage == null &&
                    addAppointmentUiState.doctorNameErrorMessage == null &&
                    addAppointmentUiState.addressErrorMessage == null
                ) {
                    viewModel.editAnalysis()
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
                text = stringResource(id = R.string.save),
            )
        }
    }
    DateMaterialDialog(dateDialogState = dateDialogState, updateData = viewModel::updateDate)
    TimeMaterialDialog(timeDialogState = timeDialogState, updateData = viewModel::updateTime)

    LaunchedEffect(resultOfRequest) {
        when (resultOfRequest) {
            is ResultOfRequest.Success<Unit> -> {
                navController.navigate(Screen.AppointmentScreen.route)
            }

            is ResultOfRequest.Error -> {
                Toast.makeText(
                    context,
                    resultOfRequest.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is ResultOfRequest.Loading -> {}
        }
    }
}