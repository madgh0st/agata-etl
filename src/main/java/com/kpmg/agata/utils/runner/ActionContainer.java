package com.kpmg.agata.utils.runner;

import java.util.function.Consumer;

public class ActionContainer<T> {
    private final Consumer<T> action;
    private final ActionScope scope;
    private final int priority;

    public ActionContainer(Consumer<T> action, ActionScope scope, int priority) {
        this.action = action;
        this.scope = scope;
        this.priority = priority;
    }

    public Consumer<T> getAction() {
        return action;
    }

    public ActionScope getScope() {
        return scope;
    }

    public int getPriority() {
        return priority;
    }
}
