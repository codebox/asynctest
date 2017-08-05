package net.codebox.asynctestutil;

import java.time.Duration;

class TimeConstraint {
    static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(15);
    static final Duration DEFAULT_CHECK_INTERVAL = Duration.ofSeconds(1);

    private Duration timeout;
    private Duration checkInterval;

    TimeConstraint() {
        this(DEFAULT_TIMEOUT, DEFAULT_CHECK_INTERVAL);
    }

    TimeConstraint(final Duration timeout) {
        this(timeout, DEFAULT_CHECK_INTERVAL);
    }

    TimeConstraint(final Duration timeout, final Duration checkInterval) {
        if (timeout.isNegative() || timeout.isZero()) {
            throw new IllegalArgumentException("Invalid timeout interval: " + format(timeout));
        }
        if (checkInterval.isNegative() || checkInterval.isZero()) {
            throw new IllegalArgumentException("Invalid check interval: " + format(checkInterval));
        }
        this.timeout = timeout;
        this.checkInterval = checkInterval;
    }

    Duration getCheckInterval() {
        return checkInterval;
    }

    Duration getTimeout() {
        return timeout;
    }

    static String format(final Duration duration) {
        long seconds = duration.getSeconds();
        return String.format("%s second%s", seconds, seconds == 1 ? "" : "s");
    }
}
