package com.sametcetinkaya.news.repositories


import android.util.Log
import com.sametcetinkaya.news.utils.Output
import retrofit2.Response
import java.io.IOException

open class BaseRepository {
    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>, error: String): T? {
        val result = newsApiOutput(call, error)
        var output: T? = null
        when (result) {
            is Output.Success -> output = result.output

            is Output.Error -> try {
                Log.e("Errorr", "The $error and the ${result.exception}")
            } catch (e: Exception) {
                Log.e("Catch", "" + e.message)
            }
        }
        return output
    }

    private suspend fun <T : Any> newsApiOutput(
        call: suspend () -> Response<T>,
        error: String
    ): Output<T> {
        val response = call.invoke()
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return@newsApiOutput Output.Success(resultResponse)
            }
        }else{
            Log.e("Errorr", "The $error and the ${response.body()}")

        }
        return Output.Error(IOException("OOps .. Something went wrong due to  $error"))
    }
}