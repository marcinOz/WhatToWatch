package pl.oziem.datasource.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/** Created by marcinoziem on 17/03/2018 WhatToWatch.
 */
@Parcelize
data class Language(
  @SerializedName("iso_639_1") val iso: String,
  @SerializedName("name") val name: String,
  @SerializedName("english_name") val englishName: String) : Parcelable
