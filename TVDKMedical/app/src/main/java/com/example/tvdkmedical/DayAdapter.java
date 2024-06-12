package com.example.tvdkmedical;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdkmedical.Day;

import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.VH> {

    private Context context;
    private List<Day> days;
    private int selectedItem = RecyclerView.NO_POSITION;

    public DayAdapter(Context context, List<Day> days) {
        this.context = context;
        this.days = days;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lưu vị trí của item được chọn
                int previousItem = selectedItem;
                selectedItem = holder.getAdapterPosition();

                // Cập nhật item trước đó và item mới được chọn
                notifyItemChanged(previousItem);
                notifyItemChanged(selectedItem);
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

        private void bindingAction() {
        }

        private void onItemViewClick(View view) {
        }

        public VH(@NonNull View v) {
            super(v);
            bindingView();
            bindingAction();
        }

        public void setData(Day day) {
            txtDay.setText(day.getDay());
            txtDayOfWeek.setText(day.getDayOfWeek());
        }
    }
}

