package com.example.healthhelper.data.api

import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.domain.model.Appointment
import com.example.healthhelper.domain.model.User
import com.example.healthhelper.utils.NULL_USER_ERROR_MESSAGE
import com.example.healthhelper.utils.UNKNOWN_ERROR_MESSAGE
import com.example.healthhelper.utils.USER_UNAUTHORIZED_ERROR_MESSAGE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class UserAppointmentsApi @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
) {

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val APPOINTMENT_USER_FIELD = "appointments"
    }

    suspend fun loadUserAppointments(): ResultOfRequest<List<Appointment>> {
        val currentUser =
            auth.currentUser ?: return ResultOfRequest.Error(USER_UNAUTHORIZED_ERROR_MESSAGE)
        return try {
            val appointments = database
                .collection(USERS_COLLECTION)
                .document(currentUser.uid)
                .get()
                .await()
                .toObject<User>()
                ?.appointments ?: return ResultOfRequest.Error(NULL_USER_ERROR_MESSAGE)
            ResultOfRequest.Success(appointments)
        } catch (e: Exception) {
            ResultOfRequest.Error(e.message ?: UNKNOWN_ERROR_MESSAGE)
        }
    }

    suspend fun addAppointment(appointment: Appointment): ResultOfRequest<Unit> {
        val currentUser =
            auth.currentUser ?: return ResultOfRequest.Error(USER_UNAUTHORIZED_ERROR_MESSAGE)
        return try {
            database
                .collection(USERS_COLLECTION)
                .document(currentUser.uid)
                .update(APPOINTMENT_USER_FIELD, FieldValue.arrayUnion(appointment))
                .await()
            ResultOfRequest.Success(Unit)
        } catch (e: IOException) {
            ResultOfRequest.Error(e.message ?: UNKNOWN_ERROR_MESSAGE)
        }
    }

    suspend fun editAppointment(appointment: Appointment): ResultOfRequest<Unit> {
        val currentUser =
            auth.currentUser ?: return ResultOfRequest.Error(USER_UNAUTHORIZED_ERROR_MESSAGE)
        return try {
            val userRef = database
                .collection(USERS_COLLECTION)
                .document(currentUser.uid)
            val user = userRef
                .get()
                .await()
                .toObject<User>() ?: return ResultOfRequest.Error(NULL_USER_ERROR_MESSAGE)
            val appointments = user.appointments.toMutableList()
            appointments[appointment.index] = appointment
            userRef
                .update(APPOINTMENT_USER_FIELD, appointments)
                .await()
            ResultOfRequest.Success(Unit)
        } catch (e: Exception) {
            ResultOfRequest.Error(e.message ?: UNKNOWN_ERROR_MESSAGE)
        }
    }

    suspend fun deleteAppointment(index: Int): ResultOfRequest<Unit> {
        val currentUser =
            auth.currentUser ?: return ResultOfRequest.Error(USER_UNAUTHORIZED_ERROR_MESSAGE)
        return try {
            val userRef = database
                .collection(USERS_COLLECTION)
                .document(currentUser.uid)
            val user = userRef
                .get()
                .await()
                .toObject<User>() ?: return ResultOfRequest.Error(NULL_USER_ERROR_MESSAGE)
            val appointments = user.appointments.toMutableList()
            appointments.removeAt(index)
            for (i in index..appointments.lastIndex) {
                appointments[i] = appointments[i].copy(
                    index = appointments[i].index - 1
                )
            }
            userRef
                .update(APPOINTMENT_USER_FIELD, appointments)
                .await()
            ResultOfRequest.Success(Unit)
        } catch (e: Exception) {
            ResultOfRequest.Error(e.message ?: UNKNOWN_ERROR_MESSAGE)
        }
    }
}