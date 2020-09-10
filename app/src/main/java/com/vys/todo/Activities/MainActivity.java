package com.vys.todo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.vys.todo.APIModels.TaskResponse;
import com.vys.todo.Adapters.SearchViewAdapter;
import com.vys.todo.Class.ApiRequestClass;
import com.vys.todo.Data.Database;
import com.vys.todo.Data.SharedPrefs;
import com.vys.todo.Fragments.AllTasksFragment;
import com.vys.todo.Fragments.FinishedFragment;
import com.vys.todo.Fragments.MissedFragment;
import com.vys.todo.Fragments.UpcomingTasksFragment;
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

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton addBtn;
    private RecyclerView searchRV;
    private SearchViewAdapter adapter;

    private LinearLayout progressBar;

    private Database db;
    public static List<TaskResponse> allTasks = new ArrayList<>();

    Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiRequestClass.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    private ApiRequestClass retrofitCall = retrofit.create(ApiRequestClass.class);

    Fragment[] fragments = {new AllTasksFragment(), new UpcomingTasksFragment(),new FinishedFragment(), new MissedFragment() };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadData();
        adapter = new SearchViewAdapter(MainActivity.this, allTasks);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("ToDo");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        db = new Database(MainActivity.this);
        tabLayout = findViewById(R.id.home_screen_tab_layout);
        viewPager = findViewById(R.id.home_screen_view_pager);
        addBtn = findViewById(R.id.home_screen_floating_btn);
        searchRV = findViewById(R.id.home_screen_search_rv);
        progressBar = findViewById(R.id.progress_bar_main);

        addBtn.setOnClickListener(view -> addNewTask());

        /**adding new tabs*/
        tabLayout.addTab(tabLayout.newTab().setText("All Tasks"));
        tabLayout.addTab(tabLayout.newTab().setText("Upcoming"));
        tabLayout.addTab(tabLayout.newTab().setText("Finished"));
        tabLayout.addTab(tabLayout.newTab().setText("Missed"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        /**setting up view pager*/
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(0);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_screen_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView;
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e(TAG, query);
                List<TaskResponse> newData = new ArrayList<>();
                for (int k = 0; k < allTasks.size(); k++) {
                    if (allTasks.get(k).getTitle().contains(query)) {
                        newData.add(allTasks.get(k));
                    }
                }
                adapter.setNewData(newData);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Log.e(TAG, "Search Clicked");
                adapter = new SearchViewAdapter(MainActivity.this, allTasks);
                searchRV.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                searchRV.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                searchRV.setAdapter(adapter);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Log.e(TAG, "Search Bar Closed");
                adapter = null;
                searchRV.setVisibility(View.GONE);
                viewPager.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home_menu_add) {
            addNewTask();
        } else if(item.getItemId() == R.id.home_menu_logout){
            SharedPrefs prefs = new SharedPrefs(MainActivity.this);
            prefs.setToken("");
            prefs.setPassword("");
            prefs.setUsername("");
            prefs.setIsLoggedIn(false);
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }
        return true;
    }

    private void addNewTask() {
        Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
        startActivity(intent);
    }

    private void enableDisableTouch(boolean type) {
        if (type) {
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    private void loadData() {
        Call<List<TaskResponse>> callGet = retrofitCall.getTasks(TOKEN);
        callGet.enqueue(new Callback<List<TaskResponse>>() {
            @Override
            public void onResponse(Call<List<TaskResponse>> call, Response<List<TaskResponse>> response) {
                if (response.isSuccessful()) {
                    allTasks = response.body();
                    adapter.setNewData(response.body());
                } else {
                    try {
                        Log.e(TAG, response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TaskResponse>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }
}