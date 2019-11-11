package pl.oziem.whattowatch.signin.usecase

import com.mboudraa.flow.Flow
import pl.oziem.commons.EmailValidator
import pl.oziem.whattowatch.signin.model.SignInFields
import pl.oziem.whattowatch.signin.states.CheckCredentialsState

object CheckCredentialsUseCase {

  fun checkCredentials(flow: Flow) {
    val credentials = CheckCredentialsState.getData(flow)
    val signInFields = SignInFields(
      credentials.email,
      EmailValidator.isValid(credentials.email),
      credentials.password.isNotEmpty()
    )
    if (signInFields.areValid) {
      CheckCredentialsState.success(flow, credentials)
    } else {
      CheckCredentialsState.error(flow, signInFields)
    }
  }
}
