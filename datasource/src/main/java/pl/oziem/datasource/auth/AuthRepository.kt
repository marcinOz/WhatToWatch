package pl.oziem.datasource.auth

import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable
import pl.oziem.datasource.models.Credentials
import pl.oziem.datasource.models.Profile
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthRepository {

  private val firebaseAuth = FirebaseAuth.getInstance()

  fun isUserLoggedIn(): Boolean = firebaseAuth.currentUser != null

  fun getUser(): Profile? = firebaseAuth.currentUser?.let {
    Profile(
      it.uid,
      it.displayName,
      it.email ?: "",
      it.isEmailVerified,
      it.photoUrl
    )
  }

  fun signUp(credentials: Credentials) = Completable.create { emitter ->
    firebaseAuth.createUserWithEmailAndPassword(
      credentials.email,
      credentials.password
    ).addOnCompleteListener {
      if (it.isSuccessful) {
        emitter.onComplete()
      } else it.exception?.let { exception ->
        emitter.onError(exception)
      }
    }
  }

  suspend fun signIn(credentials: Credentials): Profile = suspendCoroutine { continuation ->
    firebaseAuth.signInWithEmailAndPassword(
      credentials.email,
      credentials.password
    ).addOnCompleteListener {
      val profile = getUser()
      if (it.isSuccessful && profile != null) {
        continuation.resume(profile)
      } else it.exception?.let { exception ->
        continuation.resumeWithException(parseAuthException(exception))
      }
    }
  }

  fun signOut() = firebaseAuth.signOut()
}
