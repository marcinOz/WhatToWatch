package pl.oziem.whattowatch.signin.states

import com.mboudraa.flow.Flow
import com.mboudraa.flow.State
import pl.oziem.datasource.auth.AuthError

object ErrorState : State<AuthError, ErrorState.Action>() {
  sealed class Action {
    object Dismiss : Action()
  }

  fun dismiss(flow: Flow) = dispatchAction(flow, Action.Dismiss)
}
