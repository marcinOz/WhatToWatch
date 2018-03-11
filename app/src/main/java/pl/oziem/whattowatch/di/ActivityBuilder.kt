package pl.oziem.whattowatch.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.oziem.whattowatch.main.MovieListActivity
import pl.oziem.whattowatch.main.MovieListModule

/**
* Created by Marcin Oziemski on 01.03.2018 WhatToWatch.
*/
@Module
abstract class ActivityBuilder {

  @ContributesAndroidInjector(modules = [(MovieListModule::class)])
  abstract fun bindMovieListActivity(): MovieListActivity
}
