package com.example.healthhelper.data.api

import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.domain.model.User
import com.example.healthhelper.utils.UNKNOWN_ERROR_MESSAGE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserAuthenticationApi @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore
) {

    companion object {
        private const val USERS_COLLECTION = "users"
    }

    suspend fun signUp(email: String, password: String): ResultOfRequest<Unit> {
        val resultOfRequest: ResultOfRequest<Unit> = try {
            auth.createUserWithEmailAndPassword(email, password).await()
            ResultOfRequest.Success(Unit)
        } catch (e: Exception) {
            ResultOfRequest.Error(e.message ?: UNKNOWN_ERROR_MESSAGE)
        }

        return resultOfRequest
    }

    suspend fun signIn(email: String, password: String): ResultOfRequest<FirebaseUser> {
        val resultOfRequest: ResultOfRequest<FirebaseUser> = try {
            auth.signInWithEmailAndPassword(email, password).await()
            ResultOfRequest.Success(auth.currentUser!!)
        } catch (e: Exception) {
            ResultOfRequest.Error(e.message ?: UNKNOWN_ERROR_MESSAGE)
        }
        return resultOfRequest
    }

    suspend fun setUser(user: User): ResultOfRequest<Unit> {
        val resultOfRequest: ResultOfRequest<Unit> = try {
            database
                .collection(USERS_COLLECTION)
                .document(auth.currentUser!!.uid)
                .set(user)
                .await()
            ResultOfRequest.Success(Unit)
        } catch (e: Exception) {
            ResultOfRequest.Error(e.message ?: UNKNOWN_ERROR_MESSAGE)
        }
        return resultOfRequest
    }
}