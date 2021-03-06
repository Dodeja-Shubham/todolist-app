package com.vys.todo.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.vys.todo.APIModels.TaskResponse;
import com.vys.todo.Adapters.AllTasksAdapter;
import com.vys.todo.Adapters.MissedTasksAdapter;
import com.vys.todo.Class.ApiRequestClass;
import com.vys.todo.Class.RecyclerItemClickListener;
import com.vys.todo.Data.Database;
import com.vys.todo.Data.SharedPrefs;
import com.vys.todo.Data.TaskDataModel;
import com.vys.todo.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vys.todo.Activities.SplashActivity.TOKEN;

public class MissedFragment extends Fragment {

    public static String[] CATEGORIES_LIST = {"All","Default","Home","Work","Personal","Fitness","Medication"};

    private String TAG = "MissedFragment";
    private RecyclerView missedRV;
    private List<TaskResponse> missedTasks;
    private MissedTasksAdapter adapter;
    private Spinner categorySelector;
    private int selectedCategory = 0;

    LinearLayout noData;

    Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiRequestClass.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    private ApiRequestClass retrofitCall = retrofit.create(ApiRequestClass.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_missed, container, false);
        missedRV = v.findViewById(R.id.missed_rv);
        noData = v.findViewById(R.id.missed_no_data);
        loadData(0);
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
                        List<TaskResponse> newData = new ArrayList<>();
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
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            loadData(0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(0);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void showMenuPopUp(final View view, final Context mCtx, int x, int y, final int position) {
        LayoutInflater layoutInflater = (LayoutInflater) mCtx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.missed_tasks_menu, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setFocusable(true);
        popupWindow.update();
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchInterceptor((view1, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
                popupWindow.dismiss();
                return true;
            }
            return false;
        });
        popupWindow.showAsDropDown(view, x, -100);

        TextView finished = popupView.findViewById(R.id.missed_menu_finished);

        finished.setOnClickListener(view12 -> {
            Map<String, Object> obj = new HashMap<>();
            obj.put("is_completed", true);
            Call<TaskResponse> call = retrofitCall.updateTask(new SharedPrefs(getContext()).getToken(), missedTasks.get(position).getId(), obj);
            call.enqueue(new Callback<TaskResponse>() {
                @Override
                public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                    if (response.isSuccessful()) {
                        loadData(position);
                    } else {
                        try {
                            Log.e(TAG, response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<TaskResponse> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                }
            });
        });
    }

    private void loadData(int position) {
        Call<List<TaskResponse>> callGet = retrofitCall.getTasks(TOKEN);
        callGet.enqueue(new Callback<List<TaskResponse>>() {
            @Override
            public void onResponse(Call<List<TaskResponse>> call, Response<List<TaskResponse>> response) {
                if (response.isSuccessful()) {
                    missedTasks = response.body();
                    List<TaskResponse> data = new ArrayList<>();
                    for (int i = 0;i < missedTasks.size();i++){
                        if(missedTasks.get(i).getDueDate() != null){
                            if(!missedTasks.get(i).getIsCompleted() && dateMissed(missedTasks.get(i).getDueDate())){
                                data.add(missedTasks.get(i));
                            }
                        }
                    }
                    missedTasks = data;
                    adapter = new MissedTasksAdapter(getContext(),missedTasks);
                    missedRV.setLayoutManager(new LinearLayoutManager(getContext()));
                    missedRV.setAdapter(adapter);
                    missedRV.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), missedRV, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position, int x, int y) {
                            showMenuPopUp(view,getContext(),x,y,position);
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {

                        }
                    }));
                    missedRV.scrollToPosition(Math.max((position - 1), 0));
                    categorySelector.setSelection(0);
                    if(missedTasks.isEmpty()){
                        noData.setVisibility(View.VISIBLE);
                        missedRV.setVisibility(View.INVISIBLE);
                    }else{
                        noData.setVisibility(View.GONE);
                        missedRV.setVisibility(View.VISIBLE);
                    }
                } else {
                    try {
                        Log.e(TAG,response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TaskResponse>> call, Throwable t) {
                Log.e(TAG,t.getMessage());
            }
        });
    }

    boolean dateMissed(String aDate){
        int todayYear = Calendar.YEAR;
        int todayMonth = Calendar.MONTH;
        int todayDay = Calendar.DAY_OF_MONTH;

        int year = Integer.parseInt(aDate.substring(0, 4));
        int month = Integer.parseInt(aDate.substring(5, 7));
        int day = Integer.parseInt(aDate.substring(8, 10));

        if(todayYear > year){
            return true;
        }else if(todayMonth > month){
            return true;
        }else if(todayDay > day){
            return true;
        }
        return false;
    }
}