package com.example.cardverify.di

import com.example.cardverify.constants.BASE_URL
import com.example.cardverify.network.CardNetwork
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object CardModule {
    @Singleton
    @Provides
    fun provideCardDetails(): CardNetwork = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CardNetwork::class.java)

}