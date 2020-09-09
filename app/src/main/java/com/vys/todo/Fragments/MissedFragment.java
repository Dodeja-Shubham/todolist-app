package com.vys.todo.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.vys.todo.Adapters.AllTasksAdapter;
import com.vys.todo.Adapters.MissedTasksAdapter;
import com.vys.todo.Data.Database;
import com.vys.todo.Data.TaskDataModel;
import com.vys.todo.R;

import java.util.ArrayList;
import java.util.List;

public class MissedFragment extends Fragment {

    public static String[] CATEGORIES_LIST = {"All","Default","Home","Work","Personal","Fitness","Medication"};

    private String TAG = "MissedFragment";
    private RecyclerView missedRV;
    private List<TaskDataModel> missedTasks;
    private MissedTasksAdapter adapter;
    private Spinner categorySelector;
    private int selectedCategory = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_missed, container, false);
        Database db = new Database(getContext());
        missedTasks = db.getAllTasks();
        adapter = new MissedTasksAdapter(getContext(),missedTasks);
        missedRV = v.findViewById(R.id.missed_rv);
        categorySelector = v.findViewById(R.id.missed_category_selector);
        categorySelector.setAdapter(new ArrayAdapter<>(getContext(), R.layout.spinner_dropdown_item, R.id.spinner_item_tv, CATEGORIES_LIST));

        categorySelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory = i;
                if(i == 0){
                    if(adapter != null){
                        adapter.setNewData(missedTasks);
                    }
                }else{
                    if(adapter != null){
                        List<TaskDataModel> newData = new ArrayList<>();
                        for (int k = 0;k < missedTasks.size();k++){
                            if(missedTasks.get(k).getCategory().equals(CATEGORIES_LIST[selectedCategory])){
                                newData.add(missedTasks.get(k));
                            }
                        }
                        adapter.setNewData(newData);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        missedRV.setLayoutManager(new LinearLayoutManager(getContext()));
        missedRV.setAdapter(adapter);
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            reloadDataDB();
        }
    }

    private void reloadDataDB(){
        Database db = new Database(getContext());
        missedTasks = db.getAllTasks();
        missedTasks.addAll(db.getAllFinished());
        if(adapter != null){
            adapter.setNewData(missedTasks);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadDataDB();
    }
}