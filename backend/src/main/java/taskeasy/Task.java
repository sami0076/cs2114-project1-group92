package taskeasy;

import java.time.LocalDate;

// -------------------------------------------------------------------------
/**
 * Task class
 * 
 * @author Jonathan Yu
 * @version Sep 17, 2026
 */
public class Task
{
    private String name;
    private String description;
    private LocalDate deadline;
    private Priority priority;
    private boolean completed;

    // ----------------------------------------------------------
    /**
     * Create a new Task object.
     * 
     * @param name
     *            the name of the task
     * @param description
     *            the description of the task
     * @param deadline
     *            the deadline of the task
     * @param priority
     *            the priority of the task
     * @throws TaskException
     *             the exception thrown when input is invalid
     */
    public Task(
        String name,
        String description,
        LocalDate deadline,
        Priority priority)
        throws TaskException
    {
        this.setName(name);
        this.setDescription(description);
        this.setDeadline(deadline);
        this.setPriority(priority);
        this.setCompleted(false);
    }


    // ----------------------------------------------------------
    /**
     * getName() method
     * 
     * @return the name of the task
     */
    public String getName()
    {
        return this.name;
    }


    // ----------------------------------------------------------
    /**
     * set new name to a task
     * 
     * @param name
     *            the new name
     * @throws TaskException
     *             thrown when input is invalid
     */
    public void setName(String name)
        throws TaskException
    {
        if (name == null || name.length() == 0)
        {
            throw new TaskException("Task name cannot be empty.");
        }

        if (name.length() > 50)
        {
            throw new TaskException(
                "Task name cannot be longer than 50 characters.");
        }

        this.name = name;
    }


    // ----------------------------------------------------------
    /**
     * get the description of the task
     * 
     * @return the description of the task
     */
    public String getDescription()
    {
        return this.description;
    }


    // ----------------------------------------------------------
    /**
     * Update the description of a task
     * 
     * @param description
     *            the new description
     * @throws TaskException
     *             thrown when input is invalid
     */
    public void setDescription(String description)
        throws TaskException
    {
        if (description == null || description.length() == 0)
        {
            throw new TaskException("Task description cannot be empty.");
        }

        if (description.length() > 200)
        {
            throw new TaskException(
                "Task description cannot be longer than 200 characters.");
        }

        this.description = description;
    }


    // ----------------------------------------------------------
    /**
     * shows the deadline of a task
     * 
     * @return the deadline of a task
     */
    public LocalDate getDeadline()
    {
        return this.deadline;
    }


    // ----------------------------------------------------------
    /**
     * update the deadline of a task
     * 
     * @param deadline
     *            the new deadline
     * @throws TaskException
     *             thrown when input is invalid
     */
    public void setDeadline(LocalDate deadline)
        throws TaskException
    {
        if (deadline == null)
        {
            throw new TaskException("Deadline cannot be null.");
        }

        this.deadline = deadline;
    }


    // ----------------------------------------------------------
    /**
     * Get the priority of a task
     * 
     * @return the priority of the task
     */
    public Priority getPriority()
    {
        return this.priority;
    }


    // ----------------------------------------------------------
    /**
     * Update the priority of the task
     * 
     * @param p
     *            the new priority level
     * @throws TaskException
     *             thrown when input is invalid
     */
    public void setPriority(Priority p)
        throws TaskException
    {
        if (p == null)
        {
            throw new TaskException("Priority cannot be null.");
        }

        this.priority = p;
    }


    // ----------------------------------------------------------
    /**
     * tells if the task is completed
     * 
     * @return the completion status of the task
     */
    public boolean isCompleted()
    {
        return completed;
    }


    // ----------------------------------------------------------
    /**
     * update the completion status of a task
     * 
     * @param c
     *            the new completion status
     */
    public void setCompleted(boolean c)
    {
        this.completed = c;
    }


    // ----------------------------------------------------------
    /**
     * tells if the task is past-due
     * 
     * @return if the task is overdue
     */
    public boolean isOverdue()
    {
        return !completed && deadline.isBefore(LocalDate.now());
    }
}
