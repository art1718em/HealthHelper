package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.data.repository.UserAppointmentsRepository
import com.example.healthhelper.domain.model.Appointment
import com.example.healthhelper.presenter.AppointmentsProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppointmentDetailsScreenViewModel @Inject constructor(
    private val userAppointmentsRepository: UserAppointmentsRepository,
) : ViewModel() {

    private val _appointment = MutableStateFlow(Appointment())
    val appointment = _appointment.asStateFlow()

    private var deletionAppointmentJob: Job? = null
    private var addingRecommendationsAppointmentJob: Job? = null

    private val _resultOfDeletionAppointment =
        MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val resultOfDeletionAppointment = _resultOfDeletionAppointment.asStateFlow()

    private val _resultOfAddingRecommendations =
        MutableStateFlow<ResultOfRequest<Unit>>(ResultOfRequest.Loading)
    val resultOfAddingRecommendations = _resultOfAddingRecommendations.asStateFlow()

    init {
        viewModelScope.launch {
            AppointmentsProvider.appointment.collect {
                _appointment.value = it
            }
        }
    }

    fun deleteAnalysis() {
        deletionAppointmentJob?.cancel()
        deletionAppointmentJob = viewModelScope.launch {
            userAppointmentsRepository.deleteAppointment(appointment.value.index)
            userAppointmentsRepository.resultOfDeletionAppointment.collect {
                _resultOfDeletionAppointment.value = it
            }
        }
    }

    fun addRecommendations(recommendations: String) {
        addingRecommendationsAppointmentJob?.cancel()
        val updatedAppointment = appointment.value.copy(
            visited = true,
            recommendations = recommendations,
        )
        addingRecommendationsAppointmentJob = viewModelScope.launch {
            userAppointmentsRepository.editAppointment(updatedAppointment)
            userAppointmentsRepository.resultOfEditingAppointment.collect {
                _resultOfAddingRecommendations.value = it
                if (it is ResultOfRequest.Success) {
                    AppointmentsProvider.setAppointment(updatedAppointment)
                }
            }
        }
    }
}