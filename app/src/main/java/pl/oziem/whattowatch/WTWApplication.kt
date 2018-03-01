package pl.oziem.whattowatch

import android.app.Application
import android.content.Context
import pl.oziem.whattowatch.di.AppComponent
import pl.oziem.whattowatch.di.DaggerAppComponent

/**
 * Created by Marcin Oziemski on 01.03.2018.
 * Copyright (C) 2017 OKE Poland Sp. z o.o. All rights reserved.
 */
// WTW - What To Watch
class WTWApplication : Application() {

  companion object {
    fun getComponent(context: Context) =
      (context.applicationContext as WTWApplication).appComponent
  }

  lateinit var appComponent: AppComponent

  override fun onCreate() {
    super.onCreate()
    appComponent = DaggerAppComponent
      .builder()
      .application(this)
      .build()

    appComponent.inject(this)
  }
}
