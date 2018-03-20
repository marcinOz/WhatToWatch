package pl.oziem.whattowatch

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
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
  private lateinit var robot: MoviesMasterDetailsRobot


  @Before
  fun setup() {
    robot = MoviesMasterDetailsRobot()
    robot.wait(TimeUnit.SECONDS.toMillis(5))
  }

  @Test
  fun clickFirstItemAndGoUp() {
    robot
      .clickOnListItem(0)
      .checkDetailsVisibility()
      .navigateUp()
  }

  @Test
  fun clickLastItemAndGoUp() {
    robot
      .clickOnListItem(itemCount - 1)
      .checkDetailsVisibility()
      .navigateUp()
  }

  @Test
  fun clickRandomItemAndGoUp() {
    robot
      .clickOnListItem(Random().nextInt(itemCount - 1))
      .checkDetailsVisibility()
      .navigateUp()
  }
}
