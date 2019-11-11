package pl.oziem.whattowatch.profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.oziem.commons.CoroutineContextProvider
import pl.oziem.datasource.auth.AuthRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
  private val authRepository: AuthRepository,
  private val contextProvider: CoroutineContextProvider
) : ViewModel() {

  val state = MutableLiveData<ProfileState>()

  fun downloadUser() {
    authRepository.getUser()?.let {
      state.value = ProfileState.Idle(it.viewingName, it.photoUrl)
    }
  }

  fun signOut() = viewModelScope.launch {
    withContext(contextProvider.IO) {
      authRepository.signOut()
    }
    state.value = ProfileState.SigningOut
  }
}

sealed class ProfileState {
  data class Idle(val name: String, val image: Uri?): ProfileState()
  object SigningOut: ProfileState()
}
