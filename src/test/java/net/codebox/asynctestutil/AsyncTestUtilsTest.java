package net.codebox.asynctestutil;

import org.junit.Test;

import java.time.Duration;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AsyncTestUtilsTest {
    @Test
    public void checkThatAssertThatWithinMethodSetsCorrectTimeout(){
        final Duration duration = Duration.ofSeconds(3);
        AsyncTaskAssertion asyncTaskAssertion = AsyncTestUtils.assertThatWithin(duration);
        assertThat(asyncTaskAssertion.getTimeConstraint().getTimeout(), is(duration));
    }

    @Test
    public void checkThatAssertThatEventuallyMethodGivesDefaultTimeout(){
        AsyncTaskAssertion asyncTaskAssertion = AsyncTestUtils.assertThatEventually();
        assertThat(asyncTaskAssertion.getTimeConstraint().getTimeout(), is(TimeConstraint.DEFAULT_TIMEOUT));
    }
}
