package com.example.cardverify.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.ContactsContract
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cardverify.constants.Resource
import com.example.cardverify.data.CardCheck
import com.example.cardverify.di.CardApplication
import com.example.cardverify.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MainViewModel@ViewModelInject constructor(private val app: Application, private val repository: Repository):
    AndroidViewModel(app) {

  private  val _myCardDetails: MutableLiveData<Resource<CardCheck>> = MutableLiveData()
  val myCardDetails = _myCardDetails



        fun getCardDetails(queryString: String){
            viewModelScope.launch {
                safeCardDetailsCall(queryString)
            }
        }



  private  suspend fun safeCardDetailsCall(queryString: String){
        _myCardDetails.postValue(Resource.Loading())
        try {
            if (isInternetAvailable()) {
                val cardDetailsResponse = repository.getCardDetails(queryString)
                _myCardDetails.postValue(handleCardDetails(cardDetailsResponse))
            }else{
                _myCardDetails.postValue(Resource.Error("No Internet Connection", null))
            }

        }catch (t:Throwable){
            when(t){
                is IOException -> _myCardDetails.postValue(Resource.Error("Network Failure", null))
                else -> _myCardDetails.postValue(Resource.Error("Conversion Error:$t", null))
            }

        }

    }


    private  fun handleCardDetails(response: Response<CardCheck>):Resource<CardCheck>{
        if (response.isSuccessful){
            response.body()?.let { ResultResponse ->
                return Resource.Success(ResultResponse)
            }
        }
        return Resource.Error(response.message(),data = null)
    }



 private  fun isInternetAvailable():Boolean{

        val connectivityManager = getApplication<CardApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when{
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false

            }

        } else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    ConnectivityManager.TYPE_WIFI -> true
                    ContactsContract.CommonDataKinds.Email.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false

                }
            }
        }
        return false

    }

}