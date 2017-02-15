package com.akashungarala.task_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {
    List<Task> tasks;
    Context context;
    int resource;
    public TaskAdapter(Context context, int resource, List<Task> tasks) {
        super(context, resource, tasks);
        this.tasks = tasks;
        this.context = context;
        this.resource = resource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resource, parent, false);
        }
        final Task task = this.tasks.get(position);
        TextView name = (TextView) convertView.findViewById(R.id.tvTaskName);
        TextView status = (TextView) convertView.findViewById(R.id.tvTaskStatus);
        TextView due_date = (TextView) convertView.findViewById(R.id.tvTaskDueDate);
        String task_name = task.getName();
        String task_priority = task.getPriority_level();
        String task_status = task.getStatus();
        String task_due_date = task.getDue_date();
        name.setText(task_name);
        status.setText(task_status);
        due_date.setText(task_due_date);
        if(task_priority.equals("High")) {
            name.setTextColor(convertView.getResources().getColor(R.color.colorHigh));
            due_date.setTextColor(convertView.getResources().getColor(R.color.colorHigh));
            status.setTextColor(convertView.getResources().getColor(R.color.colorHigh));
        } else if(task_priority.equals("Medium")) {
            name.setTextColor(convertView.getResources().getColor(R.color.colorMedium));
            due_date.setTextColor(convertView.getResources().getColor(R.color.colorMedium));
            status.setTextColor(convertView.getResources().getColor(R.color.colorMedium));
        } else if(task_priority.equals("Low")) {
            name.setTextColor(convertView.getResources().getColor(R.color.colorLow));
            due_date.setTextColor(convertView.getResources().getColor(R.color.colorLow));
            status.setTextColor(convertView.getResources().getColor(R.color.colorLow));
        }
        return convertView;
    }
}