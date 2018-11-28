package pl.oziem.whattowatch.signin

import com.mboudraa.flow.Flow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.oziem.commons.EmailValidator
import pl.oziem.commons.withDispatcherIO
import pl.oziem.datasource.auth.AuthError
import pl.oziem.datasource.auth.AuthRepository
import pl.oziem.whattowatch.signin.model.SignInFields
import pl.oziem.whattowatch.signin.states.*

class LoginFlow(
  authRepository: AuthRepository,
  private val uiScope: CoroutineScope
) : Flow({

  startWith(LoginFormState, SignInFields(email = ""))

  forState(LoginFormState) { emailField, action ->
    when (action) {
      is LoginFormState.Action.Submit -> goto(CheckCredentialsState) using action.credentials
      is LoginFormState.Action.ValidateEmail -> stay using emailField.copy(
        isEmailValid = EmailValidator.isValid(action.email)
      )
    }
  }

  forState(CheckCredentialsState) { _, action ->
    when (action) {
      is CheckCredentialsState.Action.Success -> goto(LoadingState) using action.credentials
      is CheckCredentialsState.Action.Failure -> goBackTo(LoginFormState)
    }
  }

  forState(LoadingState) { _, action ->
    when (action) {
      is LoadingState.Action.Success -> goto(AuthenticatedState) using action.profile
      is LoadingState.Action.Failure -> goto(ErrorState) using action.error
    }
  }

  forState(AuthenticatedState) { input, action ->
    when (action) {
      is AuthenticatedState.Action.LogOut ->
        goto(LoginFormState) using SignInFields(input.email, true)
    }
  }

  forState(ErrorState) { _, action ->
    when (action) {
      is ErrorState.Action.Dismiss -> goBackTo(LoadingState)
    }
  }

  onTransition { flow, transition, action ->
    when (transition) {
      LoginFormState to CheckCredentialsState -> checkCredentials(flow)
      CheckCredentialsState to LoadingState -> uiScope.launch { tryToSignIn(authRepository, flow) }
    }
  }
})

private fun checkCredentials(flow: Flow) {
  val credentials = CheckCredentialsState.getData(flow)
  val signInFields = SignInFields(
    credentials.email,
    EmailValidator.isValid(credentials.email),
    credentials.password.isNotEmpty()
  )
  if (signInFields.areValid) {
    CheckCredentialsState.success(flow, credentials)
  } else {
    CheckCredentialsState.error(flow, signInFields)
  }
}

private suspend fun tryToSignIn(authRepository: AuthRepository, flow: Flow) {
  val credentials = LoadingState.getData(flow)
  try {
    val profile = withDispatcherIO { authRepository.signIn(credentials) }
    LoadingState.success(flow, profile)
  } catch (e: AuthError) {
    LoadingState.error(flow, e)
  }
}
