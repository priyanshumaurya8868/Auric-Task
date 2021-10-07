package com.priyanshumaurya8868.aurictask.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.priyanshumaurya8868.aurictask.Repo
import com.priyanshumaurya8868.aurictask.api.RandomAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit() = Retrofit.Builder()
        .baseUrl("https://615d466312571a00172074f2.mockapi.io/api/v1/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(RandomAPI::class.java)

    @Provides
    @Singleton
    fun provideRepo(api : RandomAPI) = Repo(api)

    @Provides
    @Singleton
    fun provideConnectivityManager(app: Application) =
        app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


}