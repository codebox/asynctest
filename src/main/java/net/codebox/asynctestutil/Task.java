package net.codebox.asynctestutil;

@FunctionalInterface
public interface Task {
    void run() throws Exception;
}
