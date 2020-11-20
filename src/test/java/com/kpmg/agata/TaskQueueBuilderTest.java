package com.kpmg.agata;

import com.kpmg.agata.test.utils.TestUtils;
import com.kpmg.agata.utils.runner.TaskQueueBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.kpmg.agata.test.utils.TestUtils.assertException;
import static com.kpmg.agata.test.utils.TestUtils.checkOrder;
import static com.kpmg.agata.utils.Utils.listOf;
import static com.kpmg.agata.utils.runner.ActionScope.GLOBAL;
import static com.kpmg.agata.utils.runner.ActionScope.LOCAL;
import static java.util.UUID.randomUUID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TaskQueueBuilderTest {

    private static final int COUNT = 100;
    private static final int PARALLEL_RETEST_COUNT = 1000;

    @SuppressWarnings("unused")
    private static <T> void devNull(T arg) {
        // no actions required
    }

    @Test
    public void sequenceTest() {
        UUID[] tasks = generateUuidTasks(COUNT);
        List<UUID> reports = new ArrayList<>();

        new TaskQueueBuilder<UUID>()
                .addAction(reports::add, GLOBAL, 0)
                .sequentialOf(tasks)
                .run();

        assertEquals(tasks.length, reports.size());
        assertTrue(isReportSequential(tasks, reports, 0));
    }

    @Test
    public void parallelTest() {
        UUID[] tasks = generateUuidTasks(COUNT);
        List<UUID> reports = Collections.synchronizedList(new ArrayList<>());

        new TaskQueueBuilder<UUID>()
                .addAction(reports::add, GLOBAL, 0)
                .parallelOf(tasks)
                .run();

        assertTrue(isReportParallel(tasks, reports, 0));
    }

    /**
     * If report collection is not thread-safe, there is a chance to fail parallelTest()
     */
    @Test
    public void parallelTestSeveralTimes() {
        for (int i = 0; i < PARALLEL_RETEST_COUNT; i++) {
            parallelTest();
        }
    }

    @Test
    public void mixedTest() {
        UUID[] seqTasks1 = generateUuidTasks(COUNT);
        UUID[] parTasks1 = generateUuidTasks(COUNT);
        UUID[] seqTasks2 = generateUuidTasks(COUNT);
        UUID[] parTasks2 = generateUuidTasks(COUNT);
        List<UUID> reports = Collections.synchronizedList(new ArrayList<>());


        new TaskQueueBuilder<UUID>()
                .addAction(reports::add, GLOBAL, 0)
                .sequentialOf(seqTasks1)
                .parallelOf(parTasks1)
                .sequentialOf(seqTasks2)
                .parallelOf(parTasks2)
                .run();

        assertTrue(isReportSequential(seqTasks1, reports, 0));
        assertTrue(isReportParallel(parTasks1, reports, COUNT));
        assertTrue(isReportSequential(seqTasks2, reports, COUNT * 2));
        assertTrue(isReportParallel(parTasks2, reports, COUNT * 3));
    }

    private UUID[] generateUuidTasks(int count) {
        return IntStream.iterate(0, i -> ++i)
                        .limit(count)
                        .mapToObj(i -> randomUUID())
                        .toArray(UUID[]::new);
    }

    private boolean isReportSequential(UUID[] task, List<UUID> report, int from) {
        return report.subList(from, from + task.length).equals(Arrays.asList(task));
    }

    private boolean isReportParallel(UUID[] task, List<UUID> report, int from) {
        List<UUID> expected = Arrays.asList(task);
        expected.sort(UUID::compareTo);

        List<UUID> actual = new ArrayList<>(report.subList(from, from + task.length));
        actual.sort(UUID::compareTo);

        return expected.equals(actual);
    }

    @Test
    public void emptyTest() {
        UUID[] tasks = generateUuidTasks(0);
        List<UUID> reports = new ArrayList<>();

        new TaskQueueBuilder<UUID>()
                .addAction(reports::add, GLOBAL, 0)
                .sequentialOf(tasks)
                .parallelOf(tasks)
                .run();

        assertEquals(0, reports.size());
    }

    @Test
    public void hierarchicalSequentialTest() {
        UUID[] seqTasks1 = generateUuidTasks(COUNT);
        UUID[] seqTasks2 = generateUuidTasks(COUNT);
        UUID[] seqTasks3 = generateUuidTasks(COUNT);
        UUID[] seqTasks4 = generateUuidTasks(COUNT);
        UUID[] seqTasks5 = generateUuidTasks(COUNT);
        UUID[] seqTasks6 = generateUuidTasks(COUNT);
        List<UUID> reports = Collections.synchronizedList(new ArrayList<>());

        new TaskQueueBuilder<UUID>()
                .addAction(reports::add, GLOBAL, 0)
                .sequentialOf(new TaskQueueBuilder<UUID>()
                        .sequentialOf(new TaskQueueBuilder<UUID>()
                                .sequentialOf(seqTasks1)
                                .sequentialOf(seqTasks2)))
                .sequentialOf(seqTasks3)
                .sequentialOf(new TaskQueueBuilder<UUID>()
                        .sequentialOf(seqTasks4)
                        .sequentialOf(seqTasks5))
                .sequentialOf(new TaskQueueBuilder<UUID>()
                        .sequentialOf(seqTasks6))
                .run();

        List<UUID[]> tasksList = new ArrayList<>();
        tasksList.add(seqTasks1);
        tasksList.add(seqTasks2);
        tasksList.add(seqTasks3);
        tasksList.add(seqTasks4);
        tasksList.add(seqTasks5);
        tasksList.add(seqTasks6);
        assertTrue(isReportSequential(tasksList, reports));
    }

    private boolean isReportSequential(List<UUID[]> tasksList, List<UUID> report) {
        int offset = 0;
        for (UUID[] tasks : tasksList) {
            if (!isReportSequential(tasks, report, offset)) return false;
            offset += tasks.length;
        }
        return true;
    }

    /**
     * Checks only if element exists!
     * Doesn't check task report positions
     */
    private boolean isReportParallel(List<UUID[]> tasksList, List<UUID> report) {
        List<UUID> expected = tasksList.stream()
                                       .flatMap(Arrays::stream)
                                       .sorted(UUID::compareTo)
                                       .collect(Collectors.toList());

        List<UUID> actual = report.stream()
                                  .sorted()
                                  .collect(Collectors.toList());

        return expected.equals(actual);

    }

    @Test
    public void hierarchicalParallelTest() {
        UUID[] seqTasks1 = generateUuidTasks(COUNT);
        UUID[] seqTasks2 = generateUuidTasks(COUNT);
        UUID[] seqTasks3 = generateUuidTasks(COUNT);
        UUID[] seqTasks4 = generateUuidTasks(COUNT);
        UUID[] seqTasks5 = generateUuidTasks(COUNT);
        UUID[] seqTasks6 = generateUuidTasks(COUNT);
        List<UUID> reports = Collections.synchronizedList(new ArrayList<>());

        new TaskQueueBuilder<UUID>()
                .addAction(reports::add, GLOBAL, 0)
                .parallelOf(new TaskQueueBuilder<UUID>()
                        .parallelOf(new TaskQueueBuilder<UUID>()
                                .parallelOf(seqTasks1)
                                .parallelOf(seqTasks2)))
                .parallelOf(seqTasks3)
                .parallelOf(new TaskQueueBuilder<UUID>()
                        .parallelOf(seqTasks4)
                        .parallelOf(seqTasks5))
                .parallelOf(new TaskQueueBuilder<UUID>()
                        .parallelOf(seqTasks6))
                .run();

        List<UUID[]> tasksList = new ArrayList<>();
        tasksList.add(seqTasks1);
        tasksList.add(seqTasks2);
        tasksList.add(seqTasks3);
        tasksList.add(seqTasks4);
        tasksList.add(seqTasks5);
        tasksList.add(seqTasks6);
        assertTrue(isReportParallel(tasksList, reports));
        // bad check, sometimes can be true
        assertFalse(isReportSequential(tasksList, reports));
    }

    @Test
    public void exceptionTest() {
        UUID[] seqTasks1 = generateUuidTasks(COUNT);
        UUID[] seqTasks2 = generateUuidTasks(COUNT);
        UUID[] seqTasks3 = generateUuidTasks(COUNT);
        List<UUID> reports = Collections.synchronizedList(new ArrayList<>());

        assertException(ArithmeticException.class,
                () -> new TaskQueueBuilder<UUID>()
                        .addAction(task -> {
                            if (task.equals(seqTasks2[0])) throw new ArithmeticException("something went wrong");
                            reports.add(task);
                        }, GLOBAL, 0)
                        .sequentialOf(new TaskQueueBuilder<UUID>()
                                .sequentialOf(seqTasks1)
                                .sequentialOf(seqTasks2))
                        .sequentialOf(seqTasks3)
                        .run());

        List<UUID[]> tasksList = new ArrayList<>();
        tasksList.add(seqTasks1);
        assertTrue(isReportSequential(tasksList, reports));
    }

    @Test
    public void multitaskTest() {
        UUID task1 = randomUUID();
        UUID task2 = randomUUID();
        UUID task3 = randomUUID();
        List<TestTask> reports = Collections.synchronizedList(new ArrayList<>());

        new TaskQueueBuilder<UUID>()
                .addAction(task -> reports.add(new TestTask(task, 3)), GLOBAL, 3)
                .sequentialOf(new TaskQueueBuilder<UUID>()
                        .addAction(task -> reports.add(new TestTask(task, 2)), GLOBAL, 2)
                        .sequentialOf(task1)
                        .sequentialOf(task2)
                        .addAction(task -> reports.add(new TestTask(task, 1)), LOCAL, 1))
                .sequentialOf(task3)
                .addAction(task -> reports.add(new TestTask(task, 0)), LOCAL, 0)
                .run();

        assertEquals(new TestTask(task1, 1), reports.get(0));
        assertEquals(new TestTask(task1, 2), reports.get(1));
        assertEquals(new TestTask(task1, 3), reports.get(2));

        assertEquals(new TestTask(task2, 1), reports.get(3));
        assertEquals(new TestTask(task2, 2), reports.get(4));
        assertEquals(new TestTask(task2, 3), reports.get(5));

        assertEquals(new TestTask(task3, 0), reports.get(6));
        assertEquals(new TestTask(task3, 3), reports.get(7));
    }

    @Test(expected = IllegalStateException.class)
    public void priorityCollisionTest() {
        new TaskQueueBuilder<UUID>()
                .addAction(TaskQueueBuilderTest::devNull, LOCAL, 0)
                .addAction(TaskQueueBuilderTest::devNull, LOCAL, 0)
                .sequentialOf(randomUUID())
                .run();
    }

    @Test(expected = IllegalStateException.class)
    public void hierarchicalPriorityCollisionTest() {
        new TaskQueueBuilder<UUID>()
                .addAction(TaskQueueBuilderTest::devNull, GLOBAL, 0)
                .sequentialOf(new TaskQueueBuilder<UUID>()
                        .addAction(TaskQueueBuilderTest::devNull, LOCAL, 0)
                        .sequentialOf(randomUUID()))
                .run();
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativePriorityTest() {
        new TaskQueueBuilder<UUID>()
                .addAction(TaskQueueBuilderTest::devNull, LOCAL, -1);
    }

    @Test
    public void invokeTest() {
        UUID task1 = randomUUID();
        UUID task2 = randomUUID();
        UUID task3 = randomUUID();
        List<UUID> reports = Collections.synchronizedList(new ArrayList<>());

        new TaskQueueBuilder<UUID>()
                .addAction(reports::add)
                .sequentialOf(task1)
                .sequentialOf(new TaskQueueBuilder<UUID>()
                        .invoke(() -> reports.add(task2))
                        .sequentialOf(task3))
                .run();

        assertTrue(checkOrder(reports, listOf(task1, task2, task3)));
    }

    private static class TestTask {

        private final UUID id;
        private final int priority;

        private TestTask(UUID id, int priority) {
            this.id = id;
            this.priority = priority;
        }

        public UUID getId() {
            return id;
        }

        public int getPriority() {
            return priority;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestTask testTask = (TestTask) o;
            return priority == testTask.priority &&
                    Objects.equals(id, testTask.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, priority);
        }

        @Override
        public String toString() {
            return "TestTask{" +
                    "id=" + id +
                    ", priority=" + priority +
                    '}';
        }
    }
}
