package pl.oziem.whattowatch.details

import android.app.Activity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_movie_detail.*
import pl.oziem.whattowatch.R
import pl.oziem.whattowatch.WTWApplication


class MovieDetailActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    if (resources.getBoolean(R.bool.isWideTabletScreen)) {
      setResult(Activity.RESULT_OK, intent)
      finish()
    }
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_movie_detail)
    supportPostponeEnterTransition()

    setSupportActionBar(detail_toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    fab.setOnClickListener { view ->
      Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
        .setAction("Action", null).show()
    }

    savedInstanceState ?: initFragment()
  }

  private fun initFragment() {
    val fragment = MovieDetailFragment().apply {
      arguments = Bundle().apply {
        putAll(intent.extras)
      }
    }
    supportFragmentManager.beginTransaction()
      .add(R.id.movie_detail_container, fragment)
      .commit()
  }

  fun setToolbarData(title: String?, url: String?) {
    toolbar_layout.title = title
    WTWApplication.getImageLoader(this).loadBackdrop(url)
      .fadeTransition()
      .fitCenter()
      .into(toolbar_image)
  }

  override fun onOptionsItemSelected(item: MenuItem) =
    when (item.itemId) {
      android.R.id.home -> {
        supportFinishAfterTransition()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
}
