package pl.oziem.whattowatch.main

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
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
import pl.oziem.datasource.models.*
import pl.oziem.datasource.models.movie.Movie
import pl.oziem.whattowatch.R
import pl.oziem.whattowatch.details.MovieDetailActivity
import pl.oziem.whattowatch.details.MovieDetailFragment
import pl.oziem.whattowatch.extensions.observe
import pl.oziem.whattowatch.extensions.withViewModel
import javax.inject.Inject

class MovieListActivity : AppCompatActivity(), MovieListContract.View {

  companion object {
    private const val DETAILS_ACTIVITY_CODE = 11
  }

  private var twoPane: Boolean = false
  private var content: MutableList<Movie> = mutableListOf()
  private var fragment: MovieDetailFragment? = null

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  override fun onCreate(savedInstanceState: Bundle?) {
    MovieDetailFragment.readState(savedInstanceState)?.apply { openDetailsActivity(this) }
    AndroidInjection.inject(this)
    super.onCreate(null) //to not save twoPaneState unintentionally
    setContentView(R.layout.activity_movie_list)

    setSupportActionBar(toolbar)
    toolbar.title = title

    fab.setOnClickListener { view ->
      Snackbar.make(view, "TODO: Add Action", Snackbar.LENGTH_LONG)
        .setAction("Action", null).show()
    }

    initData()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode != DETAILS_ACTIVITY_CODE) return
    when {
      resultCode == Activity.RESULT_OK && data?.extras != null -> data.extras.apply {
        showDetailsInSecondPane(getParcelable(MovieDetailFragment.MOVIE_ARG))
      }
    }
  }

  private fun initData() {
    twoPane = movie_detail_container != null

    withViewModel<MovieListViewModel>(viewModelFactory) {
      observe(movieDiscover, ::updateView)
      fetchMovieDiscover()
    }
    setupRecyclerView(movie_list)
  }

  override fun onSaveInstanceState(outState: Bundle?) {
    super.onSaveInstanceState(outState.apply { fragment?.saveState(this) })
  }

  private fun setupRecyclerView(recyclerView: RecyclerView) {
    recyclerView.layoutAnimation = AnimationUtils
      .loadLayoutAnimation(this, R.anim.layout_animation_fly_up)
    recyclerView.adapter = MovieListAdapter(content, this::goToDetails)
  }

  private fun updateView(resourceState: ResourceState<List<Movie>>) = when(resourceState) {
    is LoadingState -> showLoading(true)
    is PopulatedState -> populate(resourceState.data)
    is EmptyState -> showEmptyMessage()
    is ErrorState -> showError(resourceState.message)
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
    if (twoPane) showDetailsInSecondPane(movie)
    else openDetailsActivity(movie, *views)

  private fun showDetailsInSecondPane(movie: Movie) {
    fragment = MovieDetailFragment().apply {
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

  @SuppressLint("RestrictedApi")
  private fun openDetailsActivity(movie: Movie, vararg views: View = emptyArray()) {
    val intent = Intent(this, MovieDetailActivity::class.java)
      .apply { putExtra(MovieDetailFragment.MOVIE_ARG, movie) }

    val pairs =
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        views.map { view -> view to view.transitionName }
          .map { pair -> pair.toJavaPair() }.toTypedArray()
      else emptyArray()


    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *pairs)
    startActivityForResult(intent, DETAILS_ACTIVITY_CODE, options.toBundle())
  }

  private fun Pair<View, String>.toJavaPair() = android.support.v4.util.Pair(first, second)
}
