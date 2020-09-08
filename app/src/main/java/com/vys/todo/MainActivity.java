package com.vys.todo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.view.View;
import android.widget.TableLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.vys.todo.Adapters.SearchViewAdapter;
import com.vys.todo.Adapters.UpcomingTasksAdapter;
import com.vys.todo.Data.Database;
import com.vys.todo.Data.TaskDataModel;
import com.vys.todo.Fragments.FinishedFragment;
import com.vys.todo.Fragments.UpcomingTasksFragment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton addBtn;
    private RecyclerView searchRV;
    private SearchViewAdapter adapter;

    private Database db;
    private List<TaskDataModel> allTasks;

    Fragment[] fragments = {new UpcomingTasksFragment(), new FinishedFragment()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("ToDo");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        db = new Database(MainActivity.this);
        tabLayout = findViewById(R.id.home_screen_tab_layout);
        viewPager = findViewById(R.id.home_screen_view_pager);
        addBtn = findViewById(R.id.home_screen_floating_btn);
        searchRV = findViewById(R.id.home_screen_search_rv);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewTask();
            }
        });

        /**adding new tabs*/
        tabLayout.addTab(tabLayout.newTab().setText("Upcoming"));
        tabLayout.addTab(tabLayout.newTab().setText("Finished"));
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
                List<TaskDataModel> newData = new ArrayList<>();
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
                Log.e(TAG, newText);
                return true;
            }
        });
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Log.e(TAG, "Search Clicked");
                allTasks = db.getAllTasks();
                allTasks.addAll(db.getAllFinished());
                if (Build.VERSION.SDK_INT >= 24) {
                    allTasks.sort(new Comparator<TaskDataModel>() {
                        @Override
                        public int compare(TaskDataModel taskDataModel, TaskDataModel t1) {
                            return taskDataModel.getTitle().compareTo(t1.getTitle());
                        }
                    });
                }
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
//        searchView.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e(TAG,"Search Clicked");
//                allTasks = db.getAllTasks();
//                adapter = new UpcomingTasksAdapter(MainActivity.this,allTasks);
//                searchRV.setVisibility(View.VISIBLE);
//                viewPager.setVisibility(View.INVISIBLE);
//                tabLayout.setVisibility(View.INVISIBLE);
//                searchRV.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//                searchRV.setAdapter(adapter);
//
//            }
//        });
//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                Log.e(TAG,"Search Bar Closed");
//                adapter = null;
//                searchRV.setVisibility(View.INVISIBLE);
//                viewPager.setVisibility(View.VISIBLE);
//                tabLayout.setVisibility(View.VISIBLE);
//                return true;
//            }
//        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home_menu_add) {
            addNewTask();
        }
        return true;
    }

    private void addNewTask() {
        Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
        startActivity(intent);
    }
}