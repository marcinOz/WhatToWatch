package pl.oziem.datasource.analytics

import android.app.Activity
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Created by marcinoziem on 09/03/2018.
 */
class FirebaseAnalyticsMediatorImp : AnalyticsMediator {
  private var firebaseAnalytics: FirebaseAnalytics? = null

  override fun onActivityCreate(activity: Activity) {
    firebaseAnalytics = FirebaseAnalytics.getInstance(activity)
  }
}
