package pl.oziem.whattowatch.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Marcin Oziemski on 01.03.2018.
 * Copyright (C) 2017 OKE Poland Sp. z o.o. All rights reserved.
 */
@Module
class ApplicationModule {
  @Provides
  @Singleton
  fun provideContext(application: Application): Context = application
}
