package pl.oziem.datasource.models

import com.google.gson.annotations.SerializedName

/**
 * Created by MarcinOz on 2018-03-02.
 * Copyright (C) 2017 OKE Poland Sp. z o.o. All rights reserved.
 */
data class MovieDetails(@SerializedName("id") val id: String,
                        @SerializedName("title") val title: String,
                        @SerializedName("original_title") val originalTitle: String,
                        @SerializedName("adult") val adult: Boolean,
                        @SerializedName("budget") val budget: Int,
                        @SerializedName("genres") val genres: List<Genre>,
                        @SerializedName("imdb_id") val imdbId: String?,
                        @SerializedName("video") val video: Boolean,
                        @SerializedName("vote_average") val voteAverage: Float,
                        @SerializedName("vote_count") val voteCount: Int,
                        @SerializedName("backdrop_path") val backdropPath: String?,
                        @SerializedName("runtime") val runtime: Int?,
                        @SerializedName("release_date") val releaseDate: String,
                        @SerializedName("status") val status: ContentStatus,
                        @SerializedName("tagline") val tagline: String?,
                        @SerializedName("homepage") val homepage: String?,
                        @SerializedName("original_language") val originalLanguage: String,
                        @SerializedName("spoken_languages") val spokenLanguages: List<SpokenLenguage>,
                        @SerializedName("overview") val overview: String?,
                        @SerializedName("production_companies") val productionCompanies: List<ProductionCompanies>,
                        @SerializedName("belongs_to_collection") val belongsToCollection: MutableList<String>?,
                        @SerializedName("poster_path") val posterPath: String?,
                        @SerializedName("revenue") val revenue: Int,
                        @SerializedName("popularity") val popularity: Float)