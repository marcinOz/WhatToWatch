package pl.oziem.whattowatch

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.View
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import pl.oziem.datasource.analytics.AnalyticsMediator
import pl.oziem.whattowatch.di.AppComponent
import pl.oziem.whattowatch.di.DaggerAppComponent
import javax.inject.Inject


/**
* Created by Marcin Oziemski on 01.03.2018 WhatToWatch.
*/
// WTW - What To Watch
class WTWApplication : Application(), HasActivityInjector, Application.ActivityLifecycleCallbacks {

  companion object {
    fun getComponent(context: Context) =
      (context.applicationContext as WTWApplication).appComponent

    fun getImageLoader(view: View) = getImageLoader(view.context)
    fun getImageLoader(context: Context) =
      (context.applicationContext as WTWApplication).appComponent.getImageLoader().with(context)
    fun getSharedPrefMediator(context: Context) =
      (context.applicationContext as WTWApplication).appComponent.getSharedPrefMediator()
  }

  @Inject
  lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
  @Inject
  lateinit var analyticsMediator: AnalyticsMediator
  lateinit var appComponent: AppComponent

  override fun onCreate() {
    super.onCreate()
    registerActivityLifecycleCallbacks(this)
    appComponent = DaggerAppComponent
      .builder()
      .application(this)
      .build()

    appComponent.inject(this)
  }

  override fun activityInjector(): DispatchingAndroidInjector<Activity> {
    return activityDispatchingAndroidInjector
  }

  override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
    analyticsMediator.onActivityCreate(activity)
  }

  override fun onActivityStarted(activity: Activity) {}
  override fun onActivityResumed(activity: Activity) {}
  override fun onActivityPaused(activity: Activity) {}
  override fun onActivityStopped(activity: Activity) {}
  override fun onActivityDestroyed(activity: Activity) {}
  override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle?) {}
}
