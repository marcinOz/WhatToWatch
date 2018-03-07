package pl.oziem.datasource.models

import com.google.gson.annotations.SerializedName

/**
 * Created by MarcinOz on 2018-03-07.
 * Copyright (C) 2017 OKE Poland Sp. z o.o. All rights reserved.
 */
data class Genre(
  @SerializedName("id") val id: Int,
  @SerializedName("name") val name: String
)
