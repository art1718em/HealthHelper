package com.example.healthhelper.core

import com.example.healthhelper.data.api.UserAnalyzesApi
import com.example.healthhelper.data.api.UserApi
import com.example.healthhelper.data.api.UserAuthenticationApi
import com.example.healthhelper.data.api.UserDiaryApi
import com.example.healthhelper.data.repository.UserRepository
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
    fun provideUserRepository(
        userApi: UserApi,
        userDiaryApi: UserDiaryApi,
        userAnalyzesApi: UserAnalyzesApi,
    ): UserRepository {
        return UserRepository(
            userApi = userApi,
            userDiaryApi = userDiaryApi,
            userAnalyzesApi = userAnalyzesApi,
        )
    }

    @Provides
    @Singleton
    fun provideUserAnalyzesApi(
        auth: FirebaseAuth,
        database: FirebaseFirestore,
    ): UserAnalyzesApi {
        return UserAnalyzesApi(
            auth = auth,
            database = database,
        )
    }

    @Provides
    @Singleton
    fun provideUserApi(
        auth: FirebaseAuth,
        database: FirebaseFirestore,
    ): UserApi {
        return UserApi(
            auth = auth,
            database = database,
        )
    }

    @Provides
    @Singleton
    fun provideUserAuthenticationApi(
        auth: FirebaseAuth,
        database: FirebaseFirestore,
    ): UserAuthenticationApi {
        return UserAuthenticationApi(
            auth = auth,
            database = database,
        )
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