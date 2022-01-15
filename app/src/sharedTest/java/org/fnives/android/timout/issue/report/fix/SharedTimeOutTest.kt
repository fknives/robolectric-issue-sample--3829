package org.fnives.android.timout.issue.report.fix

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.fnives.android.timeout.issue.report.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(
    instrumentedPackages = ["androidx.loader.content"])
// required to access final members on androidx.loader.content.ModernAsyncTask
// reference: https://github.com/robolectric/robolectric/issues/6593
class SharedTimeOutTest {

    @JvmField
    @Rule
    val globalTimeout: SharedTimeOut = SharedTimeOut.seconds(5)

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun example() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        activityScenario.moveToState(Lifecycle.State.RESUMED)
        Espresso.onView(ViewMatchers.isRoot()).perform(ViewActions.click())
        activityScenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test(expected = IllegalStateException::class)
    fun exceptionThrowing() {
        Thread.sleep(4000)
        throw IllegalStateException("")
    }

    @Test(expected = InterruptedException::class)
    fun timeout() {
        Thread.sleep(6000)
    }

    /**
     * Remove ignore to verify manually.
     *
     * Expected Error in test report: org.junit.runners.model.TestTimedOutException: test timed out after 5000 milliseconds
     */
    @Ignore
    @Test
    fun manualVerificationOfExceptionThrownIsExpected() {
        Thread.sleep(6000)
    }

    /**
     * Remove ignore to verify manually.
     *
     * Expected Error in test report: org.junit.runners.model.TestTimedOutException: test timed out after 5000 milliseconds
     */
    @Ignore
    @Test(expected = IllegalStateException::class)
    fun manualVerificationOfExceptionThrown() {
        Thread.sleep(6000)
    }
}
