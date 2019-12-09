package pl.oziem.whattowatch.profile

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import pl.oziem.commons.observe
import pl.oziem.commons.withViewModel
import pl.oziem.whattowatch.R
import pl.oziem.whattowatch.image_loader.ImageLoader
import javax.inject.Inject


class ProfileFragment : DaggerFragment() {

  companion object {
    private const val CIRCLE_MAX_DEGREE = 359f
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

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View = inflater.inflate(R.layout.fragment_profile, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    signOutButton.setOnClickListener { viewModel.signOut() }
    viewModel.downloadUser()
    initDotAnimation()
  }

  private fun onStateChange(state: ProfileState) = when(state) {
    is ProfileState.Idle -> {
      state.image?.let {
        imageLoader.with(requireContext())
          .load(it.toString())
          .into(userImage)
      }
      userName.text = state.name
    }
    ProfileState.SigningOut -> goToSignInVIew()
  }

  private fun goToSignInVIew() {
    // TODO: need implementation
  }

  private fun initDotAnimation() {
    dot1.animateOrbit(4000)
    dot2.animateOrbit(6000)
    dot3.animateOrbit(7000, true)
  }

  private fun ImageView.animateOrbit(orbitDuration: Long, reverse: Boolean = false) {
    val anim =
      if (reverse) ValueAnimator.ofFloat(CIRCLE_MAX_DEGREE, 0f)
      else ValueAnimator.ofFloat(0f, CIRCLE_MAX_DEGREE)
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
