package com.example.healthhelper.data.repository

import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.data.api.UserAppointmentsApi
import com.example.healthhelper.domain.model.Appointment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserAppointmentsRepository @Inject constructor(
    private val userAppointmentsApi: UserAppointmentsApi,
) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())

    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments = _appointments.asStateFlow()

    private val _resultOfLoadingAppointments =
        MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val resultOfLoadingAppointments = _resultOfLoadingAppointments.asStateFlow()

    private val _resultOfAddingAppointment =
        MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val resultOfAddingAppointment = _resultOfAddingAppointment.asStateFlow()

    private val _resultOfEditingAppointment =
        MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val resultOfEditingAppointment = _resultOfEditingAppointment.asStateFlow()

    private val _resultOfDeletionAppointment =
        MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val resultOfDeletionAppointment = _resultOfDeletionAppointment.asStateFlow()

    suspend fun loadUserAppointments() {
        when (val resultOfLoadData = userAppointmentsApi.loadUserAppointments()) {
            is ResultOfRequest.Success -> {
                _appointments.value = resultOfLoadData.result
                _resultOfLoadingAppointments.value = ResultOfRequest.Success(Unit)
            }

            is ResultOfRequest.Error -> {
                _resultOfLoadingAppointments.value =
                    ResultOfRequest.Error(resultOfLoadData.errorMessage)
            }

            is ResultOfRequest.Loading -> {}
        }
    }

    suspend fun addAppointment(appointment: Appointment) {
        coroutineScope.launch {
            when (val resultOfAddingAppointment = userAppointmentsApi.addAppointment(appointment)) {
                is ResultOfRequest.Success -> {
                    _appointments.value = appointments.value.plus(appointment)
                    _resultOfAddingAppointment.value = ResultOfRequest.Success(Unit)
                }

                is ResultOfRequest.Error -> {
                    _resultOfAddingAppointment.value =
                        ResultOfRequest.Error(resultOfAddingAppointment.errorMessage)
                }

                is ResultOfRequest.Loading -> {}
            }
        }
    }

    suspend fun editAppointment(appointment: Appointment) {
        coroutineScope.launch {
            when (val resultOfEditingAppointment = userAppointmentsApi.editAppointment(appointment)) {
                is ResultOfRequest.Success -> {
                    val updatedAppointments = _appointments.value.toMutableList()
                    updatedAppointments[appointment.index] = appointment
                    _appointments.value = updatedAppointments
                    _resultOfEditingAppointment.value = ResultOfRequest.Success(Unit)
                }

                is ResultOfRequest.Error -> {
                    _resultOfEditingAppointment.value =
                        ResultOfRequest.Error(resultOfEditingAppointment.errorMessage)
                }

                is ResultOfRequest.Loading -> {}
            }
        }
    }

    suspend fun deleteAppointment(index: Int) {
        coroutineScope.launch {
            when (val resultOfDeletionAppointment = userAppointmentsApi.deleteAppointment(index)) {
                is ResultOfRequest.Success -> {
                    val updatedAppointments = _appointments.value.toMutableList()
                    updatedAppointments.removeAt(index)
                    for (i in index..updatedAppointments.lastIndex) {
                        updatedAppointments[i] = updatedAppointments[i].copy(
                            index = updatedAppointments[i].index - 1
                        )
                    }
                    _appointments.value = updatedAppointments
                    _resultOfDeletionAppointment.value = ResultOfRequest.Success(Unit)
                }

                is ResultOfRequest.Error -> {
                    _resultOfDeletionAppointment.value =
                        ResultOfRequest.Error(resultOfDeletionAppointment.errorMessage)
                }

                is ResultOfRequest.Loading -> {}
            }
        }
    }

    fun clearData() {
        _appointments.value = emptyList()
    }
}