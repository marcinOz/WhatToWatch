package pl.oziem.datasource.models

import com.google.gson.annotations.SerializedName

/**
 * Created by marcinoziem on 09/03/2018.
 */
data class Movie(@SerializedName("id") val id: String,
            @SerializedName("title") val title: String,
            @SerializedName("original_title") val originalTitle: String,
            @SerializedName("adult") val adult: Boolean,
            @SerializedName("genre_ids") val genreIds: List<Int>,
            @SerializedName("video") val video: Boolean,
            @SerializedName("vote_average") val voteAverage: Float,
            @SerializedName("vote_count") val voteCount: Int,
            @SerializedName("backdrop_path") val backdropPath: String?,
            @SerializedName("release_date") val releaseDate: String,
            @SerializedName("original_language") val originalLanguage: String,
            @SerializedName("overview") val overview: String,
            @SerializedName("poster_path") val posterPath: String?,
            @SerializedName("popularity") val popularity: Float)
