package com.example.healthhelper.data.api

import com.example.healthhelper.core.ResultOfRequest
import com.example.healthhelper.domain.model.DiaryEntry
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

class UserDiaryApi @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
) {

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val DIARY_ENTRY_USER_FIELD = "diaryEntries"
    }

    suspend fun loadUserDiaryEntries(): ResultOfRequest<List<DiaryEntry>> {
        val currentUser =
            auth.currentUser ?: return ResultOfRequest.Error(USER_UNAUTHORIZED_ERROR_MESSAGE)
        return try {
            val user = database
                .collection(USERS_COLLECTION)
                .document(currentUser.uid)
                .get()
                .await()
                .toObject<User>()
                ?.diaryEntries ?: return ResultOfRequest.Error(NULL_USER_ERROR_MESSAGE)
            ResultOfRequest.Success(user)
        } catch (e: Exception) {
            ResultOfRequest.Error(e.message ?: UNKNOWN_ERROR_MESSAGE)
        }
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

    suspend fun editDiaryEntry(diaryEntry: DiaryEntry): ResultOfRequest<Unit> {
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
            val diaryEntries = user.diaryEntries.toMutableList()
            diaryEntries[diaryEntry.index] = diaryEntry
            userRef
                .update(DIARY_ENTRY_USER_FIELD, diaryEntries)
                .await()
            ResultOfRequest.Success(Unit)
        } catch (e: Exception) {
            ResultOfRequest.Error(e.message ?: UNKNOWN_ERROR_MESSAGE)
        }
    }

    suspend fun deleteDiaryEntry(index: Int): ResultOfRequest<Unit> {
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
            val diaryEntries = user.diaryEntries.toMutableList()
            diaryEntries.removeAt(index)
            for (i in index..diaryEntries.lastIndex) {
                diaryEntries[i] = diaryEntries[i].copy(
                    index = diaryEntries[i].index - 1
                )
            }
            userRef
                .update(DIARY_ENTRY_USER_FIELD, diaryEntries)
                .await()
            ResultOfRequest.Success(Unit)
        } catch (e: Exception) {
            ResultOfRequest.Error(e.message ?: UNKNOWN_ERROR_MESSAGE)
        }
    }
}