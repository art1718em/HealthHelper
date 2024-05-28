package com.example.healthhelper.data.api

import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.domain.model.User
import com.example.healthhelper.utils.NO_EMAIL_ERROR_MESSAGE
import com.example.healthhelper.utils.NO_SIGN_ERROR_MESSAGE
import com.example.healthhelper.utils.UNKNOWN_ERROR_MESSAGE
import com.example.healthhelper.utils.USER_UNAUTHORIZED_ERROR_MESSAGE
import com.google.firebase.auth.EmailAuthProvider
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

    fun checkUser(): ResultOfRequest<Unit> {
        return if (auth.currentUser != null)
            ResultOfRequest.Success(Unit)
        else
            ResultOfRequest.Error(NO_SIGN_ERROR_MESSAGE)
    }

    suspend fun signUp(
        email: String,
        password: String,
    ): ResultOfRequest<Unit> {
        val resultOfRequest: ResultOfRequest<Unit> = try {
            auth.createUserWithEmailAndPassword(email, password).await()
            ResultOfRequest.Success(Unit)
        } catch (e: Exception) {
            ResultOfRequest.Error(e.message ?: UNKNOWN_ERROR_MESSAGE)
        }

        return resultOfRequest
    }

    suspend fun signIn(
        email: String,
        password: String,
    ): ResultOfRequest<FirebaseUser> {
        val resultOfRequest: ResultOfRequest<FirebaseUser> = try {
            auth.signInWithEmailAndPassword(email, password).await()
            ResultOfRequest.Success(auth.currentUser!!)
        } catch (e: Exception) {
            ResultOfRequest.Error(e.message ?: UNKNOWN_ERROR_MESSAGE)
        }
        return resultOfRequest
    }

    suspend fun setUser(
        user: User,
    ): ResultOfRequest<Unit> {
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

    fun getUserEmail(): ResultOfRequest<String> {
        return auth.currentUser?.let { user ->
            user.email?.let { email ->
                ResultOfRequest.Success(email)
            } ?: ResultOfRequest.Error(NO_EMAIL_ERROR_MESSAGE)
        } ?: ResultOfRequest.Error(USER_UNAUTHORIZED_ERROR_MESSAGE)
    }

    fun logOut(): ResultOfRequest<Unit> {
        val resultOfLogOut = try {
            auth.signOut()
            ResultOfRequest.Success(Unit)
        } catch (e: Exception) {
            ResultOfRequest.Error(e.message ?: UNKNOWN_ERROR_MESSAGE)
        }
        return resultOfLogOut
    }

    suspend fun changePassword(
        oldPassword: String,
        newPassword: String,
    ): ResultOfRequest<Unit> {
        return when (
            val resultOfCheckingOldPassword = checkOldPassword(
                oldPassword = oldPassword,
            )
        ) {
            is ResultOfRequest.Success -> {
                return try {
                    auth.currentUser?.let { user ->
                        user
                            .updatePassword(newPassword)
                            .await()
                        ResultOfRequest.Success(Unit)
                    } ?: ResultOfRequest.Error(USER_UNAUTHORIZED_ERROR_MESSAGE)
                } catch (e: Exception) {
                    ResultOfRequest.Error(e.message ?: UNKNOWN_ERROR_MESSAGE)
                }
            }

            else -> {
                resultOfCheckingOldPassword
            }
        }
    }


    private suspend fun checkOldPassword(oldPassword: String): ResultOfRequest<Unit> {
        val currentUser =
            auth.currentUser ?: return ResultOfRequest.Error(USER_UNAUTHORIZED_ERROR_MESSAGE)
        val userEmail = currentUser.email ?: return ResultOfRequest.Error(NO_EMAIL_ERROR_MESSAGE)
        return try {
            currentUser
                .reauthenticate(
                    EmailAuthProvider
                        .getCredential(
                            userEmail,
                            oldPassword,
                        )
                )
                .await()
            ResultOfRequest.Success(Unit)
        } catch (e: Exception) {
            ResultOfRequest.Error(e.message ?: UNKNOWN_ERROR_MESSAGE)
        }
    }
}