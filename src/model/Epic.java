package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static enums.TaskType.EPIC_TYPE;
import static utils.AppConstants.*;

public class Epic extends Task {

    private ArrayList<Integer> subTasksIds;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        super.type = EPIC_TYPE;
        super.duration = Duration.ZERO;
        super.startTime = DEFAULT_DATE_TO_EPIC_START;
        this.endTime = DEFAULT_DATE_TO_EPIC_END;
        this.subTasksIds = new ArrayList<>();
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    public void setSubTasksIds(ArrayList<Integer> subTasksIds) {
        this.subTasksIds = subTasksIds;
    }

    public void addSubTaskId(int subTaskId) {
        if (subTasksIds.contains(subTaskId) || subTaskId == this.getId()) {
            return;
        }
        subTasksIds.add(subTaskId);
    }

    public void removeSubTaskId(int subTaskId) {
        subTasksIds.remove(Integer.valueOf(subTaskId));
    }

    @Override
    public String toString() {
        String subTasksIdsToString = "";
        StringBuilder sb = new StringBuilder(subTasksIdsToString);
        for (Integer subTaskId : subTasksIds) {
            sb.append(subTaskId);
            if (subTasksIds.contains((subTaskId + 1))) {
                sb.append("/");
            }
        }
        subTasksIdsToString = sb.toString();
        if (!subTasksIdsToString.equals("")) {
            return super.getId() +
                    "," + super.getType() +
                    "," + super.getName() +
                    "," + super.getDescription() +
                    "," + super.getTaskStatus() +
                    "," + super.getDuration().toMinutes() +
                    "," + super.getStartTime().format(DATE_TIME_FORMATTER) +
                    "," + endTime.format(DATE_TIME_FORMATTER) +
                    "," + subTasksIdsToString;
        } else {
            return super.getId() +
                    "," + super.getType() +
                    "," + super.getName() +
                    "," + super.getDescription() +
                    "," + super.getTaskStatus() +
                    "," + super.getDuration().toMinutes() +
                    "," + super.getStartTime().format(DATE_TIME_FORMATTER) +
                    "," + endTime.format(DATE_TIME_FORMATTER);
        }
    }

}
