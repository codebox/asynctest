package net.codebox.asynctestutil;

@FunctionalInterface
interface Sleeper {
    void sleep(long millis) throws InterruptedException;
}
