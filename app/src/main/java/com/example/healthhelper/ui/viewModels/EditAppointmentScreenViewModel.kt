package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.R
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.data.repository.UserAppointmentsRepository
import com.example.healthhelper.domain.model.Appointment
import com.example.healthhelper.presenter.AppointmentsProvider
import com.example.healthhelper.ui.screens.main.addAppointment.AddAppointmentUiState
import com.example.healthhelper.utils.toFormattedDate
import com.example.healthhelper.utils.toFormattedTime
import com.example.healthhelper.utils.toLocalDate
import com.example.healthhelper.utils.toLocalTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class EditAppointmentScreenViewModel @Inject constructor(
    private val userAppointmentsRepository: UserAppointmentsRepository,
) : ViewModel() {

    private val _addAppointmentScreenUiState = MutableStateFlow(AddAppointmentUiState())
    val addAppointmentScreenUiState = _addAppointmentScreenUiState.asStateFlow()

    private var editingAppointmentJob: Job? = null

    private val _appointment = MutableStateFlow(Appointment())
    val appointment = _appointment.asStateFlow()

    private val _resultOfEditingAppointment =
        MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val resultOfEditingAppointment = _resultOfEditingAppointment.asStateFlow()

    init {
        viewModelScope.launch {
            AppointmentsProvider.appointment.collect {
                _appointment.value = it
                _addAppointmentScreenUiState.value = AddAppointmentUiState(
                    doctorSpecialty = appointment.value.doctorSpecialty,
                    doctorName = appointment.value.doctorName,
                    address = appointment.value.address,
                    formattedDate = appointment.value.date,
                    pickedDate = appointment.value.date.toLocalDate(),
                    formattedTime = appointment.value.time,
                    visited = appointment.value.visited,
                    recommendations = appointment.value.recommendations,
                    pickedTime = appointment.value.time.toLocalTime(),
                    doctorNameErrorMessage = null,
                    doctorSpecialtyErrorMessage = null,
                    addressErrorMessage = null,
                    recommendationsErrorMessage = null,
                )
            }
        }
    }

    fun updateDoctorSpecialty(doctorSpecialty: String) {
        _addAppointmentScreenUiState.value = addAppointmentScreenUiState.value.copy(
            doctorSpecialty = doctorSpecialty,
        )
        checkDoctorSpeciality()
    }

    fun updateDoctorName(doctorName: String) {
        _addAppointmentScreenUiState.value = addAppointmentScreenUiState.value.copy(
            doctorName = doctorName,
        )
        checkDoctorName()
    }

    fun updateAddress(address: String) {
        _addAppointmentScreenUiState.value = addAppointmentScreenUiState.value.copy(
            address = address,
        )
        checkAddress()
    }

    fun updateDate(date: LocalDate) {
        _addAppointmentScreenUiState.value = addAppointmentScreenUiState.value.copy(
            pickedDate = date,
            formattedDate = date.toFormattedDate(),
        )
    }

    fun updateTime(time: LocalTime) {
        _addAppointmentScreenUiState.value = addAppointmentScreenUiState.value.copy(
            pickedTime = time,
            formattedTime = time.toFormattedTime(),
        )
    }

    fun updateRecommendations(recommendations: String) {
        _addAppointmentScreenUiState.value = addAppointmentScreenUiState.value.copy(
            recommendations = recommendations,
        )
        checkRecommendations()
    }

    private fun checkDoctorSpeciality() {
        _addAppointmentScreenUiState.value = addAppointmentScreenUiState.value.copy(
            doctorSpecialtyErrorMessage = when {
                addAppointmentScreenUiState.value.doctorSpecialty.isEmpty() -> R.string.empty_field
                else -> null
            }
        )
    }

    private fun checkDoctorName() {
        _addAppointmentScreenUiState.value = addAppointmentScreenUiState.value.copy(
            doctorNameErrorMessage = when {
                addAppointmentScreenUiState.value.doctorName.isEmpty() -> R.string.empty_field
                else -> null
            }
        )
    }

    private fun checkAddress() {
        _addAppointmentScreenUiState.value = addAppointmentScreenUiState.value.copy(
            addressErrorMessage = when {
                addAppointmentScreenUiState.value.address.isEmpty() -> R.string.empty_field
                else -> null
            }
        )
    }

    private fun checkRecommendations() {
        _addAppointmentScreenUiState.value = addAppointmentScreenUiState.value.copy(
            recommendationsErrorMessage = when {
                addAppointmentScreenUiState.value.doctorSpecialty.isEmpty() -> R.string.empty_field
                else -> null
            }
        )
    }

    fun editAnalysis() {
        editingAppointmentJob?.cancel()
        val updatedAppointment = addAppointmentScreenUiState.value.toAppointment().copy(
            index = appointment.value.index,
        )
        editingAppointmentJob = viewModelScope.launch {
            userAppointmentsRepository.editAppointment(updatedAppointment)
            userAppointmentsRepository.resultOfEditingAppointment.collect {
                _resultOfEditingAppointment.value = it
                if (it is ResultOfRequest.Success) {
                    AppointmentsProvider.setAppointment(updatedAppointment)
                }
            }
        }
    }

}