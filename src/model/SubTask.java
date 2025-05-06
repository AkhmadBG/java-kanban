package model;

import enums.TaskStatus;
import enums.TaskType;

public class SubTask extends Task {

    private int epicId;

    public SubTask(TaskType type, String name, String description, int epicId) {
        super(type, name, description);
        this.epicId = epicId;
    }

    public SubTask(TaskType type, String name, String description, TaskStatus taskStatus, int epicId) {
        super(type, name, description, taskStatus);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return super.getId() +
                "," + super.getType() +
                "," + super.getName() +
                "," + super.getDescription() +
                "," + super.getTaskStatus() +
                "," + epicId;
    }

}
