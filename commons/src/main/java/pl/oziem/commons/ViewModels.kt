package pl.oziem.commons

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*

/**
 * Created by marcinoziem
 * on 13/06/2018 WhatToWatch.
 */
inline fun <reified T : ViewModel> FragmentActivity.getViewModel(
  viewModelFactory: ViewModelProvider.Factory
): T = ViewModelProviders.of(this, viewModelFactory)[T::class.java]

inline fun <reified T : ViewModel> FragmentActivity.withViewModel(
  viewModelFactory: ViewModelProvider.Factory,
  body: T.() -> Unit
): T = getViewModel<T>(viewModelFactory).apply(body)

inline fun <reified T : ViewModel> Fragment.getViewModel(
  viewModelFactory: ViewModelProvider.Factory
): T = ViewModelProviders.of(this, viewModelFactory)[T::class.java]

inline fun <reified T : ViewModel> Fragment.withViewModel(
  viewModelFactory: ViewModelProvider.Factory,
  body: T.() -> Unit
): T = getViewModel<T>(viewModelFactory).apply(body)

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T) -> Unit) {
  liveData.observe(this, Observer { it?.apply(body) })
}
