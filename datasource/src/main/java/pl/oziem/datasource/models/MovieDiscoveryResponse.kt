package pl.oziem.datasource.models

import com.google.gson.annotations.SerializedName

/**
 * Created by marcinoziem on 09/03/2018.
 */
data class MovieDiscoveryResponse(
  @SerializedName("page") val page: Int,
  @SerializedName("total_results") val totalResults: Int,
  @SerializedName("total_pages") val totalPages: Int,
  @SerializedName("results") val movies: List<Movie>?
)
