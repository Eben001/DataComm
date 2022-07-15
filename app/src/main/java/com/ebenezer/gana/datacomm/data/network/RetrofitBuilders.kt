package com.ebenezer.gana.datacomm.data.network

import com.ebenezer.gana.datacomm.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Builds retrofit dependencies.
 */

private const val HEADER_AUTHORIZATION = "Authorization"
private const val BASE_URL = "https://client.paytev.com/api/v1/"

private fun buildClient(): OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor(buildAuthorizationInterceptor())
        .addInterceptor(buildQueryParameterInterceptor())
        .build()

private fun buildQueryParameterInterceptor() = Interceptor { chain ->
    val originalRequest = chain.request()
    val url = originalRequest.url.newBuilder()
        .addQueryParameter("apitoken", BuildConfig.PAYTEV_API_KEY)
        .build()

    val newRequest = originalRequest.newBuilder()
        .url(url)
        .build()
    chain.proceed(newRequest)
}

private fun buildAuthorizationInterceptor() = Interceptor { chain ->
    val originalRequest = chain.request()

    val newRequest = originalRequest.newBuilder()
        .addHeader(HEADER_AUTHORIZATION, BuildConfig.PAYTEV_API_KEY)
        .build()
    chain.proceed(newRequest)
}


private fun buildRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(buildClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}

fun buildApiService(): PaytevApi =
    buildRetrofit().create(PaytevApi::class.java)