package com.example.elemental;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ArrayList<LocalDate> daysOfMonth;
    public TextView dayOfMonth;
    private OnItemListener onItemListener;
    public View parentView;

    public CalendarViewHolder(@NonNull View itemView, OnItemListener onItemListener, ArrayList<LocalDate> daysOfMonth) {
        super(itemView);

        dayOfMonth = itemView.findViewById(R.id.cellDay);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
        this.daysOfMonth = daysOfMonth;
        parentView = itemView.findViewById(R.id.parentView);

    }
    @Override
    public void onClick(View view) {
        onItemListener.onItemClick(getAdapterPosition(), daysOfMonth.get(getAdapterPosition()));

    }
}
