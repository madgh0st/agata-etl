package com.kpmg.agata.utils.runner;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.kpmg.agata.utils.runner.ActionScope.*;

public class ParallelTasks<T> implements Tasks<T> {
    private final List<Tasks<T>> tasksList = new ArrayList<>();

    @Override
    public void addTasks(Tasks<T> tasks) {
        tasksList.add(tasks);
    }

    @Override
    public void addAction(Consumer<T> action, ActionScope scope, int priority) {
        if (scope == LOCAL) {
            tasksList.forEach(tasks -> tasks.addAction(action, HERE, priority));
        }

        if (scope == GLOBAL) {
            tasksList.forEach(tasks -> tasks.addAction(action, scope, priority));
        }
    }

    @Override
    public void run() {
        tasksList.parallelStream().forEach(Tasks::run);
    }
}
