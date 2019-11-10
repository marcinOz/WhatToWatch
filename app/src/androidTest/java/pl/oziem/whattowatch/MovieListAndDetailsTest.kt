package pl.oziem.whattowatch

import androidx.test.rule.ActivityTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pl.oziem.whattowatch.splash.SplashActivity
import java.util.*
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class MovieListAndDetailsTest {

  @get:Rule
  val activityTestRule = ActivityTestRule(SplashActivity::class.java)

  private val itemCount = 20


  @Before
  fun setup() {
    movies { wait(5, TimeUnit.SECONDS) }
  }

  @Test
  fun clickFirstItemAndGoUp() {
    movies {
      clickOnMovie(0)
      checkIfMovieDetailsIsVisible()
      navigateUp()
    }
  }

  @Test
  fun clickLastItemAndGoUp() {
    movies {
      clickOnMovie(itemCount - 1)
      checkIfMovieDetailsIsVisible()
      navigateUp()
    }
  }

  @Test
  fun clickRandomItemAndGoUp() {
    movies {
      clickOnMovie(Random().nextInt(itemCount - 1))
      checkIfMovieDetailsIsVisible()
      navigateUp()
    }
  }
}
