package pl.oziem.datasource.models.movie

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
* Created by marcinoziem on 09/03/2018 WhatToWatch.
*/

@Parcelize
data class Movie(@SerializedName("id") val id: String = "",
            @SerializedName("title") val title: String = "",
            @SerializedName("original_title") val originalTitle: String = "",
            @SerializedName("adult") val adult: Boolean = false,
            @SerializedName("genre_ids") val genreIds: List<Int> = listOf(),
            @SerializedName("video") val video: Boolean = false,
            @SerializedName("vote_average") val voteAverage: Float = 0f,
            @SerializedName("vote_count") val voteCount: Int = 0,
            @SerializedName("backdrop_path") val backdropPath: String? = null,
            @SerializedName("release_date") val releaseDate: String = "",
            @SerializedName("original_language") val originalLanguage: String = "",
            @SerializedName("overview") val overview: String = "",
            @SerializedName("poster_path") val posterPath: String? = null,
            @SerializedName("popularity") val popularity: Float = 0f
) : Parcelable
