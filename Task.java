import java.time.LocalDate;

public class Task {
    private String name;
    private String description;
    private LocalDate deadline;
    private Priority priority;
    private boolean completed;


    public Task(String name, String description, LocalDate deadline, Priority priority) throws TaskException
    {
        this.setName(name);
        this.setDescription(description);
        this.setDeadline(deadline);
        this.setPriority(priority);
        this.setCompleted(false);
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name) throws TaskException
    {
        if(name==null || name.length() == 0)
        {
            throw new TaskException("Task name cannot be empty.");
        }

        if(name.length() > 50)
        {
            throw new TaskException("Task name cannot be longer than 50 characters.");
        }

        this.name = name;
    }


    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description) throws TaskException
    {
        if(description==null || description.length() == 0)
        {
            throw new TaskException("Task description cannot be empty.");
        }

        if(description.length() > 200)
        {
            throw new TaskException("Task description cannot be longer than 200 characters.");
        }

        this.description = description;
    }

    public LocalDate getDeadline()
    {
        return this.deadline;
    }

    public void setDeadline(LocalDate deadline) throws TaskException 
    {
        if (deadline == null)
        {
            throw new TaskException("Deadline cannot be null.");
        }

        this.deadline = deadline;
    }

    public Priority getPriority()
    {
        return this.priority;
    }

    public void setPriority(Priority p) throws TaskException
    {
        if (p == null)
        {
            throw new TaskException("Priority cannot be null.");
        }

        this.priority = p;
    }

    public boolean isCompleted()
    {
        return completed;
    }

    public void setCompleted(boolean c)
    {
        this.completed = c;
    }

    public boolean isOverdue()
    {
        return !completed && deadline.isBefore(LocalDate.now());
    }
}