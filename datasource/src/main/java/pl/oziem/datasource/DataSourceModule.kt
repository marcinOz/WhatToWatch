package pl.oziem.datasource

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import pl.oziem.datasource.services.ApiService
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by MarcinOz on 2018-03-02.
 * Copyright (C) 2017 OKE Poland Sp. z o.o. All rights reserved.
 */
@Module
open class DataSourceModule {

  @Singleton
  @Provides
  fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
      .readTimeout(1, TimeUnit.MINUTES)
      .writeTimeout(1, TimeUnit.MINUTES)
      .addInterceptor(HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG)
          HttpLoggingInterceptor.Level.BODY
        else
          HttpLoggingInterceptor.Level.NONE
      })
      .build()
  }

  @Provides
  @Singleton
  fun provideGson(): Gson {
    val gsonBuilder = GsonBuilder()
    gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    return gsonBuilder.create()
  }

  @Provides
  @Singleton
  fun provideApiService(gson: Gson, okHttpClient: OkHttpClient): ApiService {
    val retrofit = Retrofit.Builder()
      .addConverterFactory(GsonConverterFactory.create(gson))
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .client(okHttpClient)
      .baseUrl("test.com")
      .build()
    return retrofit.create(ApiService::class.java)
  }
}
