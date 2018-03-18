package pl.oziem.datasource.models.movie

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
* Created by marcinoziem on 09/03/2018 WhatToWatch.
*/
@Parcelize
data class MovieDiscoveryResponse(
  @SerializedName("page") val page: Int = 0,
  @SerializedName("total_results") val totalResults: Int = 0,
  @SerializedName("total_pages") val totalPages: Int = 0,
  @SerializedName("results") val movies: List<Movie>? = null
) : Parcelable
