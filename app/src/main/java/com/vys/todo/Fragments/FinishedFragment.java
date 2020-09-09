package com.vys.todo.Fragments;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.Spinner;

import com.vys.todo.Adapters.AllTasksAdapter;
import com.vys.todo.Adapters.FinishedTasksAdapter;
import com.vys.todo.Data.Database;
import com.vys.todo.Data.TaskDataModel;
import com.vys.todo.R;

import java.util.ArrayList;
import java.util.List;

public class FinishedFragment extends Fragment {

    public static String[] CATEGORIES_LIST = {"All","Default","Home","Work","Personal","Fitness","Medication"};

    private String TAG = "FinishedFragment";
    private Spinner categorySelector;
    private int selectedCategory = 0;
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

        categorySelector = v.findViewById(R.id.f_category_selector);
        categorySelector.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.spinner_dropdown_item,R.id.spinner_item_tv, CATEGORIES_LIST));

        categorySelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory = i;
                if(i == 0){
                    if(adapter != null){
                        adapter.setNewData(allFinishedTasks);
                    }
                }else{
                    if(adapter != null){
                        List<TaskDataModel> newData = new ArrayList<>();
                        for (int k = 0;k < allFinishedTasks.size();k++){
                            if(allFinishedTasks.get(k).getCategory().equals(CATEGORIES_LIST[selectedCategory])){
                                newData.add(allFinishedTasks.get(k));
                            }
                        }
                        adapter.setNewData(newData);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

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

    public void showMenuPopUp(final View view, final Context mCtx, int x, int y) {
        LayoutInflater layoutInflater = (LayoutInflater) mCtx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.all_tasks_menu, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setFocusable(true);
        popupWindow.update();
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        popupWindow.showAsDropDown(view, x, -100);
    }
}