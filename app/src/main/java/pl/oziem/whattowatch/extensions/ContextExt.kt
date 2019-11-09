package pl.oziem.whattowatch.extensions

import android.content.Context
import pl.oziem.whattowatch.WTWApplication

fun Context.isUserSignedIn(): Boolean = WTWApplication.isLoggedIn(this)
