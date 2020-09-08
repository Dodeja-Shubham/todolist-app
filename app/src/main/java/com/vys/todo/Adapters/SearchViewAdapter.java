package com.vys.todo.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vys.todo.Data.Database;
import com.vys.todo.Data.TaskDataModel;
import com.vys.todo.R;

import java.util.List;

public class SearchViewAdapter extends RecyclerView.Adapter<SearchViewAdapter.MyViewHolder> {

    Context context;
    List<TaskDataModel> list;
    private final String TAG = "SearchViewAdapter";

    public SearchViewAdapter(Context context, List<TaskDataModel> data) {
        this.context = context;
        this.list = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_tasks_rv_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        try {
            holder.name.setText(list.get(position).getTitle());
            holder.date.setText(list.get(position).getDue_date().replace("GMT+05:30 ", ""));
            holder.category.setText(list.get(position).getCategory());

            holder.delete.setVisibility(View.GONE);
            holder.completed.setVisibility(View.GONE);

            if (list.get(position).getIs_completed()) {
                holder.completed.setVisibility(View.VISIBLE);
                holder.completed.setImageDrawable(context.getDrawable(R.drawable.baseline_done_all_black_24));
                holder.name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, list.get(position).getDue_date().replace("GMT+05:30 ", ""), Toast.LENGTH_LONG).show();
                    }
                });
                holder.date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, list.get(position).getDue_date().replace("GMT+05:30 ", ""), Toast.LENGTH_LONG).show();
                    }
                });
                holder.category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, list.get(position).getDue_date().replace("GMT+05:30 ", ""), Toast.LENGTH_LONG).show();
                    }
                });
                holder.completed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, list.get(position).getDue_date().replace("GMT+05:30 ", ""), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                holder.delete.setVisibility(View.VISIBLE);
                holder.delete.setImageDrawable(context.getDrawable(R.drawable.baseline_schedule_black_24));
                holder.name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "Task is scheduled on " + list.get(position).getDue_date().replace("GMT+05:30 ", ""), Toast.LENGTH_LONG).show();
                    }
                });
                holder.date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "Task is scheduled on " + list.get(position).getDue_date().replace("GMT+05:30 ", ""), Toast.LENGTH_LONG).show();
                    }
                });
                holder.category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "Task is scheduled on " + list.get(position).getDue_date().replace("GMT+05:30 ", ""), Toast.LENGTH_LONG).show();
                    }
                });
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "Task is scheduled on " + list.get(position).getDue_date().replace("GMT+05:30 ", ""), Toast.LENGTH_LONG).show();
                    }
                });
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, date, category;
        ImageButton delete, completed;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            delete = itemView.findViewById(R.id.ut_item_delete);
            name = itemView.findViewById(R.id.ut_item_name);
            date = itemView.findViewById(R.id.ut_item_date);
            category = itemView.findViewById(R.id.ut_item_category);
            completed = itemView.findViewById(R.id.ut_item_completed_cb);
        }
    }

    public void addNewData(TaskDataModel data) {
        this.list.add(data);
        notifyDataSetChanged();
    }

    public void setNewData(List<TaskDataModel> data) {
        this.list = data;
        notifyDataSetChanged();
    }
}
