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
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vys.todo.APIModels.TaskResponse;
import com.vys.todo.Activities.MainActivity;
import com.vys.todo.Adapters.AllTasksAdapter;
import com.vys.todo.Adapters.UpcomingTasksAdapter;
import com.vys.todo.Class.ApiRequestClass;
import com.vys.todo.Class.RecyclerItemClickListener;
import com.vys.todo.Data.Database;
import com.vys.todo.Data.SharedPrefs;
import com.vys.todo.Data.TaskDataModel;
import com.vys.todo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vys.todo.Activities.LoginActivity.TOKEN;

public class AllTasksFragment extends Fragment {

    public static String[] CATEGORIES_LIST = {"All", "Default", "Home", "Work", "Personal", "Fitness", "Medication"};

    private String TAG = "AllTasksFragment";
    private RecyclerView allRV;
    private List<TaskResponse> allTasks;
    private AllTasksAdapter adapter;
    private Spinner categorySelector;
    private int selectedCategory = 0;

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
        View v = inflater.inflate(R.layout.fragment_all_tasks, container, false);
        Database db = new Database(getContext());
        allRV = v.findViewById(R.id.all_tasks_rv);
        loadData();
        categorySelector = v.findViewById(R.id.at_category_selector);
        categorySelector.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner_dropdown_item, R.id.spinner_item_tv, CATEGORIES_LIST));
        categorySelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory = i;
                if (i == 0) {
                    if (adapter != null) {
                        adapter.setNewData(allTasks);
                    }
                } else {
                    if (adapter != null) {
                        List<TaskResponse> newData = new ArrayList<>();
                        for (int k = 0; k < allTasks.size(); k++) {
                            if (allTasks.get(k).getCategory().equals(CATEGORIES_LIST[selectedCategory])) {
                                newData.add(allTasks.get(k));
                            }
                        }
                        adapter.setNewData(newData);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            loadData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void showMenuPopUp(final View view, final Context mCtx, int x, int y, final int position) {
        LayoutInflater layoutInflater = (LayoutInflater) mCtx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.all_tasks_menu, null);
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

        TextView delete = popupView.findViewById(R.id.all_menu_delete);
        TextView finished = popupView.findViewById(R.id.all_menu_finished);

        delete.setOnClickListener(view12 -> deleteTask(allTasks.get(position).getId()));

        finished.setOnClickListener(view13 -> {
            if (allTasks.get(position).getIsCompleted()) {
                Toast.makeText(mCtx, "Task is already finished", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("is_completed",true);
                    Call<TaskResponse> call = retrofitCall.updateTask(new SharedPrefs(getContext()).getToken(),allTasks.get(position).getId(),obj);
                    call.enqueue(new Callback<TaskResponse>() {
                        @Override
                        public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                            if(response.isSuccessful()){
                                loadData();
                            }else{
                                try {
                                    Log.e(TAG,response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<TaskResponse> call, Throwable t) {
                            Log.e(TAG,t.getMessage());
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            popupWindow.dismiss();
        });
    }

    private Date stringToDate(String aDate, String aFormat) {
        if (aDate == null) return null;
        ParsePosition pos = new ParsePosition(0);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        return simpledateformat.parse(aDate, pos);
    }

    private void deleteTask(int id) {

    }

    private void loadData() {
        Call<List<TaskResponse>> callGet = retrofitCall.getTasks(TOKEN);
        callGet.enqueue(new Callback<List<TaskResponse>>() {
            @Override
            public void onResponse(Call<List<TaskResponse>> call, Response<List<TaskResponse>> response) {
                if (response.isSuccessful()) {
                    allTasks = response.body();
                    adapter = new AllTasksAdapter(getContext(), allTasks);
                    allRV.setLayoutManager(new LinearLayoutManager(getContext()));
                    allRV.setAdapter(adapter);
                    allRV.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), allRV, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position, int x, int y) {
                            showMenuPopUp(view, getContext(), x, y, position);
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {

                        }
                    }));
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