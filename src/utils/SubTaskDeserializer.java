package utils;

import com.google.gson.*;
import enums.TaskStatus;
import enums.TaskType;
import model.SubTask;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;

public class SubTaskDeserializer implements JsonDeserializer<SubTask> {
    @Override
    public SubTask deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();
        int epicId = jsonObject.get("epicId").getAsInt();
        SubTask subTask = new SubTask(name, description, epicId);
        if (jsonObject.has("type")) {
            TaskType taskType = TaskType.valueOf(jsonObject.get("type").getAsString());
            subTask.setType(taskType);
        }
        if (jsonObject.has("taskStatus")) {
            TaskStatus taskStatus = TaskStatus.valueOf(jsonObject.get("taskStatus").getAsString());
            subTask.setTaskStatus(taskStatus);
        }
        if (jsonObject.has("duration")) {
            subTask.setDuration(jsonDeserializationContext.deserialize(jsonObject.get("duration"), Duration.class));
        }
        if (jsonObject.has("startTime")) {
            subTask.setStartTime(jsonDeserializationContext.deserialize(jsonObject.get("startTime"), LocalDateTime.class));
        }
        return subTask;
    }
}
