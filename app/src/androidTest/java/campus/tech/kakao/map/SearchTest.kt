package campus.tech.kakao.map

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import campus.tech.kakao.map.presentation.view.KakaoMapViewActivity
import campus.tech.kakao.map.presentation.view.SearchActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class SearchTest {

    @Test
    fun searchWordIsEntered() {
        val scenario = ActivityScenario.launch(SearchActivity::class.java)
        performSearch("카페")
        scenario.close()
    }

    @Test
    fun recyclerViewIsClicked() {
        val scenario = ActivityScenario.launch(SearchActivity::class.java)
        performSearch("카페")
        checkRecyclerView(0)
        isBottomSheetVisible()
        scenario.close()
    }

    @Test
    fun savedSearchWordRecyclerViewIsClicked() {
        val scenario = ActivityScenario.launch(SearchActivity::class.java)
        performSearch("카페")
        checkRecyclerView(0)
        pressBack()
        Thread.sleep(2000)
        checkSavedSearchWord(0)
        scenario.close()
    }

    @Test
    fun deleteSavedWordIsWorking() {
        val scenario = ActivityScenario.launch(SearchActivity::class.java)
        performSearch("카페")
        checkRecyclerView(0)
        pressBack()
        Thread.sleep(2000)
        checkSavedSearchWord(0)
        deleteSavedWord()
        scenario.close()
    }

    @Test
    fun deleteSearchWordIsWorking() {
        val scenario = ActivityScenario.launch(SearchActivity::class.java)
        performSearch("카페")
        deleteSearchWord()
        scenario.close()
    }

    @Test
    fun kakaoMapSearchButton() {
        val scenario = ActivityScenario.launch(KakaoMapViewActivity::class.java)
        onView(withId(R.id.searchButton))
            .perform(click())
        Thread.sleep(2000)
        scenario.close()
    }

    private fun performSearch(query: String) {
        onView(withId(R.id.searchWord))
            .check(matches(isDisplayed()))

        Thread.sleep(3000)

        onView(withId(R.id.searchWord))
            .perform(replaceText(query))

        Thread.sleep(3000)
    }

    private fun checkRecyclerView(position: Int) {
        onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    position, click()
                )
            )

        Thread.sleep(4000)
    }

    private fun checkSavedSearchWord(position: Int) {
        onView(withId(R.id.savedSearchWordRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    position, click()
                )
            )

        Thread.sleep(2000)
    }

    private fun deleteSavedWord() {
        onView(withId(R.id.deleteSavedWord))
            .perform(click())

        Thread.sleep(2000)
    }

    private fun deleteSearchWord() {
        onView(withId(R.id.deleteSearchWord))
            .perform(click())

        onView(withId(R.id.searchWord))
            .check(matches(withText("")))

        Thread.sleep(2000)
    }

    private fun isBottomSheetVisible() {
        onView(withId(R.id.persistent_bottom_sheet))
            .check(matches(isDisplayed()))
        Thread.sleep(2000)
    }
}