package rzl.learning.mytask.data;

public class Task {
    public String taskId;
    public String taskName;
    public String category;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String deadline;
    public String description;

    public Task() {
    }

    public Task(String taskId, String taskName, String category, String deadline, String description) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.category = category;
        this.deadline = deadline;
        this.description = description;
    }
}

