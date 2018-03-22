package pl.oziem.whattowatch

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.matcher.ViewMatchers.*
import android.view.View
import android.view.ViewGroup
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import pl.oziem.whattowatch.main.MovieListAdapter.ViewHolder
import java.util.concurrent.TimeUnit

/** Created by marcinoziem on 20/03/2018 WhatToWatch.
 */
fun movies(func: MoviesMasterDetailsRobot.() -> Unit) = MoviesMasterDetailsRobot().apply { func() }

class MoviesMasterDetailsRobot {

  fun clickOnMovie(position: Int): MoviesMasterDetailsRobot = apply {
    onView(allOf(
      withId(R.id.movie_list),
      childAtPosition(withId(R.id.frameLayout), 0)
    )).perform(actionOnItemAtPosition<ViewHolder>(position, click()))
  }

  fun navigateUp(): MoviesMasterDetailsRobot = apply {
    onView(allOf(
      withContentDescription("Navigate up"),
      childAtPosition(allOf(
        withId(R.id.detail_toolbar),
        childAtPosition(withId(R.id.toolbar_layout), 1)
      ), 1),
      isDisplayed())
    ).perform(click())
  }

  fun checkIfMovieDetailsIsVisible(): MoviesMasterDetailsRobot = apply {
    onView(withId(R.id.movie_detail))
      .check(ViewAssertions.matches(isDisplayed()))
  }

  fun wait(num: Long, unit: TimeUnit): MoviesMasterDetailsRobot = apply {
    try {
      Thread.sleep(unit.toMillis(num))
    } catch (e: InterruptedException) {
      e.printStackTrace()
    }
  }

  private fun childAtPosition(
    parentMatcher: Matcher<View>, position: Int): Matcher<View> {

    return object : TypeSafeMatcher<View>() {
      override fun describeTo(description: Description) {
        description.appendText("Child at position $position in parent ")
        parentMatcher.describeTo(description)
      }

      public override fun matchesSafely(view: View): Boolean {
        val parent = view.parent
        return (parent is ViewGroup && parentMatcher.matches(parent)
          && view == parent.getChildAt(position))
      }
    }
  }
}
