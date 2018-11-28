package pl.oziem.datasource.models

import android.net.Uri

data class Profile(
  val id: String,
  val name: String?,
  val email: String,
  val emailVerified: Boolean,
  val photoUrl: Uri?
) {
  val viewingName get() = if(!name.isNullOrEmpty()) name else email
}
