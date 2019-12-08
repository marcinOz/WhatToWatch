package pl.oziem.whattowatch.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.oziem.whattowatch.main.MainActivity
import pl.oziem.whattowatch.main.MainActivityModule
import pl.oziem.whattowatch.profile.ProfileActivity
import pl.oziem.whattowatch.signin.SignInActivity
import pl.oziem.whattowatch.splash.SplashActivity

@Module
abstract class ActivityBuilder {

  @ContributesAndroidInjector
  @ActivityScope
  abstract fun bindSplashActivity(): SplashActivity

  @ContributesAndroidInjector(modules = [(MainActivityModule::class)])
  @ActivityScope
  abstract fun bindMainActivity(): MainActivity

  @ContributesAndroidInjector
  @ActivityScope
  abstract fun bindSignInActivity(): SignInActivity

  @ContributesAndroidInjector
  @ActivityScope
  abstract fun bindProfileActivity(): ProfileActivity
}
