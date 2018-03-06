package pl.oziem.datasource.services

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by MarcinOz on 2018-03-02.
 * Copyright (C) 2017 OKE Poland Sp. z o.o. All rights reserved.
 */
interface ApiService {

  @GET("3/movie/{id}")
  fun getMovieById(@Path("id") movieId: Int, @Query("api_key") apiKey: String): Single<String>
}
