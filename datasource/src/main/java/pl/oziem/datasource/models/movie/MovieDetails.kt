package pl.oziem.datasource.models.movie

import com.squareup.moshi.Json
import pl.oziem.datasource.models.ContentStatus
import pl.oziem.datasource.models.Genre
import pl.oziem.datasource.models.ProductionCompanies
import pl.oziem.datasource.models.SpokenLanguage

/**
* Created by MarcinOz on 2018-03-02 WhatToWatch.
*/
data class MovieDetails(@Json(name = "id") val id: String,
                        @Json(name = "title") val title: String,
                        @Json(name = "original_title") val originalTitle: String,
                        @Json(name = "adult") val adult: Boolean,
                        @Json(name = "budget") val budget: Int,
                        @Json(name = "genres") val genres: List<Genre>,
                        @Json(name = "imdb_id") val imdbId: String?,
                        @Json(name = "video") val video: Boolean,
                        @Json(name = "vote_average") val voteAverage: Float,
                        @Json(name = "vote_count") val voteCount: Int,
                        @Json(name = "backdrop_path") val backdropPath: String?,
                        @Json(name = "runtime") val runtime: Int?,
                        @Json(name = "release_date") val releaseDate: String,
                        @Json(name = "status") val status: ContentStatus,
                        @Json(name = "tagline") val tagline: String?,
                        @Json(name = "homepage") val homepage: String?,
                        @Json(name = "original_language") val originalLanguage: String,
                        @Json(name = "spoken_languages") val spokenLanguages: List<SpokenLanguage>,
                        @Json(name = "overview") val overview: String?,
                        @Json(name = "production_companies") val productionCompanies: List<ProductionCompanies>,
                        @Json(name = "belongs_to_collection") val belongsToCollection: MutableList<String>?,
                        @Json(name = "poster_path") val posterPath: String?,
                        @Json(name = "revenue") val revenue: Int,
                        @Json(name = "popularity") val popularity: Float)
