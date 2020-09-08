package com.vys.todo.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.vys.todo.Adapters.UpcomingTasksAdapter;
import com.vys.todo.AddTaskActivity;
import com.vys.todo.Data.Database;
import com.vys.todo.Data.TaskDataModel;
import com.vys.todo.MainActivity;
import com.vys.todo.R;

import java.util.ArrayList;
import java.util.List;


public class UpcomingTasksFragment extends Fragment {

    public static String[] CATEGORIES_LIST = {"All Tasks","Default","Home","Work","Personal","Fitness","Medication"};

    private String TAG = "UpcomingTasksFragment";
    private RecyclerView upcomingRV;
    private List<TaskDataModel> allTasks;
    private UpcomingTasksAdapter adapter;
    private Spinner categorySelector;
    private int selectedCategory = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            reloadDataDB();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_upcoming_tasks, container, false);
        Database db = new Database(getContext());
        allTasks = db.getAllTasks();
        adapter = new UpcomingTasksAdapter(getContext(),allTasks);
        upcomingRV = v.findViewById(R.id.upcoming_rv);
        categorySelector = v.findViewById(R.id.uct_category_selector);
        categorySelector.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.spinner_dropdown_item,R.id.spinner_item_tv, CATEGORIES_LIST));

        categorySelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory = i;
                if(i == 0){
                    if(adapter != null){
                        adapter.setNewData(allTasks);
                    }
                }else{
                    if(adapter != null){
                        List<TaskDataModel> newData = new ArrayList<>();
                        for (int k = 0;k < allTasks.size();k++){
                            if(allTasks.get(k).getCategory().equals(CATEGORIES_LIST[selectedCategory])){
                                newData.add(allTasks.get(k));
                            }
                        }
                        adapter.setNewData(newData);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        upcomingRV.setLayoutManager(new LinearLayoutManager(getContext()));
        upcomingRV.setAdapter(adapter);
        return v;
    }

    private void reloadDataDB(){
        Database db = new Database(getContext());
        allTasks = db.getAllTasks();
        if(adapter != null){
            adapter.setNewData(allTasks);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadDataDB();
    }
}