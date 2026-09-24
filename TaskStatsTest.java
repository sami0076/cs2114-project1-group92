import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for TaskStats, following the Test Plan (Section 6).
 *
 * L4 contains four tasks: two completed, one incomplete due yesterday, and
 * one incomplete due tomorrow. Dates are relative to today so the tests
 * keep passing no matter when they are run.
 *
 * @author Raul
 */
public class TaskStatsTest {

    private TaskList l4;
    private TaskList empty;

    @BeforeEach
    public void setUp() {
        LocalDate today = LocalDate.now();

        l4 = new TaskList();
        Task done1 = new Task("Read chapter", "Chapter 3", today.minusDays(3), Priority.LOW);
        done1.setCompleted(true);
        Task done2 = new Task("Quiz", "Online quiz", today.plusDays(2), Priority.MEDIUM);
        done2.setCompleted(true);
        l4.addTask(done1);
        l4.addTask(done2);
        l4.addTask(new Task("Homework", "Finish lab", today.minusDays(1), Priority.HIGH));
        l4.addTask(new Task("Project", "Start design", today.plusDays(1), Priority.HIGH));

        empty = new TaskList();
    }

    // ---- Constructor ----

    @Test
    public void testConstructorAcceptsList() {
        assertDoesNotThrow(() -> new TaskStats(l4));
    }

    @Test
    public void testConstructorRejectsNull() {
        assertThrows(TaskException.class, () -> new TaskStats(null));
    }

    // ---- countCompleted ----

    @Test
    public void testCountCompleted() {
        assertEquals(2, new TaskStats(l4).countCompleted());
    }

    @Test
    public void testCountCompletedEmpty() {
        assertEquals(0, new TaskStats(empty).countCompleted());
    }

    // ---- countOverdue ----

    @Test
    public void testCountOverdue() {
        // Only "Homework" counts: "Read chapter" is past due but completed,
        // and "Project" is due tomorrow.
        assertEquals(1, new TaskStats(l4).countOverdue());
    }

    @Test
    public void testCountOverdueEmpty() {
        assertEquals(0, new TaskStats(empty).countOverdue());
    }

    // ---- completionRate ----

    @Test
    public void testCompletionRate() {
        assertEquals(0.5, new TaskStats(l4).completionRate(), 0.0001);
    }

    @Test
    public void testCompletionRateEmpty() {
        // No division by zero.
        assertEquals(0.0, new TaskStats(empty).completionRate(), 0.0001);
    }

    // ---- Live list ----

    @Test
    public void testStatsReflectLaterChanges() {
        TaskStats stats = new TaskStats(l4);
        l4.markComplete(2);
        assertEquals(3, stats.countCompleted());
        assertEquals(0, stats.countOverdue());
        assertEquals(0.75, stats.completionRate(), 0.0001);
    }
}