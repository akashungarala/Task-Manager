package com.akashungarala.task_manager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddFragment extends Fragment {
    MainActivity main;
    private TextView tvTaskDueDate;
    private Button btnTaskDueDate, btnSaveTask, btnCancel;
    private EditText etTaskName, etTaskNotes;
    private Spinner spinner_priority, spinner_status;
    String name, priority, status, due_date, notes;
    String[] priorityList, statusList;
    DatabaseReference fbRef;
    private int year, month, day;
    static final int DATE_DIALOG_ID = 0;
    public AddFragment() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = ((MainActivity) this.getActivity());
        priorityList = main.getResources().getStringArray(R.array.priority_levels);
        statusList = main.getResources().getStringArray(R.array.status);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        etTaskName = (EditText) main.findViewById(R.id.etTaskName);
        spinner_priority = (Spinner) main.findViewById(R.id.sTaskPriorityLevel);
        spinner_status = (Spinner) main.findViewById(R.id.sTaskStatus);
        tvTaskDueDate = (TextView) main.findViewById(R.id.tvTaskDueDate);
        btnTaskDueDate = (Button) main.findViewById(R.id.btnTaskDueDate);
        etTaskNotes = (EditText) main.findViewById(R.id.etTaskNotes);
        btnSaveTask = (Button) main.findViewById(R.id.btnSave);
        btnCancel = (Button) main.findViewById(R.id.btnCancel);
        PriorityAdapter adapter_priority = new PriorityAdapter(main, R.layout.priority_dropdown, priorityList);
        adapter_priority.setNotifyOnChange(true);
        spinner_priority.setAdapter(adapter_priority);
        StatusAdapter adapter_status = new StatusAdapter(main, R.layout.status_dropdown, statusList);
        adapter_status.setNotifyOnChange(true);
        spinner_status.setAdapter(adapter_status);
        spinner_priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                priority = spinner_priority.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
        spinner_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                status = spinner_status.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
        btnTaskDueDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Dialog d = showDialog(DATE_DIALOG_ID);
                d.setCancelable(false);
                d.show();
            }
        });
        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etTaskName.getText().toString();
                priority = spinner_priority.getSelectedItem().toString();
                status = spinner_status.getSelectedItem().toString();
                notes = etTaskNotes.getText().toString();
                due_date = tvTaskDueDate.getText().toString();
                if(name.isEmpty()) Toast.makeText(main, "Name for a task is mandatory", Toast.LENGTH_SHORT).show();
                else if(main.selectedTask == null) {
                    boolean valid=true;
                    for(Task t: main.tasks) if(t.getName().equals(name)) valid=false;
                    if(valid) {
                        fbRef = FirebaseDatabase.getInstance().getReference();
                        Task t = new Task(name, due_date, notes, priority, status);
                        fbRef.child("tasks").child(name).setValue(t);
                        main.goToListFragment();
                    } else Toast.makeText(main, "A task with same name already exists", Toast.LENGTH_SHORT).show();
                } else {
                    boolean valid=true;
                    for(Task t: main.tasks) if(t.getName().equals(name) && !name.equals(main.selectedTask.getName())) valid=false;
                    if(valid) {
                        fbRef = FirebaseDatabase.getInstance().getReference();
                        Task t = new Task(name, due_date, notes, priority, status);
                        fbRef.child("tasks").child(name).setValue(t);
                        if(!name.equals(main.selectedTask.getName())) fbRef.child("tasks").child(main.selectedTask.getName()).removeValue();
                        main.goToListFragment();
                    } else Toast.makeText(main, "A task with same name already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.goToListFragment();
            }
        });
        if(main.selectedTask != null) {
            etTaskName.setText(main.selectedTask.getName());
            if(main.selectedTask.getPriority_level().equals("High"))  spinner_priority.setSelection(0);
            else if(main.selectedTask.getPriority_level().equals("Medium"))  spinner_priority.setSelection(1);
            else if(main.selectedTask.getPriority_level().equals("Low"))  spinner_priority.setSelection(2);
            if(main.selectedTask.getStatus().equals("Todo"))  spinner_status.setSelection(0);
            else if(main.selectedTask.getStatus().equals("Doing"))  spinner_status.setSelection(1);
            else if(main.selectedTask.getStatus().equals("Done"))  spinner_status.setSelection(2);
            tvTaskDueDate.setText(main.selectedTask.getDue_date());
            String str[] = main.selectedTask.getDue_date().split(" ");
            day = Integer.parseInt(str[0]);
            for(int i=0; i<MainActivity.months.length; i++) {
                if(MainActivity.months[i].equals(str[1])) month=i;
            }
            year = Integer.parseInt(str[2]);
            etTaskNotes.setText(main.selectedTask.getNotes());
        } else {
            final Calendar cal = Calendar.getInstance();
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);
            updateDisplay();
        }
    }
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            AddFragment.this.year = year;
            month = monthOfYear;
            day = dayOfMonth;
            updateDisplay();
        }
    };
    private void updateDisplay() {
        tvTaskDueDate.setText(new StringBuilder().append(day).append(" ").append(MainActivity.months[month]).append(" ").append(year));
    }
    protected Dialog showDialog(int id) {
        switch(id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this.main, dateSetListener, year, month, day);
        }
        return null;
    }
    public class PriorityAdapter extends ArrayAdapter {
        Context context;
        int textViewResourceId;
        String[] priorityList;
        public PriorityAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
            this.textViewResourceId = textViewResourceId;
            this.context = context;
            this.priorityList = objects;
        }
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(this.textViewResourceId, parent, false);
            }
            TextView t = (TextView) convertView.findViewById(R.id.tvPriorityDropdown);
            t.setText(priorityList[position]);
            if (t.getText().toString().equals("High")) t.setTextColor(getResources().getColor(R.color.colorHigh));
            else if (t.getText().toString().equals("Medium")) t.setTextColor(getResources().getColor(R.color.colorMedium));
            else if (t.getText().toString().equals("Low")) t.setTextColor(getResources().getColor(R.color.colorLow));
            return convertView;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.priority_selected_item, parent, false);
            }
            TextView t = (TextView) convertView.findViewById(R.id.tvPrioritySelectedItem);
            t.setText(priorityList[position]);
            if (t.getText().toString().equals("High")) t.setTextColor(getResources().getColor(R.color.colorHigh));
            else if (t.getText().toString().equals("Medium")) t.setTextColor(getResources().getColor(R.color.colorMedium));
            else if (t.getText().toString().equals("Low")) t.setTextColor(getResources().getColor(R.color.colorLow));
            return convertView;
        }
    }
    public class StatusAdapter extends ArrayAdapter {
        Context context;
        int textViewResourceId;
        String[] statusList;
        public StatusAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
            this.textViewResourceId = textViewResourceId;
            this.context = context;
            this.statusList = objects;
        }
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(this.textViewResourceId, parent, false);
            }
            TextView t = (TextView) convertView.findViewById(R.id.tvStatusDropdown);
            t.setText(statusList[position]);
            if (t.getText().toString().equals("Todo")) t.setTextColor(getResources().getColor(R.color.colorTodo));
            else if (t.getText().toString().equals("Doing")) t.setTextColor(getResources().getColor(R.color.colorDoing));
            else if (t.getText().toString().equals("Done")) t.setTextColor(getResources().getColor(R.color.colorDone));
            return convertView;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.status_selected_item, parent, false);
            }
            TextView t = (TextView) convertView.findViewById(R.id.tvStatusSelectedItem);
            t.setText(statusList[position]);
            if (t.getText().toString().equals("Todo")) t.setTextColor(getResources().getColor(R.color.colorTodo));
            else if (t.getText().toString().equals("Doing")) t.setTextColor(getResources().getColor(R.color.colorDoing));
            else if (t.getText().toString().equals("Done")) t.setTextColor(getResources().getColor(R.color.colorDone));
            return convertView;
        }
    }
}