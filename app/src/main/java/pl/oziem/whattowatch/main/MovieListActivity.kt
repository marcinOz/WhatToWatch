package pl.oziem.whattowatch.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AnimationUtils
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_movie_list.*
import kotlinx.android.synthetic.main.movie_list.*
import pl.oziem.datasource.models.Movie
import pl.oziem.whattowatch.R
import pl.oziem.whattowatch.details.MovieDetailActivity
import pl.oziem.whattowatch.details.MovieDetailFragment
import javax.inject.Inject

class MovieListActivity : AppCompatActivity(), MovieListContract.View {

  private var mTwoPane: Boolean = false
  private var content: MutableList<Movie> = mutableListOf()

  @Inject
  lateinit var presenter: MovieListContract.Presenter

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_movie_list)

    setSupportActionBar(toolbar)
    toolbar.title = title

    fab.setOnClickListener { view ->
      Snackbar.make(view, "TODO: Add Action", Snackbar.LENGTH_LONG)
        .setAction("Action", null).show()
    }

    if (movie_detail_container != null) {
      mTwoPane = true
    }

    savedInstanceState?.run {
      content.addAll(presenter.readSavedInstanceState(this))
    }

    setupRecyclerView(movie_list)

    if (content.isNotEmpty()) return
    presenter.getMovieDiscover()
  }

  override fun onSaveInstanceState(outState: Bundle?) {
    super.onSaveInstanceState(presenter.saveInstanceState(outState))
  }

  private fun setupRecyclerView(recyclerView: RecyclerView) {
    recyclerView.layoutAnimation = AnimationUtils
      .loadLayoutAnimation(this, R.anim.layout_animation_fly_up)
    recyclerView.adapter = MovieListAdapter(content, this::goToDetails)
  }

  override fun showLoading(show: Boolean) {
    progressBar.visibility = if (show) View.VISIBLE else View.GONE
  }

  override fun showError(message: String?) {
    messageTextView.text = message ?: getString(R.string.server_error)
    messageTextView.visibility = View.VISIBLE
    showLoading(false)
  }

  override fun populate(movies: List<Movie>) {
    content.addAll(movies)
    movie_list.adapter.notifyDataSetChanged()
    movie_list.scheduleLayoutAnimation()
    showLoading(false)
  }

  override fun showEmptyMessage() {
    showError(getString(R.string.no_content))
  }

  private fun goToDetails(movie: Movie, vararg views: View) =
    if (mTwoPane) showDetailsInSecondPane(movie)
    else openDetailsActivity(movie, *views)

  private fun showDetailsInSecondPane(movie: Movie) {
    val fragment = MovieDetailFragment().apply {
      arguments = Bundle().apply {
        putParcelable(MovieDetailFragment.MOVIE_ARG, movie)
      }
    }
    supportFragmentManager
      .beginTransaction()
      .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
      .replace(R.id.movie_detail_container, fragment)
      .commit()
  }

  private fun openDetailsActivity(movie: Movie, vararg views: View) {
    val intent = Intent(this, MovieDetailActivity::class.java)
      .apply { putExtra(MovieDetailFragment.MOVIE_ARG, movie) }

    val pairs =
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        views.map { view -> view to view.transitionName }
          .map { pair -> pair.toJavaPair() }.toTypedArray()
      else emptyArray()


    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *pairs)
    startActivity(intent, options.toBundle())
  }

  private fun Pair<View, String>.toJavaPair() = android.support.v4.util.Pair(first, second)
}
