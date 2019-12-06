package pl.oziem.whattowatch.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_movie_list.*
import kotlinx.android.synthetic.main.movie_list.*
import pl.oziem.commons.observe
import pl.oziem.commons.withViewModel
import pl.oziem.datasource.models.*
import pl.oziem.datasource.models.movie.Movie
import pl.oziem.whattowatch.R
import pl.oziem.whattowatch.details.MovieDetailActivity
import pl.oziem.whattowatch.details.MovieDetailFragment
import pl.oziem.whattowatch.profile.ProfileActivity
import javax.inject.Inject

class MovieListActivity : AppCompatActivity() {

  companion object {
    private const val DETAILS_ACTIVITY_CODE = 11
  }

  private var twoPane: Boolean = false
  private var fragment: MovieDetailFragment? = null
  private lateinit var adapter: MovieListAdapter

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

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.movie_list, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
    R.id.profile -> {
      ProfileActivity.start(this)
      true
    }
    else -> super.onOptionsItemSelected(item)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode != DETAILS_ACTIVITY_CODE) return
    if (resultCode == Activity.RESULT_OK && data?.extras != null) {
      data.extras?.getParcelable<Movie>(MovieDetailFragment.MOVIE_ARG)?.let {
        showDetailsInSecondPane(it)
      }
    }
  }

  private fun initData() {
    twoPane = movie_detail_container != null

    withViewModel<MovieListViewModel>(viewModelFactory) {
      observe(getLoadState(), ::updateView)
      observe(pagedListData) { adapter.submitList(it) }
    }
    setupRecyclerView(movie_list)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState.apply { fragment?.saveState(this) })
  }

  private fun setupRecyclerView(recyclerView: RecyclerView) {
    recyclerView.layoutAnimation = AnimationUtils
      .loadLayoutAnimation(this, R.anim.layout_animation_fly_up)
    adapter = MovieListAdapter(this::goToDetails)

    (recyclerView.layoutManager as GridLayoutManager).apply {
      spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int =
          if (adapter.isLastItemPosition(position)) this@apply.spanCount
          else 1
      }
    }
    recyclerView.adapter = adapter
  }

  private fun updateView(resourceState: ResourceState) = when (resourceState) {
    is LoadingState -> showLoading(true)
    is PopulatedState -> showLoading(false)
    is EmptyState -> showEmptyMessage()
    is ErrorState -> showError(resourceState.message)
  }

  private fun showLoading(show: Boolean) {
    if (!show && progressBar.visibility == View.VISIBLE) progressBar.visibility = View.GONE
    adapter.shouldShowLoading(show)
  }

  private fun showError(message: String?) {
    messageTextView.text = message ?: getString(R.string.server_error)
    messageTextView.visibility = View.VISIBLE
    showLoading(false)
  }

  private fun showEmptyMessage() {
    showError(getString(R.string.no_content))
  }

  private fun goToDetails(movie: Movie, vararg views: View) =
    if (twoPane) showDetailsInSecondPane(movie)
    else openDetailsActivity(movie, *views)

  private fun showDetailsInSecondPane(movie: Movie) {
    fragment = MovieDetailFragment()
      .apply {
        arguments = Bundle().apply {
          putParcelable(MovieDetailFragment.MOVIE_ARG, movie)
        }
      }.also {
        supportFragmentManager
          .beginTransaction()
          .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
          .replace(R.id.movie_detail_container, it)
          .commit()
      }
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

  private fun Pair<View, String>.toJavaPair() = androidx.core.util.Pair(first, second)
}
