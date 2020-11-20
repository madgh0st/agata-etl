package com.kpmg.agata.utils.runner;

import java.util.function.Consumer;

public class InvokeTask<T> extends TerminalTask<T> {
    private final Runnable action;

    public InvokeTask(Runnable action) {
        super(null);
        this.action = action;
    }

    @Override
    public void addAction(Consumer<T> action, ActionScope scope, int priority) {
        // no actions required
    }

    @Override
    public void run() {
        action.run();
    }

    @Override
    public String toString() {
        return "InvokeTask";
    }
}
