package model;

import enums.TaskType;
import enums.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import static enums.TaskType.TASK_TYPE;
import static utils.AppConstants.DATE_TIME_FORMATTER;

public class Task implements Cloneable {

    protected int id;
    protected TaskType type;
    protected String name;
    protected TaskStatus taskStatus;
    protected String description;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(String name, String description) {
        this.type = TASK_TYPE;
        this.name = name;
        this.taskStatus = TaskStatus.NEW;
        this.description = description;
        this.duration = Duration.ZERO;
        this.startTime = LocalDateTime.now();
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

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
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
                "," + taskStatus +
                "," + duration.toMinutes() +
                "," + startTime.format(DATE_TIME_FORMATTER) +
                "," + getEndTime().format(DATE_TIME_FORMATTER);

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
