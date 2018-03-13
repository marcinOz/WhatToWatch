package pl.oziem.whattowatch.image_loader

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.view.View
import android.widget.ImageView

/**
* Created by marcinoziem on 10/03/2018 WhatToWatch.
*/
interface ImageLoader {
  fun with(context: Context): Requests
  fun with(view: View): Requests
  fun with(activity: Activity): Requests

  interface Requests {
    fun load(url: String?): Request
    fun loadPoster(url: String?): Request
    fun loadBackdrop(url: String?): Request
  }

  interface Request {
    fun placeholder(drawable: Drawable?): Request
    fun placeholder(@DrawableRes res: Int): Request
    fun error(@DrawableRes res: Int): Request
    fun error(drawable: Drawable?): Request
    fun fallback(@DrawableRes res: Int): Request
    fun fallback(drawable: Drawable?): Request

    fun fitCenter(): Request

    fun into(imageView: ImageView)
  }
}
