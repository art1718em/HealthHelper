package com.example.healthhelper.core

import com.example.healthhelper.data.api.UserAnalyzesApi
import com.example.healthhelper.data.api.UserAuthenticationApi
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideUserAnalyzesApi(
        auth: FirebaseAuth,
        database: FirebaseFirestore,
    ): UserAnalyzesApi {
        return UserAnalyzesApi(auth, database)
    }

    @Provides
    @Singleton
    fun provideUserAuthenticationApi(
        auth: FirebaseAuth,
        database: FirebaseFirestore,
    ): UserAuthenticationApi {
        return UserAuthenticationApi(auth, database)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideDataBase(): FirebaseFirestore {
        return Firebase.firestore
    }
}