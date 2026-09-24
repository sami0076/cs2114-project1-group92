package taskeasy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskEasyTest {

    private static Path saveFile;

    @Autowired
    private MockMvc mvc;

    @DynamicPropertySource
    static void useTempFile(DynamicPropertyRegistry registry) throws Exception {
        saveFile = Files.createTempFile("taskeasy", ".json");
        Files.delete(saveFile);
        registry.add("taskeasy.file", () -> saveFile.toString());
    }

    @BeforeEach
    public void clearList() throws Exception {
        while (!mvc.perform(get("/tasks")).andReturn().getResponse().getContentAsString().equals("[]")) {
            mvc.perform(delete("/tasks/0"));
        }
    }

    private String taskJson(String name, String description, String deadline, String priority) {
        return "{\"name\":\"" + name + "\",\"description\":\"" + description
            + "\",\"deadline\":\"" + deadline + "\",\"priority\":\"" + priority + "\"}";
    }

    private ResultActions send(String path, String json, boolean create) throws Exception {
        var request = create ? post(path) : put(path);
        return mvc.perform(request.contentType(MediaType.APPLICATION_JSON).content(json));
    }

    private void addHomework() throws Exception {
        send("/tasks", taskJson("Homework", "Finish lab", "2026-09-20", "HIGH"), true)
            .andExpect(status().isCreated());
    }

    private String savedFile() throws Exception {
        return Files.readString(saveFile);
    }

    @Test
    public void testGetTasksEmpty() throws Exception {
        mvc.perform(get("/tasks"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    }

    @Test
    public void testGetTasksWithHomework() throws Exception {
        addHomework();
        mvc.perform(get("/tasks"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].name").value("Homework"))
            .andExpect(jsonPath("$[0].description").value("Finish lab"))
            .andExpect(jsonPath("$[0].deadline").value("2026-09-20"))
            .andExpect(jsonPath("$[0].priority").value("HIGH"))
            .andExpect(jsonPath("$[0].completed").value(false));
    }

    @Test
    public void testCreateTask() throws Exception {
        send("/tasks", taskJson("Lab", "Write code", "2026-09-25", "LOW"), true)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Lab"))
            .andExpect(jsonPath("$.completed").value(false));
        assertTrue(savedFile().contains("\"Lab\""));
    }

    @Test
    public void testCreateTaskEmptyName() throws Exception {
        send("/tasks", taskJson("", "Write code", "2026-09-25", "LOW"), true)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").isString());
        assertFalse(Files.exists(saveFile) && savedFile().contains("Write code"));
        mvc.perform(get("/tasks")).andExpect(content().json("[]"));
    }

    @Test
    public void testCreateTaskBadDeadline() throws Exception {
        send("/tasks", taskJson("Lab", "Write code", "2026-13-45", "LOW"), true)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Deadline must be a date in yyyy-MM-dd format."));
    }

    @Test
    public void testCreateTaskBadPriority() throws Exception {
        send("/tasks", taskJson("Lab", "Write code", "2026-09-25", "URGENT"), true)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Priority must be LOW, MEDIUM, or HIGH."));
    }

    @Test
    public void testCreateTaskDuplicateName() throws Exception {
        addHomework();
        send("/tasks", taskJson("Homework", "Again", "2026-09-25", "LOW"), true)
            .andExpect(status().isBadRequest());
        mvc.perform(get("/tasks")).andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testCreateTaskBadJson() throws Exception {
        send("/tasks", "not json", true)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").isString());
    }

    @Test
    public void testUpdateTask() throws Exception {
        addHomework();
        send("/tasks/0", taskJson("Finish Homework", "Finish lab", "2026-09-20", "HIGH"), false)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Finish Homework"));
        assertTrue(savedFile().contains("Finish Homework"));
    }

    @Test
    public void testUpdateTaskBadIndex() throws Exception {
        addHomework();
        send("/tasks/9", taskJson("Other", "Other", "2026-09-20", "HIGH"), false)
            .andExpect(status().isBadRequest());
        mvc.perform(get("/tasks")).andExpect(jsonPath("$[0].name").value("Homework"));
    }

    @Test
    public void testUpdateTaskNonNumericIndex() throws Exception {
        send("/tasks/abc", taskJson("Other", "Other", "2026-09-20", "HIGH"), false)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Task index must be a whole number."));
    }

    @Test
    public void testDeleteTask() throws Exception {
        addHomework();
        mvc.perform(delete("/tasks/0")).andExpect(status().isOk());
        mvc.perform(get("/tasks")).andExpect(content().json("[]"));
        assertEquals("[ ]", savedFile().trim());
    }

    @Test
    public void testDeleteTaskBadIndex() throws Exception {
        addHomework();
        mvc.perform(delete("/tasks/9")).andExpect(status().isBadRequest());
        mvc.perform(get("/tasks")).andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testCompleteTask() throws Exception {
        addHomework();
        mvc.perform(patch("/tasks/0/complete"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.completed").value(true));
        assertTrue(savedFile().contains("\"completed\" : true"));
    }

    @Test
    public void testCompleteTaskBadIndex() throws Exception {
        mvc.perform(patch("/tasks/9/complete")).andExpect(status().isBadRequest());
    }

    @Test
    public void testStatsEmpty() throws Exception {
        mvc.perform(get("/stats"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.completed").value(0))
            .andExpect(jsonPath("$.overdue").value(0))
            .andExpect(jsonPath("$.completionRate").value(0.0));
    }

    @Test
    public void testStatsWithFourTasks() throws Exception {
        String yesterday = LocalDate.now().minusDays(1).toString();
        String tomorrow = LocalDate.now().plusDays(1).toString();
        send("/tasks", taskJson("A", "done", tomorrow, "LOW"), true);
        send("/tasks", taskJson("B", "done", tomorrow, "LOW"), true);
        send("/tasks", taskJson("C", "late", yesterday, "LOW"), true);
        send("/tasks", taskJson("D", "soon", tomorrow, "LOW"), true);
        mvc.perform(patch("/tasks/0/complete"));
        mvc.perform(patch("/tasks/1/complete"));
        mvc.perform(get("/stats"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.completed").value(2))
            .andExpect(jsonPath("$.overdue").value(1))
            .andExpect(jsonPath("$.completionRate").value(0.5));
    }
}
