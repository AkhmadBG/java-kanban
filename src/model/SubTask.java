package model;

import enums.TaskStatus;

import static enums.TaskType.SUBTASK_TYPE;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String name, String description, int epicId) {
        super(name, description);
        this.type = SUBTASK_TYPE;
        this.epicId = epicId;
    }

    public SubTask(String name, String description, TaskStatus taskStatus, int epicId) {
        super(name, description, taskStatus);
        this.type = SUBTASK_TYPE;
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
