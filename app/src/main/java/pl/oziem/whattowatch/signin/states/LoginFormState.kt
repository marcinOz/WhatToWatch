package pl.oziem.whattowatch.signin.states

import com.mboudraa.flow.Flow
import com.mboudraa.flow.State
import pl.oziem.datasource.models.Credentials
import pl.oziem.whattowatch.signin.model.SignInFields

object LoginFormState : State<SignInFields, LoginFormState.Action>() {
  sealed class Action {
    data class Submit(val credentials: Credentials) : Action()
    data class ValidateEmail(val email:String) : Action()
  }

  fun submit(flow: Flow, credentials: Credentials) = dispatchAction(flow, Action.Submit(credentials))
  fun validateEmail(flow: Flow, email: String) = dispatchAction(flow, Action.ValidateEmail(email))
}
