package pl.oziem.whattowatch.profile

import android.animation.ValueAnimator
import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_profile.*
import org.jetbrains.anko.startActivity
import pl.oziem.commons.observe
import pl.oziem.commons.withViewModel
import pl.oziem.whattowatch.R
import pl.oziem.whattowatch.extensions.isUserSignedIn
import pl.oziem.whattowatch.image_loader.ImageLoader
import pl.oziem.whattowatch.signin.SignInActivity
import javax.inject.Inject


class ProfileActivity : AppCompatActivity() {

  companion object {
    fun start(activity: Activity) =
      if (activity.isUserSignedIn()) {
        activity.startActivity<ProfileActivity>()
      } else {
        activity.startActivity<SignInActivity>()
      }
  }

  @Inject
  lateinit var imageLoader: ImageLoader

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  private val viewModel by lazy {
    withViewModel<ProfileViewModel>(viewModelFactory) {
      observe(state, ::onStateChange)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_profile)

    backArrow.setOnClickListener { onBackPressed() }
    signOutButton.setOnClickListener { viewModel.signOut() }
    viewModel.downloadUser()
    initDotAnimation()
  }

  private fun onStateChange(state: ProfileState) = when(state) {
    is ProfileState.Idle -> {
      state.image?.let {
        imageLoader.with(this)
          .load(it.toString())
          .into(userImage)
      }
      userName.text = state.name
    }
    ProfileState.SigningOut -> {
      startActivity<SignInActivity>()
      finish()
    }
  }

  private fun initDotAnimation() {
    dot1.animateOrbit(4000)
    dot2.animateOrbit(6000)
    dot3.animateOrbit(7000, true)
  }

  private fun ImageView.animateOrbit(orbitDuration: Long, reverse: Boolean = false) {
    val anim =
      if (reverse) ValueAnimator.ofFloat(359f, 0f)
      else ValueAnimator.ofFloat(0f, 359f)
    anim.addUpdateListener { valueAnimator ->
      val value = valueAnimator.animatedValue as Float
      val layoutParams = this.layoutParams as ConstraintLayout.LayoutParams
      layoutParams.circleAngle = value
      this.layoutParams = layoutParams
    }
    anim.duration = orbitDuration
    anim.interpolator = LinearInterpolator()
    anim.repeatMode = ValueAnimator.RESTART
    anim.repeatCount = ValueAnimator.INFINITE

    anim.start()
  }
}
