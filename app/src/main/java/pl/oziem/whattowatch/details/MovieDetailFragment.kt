package pl.oziem.whattowatch.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.movie_detail.*
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

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? = inflater.inflate(R.layout.movie_detail, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    movie?.initView()
    (activity as? MovieDetailActivity)?.setToolbarData(movie?.title, movie?.backdropPath)
  }

  private fun Movie.initView() {
    movieDescription.text = overview
    WTWApplication.getImageLoader(posterImage)
      .loadPoster(posterPath)
      .listener(
        { requireActivity().supportStartPostponedEnterTransition() },
        { requireActivity().supportStartPostponedEnterTransition() })
      .into(posterImage)
    titleText.text = title

    val language = WTWApplication.getSharedPrefMediator(requireContext())
      .getLanguageByIso(originalLanguage)
    subtitleText.text = getString(R.string.subtitle, releaseDate, language)
  }

  fun saveState(bundle: Bundle?) = bundle?.apply {
    movie?.let { putParcelable(MOVIE_ARG, it) }
  }
}
