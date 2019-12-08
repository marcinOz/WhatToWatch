package pl.oziem.whattowatch.main

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.oziem.whattowatch.movies.MovieListFragment

@Module
abstract class MainActivityModule {

  @ContributesAndroidInjector
  abstract fun contributeMovieListFragment() : MovieListFragment
}
