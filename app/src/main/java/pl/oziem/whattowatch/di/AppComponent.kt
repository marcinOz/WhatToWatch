package pl.oziem.whattowatch.di

import android.app.Application
import dagger.Component
import dagger.BindsInstance
import dagger.android.AndroidInjectionModule
import pl.oziem.whattowatch.WTWApplication
import pl.oziem.datasource.DataSourceModule
import javax.inject.Singleton


/**
 * Created by Marcin Oziemski on 01.03.2018.
 * Copyright (C) 2017 OKE Poland Sp. z o.o. All rights reserved.
 */
@Singleton
@Component(modules = [
  (DataSourceModule::class),
  (AndroidInjectionModule::class),
  (ActivityBuilder::class),
  (ApplicationModule::class)
])
interface AppComponent {

  @Component.Builder
  interface Builder {
    @BindsInstance fun application(application: Application): Builder
    fun build(): AppComponent
  }

  fun inject(app: WTWApplication)
}
