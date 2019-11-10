package pl.oziem.whattowatch.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mboudraa.flow.Flow
import com.mboudraa.flow.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import pl.oziem.datasource.auth.AuthRepository
import pl.oziem.datasource.models.Credentials
import pl.oziem.whattowatch.signin.states.ErrorState
import pl.oziem.whattowatch.signin.states.LoginFormState
import javax.inject.Inject

class SignInViewModel @Inject constructor(
  authRepository: AuthRepository
) : ViewModel() {

  private val flow = LoginFlow(authRepository, viewModelScope)

  fun addOnStateChangeListener(listener: (state: State<*, *>, flow: Flow) -> Unit) {
    flow.addOnStateChangeListener { state, flow ->
      viewModelScope.launch { listener(state, flow) }
    }
  }

  fun validateEmail(email: String) {
    (flow.currentState as? LoginFormState)?.validateEmail(flow, email)
  }

  fun onSignInClick(email: String, password: String) {
    (flow.currentState as? LoginFormState)?.submit(flow, Credentials(email, password))
  }

  fun onDmismissErrorDialog() {
    (flow.currentState as? ErrorState)?.dismiss(flow)
  }
}
