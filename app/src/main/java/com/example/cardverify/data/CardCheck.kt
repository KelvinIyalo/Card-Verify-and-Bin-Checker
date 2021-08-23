package com.example.cardverify.data


import com.google.gson.annotations.SerializedName

data class CardCheck(
    var bank: Bank?,
    var brand: String?, // Debit
    var country: Country?,
    var number: Number?,
    var scheme: String?, // mastercard
    var type: String? // debit
)