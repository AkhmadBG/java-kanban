package model;

import enums.TaskStatus;

import java.util.ArrayList;

import static enums.TaskType.EPIC_TYPE;

public class Epic extends Task {

    private ArrayList<Integer> subTasksIds;

    public Epic(String name, String description) {
        super(name, description);
        this.type = EPIC_TYPE;
        this.subTasksIds = new ArrayList<>();
    }

    public Epic(String name, String description, TaskStatus taskStatus) {
        super(name, description, taskStatus);
        this.type = EPIC_TYPE;
        this.subTasksIds = new ArrayList<>();
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
        return super.getId() +
                "," + super.getType() +
                "," + super.getName() +
                "," + super.getDescription() +
                "," + super.getTaskStatus() +
                "," + subTasksIdsToString;
    }

}
