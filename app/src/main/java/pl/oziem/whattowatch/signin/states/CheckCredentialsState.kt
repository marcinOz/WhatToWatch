package pl.oziem.whattowatch.signin.states

import com.mboudraa.flow.Flow
import com.mboudraa.flow.State
import pl.oziem.datasource.models.Credentials
import pl.oziem.whattowatch.signin.model.SignInFields

object CheckCredentialsState : State<Credentials, CheckCredentialsState.Action>() {

  sealed class Action {
    data class Success(val credentials: Credentials) : Action()
    data class Failure(val signInFields: SignInFields) : Action()
  }

  fun success(flow: Flow, credentials: Credentials) = dispatchAction(flow, Action.Success(credentials))
  fun error(flow: Flow, signInFields: SignInFields) = dispatchAction(flow, Action.Failure(signInFields))
}
