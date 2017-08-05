package net.codebox.asynctestutil;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.Duration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TimeConstraintTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void defaultConstructorSetsValuesCorrectly(){
        final TimeConstraint timeConstraint = new TimeConstraint();

        assertThat(timeConstraint.getCheckInterval(), is(Duration.ofSeconds(1)));
        assertThat(timeConstraint.getTimeout(), is(Duration.ofSeconds(15)));
    }

    @Test
    public void singleArgConstructorSetsValuesCorrectly(){
        final Duration timeout = Duration.ofSeconds(10);
        final TimeConstraint timeConstraint = new TimeConstraint(timeout);

        assertThat(timeConstraint.getCheckInterval(), is(Duration.ofSeconds(1)));
        assertThat(timeConstraint.getTimeout(), is(timeout));
    }

    @Test
    public void doubleArgConstructorSetsValuesCorrectly(){
        final Duration timeout = Duration.ofSeconds(10);
        final Duration checkInterval = Duration.ofSeconds(2);
        final TimeConstraint timeConstraint = new TimeConstraint(timeout, checkInterval);

        assertThat(timeConstraint.getCheckInterval(), is(checkInterval));
        assertThat(timeConstraint.getTimeout(), is(timeout));
    }

    @Test
    public void negativeTimeoutCausesException(){
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(is("Invalid timeout interval: -1 seconds"));

        new TimeConstraint(Duration.ofSeconds(-1), Duration.ofSeconds(1));
    }

    @Test
    public void zeroTimeoutCausesException(){
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(is("Invalid timeout interval: 0 seconds"));

        new TimeConstraint(Duration.ofSeconds(0), Duration.ofSeconds(1));
    }

    @Test
    public void nullTimeoutCausesException(){
        exception.expect(NullPointerException.class);

        new TimeConstraint(null, Duration.ofSeconds(1));
    }

    @Test
    public void negativeCheckIntervalCausesException(){
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(is("Invalid check interval: -1 seconds"));

        new TimeConstraint(Duration.ofSeconds(10), Duration.ofSeconds(-1));
    }

    @Test
    public void zeroCheckIntervalCausesException(){
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(is("Invalid check interval: 0 seconds"));

        new TimeConstraint(Duration.ofSeconds(10), Duration.ofSeconds(0));
    }

    @Test
    public void nullCheckIntervalCausesException(){
        exception.expect(NullPointerException.class);

        new TimeConstraint(Duration.ofSeconds(10), null);
    }

    @Test
    public void durationFormattingWorksCorrectly(){
        assertThat(TimeConstraint.format(Duration.ofSeconds(0)), is("0 seconds"));
        assertThat(TimeConstraint.format(Duration.ofSeconds(1)), is("1 second"));
        assertThat(TimeConstraint.format(Duration.ofSeconds(2)), is("2 seconds"));
    }
}
