package net.codebox.asynctestutil;

import java.time.Duration;

/**
 * Helper class for creating AsyncTaskAssertion objects.
 */
public class AsyncTestUtils {
    /**
     * Creates an AsyncTaskAssertion object with the specified timeout value.
     *
     * @param timeout the timeout value to be assigned to the AsyncTaskAssertion object
     *
     * @return a new AsyncTaskAssertion object
     */
    public static AsyncTaskAssertion assertThatWithin(Duration timeout) {
        return new AsyncTaskAssertion().withTimeout(timeout);
    }

    /**
     * Creates an AsyncTaskAssertion object with the default timeout value of 15 seconds.
     *
     * @return a new AsyncTaskAssertion object
     */
    public static AsyncTaskAssertion assertThatEventually() {
        return new AsyncTaskAssertion();
    }
}
