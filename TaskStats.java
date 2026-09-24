/**
 * Calculates read-only statistics about a TaskList.
 *
 * TaskStats keeps a reference to the live list rather than a copy, so every
 * call reflects the tasks as they are right now. It never changes any task.
 *
 * @author Raul
 */
public class TaskStats {

    private final TaskList list;

    /**
     * Creates a statistics calculator for the given list.
     *
     * @param list the task list to report on
     * @throws TaskException if list is null
     */
    public TaskStats(TaskList list) {
        if (list == null) {
            throw new TaskException("Task list cannot be null.");
        }
        this.list = list;
    }

    /**
     * Counts the tasks that are marked completed.
     *
     * @return the number of completed tasks (0 for an empty list)
     */
    public int countCompleted() {
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.getTask(i).isCompleted()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Counts the tasks that are overdue: incomplete with a deadline before
     * today. The rule itself lives in Task.isOverdue().
     *
     * @return the number of overdue tasks (0 for an empty list)
     */
    public int countOverdue() {
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.getTask(i).isOverdue()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Calculates the fraction of tasks that are completed.
     *
     * @return completed / total, from 0.0 to 1.0; 0.0 for an empty list
     */
    public double completionRate() {
        int total = list.size();
        if (total == 0) {
            return 0.0;
        }
        return (double) countCompleted() / total;
    }
}