package com.kpmg.agata.utils.runner;

import javax.annotation.CheckReturnValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class TaskQueueBuilder<T> {
    private List<Tasks<T>> rootTasksList = new ArrayList<>();
    private List<ActionContainer<T>> actionContainers = new ArrayList<>();

    /**
     * Useless method. Can be removed.
     *
     * @param priority execution order - from lowest to highest. Global value for entire TaskQueueBuilder.
     *                 Same values for different actions are not allowed.
     */
    @CheckReturnValue
    public TaskQueueBuilder<T> addAction(Consumer<T> action, ActionScope scope, int priority) {
        if (priority < 0) throw new IllegalArgumentException("Priority can't be negative");

        actionContainers.add(new ActionContainer<>(action, scope, priority));
        rootTasksList.forEach(tasks -> tasks.addAction(action, scope, priority));
        return this;
    }

    @CheckReturnValue
    public TaskQueueBuilder<T> addAction(Consumer<T> action) {
        addAction(action, ActionScope.GLOBAL, 0);
        return this;
    }

    @CheckReturnValue
    @SafeVarargs
    public final TaskQueueBuilder<T> sequentialOf(T... tasks) {
        addTasks(Arrays.stream(tasks).map(TerminalTask::new), new SequentialTasks<>());
        return this;
    }

    @CheckReturnValue
    public TaskQueueBuilder<T> sequentialOf(TaskQueueBuilder<T> taskQueueBuilder) {
        addTasks(taskQueueBuilder.rootTasksList.stream(), new SequentialTasks<>());
        return this;
    }

    @CheckReturnValue
    @SafeVarargs
    public final TaskQueueBuilder<T> parallelOf(T... tasks) {
        addTasks(Arrays.stream(tasks).map(TerminalTask::new), new ParallelTasks<>());
        return this;
    }

    @CheckReturnValue
    public TaskQueueBuilder<T> parallelOf(TaskQueueBuilder<T> taskQueueBuilder) {
        addTasks(taskQueueBuilder.rootTasksList.stream(), new ParallelTasks<>());
        return this;
    }

    private void addTasks(Stream<Tasks<T>> childTasksStream, Tasks<T> wrap) {
        childTasksStream.forEach(wrap::addTasks);
        actionContainers.forEach(actionContainer -> wrap.addAction(actionContainer.getAction(),
                actionContainer.getScope(),
                actionContainer.getPriority()));
        rootTasksList.add(wrap);
    }

    public void run() {
        Tasks<T> wrap = new SequentialTasks<>();
        rootTasksList.forEach(wrap::addTasks);
        wrap.run();
    }

    @CheckReturnValue
    public TaskQueueBuilder<T> invoke(Runnable action) {
        addTasks(Stream.of(action).map(InvokeTask::new), new SequentialTasks<>());
        return this;
    }
}
