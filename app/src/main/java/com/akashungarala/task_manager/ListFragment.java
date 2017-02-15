package com.akashungarala.task_manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFragment extends Fragment {
    MainActivity main;
    ArrayList<Task> tasks = new ArrayList<>();
    ArrayList<Task> Todo_High, Todo_Medium, Todo_Low, Doing_High, Doing_Medium, Doing_Low, Done_High, Done_Medium, Done_Low;
    ListView lvTasks;
    Button add;
    TaskAdapter tasksAdapter;
    DatabaseReference fbRef;
    public ListFragment() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = ((MainActivity)  this.getActivity());
        main.selectedTask = null;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lvTasks = (ListView) main.findViewById(R.id.lvTasks);
        tasksAdapter = new TaskAdapter(main, R.layout.task_row, tasks);
        tasksAdapter.setNotifyOnChange(true);
        View v = main.getLayoutInflater().inflate(R.layout.add_button, null);
        lvTasks.addFooterView(v);
        lvTasks.setAdapter(tasksAdapter);
        lvTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = tasksAdapter.getItem(position);
                main.goToDetailFragment(task);
            }
        });
        lvTasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int p = position;
                AlertDialog.Builder confirm_deletion = new AlertDialog.Builder(getActivity());
                confirm_deletion.setTitle("Confirm Deletion");
                confirm_deletion.setIcon(R.drawable.remove);
                confirm_deletion.setMessage("Are you sure that you want to remove the task '" + tasks.get(p).getName() + "' ?");
                confirm_deletion.setCancelable(false);
                confirm_deletion.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                fbRef = FirebaseDatabase.getInstance().getReference();
                                fbRef.child("tasks").child(tasks.get(p).getName()).removeValue();
                                main.goToListFragment();
                            }
                        });
                confirm_deletion.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                main.goToListFragment();
                            }
                        });
                AlertDialog alert = confirm_deletion.create();
                alert.show();
                return true;
            }
        });
        fbRef = FirebaseDatabase.getInstance().getReference();
        Todo_High = new ArrayList<Task>();
        Todo_Medium = new ArrayList<Task>();
        Todo_Low = new ArrayList<Task>();
        Doing_High = new ArrayList<Task>();
        Doing_Medium = new ArrayList<Task>();
        Doing_Low = new ArrayList<Task>();
        Done_High = new ArrayList<Task>();
        Done_Medium = new ArrayList<Task>();
        Done_Low = new ArrayList<Task>();
        fbRef.child("tasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                tasksAdapter.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Task task = postSnapshot.getValue(Task.class);
                    if (task.getStatus().equals("Todo")) {
                        if (task.getPriority_level().equals("High")) Todo_High.add(task);
                        else if (task.getPriority_level().equals("Medium")) Todo_Medium.add(task);
                        else if (task.getPriority_level().equals("Low")) Todo_Low.add(task);
                    } else if (task.getStatus().equals("Doing")) {
                        if (task.getPriority_level().equals("High")) Doing_High.add(task);
                        else if (task.getPriority_level().equals("Medium")) Doing_Medium.add(task);
                        else if (task.getPriority_level().equals("Low")) Doing_Low.add(task);
                    } else if (task.getStatus().equals("Done")) {
                        if (task.getPriority_level().equals("High")) Done_High.add(task);
                        else if (task.getPriority_level().equals("Medium")) Done_Medium.add(task);
                        else if (task.getPriority_level().equals("Low")) Done_Low.add(task);
                    }
                }
                if (Todo_High.size() != 0) for (Task t : Todo_High) tasksAdapter.add(t);
                if (Todo_Medium.size() != 0) for (Task t : Todo_Medium) tasksAdapter.add(t);
                if (Todo_Low.size() != 0) for (Task t : Todo_Low) tasksAdapter.add(t);
                if (Doing_High.size() != 0) for (Task t : Doing_High) tasksAdapter.add(t);
                if (Doing_Medium.size() != 0) for (Task t : Doing_Medium) tasksAdapter.add(t);
                if (Doing_Low.size() != 0) for (Task t : Doing_Low) tasksAdapter.add(t);
                if (Done_High.size() != 0) for (Task t : Done_High) tasksAdapter.add(t);
                if (Done_Medium.size() != 0) for (Task t : Done_Medium) tasksAdapter.add(t);
                if (Done_Low.size() != 0) for (Task t : Done_Low) tasksAdapter.add(t);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
        add = (Button) main.findViewById(R.id.btnAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.selectedTask = null;
                main.goToAddFragment(tasks);
            }
        });
    }
}