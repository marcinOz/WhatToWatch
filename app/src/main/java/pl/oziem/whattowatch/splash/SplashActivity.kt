package pl.oziem.whattowatch.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.crashlytics.android.Crashlytics
import com.google.android.gms.common.GoogleApiAvailability
import dagger.android.AndroidInjection
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.delay
import pl.oziem.commons.observe
import pl.oziem.commons.withViewModel
import pl.oziem.datasource.models.ErrorState
import pl.oziem.datasource.models.PopulatedState
import pl.oziem.datasource.models.ResourceState
import pl.oziem.whattowatch.R
import pl.oziem.whattowatch.main.MovieListActivity
import javax.inject.Inject

/** Created by Marcin Oziemski on 11/03/2018 WhatToWatch.
 */
class SplashActivity : AppCompatActivity() {

  companion object {
    private const val SHORT_DELAY = 500L
    private const val LONG_DELAY = SHORT_DELAY * 2
  }

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private val scaleAnimation: Animation = getScaleAnimation()
  private val fadeAnimation: Animation = getFadeAnimation()
  @Volatile
  private var animationFinished = false
  @Volatile
  private var dataFetched = false

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)
    Fabric.with(this, Crashlytics())
    setContentView(R.layout.activity_splash)

    GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this)
      .addOnCompleteListener { task ->
        if (task.isSuccessful.not()) return@addOnCompleteListener
        withViewModel<SplashViewModel>(viewModelFactory) {
          observe(fetchedData, ::updateView)
        }
      }

    initAnimation()
  }

  private fun getScaleAnimation() = ScaleAnimation(
    0f, 1f, 0f, 1f,
    Animation.RELATIVE_TO_SELF, 0.5f,
    Animation.RELATIVE_TO_SELF, 0.5f
  ).apply {
    duration = LONG_DELAY
  }

  private fun getFadeAnimation() = AlphaAnimation(0f, 1f).apply {
    duration = SHORT_DELAY
    isFillEnabled = true
    fillAfter = true
  }

  private fun initAnimation() = lifecycleScope.launchWhenResumed {
    appImage.alpha = 0f
    appName.alpha = 0f
    circle.startAnimation(scaleAnimation)
    delay(SHORT_DELAY)

    appImage.startAnimation(fadeAnimation)
    appName.startAnimation(fadeAnimation)
    appImage.alpha = 1f
    appName.alpha = 1f

    delay(LONG_DELAY)

    animationFinished = true
    startHomeActivity()
  }

  private fun updateView(resourceState: ResourceState) = when (resourceState) {
    is PopulatedState -> onDataFetched()
    is ErrorState -> showError(resourceState.message)
    else -> {/*NOTHING*/
    }
  }

  private fun showError(message: String?) {
    errorMessageTextView.text = message ?: getString(R.string.server_error)
    errorMessageTextView.visibility = View.VISIBLE
  }

  private fun onDataFetched() {
    dataFetched = true
    startHomeActivity()
  }

  @SuppressLint("PrivateResource")
  @Synchronized
  private fun startHomeActivity() {
    if (isFinishing
      || !dataFetched
      || !animationFinished
    ) return
    val options = ActivityOptionsCompat.makeCustomAnimation(
      this,
      R.anim.abc_fade_in,
      R.anim.abc_fade_out
    )
    runOnUiThread {
      startActivity(Intent(this, MovieListActivity::class.java), options.toBundle())
    }
    finish()
  }
}
