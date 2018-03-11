package pl.oziem.datasource.models

import com.google.gson.annotations.SerializedName

/**
* Created by MarcinOz on 2018-03-06 WhatToWatch.
*/
data class ErrorResponse(
  @SerializedName("status_code") val statusCode: Int? = null,
  @SerializedName("status_message") val statusMessage: String? = null
)
