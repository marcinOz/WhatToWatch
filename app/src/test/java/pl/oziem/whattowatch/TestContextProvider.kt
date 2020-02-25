package pl.oziem.whattowatch

import kotlinx.coroutines.Dispatchers
import pl.oziem.commons.CoroutineContextProvider
import kotlin.coroutines.CoroutineContext

class TestContextProvider : CoroutineContextProvider() {
    override val Main: CoroutineContext = Dispatchers.Unconfined
    override val IO: CoroutineContext = Dispatchers.Unconfined
}
