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
import com.example.tvdkmedical.models.Appointment;

import java.util.List;

public class AllAppointmentAdapter extends RecyclerView.Adapter<AllAppointmentAdapter.ViewHolder> {

    private Context context;
    private List<Appointment> appointments;
    public AllAppointmentAdapter(Context context, List<Appointment> appointments) {
        this.context = context;
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.appointment_all, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment a = appointments.get(position);
        holder.setData(a);
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtStartTime;
        private TextView txtEndTime;
        private TextView txtDoctorName;
        private TextView txtDoctorInfo;
        private TextView txtDateBooking;

        private void bindingView() {
            txtStartTime = itemView.findViewById(R.id.appointmentStartTime);
            txtEndTime = itemView.findViewById(R.id.appointmentEndTime);
            txtDoctorName = itemView.findViewById(R.id.doctorNameAppointment);
            txtDoctorInfo = itemView.findViewById(R.id.doctorInforAppointment);
            txtDateBooking = itemView.findViewById(R.id.dateBooking);
        }

        private void bindingAction() {
        }

        private void onItemViewClick(View view) {
        }

        public ViewHolder(@NonNull View v) {
            super(v);
            bindingView();
            bindingAction();
        }

        public void setData(Appointment appointment) {
            txtStartTime.setText(appointment.getStartTime().toString());
            txtEndTime.setText(appointment.getEndTime().toString());
            txtDoctorName.setText(appointment.getDoctor().getName());
            txtDoctorInfo.setText(appointment.getDoctor().getInforDoctor());
            txtDateBooking.setText(appointment.getDateBooking().toString());
        }
    }
}

