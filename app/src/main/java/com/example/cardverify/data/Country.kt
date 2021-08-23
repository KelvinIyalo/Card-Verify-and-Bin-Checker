package com.example.cardverify.data


import com.google.gson.annotations.SerializedName

data class Country(
    var alpha2: String?, // NG
    var currency: String?, // NGN
    var emoji: String?, // 🇳🇬
    var latitude: Int?, // 10
    var longitude: Int?, // 8
    var name: String?, // Nigeria
    var numeric: String? // 566
)