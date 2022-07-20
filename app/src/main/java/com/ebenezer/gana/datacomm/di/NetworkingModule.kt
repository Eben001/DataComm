package com.ebenezer.gana.datacomm.di

import com.ebenezer.gana.datacomm.BuildConfig
import com.ebenezer.gana.datacomm.data.network.PaytevApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {
    private const val HEADER_AUTHORIZATION = "Authorization"
    private const val BASE_URL = "https://client.paytev.com/api/v1/"


    @Provides
    fun provideFactory(): Converter.Factory{
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideClient(interceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(interceptor)
            .addInterceptor(buildQueryParameterInterceptor())
            .build()


    fun buildQueryParameterInterceptor() = Interceptor { chain ->
        val originalRequest = chain.request()
        val url = originalRequest.url.newBuilder()
            .addQueryParameter("apitoken", BuildConfig.PAYTEV_API_KEY)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(url)
            .build()
        chain.proceed(newRequest)
    }

    @Provides
    fun provideAuthorizationInterceptor() = Interceptor { chain ->
        val originalRequest = chain.request()

        val newRequest = originalRequest.newBuilder()
            .addHeader(HEADER_AUTHORIZATION, BuildConfig.PAYTEV_API_KEY)
            .build()
        chain.proceed(newRequest)
    }


    @Provides
    fun buildRetrofit(client: OkHttpClient, factory:Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(factory)
            .build()

    }

    @Provides
    fun buildApiService(retrofit: Retrofit): PaytevApi =
        retrofit.create(PaytevApi::class.java)
}