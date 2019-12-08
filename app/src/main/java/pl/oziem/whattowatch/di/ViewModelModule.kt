package pl.oziem.whattowatch.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import pl.oziem.whattowatch.movies.MovieListViewModel
import pl.oziem.whattowatch.profile.ProfileViewModel
import pl.oziem.whattowatch.signin.SignInViewModel
import pl.oziem.whattowatch.splash.SplashViewModel
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
class ViewModelFactory @Inject constructor(
  private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

  override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModels[modelClass]?.get() as T
}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {

  @Binds
  internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

  @Binds
  @IntoMap
  @ViewModelKey(MovieListViewModel::class)
  internal abstract fun postListViewModel(viewModel: MovieListViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(SplashViewModel::class)
  internal abstract fun postSplashViewModel(viewModel: SplashViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(SignInViewModel::class)
  internal abstract fun postSigninViewModel(viewModel: SignInViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(ProfileViewModel::class)
  internal abstract fun postProfileViewModel(viewModel: ProfileViewModel): ViewModel
}
