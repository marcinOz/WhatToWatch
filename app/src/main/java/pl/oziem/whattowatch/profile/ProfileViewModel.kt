package pl.oziem.whattowatch.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import pl.oziem.commons.withDispatcherIO
import pl.oziem.datasource.auth.AuthRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
  private val authRepository: AuthRepository
) : ViewModel() {

  private val uiScope = CoroutineScope(Dispatchers.Main)

  val state = MutableLiveData<ProfileState>()

  override fun onCleared() {
    super.onCleared()
    uiScope.coroutineContext.cancel()
  }

  fun downloadUser() {
    authRepository.getUser()?.let {
      state.value = ProfileState.Idle(it.viewingName, it.photoUrl)
    }
  }

  fun signOut() = uiScope.launch {
    withDispatcherIO {
      authRepository.signOut()
    }
    state.value = ProfileState.SigningOut
  }
}

sealed class ProfileState {
  data class Idle(val name: String, val image: Uri?): ProfileState()
  object SigningOut: ProfileState()
}
