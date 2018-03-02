package pl.oziem.datasource.models

import com.google.gson.annotations.SerializedName

/**
 * Created by MarcinOz on 2018-03-02.
 * Copyright (C) 2017 OKE Poland Sp. z o.o. All rights reserved.
 */
class TvShow {

  val id: Int = 0
  @SerializedName("poster_path") val posterPath: String? = null
  val popularity: Float = 0.toFloat()
  @SerializedName("backdrop_path") val backdropPath: String? = null
  @SerializedName("vote_average") val voteAverage: Float = 0.toFloat()
  val overview: String? = null
  @SerializedName("first_air_date") val firstAirDate: String? = null
  @SerializedName("origin_country") val originCountry: Array<String>? = null
  @SerializedName("original_language") val originalLanguage: String? = null
  @SerializedName("vote_count") val voteCount: Int = 0
  val name: String? = null
  @SerializedName("original_name") val originalName: String? = null
}
