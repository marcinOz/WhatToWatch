package pl.oziem.whattowatch.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import pl.oziem.whattowatch.image_loader.GlideImageLoader
import pl.oziem.whattowatch.image_loader.ImageLoader
import pl.oziem.whattowatch.sharedpref.SharedPreferenceMediator
import pl.oziem.whattowatch.sharedpref.SharedPreferenceMediatorImp
import javax.inject.Singleton

/**
* Created by Marcin Oziemski on 01.03.2018 WhatToWatch.
*/
@Module()
class ApplicationModule {
  @Provides
  @Singleton
  fun provideContext(application: Application): Context = application

  @Provides
  fun provideSharedPrefMediator(context: Context): SharedPreferenceMediator =
    SharedPreferenceMediatorImp(context)

  @Provides
  @Singleton
  fun provideImageLoader(sharedPrefMediator: SharedPreferenceMediator): ImageLoader =
    GlideImageLoader(sharedPrefMediator)
}
