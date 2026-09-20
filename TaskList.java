import java.util.ArrayList;
import java.time.LocalDate;

public class TaskList
{
    private ArrayList<Task> tasks;

    public TaskList()
    {
        tasks = new ArrayList<Task> ();
    }

    public void addTask(Task task) throws TaskException{
        if (task == null)
        {
            throw new TaskException("Task cannot be null.");
        }

        if (this.findTask(task.getName()) != null)
        {
            throw new TaskException("Task name must be unique.");
        }

        this.tasks.add(task);
    }

    public void removeTask(int index) throws TaskException
    {
        if (index < 0 || index >= tasks.size())
        {
            throw new TaskException("Invalid index.");
        }

        tasks.remove(index);
    }

    public Task getTask(int index) throws TaskException{
        if (index < 0 || index >= tasks.size())
        {
            throw new TaskException("Invalid index.");
        }

        return tasks.get(index);
    }

    public int size()
    {
        return tasks.size();
    }

    public Task findTask(String name)
    {
        for(Task task : tasks)
        {
            if (task.getName().equals(name))
            {
                return task;
            }
        }

        return null;
    }

    public void markComplete(int index) throws TaskException
    {
        if (index < 0 || index >= tasks.size())
        {
            throw new TaskException("Invalid task index.");
        }

        tasks.get(index).setCompleted(true);
    }

    public void editTask(int index, String name, String description,
        LocalDate deadline, Priority priority) throws TaskException
    {
        if (index < 0 || index >= tasks.size())
        {
            throw new TaskException("Invalid task index.");
        }

        Task task = tasks.get(index);

        if (!task.getName().equals(name) && findTask(name) != null)
        {
            throw new TaskException("Task name must be unique.");
        }

        task.setName(name);
        task.setDescription(description);
        task.setDeadline(deadline);
        task.setPriority(priority);
    }
}