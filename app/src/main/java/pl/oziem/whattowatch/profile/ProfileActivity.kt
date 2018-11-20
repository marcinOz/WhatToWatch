package pl.oziem.whattowatch.profile

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_profile.*
import org.jetbrains.anko.startActivity
import pl.oziem.whattowatch.R
import pl.oziem.whattowatch.extensions.isUserSignedIn
import pl.oziem.whattowatch.signin.SignInActivity

class ProfileActivity : AppCompatActivity() {

  companion object {

    fun start(activity: Activity) =
      if (activity.isUserSignedIn()) {
        activity.startActivity<ProfileActivity>()
      } else {
        activity.startActivity<SignInActivity>()
      }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_profile)

    signOutButton.setOnClickListener {  }
  }
}
