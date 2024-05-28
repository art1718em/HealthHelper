package com.example.healthhelper.data.api

import com.example.healthhelper.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import javax.inject.Inject

class UserApi @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
) {
    private var listener: ListenerRegistration? = null

    companion object {
        private const val USERS_COLLECTION = "users"
    }

    fun getUserData(updateData: (user: User?) -> Unit) {
        val currentUser = auth.currentUser ?: return
        listener = database
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
}