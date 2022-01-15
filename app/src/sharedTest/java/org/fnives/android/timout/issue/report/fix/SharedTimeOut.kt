package org.fnives.android.timout.issue.report.fix

import org.junit.rules.TestRule
import org.junit.rules.Timeout
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.junit.runners.model.TestTimedOutException
import java.util.concurrent.TimeUnit

/**
 * ## Timeout Rule which can be used in both AndroidTest in Robolectric test mimicking [Timeout][org.junit.rules.Timeout].
 *
 * ### reference issue, explaining why it's needed:
 * https://github.com/robolectric/robolectric/issues/3829
 * ### reference code snipped:
 * https://github.com/robolectric/robolectric/blob/master/junit/src/main/java/org/robolectric/internal/TimeLimitedStatement.java
 * #
 * ### Special thanks to [hoisie][https://github.com/hoisie]
 */
class SharedTimeOut(private val timeout: Long, private val timeUnit: TimeUnit) : TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return SharedTimeOutStatement(timeUnit.toMillis(timeout), base)
    }

    class SharedTimeOutStatement(
        private val timeoutInMillis: Long,
        private val statement: Statement
    ) : Statement() {
        override fun evaluate() {
            val testThread = Thread.currentThread()
            val timeoutThread = Thread(
                {
                    try {
                        Thread.sleep(timeoutInMillis)
                        testThread.interrupt()
                    } catch (e: InterruptedException) {
                        // ok
                    }
                },
                "Time-limited test"
            )
            timeoutThread.start()

            try {
                statement.evaluate()
            } catch (interruptedException: InterruptedException) {
                val testTimedOutException: Exception = TestTimedOutException(timeoutInMillis, TimeUnit.MILLISECONDS)
                testTimedOutException.stackTrace = interruptedException.stackTrace
                throw testTimedOutException
            } finally {
                timeoutThread.interrupt()
                timeoutThread.join()
            }
        }
    }

    companion object {

        /**
         * Creates a [Timeout] that will timeout a test after thegiven duration, in milliseconds.
         */
        fun millis(millis: Long): SharedTimeOut {
            return SharedTimeOut(millis, TimeUnit.MILLISECONDS)
        }

        /**
         * Creates a [Timeout] that will timeout a test after thegiven duration, in seconds.
         */
        fun seconds(seconds: Long): SharedTimeOut {
            return SharedTimeOut(seconds, TimeUnit.SECONDS)
        }
    }
}