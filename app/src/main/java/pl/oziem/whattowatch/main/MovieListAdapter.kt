package pl.oziem.whattowatch.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.movie_list_content.view.*
import pl.oziem.datasource.models.Movie
import pl.oziem.whattowatch.MovieDetailActivity
import pl.oziem.whattowatch.MovieDetailFragment
import pl.oziem.whattowatch.R
import pl.oziem.whattowatch.WTWApplication
import pl.oziem.whattowatch.dummy.DummyContent

class MovieListAdapter(private val mParentActivity: MovieListActivity,
                       private val mValues: List<Movie>,
                       private val mTwoPane: Boolean) :
  RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

  private val mOnClickListener: View.OnClickListener

  init {
    mOnClickListener = View.OnClickListener { v ->
      val item = v.tag as DummyContent.DummyItem
      if (mTwoPane) {
        val fragment = MovieDetailFragment().apply {
          arguments = Bundle().apply {
            putString(MovieDetailFragment.ARG_ITEM_ID, item.id)
          }
        }
        mParentActivity.supportFragmentManager
          .beginTransaction()
          .replace(R.id.movie_detail_container, fragment)
          .commit()
      } else {
        val intent = Intent(v.context, MovieDetailActivity::class.java).apply {
          putExtra(MovieDetailFragment.ARG_ITEM_ID, item.id)
        }
        v.context.startActivity(intent)
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.movie_list_content, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = mValues[position]
    item.posterPath?.apply {
      WTWApplication.getImageLoader(holder.itemView).load(this).into(holder.poster)
    }
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
