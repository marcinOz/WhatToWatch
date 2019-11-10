package pl.oziem.whattowatch.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.movie_detail.view.*
import pl.oziem.datasource.models.movie.Movie
import pl.oziem.whattowatch.R
import pl.oziem.whattowatch.WTWApplication

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a [MovieListActivity]
 * in two-pane mode (on tablets) or a [MovieDetailActivity]
 * on handsets.
 */
class MovieDetailFragment : Fragment() {

  companion object {
    const val MOVIE_ARG = "item_id"
    fun readState(bundle: Bundle?): Movie? = bundle?.getParcelable(MOVIE_ARG)
  }

  private var movie: Movie? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    movie = readState(arguments)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    val rootView = inflater.inflate(R.layout.movie_detail, container, false)

    val movieDetailActivity: MovieDetailActivity? = if (activity is MovieDetailActivity)
      (activity as MovieDetailActivity) else null
    movieDetailActivity?.setToolbarData(movie?.title, movie?.backdropPath)

    movie?.let {
      rootView.movie_detail.text = it.overview
      WTWApplication.getImageLoader(rootView)
        .loadPoster(it.posterPath)
        .listener(
          { movieDetailActivity?.supportStartPostponedEnterTransition() },
          { movieDetailActivity?.supportStartPostponedEnterTransition() })
        .into(rootView.poster)
      rootView.title.text = it.title

      activity?.apply {
        val language = WTWApplication.getSharedPrefMediator(this)
          .getLanguageByIso(it.originalLanguage)
        rootView.subtitle.text = getString(R.string.subtitle, it.releaseDate, language)
      }
    }
    return rootView
  }

  fun saveState(bundle: Bundle?) = bundle?.apply {
    movie?.let { putParcelable(MOVIE_ARG, it) }
  }
}
