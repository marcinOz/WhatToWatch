package pl.oziem.datasource.models

import com.squareup.moshi.Json

/**
* Created by MarcinOz on 2018-03-07 WhatToWatch.
*/
enum class ContentStatus {
  @Json(name = "Rumored") RUMORED,
  @Json(name = "Planned") PLANNED,
  @Json(name = "In Production") IN_PRODUCTION,
  @Json(name = "Post Production") POST_PRODUCTION,
  @Json(name = "Released") RELEASED,
  @Json(name = "Canceled") CANCELED
}
