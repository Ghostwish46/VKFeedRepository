package dev.ghost.homework_2.di

import dagger.Module
import dagger.Provides
import dev.ghost.homework_2.model.network.ApiVariables
import dev.ghost.homework_2.model.network.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun prodvideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor {
                chain ->
            val newUrl = chain.request().url()
                .newBuilder()
                .addQueryParameter("access_token", ApiVariables.apiToken)
                .addQueryParameter("v", ApiVariables.API_VERSION)
                .build()

            val newRequest = chain.request()
                .newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)
        }
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(ApiVariables.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}
