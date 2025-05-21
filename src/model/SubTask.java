package model;

import enums.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

import static enums.TaskType.SUBTASK_TYPE;
import static utils.AppConstants.DATE_TIME_FORMATTER;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String name, String description, Duration duration, LocalDateTime startTime, int epicId) {
        super(name, description, duration, startTime);
        initSubTask(epicId);
    }

    public SubTask(String name, String description, TaskStatus taskStatus, Duration duration, LocalDateTime startTime, int epicId) {
        super(name, description, taskStatus, duration, startTime);
        initSubTask(epicId);
    }

    private void initSubTask(int epicId) {
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
                "," + super.getDuration().toMinutes() +
                "," + super.getStartTime().format(DATE_TIME_FORMATTER) +
                "," + super.getEndTime().format(DATE_TIME_FORMATTER) +
                "," + epicId;
    }

}
