package com.example.healthhelper.data.api

import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.domain.model.Analysis
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

class UserAnalyzesApi @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
) {

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val ANALYSIS_USER_FIELD = "analyzes"
    }

    suspend fun loadUserAnalyzes(): ResultOfRequest<List<Analysis>> {
        val currentUser =
            auth.currentUser ?: return ResultOfRequest.Error(USER_UNAUTHORIZED_ERROR_MESSAGE)
        return try {
            val user = database
                .collection(USERS_COLLECTION)
                .document(currentUser.uid)
                .get()
                .await()
                .toObject<User>()
                ?.analyzes ?: return ResultOfRequest.Error(NULL_USER_ERROR_MESSAGE)
            ResultOfRequest.Success(user)
        } catch (e: Exception) {
            ResultOfRequest.Error(e.message ?: UNKNOWN_ERROR_MESSAGE)
        }
    }

    suspend fun addAnalysis(analysis: Analysis): ResultOfRequest<Unit> {
        val currentUser =
            auth.currentUser ?: return ResultOfRequest.Error(USER_UNAUTHORIZED_ERROR_MESSAGE)
        return try {
            database
                .collection(USERS_COLLECTION)
                .document(currentUser.uid)
                .update(ANALYSIS_USER_FIELD, FieldValue.arrayUnion(analysis))
                .await()
            ResultOfRequest.Success(Unit)
        } catch (e: IOException) {
            ResultOfRequest.Error(e.message ?: UNKNOWN_ERROR_MESSAGE)
        }
    }

    suspend fun editAnalysis(analysis: Analysis): ResultOfRequest<Unit> {
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
            val analyzes = user.analyzes.toMutableList()
            analyzes[analysis.index] = analysis
            userRef
                .update(ANALYSIS_USER_FIELD, analyzes)
                .await()
            ResultOfRequest.Success(Unit)
        } catch (e: Exception) {
            ResultOfRequest.Error(e.message ?: UNKNOWN_ERROR_MESSAGE)
        }
    }

    suspend fun deleteAnalysis(index: Int): ResultOfRequest<Unit> {
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
            val analyzes = user.analyzes.toMutableList()
            analyzes.removeAt(index)
            for (i in index..analyzes.lastIndex) {
                analyzes[i] = analyzes[i].copy(
                    index = analyzes[i].index - 1
                )
            }
            userRef
                .update(ANALYSIS_USER_FIELD, analyzes)
                .await()
            ResultOfRequest.Success(Unit)
        } catch (e: Exception) {
            ResultOfRequest.Error(e.message ?: UNKNOWN_ERROR_MESSAGE)
        }
    }
}