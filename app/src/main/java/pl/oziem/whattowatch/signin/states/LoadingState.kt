package pl.oziem.whattowatch.signin.states

import com.mboudraa.flow.Flow
import com.mboudraa.flow.State
import pl.oziem.datasource.auth.AuthError
import pl.oziem.datasource.models.Credentials
import pl.oziem.datasource.models.Profile

object LoadingState : State<Credentials, LoadingState.Action>() {

  sealed class Action {
    data class Success(val profile: Profile) : Action()
    data class Failure(val error: AuthError) : Action()
  }

  fun success(flow: Flow, profile: Profile) = dispatchAction(flow, Action.Success(profile))
  fun error(flow: Flow, error: AuthError) = dispatchAction(flow, Action.Failure(error))
}
