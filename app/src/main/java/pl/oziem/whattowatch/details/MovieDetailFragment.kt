package pl.oziem.whattowatch.details

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.movie_detail.view.*
import pl.oziem.datasource.models.Movie
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
  }

  private var mItem: Movie? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    arguments?.let {
      if (it.containsKey(MOVIE_ARG)) {
        mItem = it.getParcelable(MOVIE_ARG)
        activity?.toolbar_layout?.title = mItem?.title
      }
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    val rootView = inflater.inflate(R.layout.movie_detail, container, false)

    val activity: MovieDetailActivity? = if (activity is MovieDetailActivity)
      (activity as MovieDetailActivity) else null
    activity?.setToolbarImage(mItem?.backdropPath)

    // Show the dummy content as text in a TextView.
    mItem?.let {
      rootView.movie_detail.text = it.overview
      WTWApplication.getImageLoader(rootView)
        .loadPosterWithListener(it.posterPath,
          { activity?.supportStartPostponedEnterTransition() },
          { activity?.supportStartPostponedEnterTransition() })
        .into(rootView.poster)
      rootView.title.text = it.title
      rootView.subtitle.text = "Release Date: ${it.releaseDate} " +
        "Original Language: ${it.originalLanguage.toUpperCase()}"
    }
    return rootView
  }
}
