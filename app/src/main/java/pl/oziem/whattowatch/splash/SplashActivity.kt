package pl.oziem.whattowatch.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import com.crashlytics.android.Crashlytics
import dagger.android.AndroidInjection
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_splash.*
import pl.oziem.whattowatch.R
import pl.oziem.whattowatch.main.MovieListActivity
import javax.inject.Inject

/** Created by Marcin Oziemski on 11/03/2018 WhatToWatch.
 */
class SplashActivity : AppCompatActivity(), SplashContract.View {

  companion object {
    private const val SHORT_DELAY = 500L
    private const val SCALE_ANIM_DELAY = SHORT_DELAY * 2
  }

  @Inject
  lateinit var presenter: SplashContract.Presenter
  private var scaleAnimation: Animation? = null
  private var fadeAnimation: Animation? = null
  private var wasHomeCalled: Boolean = false
  private var dataIsReady: Boolean = false

  private val animationListener = object : Animation.AnimationListener {
    override fun onAnimationStart(animation: Animation) {
      if (animation == scaleAnimation) {
        appImage.postDelayed({
          appImage.alpha = 1f
          appName.alpha = 1f
          appImage.startAnimation(fadeAnimation)
          appName.startAnimation(fadeAnimation)
        }, SHORT_DELAY)
      }
    }

    override fun onAnimationEnd(animation: Animation) {
      if (animation == fadeAnimation) {
        appImage.postDelayed({ startHomeActivity() }, SHORT_DELAY)
      }
    }

    override fun onAnimationRepeat(animation: Animation) {}
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)
    Fabric.with(this, Crashlytics())
    setContentView(R.layout.activity_splash)

    initLogoAnimation()
    presenter.fetchData(this)
  }

  private fun initLogoAnimation() {
    scaleAnimation = ScaleAnimation(0f, 1f, 0f, 1f,
      Animation.RELATIVE_TO_SELF, 0.5f,
      Animation.RELATIVE_TO_SELF, 0.5f
    ).apply {
      duration = SCALE_ANIM_DELAY
      setAnimationListener(animationListener)
    }

    circle.animation = scaleAnimation

    fadeAnimation = AlphaAnimation(0f, 1f).apply {
      duration = SHORT_DELAY
      isFillEnabled = true
      fillAfter = true
      setAnimationListener(animationListener)
    }
    appImage.alpha = 0f
    appName.alpha = 0f
  }

  override fun onResume() {
    super.onResume()
    scaleAnimation?.start()
  }

  override fun showError(message: String?) {
    errorMessageTextView.text = message ?: getString(R.string.server_error)
    errorMessageTextView.visibility = View.VISIBLE
  }

  override fun onDataFetched() {
    dataIsReady = true
    if (wasHomeCalled) {
      startHomeActivity()
    }
  }

  @SuppressLint("PrivateResource")
  private fun startHomeActivity() {
    wasHomeCalled = true
    if (!dataIsReady || isFinishing) return
    val options = ActivityOptionsCompat.makeCustomAnimation(this,
      android.support.v7.appcompat.R.anim.abc_fade_in,
      android.support.v7.appcompat.R.anim.abc_fade_out)
    runOnUiThread {
      startActivity(Intent(this, MovieListActivity::class.java), options.toBundle())
    }
    finish()
  }
}
