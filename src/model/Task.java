package model;

import enums.TaskType;
import enums.TaskStatus;

import java.util.Objects;

public class Task implements Cloneable {

    protected int id;
    protected TaskType type;
    protected String name;
    protected TaskStatus taskStatus;
    protected String description;

    public Task(TaskType type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.taskStatus = TaskStatus.NEW;
    }

    public Task(TaskType type, String name, String description, TaskStatus taskStatus) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id +
                "," + type +
                "," + name +
                "," + description +
                "," + taskStatus;

    }

    @Override
    public Task clone() {
        try {
            Task clone = (Task) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

}
