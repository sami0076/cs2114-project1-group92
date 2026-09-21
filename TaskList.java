import java.util.ArrayList;
import java.time.LocalDate;

// -------------------------------------------------------------------------
/**
 *  This is a class of TaskList
 *  @author Jonathan Yu
 *  @version Sep 17, 2026
 */
public class TaskList
{
    private ArrayList<Task> tasks;

    /**
     * constructor
     */
    public TaskList()
    {
        tasks = new ArrayList<Task> ();
    }
    
    /**
     * adds a task to the list
     * @param task the task we want to add
     * @throws TaskException when input is invalid
     * 
     */
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

    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     * @param index of the task we want to remove
     * @throws TaskException when input is invalid
     */
    public void removeTask(int index) throws TaskException
    {
        if (index < 0 || index >= tasks.size())
        {
            throw new TaskException("Invalid index.");
        }

        tasks.remove(index);
    }

    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     * @param index of the task we want to receive
     * @return the task
     * @throws TaskException when input is invalid
     */
    public Task getTask(int index) throws TaskException{
        if (index < 0 || index >= tasks.size())
        {
            throw new TaskException("Invalid index.");
        }

        return tasks.get(index);
    }

    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     * @return the size of the list
     */
    public int size()
    {
        return tasks.size();
    }

    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     * @param name the name of the task we want to find
     * @return the task with the task name
     */
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

    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     * @param index of the task we want to mark complete
     * @throws TaskException when the input is not valid
     */
    public void markComplete(int index) throws TaskException
    {
        if (index < 0 || index >= tasks.size())
        {
            throw new TaskException("Invalid task index.");
        }

        tasks.get(index).setCompleted(true);
    }

    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     * @param index of the task we want to edit
     * @param name the new name
     * @param description the new description
     * @param deadline the new deadline
     * @param priority the new priority
     * @throws TaskException when the input is invalid
     */
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