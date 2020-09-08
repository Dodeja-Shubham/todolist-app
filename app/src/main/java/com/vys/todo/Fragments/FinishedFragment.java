package com.vys.todo.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vys.todo.Adapters.FinishedTasksAdapter;
import com.vys.todo.Data.Database;
import com.vys.todo.Data.TaskDataModel;
import com.vys.todo.R;

import java.util.List;

public class FinishedFragment extends Fragment {

    private RecyclerView finishedRV;
    private List<TaskDataModel> allFinishedTasks;
    private FinishedTasksAdapter adapter;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            reloadDataDB();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_finished, container, false);
        Database db = new Database(getContext());
        allFinishedTasks = db.getAllFinished();
        adapter = new FinishedTasksAdapter(getContext(),allFinishedTasks);
        finishedRV = v.findViewById(R.id.finished_rv);
        finishedRV.setLayoutManager(new LinearLayoutManager(getContext()));
        finishedRV.setAdapter(adapter);
        return v;
    }

    private void reloadDataDB(){
        Database db = new Database(getContext());
        allFinishedTasks = db.getAllFinished();
        if(adapter != null){
            adapter.setNewData(allFinishedTasks);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadDataDB();
    }
}