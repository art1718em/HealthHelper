package com.example.healthhelper.ui.screens.main.appointmentDetails

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.healthhelper.R
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.ui.screens.AlertDialogWarning
import com.example.healthhelper.ui.screens.Screen
import com.example.healthhelper.ui.screens.TextInputDialog
import com.example.healthhelper.ui.viewModels.AppointmentDetailsScreenViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppointmentDetailsScreen(
    navController: NavController,
    viewModel: AppointmentDetailsScreenViewModel,
) {
    val context = LocalContext.current

    val appointment by viewModel.appointment.collectAsState()

    val resultOfDeletionAppointment = viewModel.resultOfDeletionAppointment.collectAsState().value

    val resultOfAddingRecommendations =
        viewModel.resultOfAddingRecommendations.collectAsState().value

    var openAlertDialogWarning by remember {
        mutableStateOf(false)
    }

    var openInputTextDialog by remember {
        mutableStateOf(false)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.EditAppointmentScreen.route)
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = stringResource(id = R.string.icon_edit),
                )
            }
        }
    ) {
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
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.detailed_description),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
                IconButton(
                    modifier = Modifier
                        .height(32.dp)
                        .width(32.dp),
                    onClick = { openAlertDialogWarning = true },
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(4.dp),
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = stringResource(id = R.string.icon_delete),
                    )

                }
            }
            Spacer(
                modifier = Modifier
                    .height(16.dp),
            )
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                ),
            ) {
                Column(
                    modifier = Modifier
                        .padding(4.dp),
                ) {
                    Text(
                        modifier = Modifier
                            .padding(end = 8.dp),
                        text = stringResource(id = R.string.doctor_specialization),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = appointment.doctorSpecialty,
                        fontSize = 20.sp,
                    )
                    Spacer(
                        modifier = Modifier
                            .height(8.dp),
                    )
                    Text(
                        modifier = Modifier
                            .padding(end = 8.dp),
                        text = stringResource(id = R.string.doctor_name),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = appointment.doctorName,
                        fontSize = 20.sp,
                    )
                    Spacer(
                        modifier = Modifier
                            .height(8.dp),
                    )
                    Text(
                        modifier = Modifier
                            .padding(end = 8.dp),
                        text = stringResource(id = R.string.date),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = appointment.date,
                        fontSize = 20.sp,
                    )
                    Spacer(
                        modifier = Modifier
                            .height(8.dp),
                    )
                    Text(
                        modifier = Modifier
                            .padding(end = 8.dp),
                        text = stringResource(id = R.string.time),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = appointment.time,
                        fontSize = 20.sp,
                    )
                    Spacer(
                        modifier = Modifier
                            .height(8.dp),
                    )
                    Text(
                        modifier = Modifier
                            .padding(end = 8.dp),
                        text = stringResource(id = R.string.address),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = appointment.address,
                        fontSize = 20.sp,
                    )
                    Spacer(
                        modifier = Modifier
                            .height(8.dp),
                    )
                    if (appointment.visited) {
                        Text(
                            modifier = Modifier
                                .padding(end = 8.dp),
                            text = stringResource(id = R.string.recommendations),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = appointment.recommendations ?: "",
                            fontSize = 20.sp,
                        )

                    }
                }
            }
            if (!appointment.visited) {
                FilledTonalButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = { openInputTextDialog = true },
                ) {
                    Text(
                        text = stringResource(id = R.string.appointment_took_place),
                    )
                }
            }

            if (openAlertDialogWarning) {
                AlertDialogWarning(
                    onDismissRequest = { openAlertDialogWarning = false },
                    onConfirmation = {
                        openAlertDialogWarning = false
                        viewModel.deleteAnalysis()
                    },
                    dialogTitle = stringResource(id = R.string.warning),
                    dialogText = stringResource(id = R.string.want_delete_appointment),
                )
            }

            if (openInputTextDialog) {
                TextInputDialog(
                    onCancel = { openInputTextDialog = false },
                    onSave = { inputString ->
                        openInputTextDialog = false
                        viewModel.addRecommendations(inputString)
                    }
                )
            }

            LaunchedEffect(resultOfDeletionAppointment) {
                when (resultOfDeletionAppointment) {
                    is ResultOfRequest.Success<Unit> -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.successful_deletion_appointment),
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.navigate(Screen.AppointmentScreen.route)
                    }

                    is ResultOfRequest.Error -> {
                        Toast.makeText(
                            context,
                            resultOfDeletionAppointment.errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is ResultOfRequest.Loading -> {}
                }
            }

            LaunchedEffect(resultOfAddingRecommendations) {
                when (resultOfAddingRecommendations) {
                    is ResultOfRequest.Error -> {
                        Toast.makeText(
                            context,
                            resultOfAddingRecommendations.errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {}
                }
            }
        }
    }
}
