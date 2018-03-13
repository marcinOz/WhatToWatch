package pl.oziem.whattowatch.image_loader

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import pl.oziem.whattowatch.sharedpref.SharedPreferenceMediator

/** Created by marcinoziem on 10/03/2018 WhatToWatch.
 */
class GlideImageLoader(val sharedPrefMediator: SharedPreferenceMediator) : ImageLoader {

  override fun with(view: View) = Requests(GlideApp.with(view))
  override fun with(context: Context) = Requests(GlideApp.with(context))
  override fun with(activity: Activity) = Requests(GlideApp.with(activity))

  private fun String?.makeItProperUrl(size: String?): String? = this?.let {
    sharedPrefMediator.getImageSecureBaseUrl() + (size ?: "") + it
  }

  inner class Requests(private val glideRequests: GlideRequests) : ImageLoader.Requests {
    override fun load(url: String?) = Request(glideRequests.load(url))
    override fun loadPoster(url: String?) = Request(glideRequests.load(
      url.makeItProperUrl(sharedPrefMediator.getPosterSizes().getOrNull(1)))
      .transition(DrawableTransitionOptions.withCrossFade())
      .fitCenter())
    override fun loadBackdrop(url: String?) = Request(glideRequests.load(
      url.makeItProperUrl(sharedPrefMediator.getBackdropSizes().firstOrNull()))
      .transition(DrawableTransitionOptions.withCrossFade()))
  }

  class Request<T>(private val glideRequest: GlideRequest<T>) : ImageLoader.Request {
    override fun placeholder(drawable: Drawable?) = Request(glideRequest.placeholder(drawable))
    override fun placeholder(@DrawableRes res: Int) = Request(glideRequest.placeholder(res))
    override fun error(drawable: Drawable?) = Request(glideRequest.error(drawable))
    override fun error(@DrawableRes res: Int) = Request(glideRequest.error(res))
    override fun fallback(drawable: Drawable?) = Request(glideRequest.fallback(drawable))
    override fun fallback(@DrawableRes res: Int) = Request(glideRequest.fallback(res))

    override fun fitCenter() = Request(glideRequest.fitCenter())

    override fun into(imageView: ImageView) {
      glideRequest.into(imageView)
    }
  }
}
