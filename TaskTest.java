import student.TestCase;

import java.time.LocalDate;

/**
 * Test class for Task
 * 
 * @author Jonathan Yu
 * @version Sep 20, 2026
 */

public class TaskTest
    extends TestCase
{

    private Task task;
    private LocalDate deadline;

    /**
     * setUp the task
     */
    public void setUp()
    {
        deadline = LocalDate.now().plusDays(7);
        try
        {
            task =
                new Task("Project1", "Build TaskEasy", deadline, Priority.HIGH);
        }

        catch (TaskException e)
        {
            fail("Could not create task.");
        }
    }


    /**
     * test getName()
     */
    public void testGetName()
    {
        assertEquals(task.getName(), "Project1");
    }


    /**
     * test setName() - valid input
     * 
     * @throws TaskException
     */

    public void testSetName()
        throws TaskException
    {
        // valid input
        task.setName("Project TaskEasy");
        assertEquals(task.getName(), "Project TaskEasy");
    }


    /**
     * test setName() - invalid inputs
     */
    public void testSetName2()
    {
        // bad input - null
        Exception thrown;
        try
        {
            task.setName(null);
        }

        catch (TaskException e)
        {
            thrown = e;
            assertNotNull(thrown);
            assertEquals("Task name cannot be empty.", thrown.getMessage());
        }

        // bad input - empty name
        try
        {
            task.setName("");
        }

        catch (TaskException e)
        {
            thrown = e;
            assertNotNull(thrown);
            assertEquals("Task name cannot be empty.", thrown.getMessage());
        }

        // bad input - name too long
        try
        {
            String name = "";
            for (int i = 0; i <= 50; i++)
            {
                name += "n";
            }

            task.setName(name);
        }

        catch (TaskException e)
        {
            thrown = e;
            assertNotNull(thrown);
            assertEquals(
                "Task name cannot be longer than 50 characters.",
                thrown.getMessage());
        }

    }


    /**
     * test setDescription - valid input
     * 
     * @throws TaskException
     */
    public void testSetDescription()
        throws TaskException
    {
        task.setDescription("Review for Exam 1.");
        assertEquals("Review for Exam 1.", task.getDescription());
    }


    /**
     * test setDescription - invalid inputs
     */
    public void testSetDescription2()
    {
        Exception thrown;

        // null description
        try
        {
            task.setDescription(null);
        }
        catch (TaskException e)
        {
            thrown = e;
            assertNotNull(thrown);
            assertEquals(
                "Task description cannot be empty.",
                thrown.getMessage());

        }

        // empty description
        try
        {
            task.setDescription("");
        }
        catch (TaskException e)
        {
            thrown = e;
            assertNotNull(thrown);
            assertEquals(
                "Task description cannot be empty.",
                thrown.getMessage());
        }

        // description too long
        try
        {
            String des = "";
            for (int i = 0; i <= 200; i++)
            {
                des += "d";
            }

            task.setDescription(des);
        }

        catch (TaskException e)
        {
            thrown = e;
            assertNotNull(thrown);
            assertEquals(
                "Task description cannot be longer than 200 characters.",
                thrown.getMessage());
        }
    }


    /**
     * test getDeadline
     */
    public void testGetDeadline()
    {
        assertEquals(deadline, task.getDeadline());
    }


    /**
     * test setDeadline() - valid input
     * 
     * @throws TaskException
     */
    public void testSetDeadline()
        throws TaskException
    {
        LocalDate newDeadline = deadline.plusDays(7);
        task.setDeadline(newDeadline);
        assertEquals(newDeadline, task.getDeadline());
    }


    /**
     * test setDeadline() - invalid input
     */
    public void testSetDeadline2()
    {
        Exception thrown;
        // null
        try
        {
            task.setDeadline(null);
        }
        catch (TaskException e)
        {
            thrown = e;
            assertNotNull(thrown);
            assertEquals("Deadline cannot be null.", thrown.getMessage());
        }

    }


    /**
     * test getPriority()
     */
    public void testGetPriotity()
    {
        assertEquals(task.getPriority(), Priority.HIGH);
    }


    /**
     * test setPriority() - valid input
     * 
     * @throws TaskException
     */
    public void testSetPriority()
        throws TaskException
    {
        task.setPriority(Priority.MEDIUM);
        assertEquals(Priority.MEDIUM, task.getPriority());
    }


    /**
     * test setPriority() - invalid input
     */
    public void testSetPriority2()
    {
        Exception thrown;
        // null
        try
        {
            task.setPriority(null);
        }
        catch (TaskException e)
        {
            thrown = e;
            assertNotNull(thrown);
            assertEquals("Priority cannot be null.", thrown.getMessage());
        }

    }


    /**
     * test isCompleted()
     */
    public void testIsCompleted()
    {
        assertFalse(task.isCompleted());
    }


    /**
     * test setCompleted
     */
    public void testSetCompleted()
    {
        task.setCompleted(true);
        assertTrue(task.isCompleted());
    }


    /**
     * test isOverdue()
     * 
     * @throws TaskException
     */
    public void testIsOverdue()
        throws TaskException
    {
        assertFalse(task.isOverdue());

        // completed before deadline
        task.setCompleted(true);
        LocalDate newDeadline = deadline.minusDays(10);
        task.setDeadline(newDeadline);
        assertFalse(task.isOverdue());

        // past due
        task.setCompleted(false);
        assertTrue(task.isOverdue());
    }

}
