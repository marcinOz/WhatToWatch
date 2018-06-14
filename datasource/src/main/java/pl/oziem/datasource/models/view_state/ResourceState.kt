package pl.oziem.datasource.models.view_state

/**
 * Created by marcinoziem
 * on 13/06/2018 WhatToWatch.
 */
sealed class ResourceState<T>(val state: ViewState) {
  override fun hashCode(): Int = this.state.ordinal
  override fun equals(other: Any?) = equalss(other)
}

class LoadingState<T> : ResourceState<T>(ViewState.LOADING)
class EmptyState<T> : ResourceState<T>(ViewState.EMPTY)

data class PopulatedState<T>(val data: T) : ResourceState<T>(ViewState.POPULATED) {
  override fun hashCode(): Int = this.state.ordinal + (data?.hashCode() ?: 0)
  override fun equals(other: Any?) = equalss(other) && data === (other as PopulatedState<*>).data
}

data class ErrorState<T>(val message: String?) : ResourceState<T>(ViewState.ERROR) {
  override fun hashCode(): Int = this.state.ordinal + (message?.hashCode() ?: 0)
  override fun equals(other: Any?) = equalss(other) && message === (other as ErrorState<*>).message
}


inline fun <reified T : ResourceState<*>> T.equalss(other: Any?) = when {
  this === other -> true
  other !is T || state != other.state -> false
  else -> true
}
