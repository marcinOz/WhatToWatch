package pl.oziem.whattowatch

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.movie_detail.view.*
import pl.oziem.datasource.models.Movie

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a [MovieListActivity]
 * in two-pane mode (on tablets) or a [MovieDetailActivity]
 * on handsets.
 */
class MovieDetailFragment : Fragment() {

  companion object {
    const val ARG_ITEM_ID = "item_id"
  }

  private var mItem: Movie? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    arguments?.let {
      if (it.containsKey(ARG_ITEM_ID)) {
        mItem = it.getParcelable(ARG_ITEM_ID)
        activity?.toolbar_layout?.title = mItem?.title
      }
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    val rootView = inflater.inflate(R.layout.movie_detail, container, false)

    // Show the dummy content as text in a TextView.
    mItem?.let {
      rootView.movie_detail.text = it.overview
    }

    if (activity is MovieDetailActivity) {
      (activity as MovieDetailActivity).setToolbarImage(mItem?.backdropPath)
    }

    return rootView
  }
}
