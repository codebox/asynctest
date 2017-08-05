AsyncTest
=========
This simple Java library helps you write better tests for asynchronous systems.

Here is a common integration testing anti-pattern:

    sendRequest();
    Thread.sleep(5000);
    checkThatResponseIsCorrect();

If the response is returned quickly then the test will still wait for 5 seconds before moving on, making your test suite slow. On the other hand, if the response is returned correctly but takes slightly longer than 5 seconds then the test will fail, which may not be what you want.

Using the asynctest library, you could re-write the test like this: 

    sendRequest();
    assertThatEventually().thisTaskWillSucceed(this::checkThatResponseIsCorrect);

Behind the scenes the library will run the ```checkThatResponseIsCorrect``` method repeatedly, stopping when the method completes without throwing any Exceptions, or when the time limit of 15 seconds is reached. 

To specify a different time limit, you can do this:

    sendRequest();
    assertThatWithin(Duration.ofSeconds(5)).thisTaskWillSucceed(this::checkThatResponseIsCorrect);

To assert that an Exception does get thrown, just change the code to this:

    assertThatWithin(Duration.ofSeconds(5)).thisTask(this::sendRequest).willThrow(HttpException.class);

When an assertion fails you will see a comprehensive error message explaining what happened, for example:

    java.lang.AssertionError: Operation did not throw org.apache.commons.httpclient.HttpException 
    within the timeout interval of 5 seconds. The task ran 5 times. There were no AssertionErrors. 
    No other Exceptions were thrown.


