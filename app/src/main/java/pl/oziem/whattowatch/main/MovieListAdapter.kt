package pl.oziem.whattowatch.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.movie_list_content.view.*
import pl.oziem.datasource.models.Movie
import pl.oziem.whattowatch.R
import pl.oziem.whattowatch.WTWApplication

class MovieListAdapter(private val mValues: List<Movie>,
                       private val onItemClick: (Movie, Array<View>) -> Unit) :
  RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

  private val mOnClickListener: View.OnClickListener

  init {
    mOnClickListener = View.OnClickListener { v ->
      val item = v.tag as Movie
      onItemClick(item, arrayOf(v.title, v.poster))
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.movie_list_content, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = mValues[position]
    WTWApplication.getImageLoader(holder.itemView)
      .loadPosterWithTransition(item.posterPath)
      .fitCenter()
      .error(R.drawable.vod_default_poster)
      .into(holder.poster)
    holder.title.text = item.title

    with(holder.itemView) {
      tag = item
      setOnClickListener(mOnClickListener)
    }
  }

  override fun getItemCount(): Int {
    return mValues.size
  }

  inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
    val poster: ImageView = mView.poster
    val title: TextView = mView.title
  }
}
