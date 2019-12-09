package pl.oziem.whattowatch.main

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.oziem.whattowatch.movies.MovieListFragment
import pl.oziem.whattowatch.profile.ProfileFragment

@Module
abstract class MainActivityModule {

  @ContributesAndroidInjector
  abstract fun contributeMovieListFragment() : MovieListFragment

  @ContributesAndroidInjector
  abstract fun contributeProfileFragment() : ProfileFragment
}
