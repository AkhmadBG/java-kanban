package utils;

import enums.TaskStatus;
import enums.TaskType;
import managers.FileBackedTaskManager;
import model.Epic;
import model.SubTask;
import model.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVFormat {

    public static Task fromString(String value) {
        String[] arrayTaskString = value.split(",");

        int id = Integer.parseInt(arrayTaskString[0]);
        TaskType taskType = TaskType.valueOf(arrayTaskString[1]);
        String name = arrayTaskString[2];
        String description = arrayTaskString[3];
        TaskStatus taskStatus = TaskStatus.valueOf(arrayTaskString[4]);

        switch (taskType) {
            case TASK_TYPE:
                Task task = new Task(name, description, taskStatus);
                task.setId(id);
                return task;
            case EPIC_TYPE:
                Epic epic = new Epic(name, description, taskStatus);
                epic.setId(id);
                if (arrayTaskString.length > 5) {
                    String[] arraySubTasksIds = arrayTaskString[5].split("/");
                    ArrayList<Integer> subTasksIdsFromString = new ArrayList<>();
                    for (String subTaskId : arraySubTasksIds) {
                        subTasksIdsFromString.add(Integer.parseInt(subTaskId));
                    }
                    epic.setSubTasksIds(subTasksIdsFromString);
                }
                epic.setId(id);
                return epic;
            case SUBTASK_TYPE:
                SubTask subTask = new SubTask(name, description, taskStatus,
                        Integer.parseInt(arrayTaskString[5]));
                subTask.setId(id);
                return subTask;
            default:
                return null;
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager;
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            fileBackedTaskManager = new FileBackedTaskManager(file);
            while (bufferedReader.ready()) {
                String taskString = bufferedReader.readLine();
                if (!taskString.equals("id,type,name,description,status,epic")) {
                    String[] arrayTaskString = taskString.split(",");
                    switch (arrayTaskString[1]) {
                        case "TASK_TYPE":
                            fileBackedTaskManager.getTasks().put(Integer.parseInt(arrayTaskString[0]),
                                    fromString(taskString));
                            break;
                        case "EPIC_TYPE":
                            fileBackedTaskManager.getEpics().put(Integer.parseInt(arrayTaskString[0]),
                                    (Epic) fromString(taskString));
                            break;
                        case "SUBTASK_TYPE":
                            fileBackedTaskManager.getSubTasks().put(Integer.parseInt(arrayTaskString[0]),
                                    (SubTask) fromString(taskString));
                            break;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileBackedTaskManager;
    }

}
