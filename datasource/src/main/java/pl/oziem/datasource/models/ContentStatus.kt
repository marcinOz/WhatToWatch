package pl.oziem.datasource.models

import com.google.gson.annotations.SerializedName

/**
* Created by MarcinOz on 2018-03-07 WhatToWatch.
*/
enum class ContentStatus {
  @SerializedName("Rumored") RUMORED,
  @SerializedName("Planned") PLANNED,
  @SerializedName("In Production") IN_PRODUCTION,
  @SerializedName("Post Production") POST_PRODUCTION,
  @SerializedName("Released") RELEASED,
  @SerializedName("Canceled") CANCELED
}
