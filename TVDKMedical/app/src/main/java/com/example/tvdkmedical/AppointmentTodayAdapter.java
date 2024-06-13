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
import com.example.tvdkmedical.models.Doctor;

import java.util.List;

public class AppointmentTodayAdapter extends RecyclerView.Adapter<AppointmentTodayAdapter.ViewHolder> {

    private Context context;
    private List<Appointment> appointments;
    private List<Doctor> doctors;
    public AppointmentTodayAdapter(Context context, List<Appointment> appointments, List<Doctor> doctors) {
        this.context = context;
        this.appointments = appointments;
        this.doctors = doctors;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.appointment_today, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment a = appointments.get(position);
        Doctor d = findDoctorById(Integer.parseInt(a.getDoctorId())); // Find the doctor corresponding to the appointment
        holder.setData(a, d);
    }
    public Doctor findDoctorById(int doctorId) {
        for (Doctor doctor : doctors) {
            if (doctor.getDoctorId() == doctorId) {
                return doctor;
            }
        }
        return null;
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

        private void bindingView() {
            txtStartTime = itemView.findViewById(R.id.startTime);
            txtEndTime = itemView.findViewById(R.id.endTime);
            txtDoctorName = itemView.findViewById(R.id.doctorName);
            txtDoctorInfo = itemView.findViewById(R.id.doctorInfor);
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

        public void setData(Appointment appointment, Doctor doctor) {
            txtStartTime.setText(appointment.getStartTime().toString());
            txtEndTime.setText(appointment.getEndTime().toString());
            if (doctor != null) {
                txtDoctorName.setText(doctor.getName());
                txtDoctorInfo.setText(doctor.getInforDoctor());
            } else {
                txtDoctorName.setText("Unknown Doctor");
                txtDoctorInfo.setText("");
            }
        }


    }
}

