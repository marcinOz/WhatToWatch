package pl.oziem.whattowatch.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import pl.oziem.whattowatch.firebase.FirebaseRemoteConfigHelper
import javax.inject.Singleton

/**
 * Created by Marcin Oziemski on 01.03.2018.
 * Copyright (C) 2017 OKE Poland Sp. z o.o. All rights reserved.
 */
@Module()
class ApplicationModule {
  @Provides
  @Singleton
  fun provideContext(application: Application): Context = application

  @Provides
  @Singleton
  fun provideFirebaseRemoteConfigHelper(): FirebaseRemoteConfigHelper = FirebaseRemoteConfigHelper()

  @Provides
  fun provideTMDbApiKey(firebaseRemoteConfigHelper: FirebaseRemoteConfigHelper): ApiKey =
    firebaseRemoteConfigHelper.getTMDbApiKey()
}

typealias ApiKey = String
