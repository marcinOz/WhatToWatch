package pl.oziem.whattowatch.signin.states

import com.mboudraa.flow.Flow
import com.mboudraa.flow.State
import pl.oziem.datasource.models.Profile

object AuthenticatedState : State<Profile, AuthenticatedState.Action>() {
  sealed class Action {
    object LogOut : Action()
  }

  fun logOut(flow: Flow) = dispatchAction(flow, Action.LogOut)
}
