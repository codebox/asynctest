package net.codebox.asynctestutil;

@FunctionalInterface
interface Task {
    void run() throws Exception;
}
