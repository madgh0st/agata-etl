package com.kpmg.agata.utils.runner;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.kpmg.agata.utils.runner.ActionScope.HERE;
import static com.kpmg.agata.utils.runner.ActionScope.GLOBAL;

public class TerminalTask<T> implements Tasks<T> {
    private final Map<Integer, Consumer<T>> actions = new HashMap<>();
    private final T task;

    public TerminalTask(T task) {
        this.task = task;
    }

    @Override
    public void addTasks(Tasks<T> tasks) {
        throw new UnsupportedOperationException("Adding more then one task to TerminalTask is not allowed");
    }

    @Override
    public void addAction(Consumer<T> action, ActionScope scope, int priority) {
        if (actions.containsKey(priority)) {
            throw new IllegalStateException("Actions must have different priorities");
        }

        if (scope == HERE || scope == GLOBAL) {
            actions.put(priority, action);
        }
    }

    @Override
    public void run() {
        actions.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .forEach(action -> action.accept(task));
    }

    @Override
    public String toString() {
        return "TerminalTask{" +
                "task=" + task +
                '}';
    }
}
