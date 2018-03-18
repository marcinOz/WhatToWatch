package pl.oziem.datasource.models

import com.google.gson.annotations.SerializedName

/**
* Created by MarcinOz on 2018-03-07 WhatToWatch.
*/
data class SpokenLanguage(
  @SerializedName("iso_639_1") val iso: String,
  @SerializedName("name") val name: String
)
