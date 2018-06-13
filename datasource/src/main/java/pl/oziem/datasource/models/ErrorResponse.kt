package pl.oziem.datasource.models

import com.squareup.moshi.Json

/**
* Created by MarcinOz on 2018-03-06 WhatToWatch.
*/
data class ErrorResponse(
  @Json(name = "status_code") val statusCode: Int? = null,
  @Json(name = "status_message") val statusMessage: String? = null
)
