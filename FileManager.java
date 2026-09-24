import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Saves a TaskList to, and loads it from, a JSON save file.
 *
 * The file holds a JSON array; each object has name, description,
 * deadline (ISO yyyy-MM-dd), priority, and completed. Loading rebuilds every
 * task through the Task constructor and TaskList.addTask, so data read from
 * the file passes the same validation as data typed by the user.
 *
 * @author Raul
 */
public class FileManager {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final String filePath;

    /**
     * Stores the path of the save file. The file is not opened or created.
     *
     * @param filePath path of the JSON save file, e.g. "tasks.json"
     * @throws TaskException if filePath is null
     */
    public FileManager(String filePath) {
        if (filePath == null) {
            throw new TaskException("File path cannot be null.");
        }
        this.filePath = filePath;
    }

    /**
     * Writes every task in the list to the save file as JSON, replacing
     * whatever the file contained before.
     *
     * @param list the tasks to save
     * @throws TaskException if list is null or the file cannot be written
     */
    public void save(TaskList list) {
        if (list == null) {
            throw new TaskException("Task list cannot be null.");
        }

        List<Map<String, Object>> data = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Task task = list.getTask(i);
            // LinkedHashMap keeps the fields in this order in the file.
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("name", task.getName());
            entry.put("description", task.getDescription());
            entry.put("deadline", task.getDeadline().toString()); // yyyy-MM-dd
            entry.put("priority", task.getPriority().name());
            entry.put("completed", task.isCompleted());
            data.add(entry);
        }

        try {
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), data);
        }
        catch (IOException e) {
            throw new TaskException("Could not save tasks to " + filePath + ".");
        }
    }

    /**
     * Builds a new TaskList from the save file.
     *
     * @return the tasks in the file, in order; an empty TaskList if the file
     *         does not exist yet
     * @throws TaskException if the file cannot be read or contains invalid
     *                       task data
     */
    public TaskList loadTasks() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new TaskList(); // first run: nothing saved yet
        }

        JsonNode root;
        try {
            root = MAPPER.readTree(file);
        }
        catch (IOException e) {
            throw new TaskException("Could not read " + filePath + ".");
        }
        if (root == null || !root.isArray()) {
            throw new TaskException("Save file must contain a list of tasks.");
        }

        TaskList list = new TaskList();
        for (int i = 0; i < root.size(); i++) {
            try {
                list.addTask(toTask(root.get(i)));
            }
            catch (TaskException e) {
                throw new TaskException("Invalid task " + (i + 1) + " in "
                    + filePath + ": " + e.getMessage());
            }
        }
        return list;
    }

    /**
     * Converts one JSON object into a Task. The Task constructor performs
     * the name, description, deadline, and priority validation.
     */
    private Task toTask(JsonNode node) {
        if (node == null || !node.isObject()) {
            throw new TaskException("Each task must be a JSON object.");
        }

        String name = readText(node, "name");
        String description = readText(node, "description");

        LocalDate deadline;
        try {
            deadline = LocalDate.parse(readText(node, "deadline"));
        }
        catch (DateTimeParseException e) {
            throw new TaskException("Deadline must be a date in yyyy-MM-dd format.");
        }

        Priority priority;
        try {
            priority = Priority.valueOf(readText(node, "priority"));
        }
        catch (IllegalArgumentException e) {
            throw new TaskException("Priority must be LOW, MEDIUM, or HIGH.");
        }

        JsonNode completed = node.get("completed");
        if (completed == null || !completed.isBoolean()) {
            throw new TaskException("Field \"completed\" must be true or false.");
        }

        Task task = new Task(name, description, deadline, priority);
        task.setCompleted(completed.asBoolean());
        return task;
    }

    /**
     * Returns a required text field, or throws if it is missing or not text.
     */
    private String readText(JsonNode node, String field) {
        JsonNode value = node.get(field);
        if (value == null || !value.isTextual()) {
            throw new TaskException("Field \"" + field + "\" is missing or not text.");
        }
        return value.asText();
    }
}