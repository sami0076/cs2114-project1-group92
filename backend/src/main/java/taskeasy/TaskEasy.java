package taskeasy;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@SpringBootApplication
@RestController
public class TaskEasy {

    public record TaskRequest(String name, String description, String deadline, String priority) {
    }

    public record TaskView(String name, String description, String deadline, String priority, boolean completed) {
    }

    private final TaskList list;
    private final FileManager files;
    private final TaskStats stats;

    public TaskEasy(@Value("${taskeasy.file:tasks.json}") String filePath) {
        files = new FileManager(filePath);
        list = files.loadTasks();
        stats = new TaskStats(list);
    }

    public static void main(String[] args) {
        SpringApplication.run(TaskEasy.class, args);
    }

    @GetMapping("/tasks")
    public synchronized List<TaskView> getTasks() {
        List<TaskView> views = new ArrayList<>();
        for (Task task : list.getAllTasks()) {
            views.add(toView(task));
        }
        return views;
    }

    @PostMapping("/tasks")
    public synchronized ResponseEntity<TaskView> createTask(@RequestBody TaskRequest request) {
        Task task = new Task(request.name(), request.description(),
            parseDeadline(request.deadline()), parsePriority(request.priority()));
        list.addTask(task);
        files.save(list);
        return ResponseEntity.status(HttpStatus.CREATED).body(toView(task));
    }

    @PutMapping("/tasks/{index}")
    public synchronized TaskView updateTask(@PathVariable int index, @RequestBody TaskRequest request) {
        list.editTask(index, request.name(), request.description(),
            parseDeadline(request.deadline()), parsePriority(request.priority()));
        files.save(list);
        return toView(list.getTask(index));
    }

    @DeleteMapping("/tasks/{index}")
    public synchronized void deleteTask(@PathVariable int index) {
        list.removeTask(index);
        files.save(list);
    }

    @PatchMapping("/tasks/{index}/complete")
    public synchronized TaskView completeTask(@PathVariable int index) {
        list.markComplete(index);
        files.save(list);
        return toView(list.getTask(index));
    }

    @GetMapping("/stats")
    public synchronized Map<String, Object> getStats() {
        return Map.of(
            "completed", stats.countCompleted(),
            "overdue", stats.countOverdue(),
            "completionRate", stats.completionRate());
    }

    @ExceptionHandler({TaskException.class, DateTimeParseException.class, IllegalArgumentException.class})
    public ResponseEntity<Map<String, String>> handleBadInput(Exception e) {
        return badRequest(e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleBadBody(HttpMessageNotReadableException e) {
        return badRequest("Request body must be JSON with name, description, deadline, and priority.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleBadIndex(MethodArgumentTypeMismatchException e) {
        return badRequest("Task index must be a whole number.");
    }

    private ResponseEntity<Map<String, String>> badRequest(String message) {
        return ResponseEntity.badRequest().body(Map.of("message", message));
    }

    private LocalDate parseDeadline(String text) {
        if (text == null || text.isBlank()) {
            throw new TaskException("Deadline is required.");
        }
        try {
            return LocalDate.parse(text);
        }
        catch (DateTimeParseException e) {
            throw new TaskException("Deadline must be a date in yyyy-MM-dd format.");
        }
    }

    private Priority parsePriority(String text) {
        if (text == null || text.isBlank()) {
            throw new TaskException("Priority is required.");
        }
        try {
            return Priority.valueOf(text.trim().toUpperCase());
        }
        catch (IllegalArgumentException e) {
            throw new TaskException("Priority must be LOW, MEDIUM, or HIGH.");
        }
    }

    private TaskView toView(Task task) {
        return new TaskView(task.getName(), task.getDescription(),
            task.getDeadline().toString(), task.getPriority().name(), task.isCompleted());
    }
}
