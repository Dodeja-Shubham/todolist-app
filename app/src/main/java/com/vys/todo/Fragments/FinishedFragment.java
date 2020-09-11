package com.vys.todo.Fragments;

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
import com.vys.todo.Adapters.FinishedTasksAdapter;
import com.vys.todo.Class.ApiRequestClass;
import com.vys.todo.Class.RecyclerItemClickListener;
import com.vys.todo.Data.Database;
import com.vys.todo.Data.TaskDataModel;
import com.vys.todo.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vys.todo.Activities.SplashActivity.TOKEN;

public class FinishedFragment extends Fragment {

    public static String[] CATEGORIES_LIST = {"All","Default","Home","Work","Personal","Fitness","Medication"};

    private String TAG = "FinishedFragment";
    private Spinner categorySelector;
    private int selectedCategory = 0;
    private RecyclerView finishedRV;
    private List<TaskResponse> allFinishedTasks;
    private FinishedTasksAdapter adapter;

    LinearLayout noData;

    Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiRequestClass.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    private ApiRequestClass retrofitCall = retrofit.create(ApiRequestClass.class);

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            loadData();
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

        finishedRV = v.findViewById(R.id.finished_rv);
        noData = v.findViewById(R.id.finished_no_data);

        loadData();

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
                        List<TaskResponse> newData = new ArrayList<>();
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

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        Call<List<TaskResponse>> callGet = retrofitCall.getTasks(TOKEN);
        callGet.enqueue(new Callback<List<TaskResponse>>() {
            @Override
            public void onResponse(Call<List<TaskResponse>> call, Response<List<TaskResponse>> response) {
                if (response.isSuccessful()) {
                    allFinishedTasks = response.body();
                    List<TaskResponse> data = new ArrayList<>();
                    for (int i = 0;i < allFinishedTasks.size();i++){
                        if(allFinishedTasks.get(i).getIsCompleted()){
                            data.add(allFinishedTasks.get(i));
                        }
                    }
                    allFinishedTasks = data;
                    adapter = new FinishedTasksAdapter(getContext(),allFinishedTasks);
                    finishedRV.setLayoutManager(new LinearLayoutManager(getContext()));
                    finishedRV.setAdapter(adapter);
                    categorySelector.setSelection(0);
                    if(allFinishedTasks.isEmpty()){
                        noData.setVisibility(View.VISIBLE);
                        finishedRV.setVisibility(View.INVISIBLE);
                    }else{
                        noData.setVisibility(View.GONE);
                        finishedRV.setVisibility(View.VISIBLE);
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
}