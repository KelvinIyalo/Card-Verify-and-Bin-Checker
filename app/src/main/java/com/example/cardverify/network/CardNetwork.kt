package com.example.cardverify.network

import com.example.cardverify.data.CardCheck
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CardNetwork {
    @GET("{Query}")
    suspend fun getCardDetails(
    @Path("Query")queryString:String
    ):Response<CardCheck>
}