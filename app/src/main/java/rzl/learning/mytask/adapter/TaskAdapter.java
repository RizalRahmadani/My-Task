package rzl.learning.mytask.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import rzl.learning.mytask.R;
import rzl.learning.mytask.data.Task;
import rzl.learning.mytask.ui.TaskDetailActivity;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private Context context;

    // Constructor
    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

//        String taskName = task.getTaskName();
//        if (taskName != null && taskName.length() > 0) {
//            taskName = taskName.substring(0, 1).toUpperCase() + taskName.substring(1).toLowerCase();
//        }
        holder.taskName.setText(task.getTaskName());
        holder.taskDeadline.setText(task.getDeadline());


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TaskDetailActivity.class);
            intent.putExtra("taskId", task.getTaskId());
            intent.putExtra("taskName", task.getTaskName());
            intent.putExtra("deadline", task.getDeadline());
            intent.putExtra("category", task.getCategory());
            intent.putExtra("description", task.getDescription());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    // ViewHolder untuk RecyclerView
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskName, taskDeadline;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.tvTaskTitle);
            taskDeadline = itemView.findViewById(R.id.tvTaskDate);

        }
    }
}