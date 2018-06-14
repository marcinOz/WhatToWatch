package pl.oziem.whattowatch.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.oziem.whattowatch.main.MovieListActivity
import pl.oziem.whattowatch.splash.SplashActivity
import pl.oziem.whattowatch.splash.SplashModule

/**
* Created by Marcin Oziemski on 01.03.2018 WhatToWatch.
*/
@Module
abstract class ActivityBuilder {

  @ContributesAndroidInjector(modules = [(SplashModule::class)])
  @ActivityScope
  abstract fun bindSplashActivity(): SplashActivity

  @ContributesAndroidInjector()
  @ActivityScope
  abstract fun bindMovieListActivity(): MovieListActivity
}
