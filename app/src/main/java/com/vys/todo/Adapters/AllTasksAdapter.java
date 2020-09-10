package com.vys.todo.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.vys.todo.APIModels.TaskResponse;
import com.vys.todo.R;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AllTasksAdapter extends RecyclerView.Adapter<AllTasksAdapter.MyViewHolder> {

    Context context;
    List<TaskResponse> list;
    private final String TAG = "AllTasksAdapter";

    public AllTasksAdapter(Context context, List<TaskResponse> data) {
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            holder.name.setText(list.get(position).getTitle());
            holder.date.setText(list.get(position).getDueDate());
            holder.category.setText(list.get(position).getCategory());
            if(list.get(position).getIsCompleted()){
                holder.completed.setImageDrawable(context.getDrawable(R.drawable.ic_done_all));
                holder.date.setText(list.get(position).getDueDate());
            }else if(dateMissed(list.get(position).getDueDate())){
                holder.completed.setImageDrawable(context.getDrawable(R.drawable.ic_error));
            }else {
                holder.completed.setImageDrawable(context.getDrawable(R.drawable.ic_clock));
            }
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
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

    private Date stringToDate(String aDate, String aFormat) {
        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        return simpledateformat.parse(aDate, pos);
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
