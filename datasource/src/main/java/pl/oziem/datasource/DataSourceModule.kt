package pl.oziem.datasource

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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
import retrofit2.converter.moshi.MoshiConverterFactory
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
  fun provideMoshi(): Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

  @Provides
  fun provideApiService(moshi: Moshi, okHttpClient: OkHttpClient, context: Context): ApiService {
    val retrofit = Retrofit.Builder()
      .addConverterFactory(MoshiConverterFactory.create(moshi))
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
