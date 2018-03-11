package pl.oziem.datasource.models

import com.google.gson.annotations.SerializedName

/**
* Created by MarcinOz on 2018-03-07 WhatToWatch.
*/
data class ProductionCompanies(
  @SerializedName("id") val id: Int,
  @SerializedName("name") val name: String
)
