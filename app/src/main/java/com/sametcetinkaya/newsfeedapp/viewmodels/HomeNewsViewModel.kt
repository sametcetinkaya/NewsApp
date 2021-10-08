package com.sametcetinkaya.newsfeedapp.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sametcetinkaya.newsfeedapp.NewsApplication
import com.sametcetinkaya.newsfeedapp.models.NewsResponse
import com.sametcetinkaya.newsfeedapp.repository.Repository
import com.sametcetinkaya.newsfeedapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeNewsViewModel
@Inject constructor(app: Application,
                    private val repository: Repository
                    ): AndroidViewModel(app){

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var paginationNumber = 1


    init {
        getBreakingNews()
    }


    fun getBreakingNews() = viewModelScope.launch {
        safeBreakingNewsCall()

    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { newsResponse ->
                return Resource.Success(newsResponse)
            }
        }
        return Resource.Error(response.message())
    }


    private suspend fun safeBreakingNewsCall(){
        breakingNews.postValue(Resource.Loading())
        try {
            if(hasInternet()){
                val response = repository.getBreakingNews(paginationNumber)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            }
            else{
                breakingNews.postValue(Resource.Error("Network Error"))
            }
        }
        catch (t:Throwable){
            when(t){
                is IOException -> breakingNews.postValue(Resource.Error("Retrofit Error"))
                else-> breakingNews.postValue(Resource.Error("Conversion Error"))
            }
        }

    }



    private fun hasInternet():Boolean{
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when{
                capabilities.hasTransport(TRANSPORT_WIFI) ->true
                capabilities.hasTransport(TRANSPORT_CELLULAR)->true
                capabilities.hasTransport(TRANSPORT_ETHERNET)->true
                else-> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_ETHERNET ->true
                    ConnectivityManager.TYPE_MOBILE ->true
                    else-> false
                }
            }
        }
        return false
    }
}