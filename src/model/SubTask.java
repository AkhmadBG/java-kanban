package model;

import static enums.TaskType.SUBTASK_TYPE;
import static utils.AppConstants.DATE_TIME_FORMATTER;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String name, String description, int epicId) {
        super(name, description);
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
