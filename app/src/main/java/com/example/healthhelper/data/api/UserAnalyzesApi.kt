package com.example.healthhelper.data.api

import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.domain.model.Analysis
import com.example.healthhelper.domain.model.User
import com.example.healthhelper.utils.UNKNOWN_ERROR_MESSAGE
import com.example.healthhelper.utils.USER_UNAUTHORIZED_ERROR_MESSAGE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class UserAnalyzesApi @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore
) {

    private var listenerOfAnalyzes: ListenerRegistration? = null

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val ANALYSIS_USER_FIELD = "analyzes"
    }


    fun getAnalyzes(updateData: (user: User?) -> Unit) {
        val currentUser = auth.currentUser ?: return
        listenerOfAnalyzes = database
            .collection(USERS_COLLECTION)
            .document(currentUser.uid)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (value != null && value.exists()) {
                    updateData(value.toObject<User>())
                }
            }
    }

    suspend fun addAnalysis(analysis: Analysis): ResultOfRequest<Unit> {
        val currentUser =
            auth.currentUser ?: return ResultOfRequest.Error(USER_UNAUTHORIZED_ERROR_MESSAGE)
        val resultOfAddingAnalysis: ResultOfRequest<Unit> = try {
            database
                .collection(USERS_COLLECTION)
                .document(currentUser.uid)
                .update(ANALYSIS_USER_FIELD, FieldValue.arrayUnion(analysis))
                .await()
            ResultOfRequest.Success(Unit)
        } catch (e: IOException) {
            ResultOfRequest.Error(e.message ?: UNKNOWN_ERROR_MESSAGE)
        }
        return resultOfAddingAnalysis
    }
}