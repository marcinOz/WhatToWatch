package pl.oziem.datasource.auth

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

sealed class AuthError : Exception() {
  /**
   * The password is invalid or the user does not have a password.
   */
  object InvalidCredentials : AuthError()
  object InvalidUser : AuthError()
  object NetworkUnavailable : AuthError()
  object Unexpected : AuthError()
}

fun parseAuthException(exception: Throwable) = when (exception) {
  is FirebaseAuthInvalidCredentialsException -> AuthError.InvalidCredentials
  is FirebaseAuthInvalidUserException -> AuthError.InvalidUser
  is FirebaseNetworkException -> AuthError.NetworkUnavailable
  else -> AuthError.Unexpected
}
