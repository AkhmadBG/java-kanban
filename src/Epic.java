import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTasksIds;

    public Epic(int id, String name, String description) {
        super(id, name, description);
        super.setTaskStatus(TaskStatus.NEW);
        this.subTasksIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    public void setSubTasksIds(ArrayList<Integer> subTasksIds) {
        this.subTasksIds = subTasksIds;
    }

    public void addSubTaskId(int subTaskId) {
        subTasksIds.add(subTaskId);
    }

    public void removeSubTaskId(int subTaskId) {
        subTasksIds.remove(Integer.valueOf(subTaskId));
    }

    @Override
    public String toString() {
        return "Epic {" +
                " id=" + super.getId() +
                ", name='" + super.getName() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", taskStatus=" + super.getTaskStatus() +
                ", subTasksIds=" + subTasksIds + " }";
    }
}
