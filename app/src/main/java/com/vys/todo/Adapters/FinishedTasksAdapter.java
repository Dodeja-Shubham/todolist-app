package com.vys.todo.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.vys.todo.APIModels.TaskResponse;
import com.vys.todo.R;
import java.util.List;

public class FinishedTasksAdapter extends RecyclerView.Adapter<FinishedTasksAdapter.MyViewHolder> {

    Context context;
    List<TaskResponse> list;
    private final String TAG = "FinishedTasksAdapter";

    public FinishedTasksAdapter(Context context, List<TaskResponse> data) {
        this.context = context;
        this.list = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_tasks_rv_item, parent, false);
        return new MyViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        try {
            holder.name.setText(list.get(position).getTitle());
            holder.date.setText(list.get(position).getDueDate());
            holder.category.setText(list.get(position).getCategory());
            holder.completed.setImageDrawable(context.getDrawable(R.drawable.ic_done_all));
            holder.completed.setColorFilter(context.getColor(android.R.color.holo_green_light), android.graphics.PorterDuff.Mode.MULTIPLY);
            holder.colorHolder.setCardBackgroundColor(Color.parseColor(list.get(position).getColour()));
            holder.desc.setText(list.get(position).getDesc());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, date, category,desc;
        ImageView delete, completed;
        CardView colorHolder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            delete = itemView.findViewById(R.id.ut_item_delete);
            name = itemView.findViewById(R.id.ut_item_name);
            date = itemView.findViewById(R.id.ut_item_date);
            category = itemView.findViewById(R.id.ut_item_category);
            completed = itemView.findViewById(R.id.ut_item_completed_cb);
            colorHolder = itemView.findViewById(R.id.ut_item_color);
            desc = itemView.findViewById(R.id.ut_item_desc);
        }
    }

    public void addNewData(TaskResponse data) {
        this.list.add(data);
        notifyDataSetChanged();
    }

    public void setNewData(List<TaskResponse> data) {
        this.list = data;
        notifyDataSetChanged();
    }
}
