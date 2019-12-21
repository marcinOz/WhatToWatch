package pl.oziem.whattowatch.movies

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_movie_list.*
import kotlinx.android.synthetic.main.movie_list.*
import pl.oziem.commons.observe
import pl.oziem.commons.withViewModel
import pl.oziem.datasource.models.*
import pl.oziem.datasource.models.movie.Movie
import pl.oziem.whattowatch.R
import pl.oziem.whattowatch.details.MovieDetailActivity
import pl.oziem.whattowatch.details.MovieDetailFragment
import javax.inject.Inject

class MovieListFragment : DaggerFragment() {

  companion object {
    private const val DETAILS_ACTIVITY_CODE = 11
  }

  private var twoPane: Boolean = false
  private var fragment: MovieDetailFragment? = null
  private lateinit var adapter: MovieListAdapter

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View = inflater.inflate(R.layout.fragment_movie_list, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    MovieDetailFragment.readState(savedInstanceState)?.apply { openDetailsActivity(this) }
    super.onViewCreated(view, null) //to not save twoPaneState unintentionally

    fab.setOnClickListener { view ->
      Snackbar.make(view, "TODO: Add Action", Snackbar.LENGTH_LONG)
        .setAction("Action", null).show()
    }
    twoPane = movie_detail_container != null
    setupRecyclerView(movie_list)
    initViewModel()
  }

  private fun initViewModel() {
    withViewModel<MovieListViewModel>(viewModelFactory) {
      observe(getLoadState(), ::updateView)
      observe(pagedListData) { adapter.submitList(it) }
    }
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

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState.apply { fragment?.saveState(this) })
  }

  private fun setupRecyclerView(recyclerView: RecyclerView) {
    recyclerView.layoutAnimation = AnimationUtils
      .loadLayoutAnimation(requireContext(), R.anim.layout_animation_fly_up)
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
        childFragmentManager
          .beginTransaction()
          .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
          .replace(R.id.movie_detail_container, it)
          .commit()
      }
  }

  @SuppressLint("RestrictedApi")
  private fun openDetailsActivity(movie: Movie, vararg views: View = emptyArray()) {
    val intent = Intent(requireContext(), MovieDetailActivity::class.java)
      .apply { putExtra(MovieDetailFragment.MOVIE_ARG, movie) }

    val pairs = views.map { view -> view to view.transitionName }
          .map { pair -> pair.toJavaPair() }.toTypedArray()

    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), *pairs)
    startActivityForResult(intent, DETAILS_ACTIVITY_CODE, options.toBundle())
  }

  private fun Pair<View, String>.toJavaPair() = androidx.core.util.Pair(first, second)
}
