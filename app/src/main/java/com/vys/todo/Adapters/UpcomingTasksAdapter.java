package com.vys.todo.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vys.todo.Data.TaskDataModel;
import com.vys.todo.R;
import java.util.List;

public class UpcomingTasksAdapter extends RecyclerView.Adapter<UpcomingTasksAdapter.MyViewHolder> {

    Context context;
    List<TaskDataModel> list;
    private final String TAG = "UpcomingTasksAdapter";

    public UpcomingTasksAdapter(Context context, List<TaskDataModel> data) {
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            holder.name.setText(list.get(position).getTitle());
            holder.date.setText(list.get(position).getDue_date().replace("GMT+05:30 ",""));
            holder.category.setText(list.get(position).getCategory());
            holder.completed.setImageDrawable(context.getDrawable(R.drawable.ic_clock));
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
        ImageView delete, completed;

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
