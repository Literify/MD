package com.literify.data.remote.retrofit

import com.literify.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.LITERIFY_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder().build())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}