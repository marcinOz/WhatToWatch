package pl.oziem.whattowatch.extensions

import androidx.constraintlayout.motion.widget.MotionLayout
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun MotionLayout.awaitTransitionEnd() = suspendCancellableCoroutine<Unit> { cont ->
  setTransitionListener(object : MotionLayout.TransitionListener {
    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) = Unit
    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) = Unit
    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) = Unit
    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
      cont.resume(Unit)
    }
  })
}
