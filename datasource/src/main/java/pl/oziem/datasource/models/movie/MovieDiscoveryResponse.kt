package pl.oziem.datasource.models.movie

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
* Created by marcinoziem on 09/03/2018 WhatToWatch.
*/
@Parcelize
data class MovieDiscoveryResponse(
  @Json(name = "page") val page: Int = 0,
  @Json(name = "total_results") val totalResults: Int = 0,
  @Json(name = "total_pages") val totalPages: Int = 0,
  @Json(name = "results") val movies: List<Movie>? = null
) : Parcelable
