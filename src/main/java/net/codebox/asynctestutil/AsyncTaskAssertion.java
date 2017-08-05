package net.codebox.asynctestutil;

import java.time.Clock;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

import static org.junit.Assert.fail;

/**
 * Class that represents an assertion that an asynchronous task will succeed or fail within
 * a specified time. Each instance of this class requires a Task object, and a timeout
 * value represented by a Duration object. If no timeout is specified a default value of
 * 15 seconds will be used.
 */
public class AsyncTaskAssertion {
    private TimeConstraint timeConstraint = new TimeConstraint();
    private Task task;
    private AssertionError lastAssertionError;
    private Exception lastException;
    private boolean succeeded;
    private BooleanSupplier assertionPassed;
    private int taskExecutionCount = 0;
    private Clock clock = Clock.systemDefaultZone();
    private Sleeper sleeper = Thread::sleep;

    /**
     * Sets the timeout value to be associated with this object.
     *
     * @param timeout length of time beyond which the assertion will fail
     * @return a reference to the current instance
     */
    public AsyncTaskAssertion withTimeout(final Duration timeout) {
        this.timeConstraint = new TimeConstraint(timeout);
        return this;
    }

    /**
     * Sets the Task to be associated with this object.
     *
     * @param task the task that will be executed by this object
     * @return a reference to the current instance
     */
    public AsyncTaskAssertion thisTask(final Task task) {
        this.task = task;
        return this;
    }

    /**
     * Sets the Task to be associated with this object, and indicates that the task must
     * complete without any exceptions/errors being thrown in order for the assertion to succeed.
     *
     * @param task the task that will be executed by this object
     */
    public void thisTaskWillSucceed(final Task task) {
        this.task = task;
        willSucceed();
    }

    /**
     * Indicates that the task associated with this object must complete without any exceptions/errors being
     * thrown in order for the assertion to succeed.
     */
    public void willSucceed() {
        assertionPassed = () -> succeeded;

        runTask();

        if (!assertionPassed.getAsBoolean()) {
            addDetailsToMsgAndFail(String.format("Operation did not complete successfully within the timeout interval of %s",
                    TimeConstraint.format(timeConstraint.getTimeout())));
        }
    }

    /**
     * Indicates that the task associated with this object must throw the specified Exception in order
     * for the assertion to succeed.
     *
     * @param ex class representing the type of Exception that must be thrown
     */
    public void willThrow(final Class<? extends Exception> ex) {
        assertionPassed = () -> lastException != null && ex.isAssignableFrom(lastException.getClass());

        runTask();

        if (!assertionPassed.getAsBoolean()) {
            addDetailsToMsgAndFail(String.format("Operation did not throw %s within the timeout interval of %s",
                    ex.getCanonicalName(), TimeConstraint.format(timeConstraint.getTimeout())));
        }
    }

    private void addDetailsToMsgAndFail(final String failureMsgInitial) {
        final List<String> failureMsg = new ArrayList<>();

        failureMsg.add(failureMsgInitial);

        failureMsg.add(String.format("The task ran %s time%s", taskExecutionCount, taskExecutionCount == 1 ? "" : "s"));

        if (lastAssertionError != null) {
            failureMsg.add(String.format("The last AssertionError was %s", lastAssertionError));
        } else {
            failureMsg.add("There were no AssertionErrors");
        }

        if (lastException != null) {
            failureMsg.add(String.format("The last Exception thrown was %s", lastException));
        } else {
            failureMsg.add("No other Exceptions were thrown");
        }

        fail(String.join(". ", failureMsg));
    }

    private void runTask() {
        long timeoutTs = clock.millis() + timeConstraint.getTimeout().toMillis();

        while (clock.millis() < timeoutTs) {
            try {
                taskExecutionCount++;
                task.run();
                succeeded = true;

            } catch (AssertionError ex) {
                lastAssertionError = ex;

            } catch (Exception ex) {
                lastException = ex;

            } finally {
                if (assertionPassed.getAsBoolean()) {
                    return;
                }
                sleep();
            }
        }
    }

    private void sleep() {
        try {
            sleeper.sleep(timeConstraint.getCheckInterval().toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Main thread was interrupted while waiting for asynchronous task to complete", e);
        }
    }

    void setClock(final Clock clock){
        this.clock = clock;
    }

    void setSleeper(final Sleeper sleeper) {
        this.sleeper = sleeper;
    }

    TimeConstraint getTimeConstraint() {
        return timeConstraint;
    }
}
