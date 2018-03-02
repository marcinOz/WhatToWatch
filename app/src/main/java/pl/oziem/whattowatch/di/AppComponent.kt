package pl.oziem.whattowatch.di

import android.app.Application
import dagger.Component
import dagger.BindsInstance
import dagger.android.AndroidInjectionModule
import pl.oziem.whattowatch.WTWApplication
import pl.oziem.datasource.DataSourceModule


/**
 * Created by Marcin Oziemski on 01.03.2018.
 * Copyright (C) 2017 OKE Poland Sp. z o.o. All rights reserved.
 */
@Component(modules = [
  (AndroidInjectionModule::class),
  (ApplicationModule::class),
  (ActivityBuilder::class),
  (DataSourceModule::class)
])
interface AppComponent {

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(application: Application): Builder

    fun build(): AppComponent
  }

  fun inject(app: WTWApplication)
}
