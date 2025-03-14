public class SubTask extends Task {

    private int epicId;

    public SubTask(int id, String name, String description, int epicId) {
        super(id, name, description);
        super.setTaskStatus(TaskStatus.NEW);
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
        return "SubTask {" +
                " id=" + super.getId() +
                ", name='" + super.getName() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", taskStatus=" + super.getTaskStatus() +
                ", epicId=" + epicId + " }";
    }
}
