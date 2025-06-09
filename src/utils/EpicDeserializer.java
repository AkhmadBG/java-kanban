package utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import enums.TaskStatus;
import enums.TaskType;
import model.Epic;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EpicDeserializer implements JsonDeserializer<Epic> {
    @Override
    public Epic deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();
        Epic epic = new Epic(name, description);
        if (jsonObject.has("type")) {
            TaskType taskType = TaskType.valueOf(jsonObject.get("type").getAsString());
            epic.setType(taskType);
        }
        if (jsonObject.has("taskStatus")) {
            TaskStatus taskStatus = TaskStatus.valueOf(jsonObject.get("taskStatus").getAsString());
            epic.setTaskStatus(taskStatus);
        }
        if (jsonObject.has("duration")) {
            epic.setDuration(jsonDeserializationContext.deserialize(jsonObject.get("duration"), Duration.class));
        }
        if (jsonObject.has("startTime")) {
            epic.setStartTime(jsonDeserializationContext.deserialize(jsonObject.get("startTime"), LocalDateTime.class));
        }
        if (jsonObject.has("subTasksIds")) {
            Type listType = new TypeToken<List<Integer>>() {
            }.getType();
            ArrayList<Integer> subTasksIds = jsonDeserializationContext.deserialize(jsonObject.get("subTasksIds"), listType);
            epic.setSubTasksIds(subTasksIds);
        }
        return epic;
    }
}
