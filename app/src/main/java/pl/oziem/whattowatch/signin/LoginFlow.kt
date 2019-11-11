package pl.oziem.whattowatch.signin

import com.mboudraa.flow.Flow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.oziem.commons.EmailValidator
import pl.oziem.datasource.auth.AuthRepository
import pl.oziem.whattowatch.signin.model.SignInFields
import pl.oziem.whattowatch.signin.states.*
import pl.oziem.whattowatch.signin.usecase.CheckCredentialsUseCase
import pl.oziem.whattowatch.signin.usecase.TryToSignInUseCase

class LoginFlow(
  authRepository: AuthRepository,
  private val viewModelScope: CoroutineScope,
  private val tryToSignInUseCase: TryToSignInUseCase
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
      is ErrorState.Action.Dismiss -> goBackTo(LoginFormState)
    }
  }

  onTransition { flow, transition, action ->
    when (transition) {
      LoginFormState to CheckCredentialsState -> CheckCredentialsUseCase.checkCredentials(flow)
      CheckCredentialsState to LoadingState -> viewModelScope.launch {
        tryToSignInUseCase.tryToSignIn(authRepository, flow)
      }
    }
  }
})


