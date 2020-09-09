package com.vys.todo.Fragments;

import android.annotation.SuppressLint;
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
import android.widget.TextView;
import android.widget.Toast;

import com.vys.todo.Adapters.AllTasksAdapter;
import com.vys.todo.Adapters.UpcomingTasksAdapter;
import com.vys.todo.Class.RecyclerItemClickListener;
import com.vys.todo.Data.Database;
import com.vys.todo.Data.TaskDataModel;
import com.vys.todo.R;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AllTasksFragment extends Fragment {

    public static String[] CATEGORIES_LIST = {"All","Default","Home","Work","Personal","Fitness","Medication"};

    private String TAG = "AllTasksFragment";
    private RecyclerView allRV;
    private List<TaskDataModel> allTasks;
    private AllTasksAdapter adapter;
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
        View v = inflater.inflate(R.layout.fragment_all_tasks, container, false);
        Database db = new Database(getContext());
        allTasks = db.getAllTasks();
        allTasks.addAll(db.getAllFinished());
        allTasks.addAll(db.getAllMissed());
        adapter = new AllTasksAdapter(getContext(),allTasks);
        allRV = v.findViewById(R.id.all_tasks_rv);
        categorySelector = v.findViewById(R.id.at_category_selector);
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

        allRV.setLayoutManager(new LinearLayoutManager(getContext()));
        allRV.setAdapter(adapter);

        allRV.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), allRV, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position,int x,int y) {
                showMenuPopUp(view,getContext(),x,y,position);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
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
        allTasks = db.getAllTasks();
        allTasks.addAll(db.getAllFinished());
        allTasks.addAll(db.getAllMissed());
        if(adapter != null){
            adapter.setNewData(allTasks);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadDataDB();
    }

    public void showMenuPopUp(final View view, final Context mCtx,int x,int y,final int position) {
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

        TextView delete = popupView.findViewById(R.id.all_menu_delete);
        TextView finished = popupView.findViewById(R.id.all_menu_finished);
        TextView missed = popupView.findViewById(R.id.all_menu_missed);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database db = new Database(getContext());
                db.deleteTask(allTasks.get(position).getId());
                allTasks.remove(position);
                adapter.notifyDataSetChanged();
                popupWindow.dismiss();
            }
        });

        finished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database db = new Database(getContext());
                Date date = stringToDate(allTasks.get(position).getDue_date(), "EEE MMM d HH:mm:ss zz yyyy");
                if(allTasks.get(position).getIs_completed()){
                    Toast.makeText(mCtx,"Task is already finished",Toast.LENGTH_LONG).show();
                }else if(Calendar.getInstance().getTime().compareTo(date) < 0){
                    db.deleteMissed(allTasks.get(position).getId());
                    db.insertFinished(allTasks.get(position).getId(), allTasks.get(position).getTitle()
                            , allTasks.get(position).getDue_date(), allTasks.get(position).getCreated_at()
                            , "true", allTasks.get(position).getColour(), allTasks.get(position).getCategory());
                    allTasks.remove(position);
                    adapter.notifyDataSetChanged();
                    popupWindow.dismiss();
                }else {
                    db.deleteTask(allTasks.get(position).getId());
                    db.insertFinished(allTasks.get(position).getId(), allTasks.get(position).getTitle()
                            , allTasks.get(position).getDue_date(), allTasks.get(position).getCreated_at()
                            , "true", allTasks.get(position).getColour(), allTasks.get(position).getCategory());
                    allTasks.remove(position);
                    adapter.notifyDataSetChanged();
                    popupWindow.dismiss();
                }
            }
        });

        missed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database db = new Database(getContext());
                Date date = stringToDate(allTasks.get(position).getDue_date(), "EEE MMM d HH:mm:ss zz yyyy");

                db.deleteTask(allTasks.get(position).getId());
                db.insertMissed(allTasks.get(position).getId(), allTasks.get(position).getTitle()
                        , allTasks.get(position).getDue_date(), allTasks.get(position).getCreated_at()
                        , "false", allTasks.get(position).getColour(), allTasks.get(position).getCategory());
                allTasks.remove(position);
                adapter.notifyDataSetChanged();
                popupWindow.dismiss();

                if(allTasks.get(position).getIs_completed()){
                    db.deleteFinished(allTasks.get(position).getId());
                    db.insertMissed(allTasks.get(position).getId(), allTasks.get(position).getTitle()
                            , allTasks.get(position).getDue_date(), allTasks.get(position).getCreated_at()
                            , "false", allTasks.get(position).getColour(), allTasks.get(position).getCategory());
                    allTasks.remove(position);
                    adapter.notifyDataSetChanged();
                    popupWindow.dismiss();
                }else if(Calendar.getInstance().getTime().compareTo(date) > 0){
                    Toast.makeText(mCtx,"Task already marked as missed",Toast.LENGTH_LONG).show();
                }else {
                    db.deleteTask(allTasks.get(position).getId());
                    db.insertMissed(allTasks.get(position).getId(), allTasks.get(position).getTitle()
                            , allTasks.get(position).getDue_date(), allTasks.get(position).getCreated_at()
                            , "false", allTasks.get(position).getColour(), allTasks.get(position).getCategory());
                    allTasks.remove(position);
                    adapter.notifyDataSetChanged();
                    popupWindow.dismiss();
                }
            }
        });
    }

    private Date stringToDate(String aDate, String aFormat) {
        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        return simpledateformat.parse(aDate, pos);
    }
}