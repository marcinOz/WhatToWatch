package pl.oziem.whattowatch.signin

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.annotation.MainThread
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.TranslateAnimation
import com.jakewharton.rxbinding3.widget.textChanges
import com.mboudraa.flow.Flow
import com.mboudraa.flow.State
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_signin.*
import pl.oziem.commons.getViewModel
import pl.oziem.datasource.auth.AuthError
import pl.oziem.whattowatch.R
import pl.oziem.whattowatch.profile.ProfileActivity
import pl.oziem.whattowatch.signin.states.AuthenticatedState
import pl.oziem.whattowatch.signin.states.ErrorState
import pl.oziem.whattowatch.signin.states.LoadingState
import pl.oziem.whattowatch.signin.states.LoginFormState
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SignInActivity : AppCompatActivity() {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  private val viewModel by lazy {
    getViewModel<SignInViewModel>(viewModelFactory)
  }

  private val compositeDisposable = CompositeDisposable()
  private var bounceAnimation: Animation? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_signin)

    compositeDisposable += emailInputEditText.textChanges()
      .doOnNext { emailInputLayout.error = "" }
      .filter { it.length >= MIN_EMAIL_LENGTH }
      .debounce(EMAIL_EDIT_DEBOUNCE_DURATION, TimeUnit.SECONDS)
      .subscribe {
        viewModel.validateEmail(it.toString())
      }

    compositeDisposable += passwordInputEditText.textChanges()
      .subscribe { passwordInputLayout.error = null }

    signInButton.setOnClickListener {
      viewModel.onSignInClick(
        emailInputEditText.text.toString(),
        passwordInputEditText.text.toString()
      )
    }

    viewModel.addOnStateChangeListener(::onStateChange)
  }

  override fun onDestroy() {
    compositeDisposable.dispose()
    super.onDestroy()
  }

  @MainThread
  private fun onStateChange(state: State<*, *>, flow: Flow) {
    when (state) {
      is LoginFormState -> {
        stopLoadingAnimation()
        signInButton.isEnabled = true
        val data = state.getData(flow)
        emailInputLayout.error =
          if (!data.isEmailValid) getString(R.string.invalid_email_title)
          else null
        passwordInputLayout.error =
          if (!data.isPasswordValid) getString(R.string.invalid_password_title)
          else null
      }
      is LoadingState -> {
        signInButton.isEnabled = false
        passwordInputLayout.error = null
        startLoadingAnimation()
      }
      is ErrorState -> {
        val data = state.getData(flow)
        showErrorDialog(data)
      }
      is AuthenticatedState -> {
        stopLoadingAnimation()
        ProfileActivity.start(this@SignInActivity)
        finish()
      }
    }
  }

  private fun showErrorDialog(error: AuthError) = when (error) {
    AuthError.InvalidCredentials -> showOkDialog(
      R.string.invalid_credentials_title,
      R.string.invalid_credentials_message
    )
    AuthError.InvalidUser -> showOkDialog(
      R.string.invalid_email_title,
      R.string.invalid_email_message
    )
    AuthError.NetworkUnavailable -> showOkDialog(
      R.string.network_error_title,
      R.string.network_error_message
    )
    AuthError.Unexpected -> showOkDialog(
      R.string.unexpected_error_title,
      R.string.unexpected_error_message
    )
  }

  private fun showOkDialog(@StringRes title: Int, @StringRes message: Int) {
    AlertDialog.Builder(this)
      .setTitle(title)
      .setMessage(message)
      .setOnDismissListener { viewModel.onDmismissErrorDialog() }
      .setPositiveButton(getString(R.string.ok)) { _, _ -> viewModel.onDmismissErrorDialog() }
      .create()
      .show()
  }

  private fun startLoadingAnimation() {
    progressBar.visibility = View.VISIBLE
    circle.post {
      bounceAnimation = TranslateAnimation(0f, 0f, 0f, 40f).apply {
        interpolator = BounceInterpolator()
        repeatMode = Animation.REVERSE
        repeatCount = 1
        duration = 500
      }
      circle.startAnimation(bounceAnimation)
    }
  }

  private fun stopLoadingAnimation() {
    if (progressBar.visibility == View.GONE) return
    progressBar.visibility = View.GONE
  }

  companion object {
    private const val MIN_EMAIL_LENGTH = 4
    private const val EMAIL_EDIT_DEBOUNCE_DURATION = 1L
  }
}
