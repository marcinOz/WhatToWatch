package pl.oziem.whattowatch.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.design.widget.Snackbar
import dagger.android.AndroidInjection
import io.reactivex.rxkotlin.subscribeBy

import pl.oziem.whattowatch.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_movie_list.*

import kotlinx.android.synthetic.main.movie_list.*
import pl.oziem.whattowatch.MovieDetailActivity
import pl.oziem.whattowatch.R
import pl.oziem.whattowatch.firebase.FirebaseRemoteConfigHelper
import javax.inject.Inject

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [MovieDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class MovieListActivity : AppCompatActivity(), MovieListContract.View {

  /**
   * Whether or not the activity is in two-pane mode, i.e. running on a tablet
   * device.
   */
  private var mTwoPane: Boolean = false

  @Inject lateinit var presenter: MovieListContract.Presenter
  @Inject lateinit var firebaseRemoteConfig: FirebaseRemoteConfigHelper

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_movie_list)

    setSupportActionBar(toolbar)
    toolbar.title = title

    fab.setOnClickListener { view ->
      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        .setAction("Action", null).show()
    }

    if (movie_detail_container != null) {
      // The detail container view will be present only in the
      // large-screen layouts (res/values-w900dp).
      // If this view is present, then the
      // activity should be in two-pane mode.
      mTwoPane = true
    }

    setupRecyclerView(movie_list)

    firebaseRemoteConfig.fetch(this).subscribeBy (
      onComplete = { presenter.getMovieById(2) },
      onError = { setText("server error") }
    )
  }

  private fun setupRecyclerView(recyclerView: RecyclerView) {
    recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane)
  }

  override fun setText(result: String) {
    text?.text = result
  }
}
