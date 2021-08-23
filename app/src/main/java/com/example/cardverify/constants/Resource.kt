package com.example.cardverify.constants

sealed class Resource<T>(
        val  data:T? = null,
        val  Message: String? = null
) {
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(Message: String?,data: T?): Resource<T>(data,Message)
    class Loading<T>:Resource<T>()
}
