package pl.oziem.whattowatch.signin.usecase

import com.mboudraa.flow.Flow
import kotlinx.coroutines.withContext
import pl.oziem.commons.CoroutineContextProvider
import pl.oziem.datasource.auth.AuthError
import pl.oziem.datasource.auth.AuthRepository
import pl.oziem.whattowatch.signin.states.LoadingState
import javax.inject.Inject

class TryToSignInUseCase @Inject constructor(
  private val contextProvider: CoroutineContextProvider
) {

  suspend fun tryToSignIn(authRepository: AuthRepository, flow: Flow) {
    val credentials = LoadingState.getData(flow)
    try {
      val profile = withContext(contextProvider.IO) { authRepository.signIn(credentials) }
      LoadingState.success(flow, profile)
    } catch (e: AuthError) {
      LoadingState.error(flow, e)
    }
  }
}
