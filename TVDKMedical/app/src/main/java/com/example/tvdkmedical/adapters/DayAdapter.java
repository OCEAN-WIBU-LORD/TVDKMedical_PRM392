package com.example.tvdkmedical.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tvdkmedical.models.Day;
import com.example.tvdkmedical.R;

import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.VH> {

    private Context context;
    private List<Day> days;
    private int selectedItem = RecyclerView.NO_POSITION;
    private OnDayClickListener onDayClickListener;
    private OnAddDayClickListener onAddDayClickListener;

   public void clearSelection() {
    // Assuming you have a way to track the selected state in your Day model or adapter
    // Reset the selected state for all days
       selectedItem = RecyclerView.NO_POSITION;

       notifyDataSetChanged(); // Notify the adapter to refresh the views
    }

    public interface OnDayClickListener {
        void onDayClick(int position, Day day);
    }

    public interface OnAddDayClickListener {
        void onAddDayClick(int position, Day day);
    }

    public DayAdapter(Context context, List<Day> days, OnAddDayClickListener onAddDayClickListener) {
        this.context = context;
        this.days = days;
        this.onAddDayClickListener = onAddDayClickListener;
    }
    public DayAdapter(Context context, List<Day> days, OnAddDayClickListener onAddDayClickListener,int selectedItem) {
        this.context = context;
        this.days = days;
        this.onAddDayClickListener = onAddDayClickListener;
        this.selectedItem = selectedItem;

    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_day, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = (int) (parent.getWidth() / 6.0f);
        view.setLayoutParams(layoutParams);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Day day = days.get(position);
        holder.setData(day);

        // Đặt màu nền và màu chữ dựa trên vị trí của item
        if (position == selectedItem) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
            holder.txtDay.setTextColor(context.getResources().getColor(R.color.colorBlue));
            holder.txtDayOfWeek.setTextColor(context.getResources().getColor(R.color.colorBlue));
            holder.txtDay.setTextSize(23);
            holder.txtDayOfWeek.setTextSize(23);
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
            holder.txtDay.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.txtDayOfWeek.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.txtDay.setTextSize(20);
            holder.txtDayOfWeek.setTextSize(20);
        }

        holder.itemView.setOnClickListener(v -> {
            // Lưu vị trí của item được chọn
            int previousItem = selectedItem;
            selectedItem = holder.getAdapterPosition();

            // Cập nhật item trước đó và item mới được chọn
            notifyItemChanged(previousItem);
            notifyItemChanged(selectedItem);

            // Gọi sự kiện nhấp chuột
            if (onDayClickListener != null) {
                onDayClickListener.onDayClick(position, day);
            }

            if (onAddDayClickListener != null) {
                onAddDayClickListener.onAddDayClick(position, day);
            }
        });


    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    protected class VH extends RecyclerView.ViewHolder {
        private TextView txtDay;
        private TextView txtDayOfWeek;

        private void bindingView() {
            txtDay = itemView.findViewById(R.id.txtDay);
            txtDayOfWeek = itemView.findViewById(R.id.txtDayofWeek);
        }

        public VH(@NonNull View v) {
            super(v);
            bindingView();
        }

        public void setData(Day day) {
            txtDay.setText(day.getDay());
            txtDayOfWeek.setText(day.getDayOfWeek());
        }
    }
}
