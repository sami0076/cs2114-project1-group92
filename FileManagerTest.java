import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for FileManager, following the Test Plan (Section 6).
 *
 * Every test works inside a fresh temporary folder that JUnit creates and
 * deletes, so no test touches the real tasks.json.
 *
 * @author Raul
 */
public class FileManagerTest {

    @TempDir
    Path dir;

    private TaskList twoTasks; // L plus "Lab"

    @BeforeEach
    public void setUp() {
        twoTasks = new TaskList();
        twoTasks.addTask(new Task("Homework", "Finish lab",
            LocalDate.of(2026, 9, 20), Priority.HIGH));
        Task lab = new Task("Lab", "Write code", LocalDate.of(2026, 9, 25), Priority.LOW);
        lab.setCompleted(true);
        twoTasks.addTask(lab);
    }

    /** Writes text to a file in the temp folder and returns its path. */
    private String writeFile(String name, String contents) throws IOException {
        Path file = dir.resolve(name);
        Files.writeString(file, contents);
        return file.toString();
    }

    /** A valid one-task JSON object, with one field swapped in for testing. */
    private String taskJson(String name, String deadline, String priority) {
        return "{\"name\":" + name + ",\"description\":\"Finish lab\","
            + "\"deadline\":" + deadline + ",\"priority\":" + priority
            + ",\"completed\":false}";
    }

    // ---- Constructor ----

    @Test
    public void testConstructorDoesNotCreateFile() {
        Path file = dir.resolve("tasks.json");
        new FileManager(file.toString());
        assertFalse(Files.exists(file));
    }

    @Test
    public void testConstructorRejectsNull() {
        assertThrows(TaskException.class, () -> new FileManager(null));
    }

    // ---- save ----

    @Test
    public void testSaveWritesAllTasks() throws IOException {
        Path file = dir.resolve("tasks.json");
        new FileManager(file.toString()).save(twoTasks);

        String json = Files.readString(file);
        assertTrue(json.contains("\"Homework\""));
        assertTrue(json.contains("\"Lab\""));
        assertTrue(json.contains("\"2026-09-20\""));
        assertTrue(json.contains("\"HIGH\""));
    }

    @Test
    public void testSaveReplacesOldContents() throws IOException {
        Path file = dir.resolve("tasks.json");
        FileManager files = new FileManager(file.toString());
        files.save(twoTasks);
        files.save(new TaskList());
        assertEquals(0, files.loadTasks().size());
    }

    @Test
    public void testSaveUnwritablePath() throws IOException {
        // A path "inside" a regular file can never be written, on any OS
        // and even with administrator rights.
        String blocker = writeFile("notAFolder", "x");
        FileManager files = new FileManager(blocker + "/tasks.json");
        assertThrows(TaskException.class, () -> files.save(twoTasks));
    }

    @Test
    public void testSaveRejectsNull() {
        FileManager files = new FileManager(dir.resolve("tasks.json").toString());
        assertThrows(TaskException.class, () -> files.save(null));
    }

    // ---- loadTasks ----

    @Test
    public void testLoadRoundTrip() {
        FileManager files = new FileManager(dir.resolve("tasks.json").toString());
        files.save(twoTasks);

        TaskList loaded = files.loadTasks();
        assertEquals(2, loaded.size());

        Task first = loaded.getTask(0);
        assertEquals("Homework", first.getName());
        assertEquals("Finish lab", first.getDescription());
        assertEquals(LocalDate.of(2026, 9, 20), first.getDeadline());
        assertEquals(Priority.HIGH, first.getPriority());
        assertFalse(first.isCompleted());

        Task second = loaded.getTask(1);
        assertEquals("Lab", second.getName());
        assertEquals(Priority.LOW, second.getPriority());
        assertTrue(second.isCompleted());
    }

    @Test
    public void testLoadMissingFileGivesEmptyList() {
        FileManager files = new FileManager(dir.resolve("missing.json").toString());
        assertEquals(0, files.loadTasks().size());
    }

    @Test
    public void testLoadEmptyArray() throws IOException {
        FileManager files = new FileManager(writeFile("tasks.json", "[]"));
        assertEquals(0, files.loadTasks().size());
    }

    @Test
    public void testLoadBlankName() throws IOException {
        String path = writeFile("tasks.json",
            "[" + taskJson("\"\"", "\"2026-09-20\"", "\"HIGH\"") + "]");
        assertThrows(TaskException.class, () -> new FileManager(path).loadTasks());
    }

    @Test
    public void testLoadBadDate() throws IOException {
        String path = writeFile("tasks.json",
            "[" + taskJson("\"Homework\"", "\"2026-13-45\"", "\"HIGH\"") + "]");
        assertThrows(TaskException.class, () -> new FileManager(path).loadTasks());
    }

    @Test
    public void testLoadBadPriority() throws IOException {
        String path = writeFile("tasks.json",
            "[" + taskJson("\"Homework\"", "\"2026-09-20\"", "\"URGENT\"") + "]");
        assertThrows(TaskException.class, () -> new FileManager(path).loadTasks());
    }

    @Test
    public void testLoadMissingField() throws IOException {
        String path = writeFile("tasks.json",
            "[{\"name\":\"Homework\",\"description\":\"Finish lab\"}]");
        assertThrows(TaskException.class, () -> new FileManager(path).loadTasks());
    }

    @Test
    public void testLoadDuplicateNames() throws IOException {
        String task = taskJson("\"Homework\"", "\"2026-09-20\"", "\"HIGH\"");
        String path = writeFile("tasks.json", "[" + task + "," + task + "]");
        assertThrows(TaskException.class, () -> new FileManager(path).loadTasks());
    }

    @Test
    public void testLoadNotJson() throws IOException {
        String path = writeFile("tasks.json", "this is not json");
        assertThrows(TaskException.class, () -> new FileManager(path).loadTasks());
    }

    @Test
    public void testLoadNotAnArray() throws IOException {
        String path = writeFile("tasks.json",
            taskJson("\"Homework\"", "\"2026-09-20\"", "\"HIGH\""));
        assertThrows(TaskException.class, () -> new FileManager(path).loadTasks());
    }
}