package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.R
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.data.repository.UserAppointmentsRepository
import com.example.healthhelper.ui.screens.main.addAppointment.AddAppointmentUiState
import com.example.healthhelper.utils.toFormattedDate
import com.example.healthhelper.utils.toFormattedTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AddAppointmentViewModel @Inject constructor(
    private val userAppointmentsRepository: UserAppointmentsRepository,
) : ViewModel() {

    private val _addAppointmentScreenUiState = MutableStateFlow(AddAppointmentUiState())
    val addAppointmentScreenUiState = _addAppointmentScreenUiState.asStateFlow()

    private var addingAppointmentJob: Job? = null

    private val _resultOfAddingAppointment =
        MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val resultOfAddingAppointment = _resultOfAddingAppointment.asStateFlow()

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

    fun addAppointment(index: Int) {
        addingAppointmentJob?.cancel()
        val appointment = addAppointmentScreenUiState.value.toAppointment().copy(
            index = index,
        )
        addingAppointmentJob = viewModelScope.launch {
            userAppointmentsRepository.addAppointment(appointment)
            userAppointmentsRepository.resultOfAddingAppointment.collect {
                _resultOfAddingAppointment.value = it
            }
        }
    }
}