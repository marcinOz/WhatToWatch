package pl.oziem.datasource.models.configuration

import com.squareup.moshi.Json

/** Created by marcinoziem on 11/03/2018 WhatToWatch.
*/
data class Configuration(
  @Json(name = "images") val imagesConfig: ImagesConfiguration = ImagesConfiguration(),
  @Json(name = "change_keys") val changeKeys: List<String> = listOf()
)
