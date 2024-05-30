package com.example.healthhelper.data.api

import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.domain.model.User
import com.example.healthhelper.utils.NULL_USER_ERROR_MESSAGE
import com.example.healthhelper.utils.UNKNOWN_ERROR_MESSAGE
import com.example.healthhelper.utils.USER_UNAUTHORIZED_ERROR_MESSAGE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserApi @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
) {

    companion object {
        private const val USERS_COLLECTION = "users"
    }

    suspend fun loadUserData(): ResultOfRequest<User> {
        val currentUser =
            auth.currentUser ?: return ResultOfRequest.Error(USER_UNAUTHORIZED_ERROR_MESSAGE)
        return try {
            val user = database
                .collection(USERS_COLLECTION)
                .document(currentUser.uid)
                .get()
                .await()
                .toObject<User>() ?: return ResultOfRequest.Error(NULL_USER_ERROR_MESSAGE)
            ResultOfRequest.Success(user)
        } catch (e: Exception) {
            ResultOfRequest.Error(e.message ?: UNKNOWN_ERROR_MESSAGE)
        }
    }
}