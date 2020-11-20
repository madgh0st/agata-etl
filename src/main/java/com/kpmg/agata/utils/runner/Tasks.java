package com.kpmg.agata.utils.runner;

import java.util.function.Consumer;

public interface Tasks<T> {
    void addTasks(Tasks<T> tasks);

    void addAction(Consumer<T> action, ActionScope scope, int priority);

    void run();
}
