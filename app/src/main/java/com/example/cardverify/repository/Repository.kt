package com.example.cardverify.repository

import com.example.cardverify.data.CardCheck
import com.example.cardverify.network.CardNetwork
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(private val api:CardNetwork) {

    suspend fun getCardDetails(queryString:String):Response<CardCheck>{
        return api.getCardDetails(queryString)
    }
}