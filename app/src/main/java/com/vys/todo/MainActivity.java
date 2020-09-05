package com.vys.todo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.view.View;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.vys.todo.Fragments.FinishedFragment;
import com.vys.todo.Fragments.UpcomingTasksFragment;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    Fragment[] fragments = {new UpcomingTasksFragment(),new FinishedFragment()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("ToDo");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.home_screen_tab_layout);
        viewPager = findViewById(R.id.home_screen_view_pager);

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
        getMenuInflater().inflate(R.menu.home_screen_menu,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView;
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e(TAG,query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e(TAG,newText);
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG,"Search Clicked");
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.e(TAG,"Search Bar Closed");
                return false;
            }
        });
        return true;
    }
}