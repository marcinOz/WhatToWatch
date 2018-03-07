package pl.oziem.datasource.models

import com.google.gson.annotations.SerializedName

/**
 * Created by MarcinOz on 2018-03-07.
 * Copyright (C) 2017 OKE Poland Sp. z o.o. All rights reserved.
 */
data class SpokenLenguage(
  @SerializedName("iso_639_1") val iso: String,
  @SerializedName("name") val name: String
)