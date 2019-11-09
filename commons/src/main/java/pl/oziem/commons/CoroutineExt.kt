package pl.oziem.commons

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> withDispatcherIO(
  block: suspend CoroutineScope.() -> T
) = withContext(Dispatchers.IO, block)
