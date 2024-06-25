package com.example.tvdkmedical.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdkmedical.R;

import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.VH> {

    private Context context;
    private List<String> times;
    private List<String> disabledTimes; // Danh sách giờ không thể chọn
    private int selectedItem = RecyclerView.NO_POSITION; // Vị trí item được chọn, ban đầu không có item nào được chọn

    public TimeAdapter(Context context, List<String> times) {
        this.context = context;
        this.times = times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
        notifyDataSetChanged(); // Cập nhật lại RecyclerView khi danh sách giờ có thể chọn thay đổi
    }

    public void setDisabledTimes(List<String> disabledTimes) {
        this.disabledTimes = disabledTimes;
        notifyDataSetChanged(); // Cập nhật lại RecyclerView khi danh sách giờ không thể chọn thay đổi
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_time, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = (int) (parent.getWidth() / 4.0f);
        view.setLayoutParams(layoutParams);
        return new VH(view);


    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        String time = times.get(position);
        holder.setData(time);

        // Kiểm tra nếu thời gian đang được xem xét là thời gian không thể chọn
        if (disabledTimes != null && disabledTimes.contains(time)) {
            holder.setEnabled(false); // Vô hiệu hóa item trong trường hợp không thể chọn
        } else {
            holder.setEnabled(true);
        }

        // Kiểm tra xem item có phải là item được chọn hay không
        if (position == selectedItem) {
            holder.setSelected(true); // Đặt background màu xanh, chữ màu trắng
        } else {
            holder.setSelected(false); // Đặt lại background mặc định
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lưu vị trí item được chọn
                int previousSelectedItem = selectedItem;
                selectedItem = holder.getAdapterPosition();

                // Thông báo cập nhật lại giao diện cho item được chọn và item trước đó
                notifyItemChanged(selectedItem);
                notifyItemChanged(previousSelectedItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return times.size();
    }

    protected class VH extends RecyclerView.ViewHolder {
        private TextView txtTime;

        public VH(@NonNull View v) {
            super(v);
            txtTime = v.findViewById(R.id.txtTime);
        }

        public void setData(String time) {
            txtTime.setText(time);
        }

        public void setEnabled(boolean enabled) {
            itemView.setEnabled(enabled);
            itemView.setAlpha(enabled ? 1.0f : 0.5f); // Điều chỉnh độ mờ để chỉ ra tính khả dụng
        }

        public void setSelected(boolean selected) {
            if (selected) {
                itemView.setBackgroundResource(R.color.light_blue_600); // Thiết lập background màu xanh
                txtTime.setTextColor(context.getResources().getColor(android.R.color.white)); // Thiết lập màu chữ trắng
            } else {
                itemView.setBackgroundResource(android.R.color.transparent); // Đặt lại background mặc định
                txtTime.setTextColor(context.getResources().getColor(android.R.color.black)); // Đặt lại màu chữ mặc định
            }
        }
    }
}
