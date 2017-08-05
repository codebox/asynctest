package net.codebox.asynctestutil;

import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

public class AsyncTaskAssertionTest {
    private Clock clock = new MockClock();
    private Sleeper sleeper = new MockSleeper();
    private AsyncTaskAssertion asyncAssertion;
    private long nowTs;

    @Before
    public void setup(){
        asyncAssertion = new AsyncTaskAssertion();
        asyncAssertion.setClock(clock);
        asyncAssertion.setSleeper(sleeper);
    }

    @Test
    public void taskThatCompletesInTimeWillNotCauseFailure(){
        final SuccessTask task = new SuccessTask(2);
        asyncAssertion.withTimeout(Duration.ofSeconds(3)).thisTaskWillSucceed(task);
    }

    @Test
    public void taskThatThrowsExceptionsButCompletesInTimeWillNotCauseFailure(){
        final ExceptionsUntilSuccessTask task = new ExceptionsUntilSuccessTask(2);
        asyncAssertion.withTimeout(Duration.ofSeconds(3)).thisTaskWillSucceed(task);
    }

    @Test(expected = AssertionError.class)
    public void taskThatDoesNotCompleteInTimeWillCauseFailure(){
        final SuccessTask task = new SuccessTask(3);
        asyncAssertion.withTimeout(Duration.ofSeconds(2)).thisTaskWillSucceed(task);
    }

    @Test
    public void taskThatThrowsExpectedExceptionWillNotCauseFailure(){
        final ExceptionTask task = new ExceptionTask(2, new RuntimeException());
        asyncAssertion.withTimeout(Duration.ofSeconds(3)).thisTask(task).willThrow(RuntimeException.class);
    }

    @Test(expected = AssertionError.class)
    public void taskThatDoesNotThrowExpectedExceptionWillCauseFailure(){
        final ExceptionTask task = new ExceptionTask(3, new RuntimeException());
        asyncAssertion.withTimeout(Duration.ofSeconds(2)).thisTask(task).willThrow(RuntimeException.class);
    }

    @Test(expected = AssertionError.class)
    public void taskThatThrowsWrongTypeOfExceptionWillCauseFailure(){
        final ExceptionTask task = new ExceptionTask(1, new IllegalArgumentException());
        asyncAssertion.withTimeout(Duration.ofSeconds(2)).thisTask(task).willThrow(NullPointerException.class);
    }

    class SuccessTask implements Task{
        private long successTs;
        SuccessTask(int secondsUntilSuccess){
            successTs = nowTs + secondsUntilSuccess * 1000;
        }

        @Override
        public void run() {
            if (nowTs < successTs){
                doThrow();
            }
        }

        void doThrow(){
            throw new AssertionError();
        }
    }

    class ExceptionsUntilSuccessTask extends SuccessTask {
        ExceptionsUntilSuccessTask(int secondsUntilSuccess) {
            super(secondsUntilSuccess);
        }

        @Override
        void doThrow() {
            throw new RuntimeException();
        }
    }

    class ExceptionTask implements Task{
        private long exceptionTs;
        private RuntimeException ex;

        ExceptionTask(int secondsUntilException, RuntimeException ex){
            this.ex = ex;
            this.exceptionTs = nowTs + secondsUntilException * 1000;
        }

        @Override
        public void run() throws Exception{
            if (nowTs >= exceptionTs){
                throw ex;
            }
        }
    }

     class MockSleeper implements Sleeper {
         @Override
         public void sleep(long millis) throws InterruptedException {
             nowTs += millis;
         }
     }

     class MockClock extends Clock{
        @Override
        public ZoneId getZone() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Clock withZone(ZoneId zone) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Instant instant() {
            return new Date(nowTs).toInstant();
        }
    }

}
