package taskeasy;

import student.TestCase;

import java.time.LocalDate;

// -------------------------------------------------------------------------
/**
 * Test Class for TaskList
 * 
 * @author Jonathan Yu
 * @version Sep 20, 2026
 */
public class TaskListTest extends TestCase
{
    // ~ Fields ................................................................
    private TaskList taskList;
    private Task task1;
    private Task task2;

    // ~ Constructors ..........................................................
    /**
     * sets up a new TaskList
     */
    public void setUp() {
        taskList = new TaskList();
        try {
            task1 = new Task("Task1", "First Task", LocalDate.now().plusDays(7), Priority.HIGH);
            task2 = new Task("Task2", "Second Task", LocalDate.now().plusDays(5), Priority.LOW);
        }
        
        catch (TaskException e){
            fail("Could not create task.");
        }
    }
    // ~Public Methods ........................................................

    /**
     * test TaskList
     */
    public void testTaskList() {
        assertEquals(0, taskList.size());
    }
    
    /**
     * test addTask() - valid input
     * @throws TaskException when input is invalid
     */
    public void testAddTask() throws TaskException
    {
        taskList.addTask(task1);
        assertEquals(1,taskList.size());
        assertEquals(task1, taskList.getTask(0));
    }
    
    /**
     * test addTask() - invalid inputs
     */
    
    public void testAddTask2() {
        Exception thrown;
        
        // null input
        try {
            taskList.addTask(null);
        }
        
        catch (TaskException e) {
            thrown = e;
            assertNotNull(thrown);
            assertEquals("Task cannot be null.", thrown.getMessage());
            
        }
        
        //duplicate tasks
        try {
            taskList.addTask(task1);
            
            Task duplicate = new Task("Task1", "First Task", LocalDate.now().plusDays(7), Priority.MEDIUM);
            
            taskList.addTask(duplicate);
            
        }
        
        catch (TaskException e){
            thrown = e;
            assertNotNull(thrown);
            assertEquals("Task name must be unique.", thrown.getMessage());
        }
    }
    
    /**
     * test getTask() - valid input
     */
    public void testGetTask() throws TaskException{
        taskList.addTask(task1);
        assertEquals(task1, taskList.getTask(0));
    }
    
    /**
     * test getTask() - invalid input
     */
    public void testGetTask2() {
        Exception thrown;
        
        try {
            taskList.addTask(task1);
            taskList.getTask(-1);
        }
        catch (TaskException e) {
            thrown = e;
            assertNotNull(thrown);
            assertEquals("Invalid index.", thrown.getMessage());
        }
        
        try {
            taskList.addTask(task2);
            taskList.getTask(2);
        }
        catch (TaskException e) {
            thrown = e;
            assertNotNull(thrown);
            assertEquals("Invalid index.", thrown.getMessage());
        }
        
    }
    
    /**
     * test removetask() - valid input
     * @throws TaskException when input is invalid
     */
    public void testRemoveTask() throws TaskException{
        taskList.addTask(task1);
        taskList.addTask(task2);
        
        taskList.removeTask(0);
        
        assertEquals(1, taskList.size());
        assertEquals(task2,taskList.getTask(0));
    }
    
    /**
     * test removeTask() - invalid inputs
     */
    public void testRemoveTask2() {
        Exception thrown;
        
        try {
            taskList.addTask(task1);
            taskList.removeTask(-1);
        }
        catch (TaskException e) {
            thrown = e;
            assertNotNull(thrown);
            assertEquals("Invalid index.", thrown.getMessage());
        }
        
        try {
            taskList.addTask(task2);
            taskList.removeTask(2);
        }
        catch (TaskException e) {
            thrown = e;
            assertNotNull(thrown);
            assertEquals("Invalid index.", thrown.getMessage());
        }
    }
    
    /**
     * test markComplete() - valid input
     */
    public void testMarkComplete() throws TaskException{
        taskList.addTask(task1);
        assertFalse(task1.isCompleted());
        
        taskList.markComplete(0);
        assertTrue(task1.isCompleted());
    }
    
    /**
     * test markComplete() - invalid inputs
     * 
     */
    public void testMarkComplete2() {
        Exception thrown;
        
        try {
            taskList.addTask(task1);
            taskList.markComplete(2);
        }
        catch (TaskException e) {
            thrown = e;
            assertNotNull(thrown);
            assertEquals("Invalid task index.", thrown.getMessage());
        }
        
        try {
            taskList.markComplete(-2);
        }
        catch (TaskException e) {
            thrown = e;
            assertNotNull(thrown);
            assertEquals("Invalid task index.", thrown.getMessage());
        }
    }
    
    /**
     * test editTask() - valid index
     * @throws TaskException when invalid input
     */
    public void testEditTask() throws TaskException{
        taskList.addTask(task1);
        
        LocalDate newDeadline = LocalDate.now().plusDays(10);
        
        taskList.editTask(0,"new name", "new description", newDeadline, Priority.MEDIUM);
        
        assertEquals("new name", task1.getName()); 
        assertEquals("new description", task1.getDescription()); 
        assertEquals(newDeadline, task1.getDeadline()); 
        assertEquals(Priority.MEDIUM, task1.getPriority());
        
    }
    /**
     * test editTask - valid input2
     * @throws TaskException when input is invalid
     */
    public void testEditTest1() throws TaskException {
        taskList.addTask(task1);
        taskList.addTask(task2);
        
        taskList.editTask(0,"Task1", "new description", LocalDate.now(), Priority.LOW);
        assertEquals("Task1",task1.getName());
        assertEquals("new description", task1.getDescription());
        
    }
    /**
     * test editTask - invalid inputs
     */
    public void testEditTask2() {
        Exception thrown;
        
        try { 
            taskList.editTask(-1, "new name", "new description", LocalDate.now(), Priority.HIGH); 
        }
        
        catch (TaskException e) {
            thrown = e;
            assertNotNull(thrown);
            assertEquals("Invalid task index.", thrown.getMessage());
               
        }
        
        try { 
            taskList.editTask(3, "new name", "new description", LocalDate.now(), Priority.HIGH); 
        }
        
        catch (TaskException e) {
            thrown = e;
            assertNotNull(thrown);
            assertEquals("Invalid task index.", thrown.getMessage());
               
        }
        
        try { 
            taskList.addTask(task1);
            taskList.addTask(task2);
            taskList.editTask(0, "Task2", "new description", LocalDate.now(), Priority.HIGH); 
            
        }
        
        catch (TaskException e) {
            thrown = e;
            assertNotNull(thrown);
            assertEquals("Task name must be unique.", thrown.getMessage());
               
        }
        
        
        
    }
}
