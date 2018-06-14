package pl.oziem.datasource.models

/**
 * Created by marcinoziem
 * on 13/06/2018 WhatToWatch.
 */
sealed class ResourceState<T>

class LoadingState<T> : ResourceState<T>() {
  override fun hashCode(): Int = 1
  override fun equals(other: Any?) = equalz(other)
}
class EmptyState<T> : ResourceState<T>() {
  override fun hashCode(): Int = 2
  override fun equals(other: Any?) = equalz(other)
}
data class PopulatedState<T>(val data: T) : ResourceState<T>()
data class ErrorState<T>(val message: String?) : ResourceState<T>()


inline fun <reified T : ResourceState<*>> T.equalz(other: Any?) = when {
  this === other -> true
  other !is T -> false
  else -> true
}
