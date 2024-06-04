package com.example.healthhelper.ui.screens.main.appointments

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.healthhelper.R
import com.example.healthhelper.ui.screens.Screen
import com.example.healthhelper.ui.viewModels.AppointmentsScreenViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppointmentsScreen(
    navController: NavController,
    viewModel: AppointmentsScreenViewModel,
) {

    val appointmentsUIState by viewModel.appointmentsUiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddAppointmentScreen.route)
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.icon_add),
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
            Text(
                text = stringResource(id = R.string.doctor_appointments),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(
                modifier = Modifier
                    .height(8.dp),
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                itemsIndexed(appointmentsUIState) { index, appointment ->
                    AppointmentCard(
                        appointment = appointment,
                        index = index,
                        addAppointmentToPresenter = viewModel::addAppointmentToPresenter,
                        navigateToDetails = navController::navigate,
                    )
                }
                item {
                    Spacer(
                        modifier = Modifier
                            .height(56.dp),
                    )
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentCard(
    appointment: AppointmentUIState,
    index: Int,
    addAppointmentToPresenter: (index: Int) -> Unit,
    navigateToDetails: (route: String) -> Unit,
) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = if (appointment.isVisited) {
                MaterialTheme.colorScheme.inversePrimary
            } else MaterialTheme.colorScheme.background,

            ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline,
        ),
        onClick = {
            addAppointmentToPresenter(index)
            navigateToDetails(Screen.AppointmentDetailsScreen.route)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .height(IntrinsicSize.Min),
        ) {
            Column(
                modifier = Modifier
                    .weight(0.4f),
            ) {
                Text(
                    text = appointment.time,
                    fontSize = 20.sp,
                )
                Text(
                    text = appointment.date,
                    fontSize = 20.sp,
                )
            }
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = appointment.doctorSpecialty,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}


