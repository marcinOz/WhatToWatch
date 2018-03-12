package pl.oziem.datasource.models.configuration

import com.google.gson.annotations.SerializedName

/** Created by marcinoziem on 11/03/2018 WhatToWatch.
*/
data class Configuration(
  @SerializedName("images") val imagesConfig: ImagesConfiguration = ImagesConfiguration(),
  @SerializedName("change_keys") val changeKeys: List<String> = listOf()
)
