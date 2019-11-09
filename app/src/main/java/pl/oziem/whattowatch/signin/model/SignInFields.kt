package pl.oziem.whattowatch.signin.model

data class SignInFields(
  val email: String,
  val isEmailValid: Boolean = true,
  val isPasswordValid: Boolean = true
) {
  val areValid get() = isEmailValid && isPasswordValid
}
