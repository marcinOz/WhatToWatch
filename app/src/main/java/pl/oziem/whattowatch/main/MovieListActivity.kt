package pl.oziem.whattowatch.main

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.crashlytics.android.Crashlytics
import com.google.android.gms.common.GoogleApiAvailability
import dagger.android.AndroidInjection
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_movie_list.*
import kotlinx.android.synthetic.main.movie_list.*
import pl.oziem.datasource.models.Movie
import pl.oziem.whattowatch.R
import javax.inject.Inject


class MovieListActivity : AppCompatActivity(), MovieListContract.View {

  private var mTwoPane: Boolean = false
  private var content: MutableList<Movie> = mutableListOf()

  @Inject
  lateinit var presenter: MovieListContract.Presenter

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)
    Fabric.with(this, Crashlytics())
    setContentView(R.layout.activity_movie_list)

    setSupportActionBar(toolbar)
    toolbar.title = title

    fab.setOnClickListener { view ->
      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        .setAction("Action", null).show()
    }

    if (movie_detail_container != null) {
      mTwoPane = true
    }

    if (savedInstanceState != null) {
      content.addAll(presenter.readSavedInstanceState(savedInstanceState))
    }

    setupRecyclerView(movie_list)

    if (content.isNotEmpty()) return
    GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this)
      .addOnCompleteListener { task ->
        if (task.isSuccessful) presenter.initDownloadData(this)
      }
  }

  override fun onSaveInstanceState(outState: Bundle?) {
    super.onSaveInstanceState(presenter.saveInstanceState(outState))
  }

  private fun setupRecyclerView(recyclerView: RecyclerView) {
    recyclerView.adapter = MovieListAdapter(this, content, mTwoPane)
  }

  override fun showLoading(show: Boolean) {
    progressBar.visibility = if (show) View.VISIBLE else View.GONE
  }

  override fun showError(message: String) {
    messageTextView.text = message
    messageTextView.visibility = View.VISIBLE
    showLoading(false)
  }

  override fun populate(movies: List<Movie>) {
    content.addAll(movies)
    movie_list.adapter.notifyDataSetChanged()
    showLoading(false)
  }

  override fun showEmptyMessage() {
    showError(getString(R.string.no_content))
  }
}
