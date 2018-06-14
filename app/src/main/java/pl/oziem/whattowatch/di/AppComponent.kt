package pl.oziem.whattowatch.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import pl.oziem.datasource.DataSourceModule
import pl.oziem.whattowatch.WTWApplication
import pl.oziem.whattowatch.image_loader.ImageLoader
import pl.oziem.whattowatch.sharedpref.SharedPreferenceMediator
import javax.inject.Singleton


/**
* Created by Marcin Oziemski on 01.03.2018 WhatToWatch.
*/
@Singleton
@Component(modules = [
  (DataSourceModule::class),
  (AndroidInjectionModule::class),
  (ActivityBuilder::class),
  (ApplicationModule::class),
  (ViewModelModule::class)
])
interface AppComponent {

  @Component.Builder
  interface Builder {
    @BindsInstance fun application(application: Application): Builder
    fun build(): AppComponent
  }

  fun inject(app: WTWApplication)

  fun getImageLoader(): ImageLoader
  fun getSharedPrefMediator(): SharedPreferenceMediator
}
