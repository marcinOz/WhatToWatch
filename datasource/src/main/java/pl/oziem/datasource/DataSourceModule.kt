package pl.oziem.datasource

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import pl.oziem.datasource.analytics.AnalyticsMediator
import pl.oziem.datasource.analytics.FirebaseAnalyticsMediatorImp
import pl.oziem.datasource.dataprovider.DataProvider
import pl.oziem.datasource.dataprovider.DataProviderImp
import pl.oziem.datasource.remote_config.FirebaseRemoteConfigMediator
import pl.oziem.datasource.remote_config.FirebaseRemoteConfigMediatorImp
import pl.oziem.datasource.services.ApiService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/** Created by MarcinOz on 2018-03-02 WhatToWatch.
 */
@Module
open class DataSourceModule {

  @Provides
  fun provideOkHttpClient(context: Context): OkHttpClient =
    OkHttpClient.Builder()
      .readTimeout(20, TimeUnit.SECONDS)
      .writeTimeout(20, TimeUnit.SECONDS)
      .addInterceptor(OkHttpInterceptors.loggingInterceptor())
      .addInterceptor(OkHttpInterceptors.rewriteCacheControlInterceptor(context))
      .cache(Cache(
        File(context.cacheDir, "responses"),
        10 * 1024 * 1024  //10MB
      ))
      .build()

  @Provides
  fun provideGson(): Gson {
    val gsonBuilder = GsonBuilder()
    gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    return gsonBuilder.create()
  }

  @Provides
  fun provideApiService(gson: Gson, okHttpClient: OkHttpClient, context: Context): ApiService {
    val retrofit = Retrofit.Builder()
      .addConverterFactory(GsonConverterFactory.create(gson))
      .addConverterFactory(ScalarsConverterFactory.create())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .client(okHttpClient)
      .baseUrl(context.getString(R.string.base_url))
      .build()
    return retrofit.create(ApiService::class.java)
  }

  @Provides
  @Singleton
  fun provideDataProvider(apiService: ApiService,
                          firebaseRemoteConfigMediator: FirebaseRemoteConfigMediator): DataProvider {
    return DataProviderImp(apiService, firebaseRemoteConfigMediator)
  }

  @Provides
  @Singleton
  fun provideFirebaseRemoteConfigMediator(): FirebaseRemoteConfigMediator = FirebaseRemoteConfigMediatorImp()

  @Provides
  fun provideAnalyticsMediator(): AnalyticsMediator = FirebaseAnalyticsMediatorImp()
}
