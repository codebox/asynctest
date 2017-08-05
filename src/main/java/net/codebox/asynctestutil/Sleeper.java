package net.codebox.asynctestutil;

@FunctionalInterface
public interface Sleeper {
    void sleep(long millis) throws InterruptedException;
}
