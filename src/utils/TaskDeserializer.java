package utils;

import com.google.gson.*;
import enums.TaskStatus;
import enums.TaskType;
import model.Task;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;

public class TaskDeserializer implements JsonDeserializer<Task> {
    @Override
    public Task deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();
        Task task = new Task(name, description);
        if (jsonObject.has("type")) {
            TaskType taskType = TaskType.valueOf(jsonObject.get("type").getAsString());
            task.setType(taskType);
        }
        if (jsonObject.has("taskStatus")) {
            TaskStatus taskStatus = TaskStatus.valueOf(jsonObject.get("taskStatus").getAsString());
            task.setTaskStatus(taskStatus);
        }
        if (jsonObject.has("duration")) {
            task.setDuration(jsonDeserializationContext.deserialize(jsonObject.get("duration"), Duration.class));
        }
        if (jsonObject.has("startTime")) {
            task.setStartTime(jsonDeserializationContext.deserialize(jsonObject.get("startTime"), LocalDateTime.class));
        }
        return task;
    }
}
