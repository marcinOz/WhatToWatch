package pl.oziem.datasource.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/** Created by marcinoziem on 17/03/2018 WhatToWatch.
 */
@Parcelize
data class Language(
  @Json(name = "iso_639_1") val iso: String,
  @Json(name = "name") val name: String,
  @Json(name = "english_name") val englishName: String) : Parcelable
