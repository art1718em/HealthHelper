package com.example.healthhelper.data.api

import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.domain.model.DiaryEntry
import com.example.healthhelper.utils.UNKNOWN_ERROR_MESSAGE
import com.example.healthhelper.utils.USER_UNAUTHORIZED_ERROR_MESSAGE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class UserDiaryApi @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
) {

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val DIARY_ENTRY_USER_FIELD = "diaryEntries"
    }

    suspend fun addDiaryEntry(diaryEntry: DiaryEntry): ResultOfRequest<Unit> {
        val currentUser =
            auth.currentUser ?: return ResultOfRequest.Error(USER_UNAUTHORIZED_ERROR_MESSAGE)
        val resultOfAddingAnalysis: ResultOfRequest<Unit> = try {
            database
                .collection(USERS_COLLECTION)
                .document(currentUser.uid)
                .update(DIARY_ENTRY_USER_FIELD, FieldValue.arrayUnion(diaryEntry))
                .await()
            ResultOfRequest.Success(Unit)
        } catch (e: IOException) {
            ResultOfRequest.Error(e.message ?: UNKNOWN_ERROR_MESSAGE)
        }
        return resultOfAddingAnalysis
    }
}