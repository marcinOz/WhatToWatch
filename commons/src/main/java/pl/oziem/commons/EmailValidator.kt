package pl.oziem.commons

import android.util.Patterns

object EmailValidator {
  fun isValid(email: String) =
    if (email.isEmpty()) false
    else Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
