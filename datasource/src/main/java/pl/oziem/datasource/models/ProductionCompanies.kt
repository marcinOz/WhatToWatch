package pl.oziem.datasource.models

import com.squareup.moshi.Json

/**
* Created by MarcinOz on 2018-03-07 WhatToWatch.
*/
data class ProductionCompanies(
  @Json(name = "id") val id: Int,
  @Json(name = "name") val name: String
)
