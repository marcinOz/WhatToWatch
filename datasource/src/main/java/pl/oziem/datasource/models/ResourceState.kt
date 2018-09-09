package pl.oziem.datasource.models

/**
 * Created by marcinoziem
 * on 13/06/2018 WhatToWatch.
 */
sealed class ResourceState

object LoadingState : ResourceState()
object EmptyState : ResourceState()
object PopulatedState : ResourceState()
data class ErrorState(val message: String?) : ResourceState()
