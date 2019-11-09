package pl.oziem.whattowatch.main

import android.arch.paging.PagedListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.movie_list_content.view.*
import pl.oziem.datasource.models.movie.Movie
import pl.oziem.whattowatch.R
import pl.oziem.whattowatch.WTWApplication

/**
 * Created by marcinoziem
 * on 26/06/2018 WhatToWatch.
 */
class MovieListAdapter(private val onItemClick: (Movie, Array<View>) -> Unit)
  : PagedListAdapter<Movie, RecyclerView.ViewHolder>(Movie.DIFF_CALLBACK) {

  private val mOnClickListener: View.OnClickListener
  private var shouldShowLoading: Boolean = true

  init {
    mOnClickListener = View.OnClickListener { v ->
      val item = v.tag as Movie
      onItemClick(item, arrayOf(v.title, v.poster))
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
    if (viewType == TYPE_ITEM) {
      ViewHolder(
        LayoutInflater.from(parent.context)
          .inflate(R.layout.movie_list_content, parent, false)
      )
    } else {
      LoadingViewHolder(
        LayoutInflater.from(parent.context)
          .inflate(R.layout.movie_list_loading_item, parent, false)
      )
    }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    if (holder is ViewHolder) {
      val item = getItem(position)
      WTWApplication.getImageLoader(holder.itemView)
        .loadPoster(item?.posterPath)
        .fadeTransition()
        .fitCenter()
        .error(R.drawable.vod_default_poster)
        .into(holder.poster)
      holder.title.text = item?.title

      with(holder.itemView) {
        tag = item
        setOnClickListener(mOnClickListener)
      }
    }
  }

  fun shouldShowLoading(should: Boolean) {
    if (should) {
      notifyItemInserted(itemCount)
    }
    shouldShowLoading = should
  }

  override fun getItemViewType(position: Int): Int =
    if (shouldShowLoading && isLastItemPosition(position)) TYPE_PROGRESS
    else TYPE_ITEM

  fun isLastItemPosition(position: Int): Boolean = position == (itemCount - 1)

  inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
    val poster: ImageView = mView.poster
    val title: TextView = mView.title
  }

  inner class LoadingViewHolder(mView: View) : RecyclerView.ViewHolder(mView)

  companion object {
    private const val TYPE_PROGRESS = 0
    private const val TYPE_ITEM = 1
  }
}
