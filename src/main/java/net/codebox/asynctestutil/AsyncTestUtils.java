package net.codebox.asynctestutil;

import java.time.Duration;

public class AsyncTestUtils {
    public static AsyncTaskAssertion assertThatWithin(Duration timeout) {
        return new AsyncTaskAssertion().withTimeout(timeout);
    }

    public static AsyncTaskAssertion assertThatEventually() {
        return new AsyncTaskAssertion();
    }
}
