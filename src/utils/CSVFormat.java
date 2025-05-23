package utils;

import enums.TaskStatus;
import enums.TaskType;
import model.Epic;
import model.SubTask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static utils.AppConstants.DATE_TIME_FORMATTER;

public class CSVFormat {

    public static Task fromString(String value) {
        String[] arrayTaskString = value.split(",");

        int id = Integer.parseInt(arrayTaskString[0]);
        TaskType taskType = TaskType.valueOf(arrayTaskString[1]);
        String name = arrayTaskString[2];
        String description = arrayTaskString[3];
        TaskStatus taskStatus = TaskStatus.valueOf(arrayTaskString[4]);
        Duration duration = Duration.ofMinutes(Integer.parseInt(arrayTaskString[5]));
        LocalDateTime startTime = LocalDateTime.parse(arrayTaskString[6], DATE_TIME_FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse(arrayTaskString[7], DATE_TIME_FORMATTER);

        switch (taskType) {
            case TASK_TYPE:
                Task task = new Task(name, description);
                task.setId(id);
                task.setTaskStatus(taskStatus);
                task.setDuration(duration);
                task.setStartTime(startTime);
                return task;
            case EPIC_TYPE:
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setTaskStatus(taskStatus);
                epic.setDuration(duration);
                epic.setEndTime(endTime);
                epic.setStartTime(startTime);
                if (arrayTaskString.length > 8) {
                    String[] arraySubTasksIds = arrayTaskString[8].split("/");
                    ArrayList<Integer> subTasksIdsFromString = new ArrayList<>();
                    for (String subTaskId : arraySubTasksIds) {
                        subTasksIdsFromString.add(Integer.parseInt(subTaskId));
                    }
                    epic.setSubTasksIds(subTasksIdsFromString);
                }
                epic.setId(id);
                return epic;
            case SUBTASK_TYPE:
                SubTask subTask = new SubTask(name, description, Integer.parseInt(arrayTaskString[8]));
                subTask.setId(id);
                subTask.setTaskStatus(taskStatus);
                subTask.setDuration(duration);
                subTask.setStartTime(startTime);
                return subTask;
            default:
                return null;
        }
    }

}
