package pl.oziem.whattowatch.image_loader

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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
    override fun load(url: String?) = DrawableRequest(glideRequests.load(url))
    override fun loadBackdrop(url: String?) = DrawableRequest(glideRequests
      .load(url.makeItProperUrl(sharedPrefMediator.getBackdropSizes().firstOrNull()))
      .diskCacheStrategy(DiskCacheStrategy.DATA))

    override fun loadPoster(url: String?) = DrawableRequest(glideRequests
      .load(url.makeItProperUrl(sharedPrefMediator.getPosterSizes().getOrNull(1)))
      .diskCacheStrategy(DiskCacheStrategy.DATA))
  }

  inner class DrawableRequest(private val glideRequest: GlideRequest<Drawable>)
    : Request<Drawable>(glideRequest), ImageLoader.DrawableRequest {
    override fun fadeTransition() =
      DrawableRequest(glideRequest.transition(DrawableTransitionOptions.withCrossFade()))

    override fun listener(onSuccess: () -> Unit, onFailure: () -> Unit) =
      DrawableRequest(glideRequest.addListener(onSuccess, onFailure))
  }

  open class Request<T>(private val glideRequest: GlideRequest<T>) : ImageLoader.Request {
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

  private fun GlideRequest<Drawable>.addListener(onSuccess: () -> Unit,
                                                 onFailure: () -> Unit): GlideRequest<Drawable> {
    return listener(object : RequestListener<Drawable> {
      override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?,
                                isFirstResource: Boolean): Boolean {
        onFailure()
        return false
      }

      override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?,
                                   dataSource: DataSource?, isFirstResource: Boolean): Boolean {
        onSuccess()
        return false
      }
    })
  }
}
