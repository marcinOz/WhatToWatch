package pl.oziem.datasource.services

import io.reactivex.Single
import pl.oziem.datasource.models.MovieDetails
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by MarcinOz on 2018-03-02.
 * Copyright (C) 2017 OKE Poland Sp. z o.o. All rights reserved.
 */
interface ApiService {

  @GET("3/movie/{id}")
  fun getMovieDetailsById(@Path("id") movieId: Int,
                          @Query("api_key") apiKey: String): Single<MovieDetails>
}
