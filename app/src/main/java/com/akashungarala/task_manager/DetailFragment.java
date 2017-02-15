package com.akashungarala.task_manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DetailFragment extends Fragment {
    MainActivity main;
    TextView tvTaskName, tvTaskPriority, tvTaskStatus, tvTaskDueDate, tvTaskNotes;
    Button btnEdit, btnRemove, btnCancel;
    public DetailFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        main = ((MainActivity)  this.getActivity());
        Task task = main.selectedTask;
        tvTaskName = (TextView) main.findViewById(R.id.tvTaskName);
        tvTaskPriority = (TextView) main.findViewById(R.id.tvTaskPriority);
        tvTaskStatus = (TextView) main.findViewById(R.id.tvTaskStatus);
        tvTaskDueDate = (TextView) main.findViewById(R.id.tvTaskDueDate);
        tvTaskNotes = (TextView) main.findViewById(R.id.tvTaskNotes);
        tvTaskName.setText(task.getName());
        tvTaskPriority.setText(task.getPriority_level());
        tvTaskStatus.setText(task.getStatus());
        tvTaskDueDate.setText(task.getDue_date());
        tvTaskNotes.setText(task.getNotes());
        if(task.getPriority_level().equals("High")) tvTaskPriority.setTextColor(main.getResources().getColor(R.color.colorHigh));
        else if(task.getPriority_level().equals("Medium")) tvTaskPriority.setTextColor(main.getResources().getColor(R.color.colorMedium));
        else if(task.getPriority_level().equals("Low")) tvTaskPriority.setTextColor(main.getResources().getColor(R.color.colorLow));
        if(task.getStatus().equals("Todo")) tvTaskStatus.setTextColor(main.getResources().getColor(R.color.colorTodo));
        else if(task.getStatus().equals("Doing")) tvTaskStatus.setTextColor(main.getResources().getColor(R.color.colorDoing));
        else if(task.getStatus().equals("Done")) tvTaskStatus.setTextColor(main.getResources().getColor(R.color.colorDone));
        btnEdit = (Button) main.findViewById(R.id.btnEdit);
        btnRemove = (Button) main.findViewById(R.id.btnRemove);
        btnCancel = (Button) main.findViewById(R.id.btnCancel);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.goToAddFragment();
            }
        });
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder confirm_deletion = new AlertDialog.Builder(getActivity());
                confirm_deletion.setTitle("Confirm Deletion");
                confirm_deletion.setIcon(R.drawable.remove);
                confirm_deletion.setMessage("Are you sure that you want to remove the task '"+main.selectedTask.getName()+"' ?");
                confirm_deletion.setCancelable(false);
                confirm_deletion.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                main.removeTask();
                            }
                        });
                confirm_deletion.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                main.goToDetailFragment(main.selectedTask);
                            }
                        });
                AlertDialog alert = confirm_deletion.create();
                alert.show();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.selectedTask = null;
                main.goToListFragment();
            }
        });
    }
}
