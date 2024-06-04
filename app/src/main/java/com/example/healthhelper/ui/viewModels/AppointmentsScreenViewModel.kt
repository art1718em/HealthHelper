package com.example.healthhelper.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthhelper.data.repository.UserAppointmentsRepository
import com.example.healthhelper.domain.model.Appointment
import com.example.healthhelper.presenter.AppointmentsProvider
import com.example.healthhelper.utils.sortAppointments
import com.example.healthhelper.utils.toAppointmentUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppointmentsScreenViewModel @Inject constructor(
    private val userAppointmentsRepository: UserAppointmentsRepository,
) : ViewModel() {

    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    private val appointments = _appointments.asStateFlow()

    val appointmentsUiState = appointments.map { appointments ->
        appointments.toAppointmentUiState()
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    init {
        viewModelScope.launch {
            userAppointmentsRepository.appointments.collect { appointments ->
                _appointments.value = appointments.sortAppointments()
            }
        }

    }

    fun addAppointmentToPresenter(index: Int) {
        AppointmentsProvider.setAppointment(appointments.value[index])
    }

    fun getAppointmentsSize() = appointments.value.size
}