package com.example.finalproject;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.ViewHolder> {
    private Context context;
    private List<AssignmentItem> assignmentItems;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AssignmentItem item);
    }

    public AssignmentAdapter(Context context, List<AssignmentItem> assignmentItems, OnItemClickListener listener) {
        this.context = context;
        this.assignmentItems = assignmentItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_assignment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // --- CHANGE 3: Get the AssignmentItem for the current position ---
        AssignmentItem currentItem = assignmentItems.get(position);
        Assignment assignment = currentItem.getAssignment(); // Get the pure assignment from the wrapper
        String status = currentItem.getSubmissionStatus(); // Get the status from the wrapper

        // Now populate the views
        holder.title.setText(assignment.getTitle());
        holder.desc.setText(assignment.getDescription());
        holder.dueDate.setText(assignment.getDueDate()); // The due date is already formatted
        holder.status.setText(status);

        // Set color based on status (the status text is already nicely formatted)
        // Note: Compare against the formatted status string.
        switch (status.trim().toLowerCase()) {
            case "graded":
                holder.status.setTextColor(Color.parseColor("#008000")); // Green
                break;
            case "submitted":
                holder.status.setTextColor(Color.parseColor("#0000FF")); // Blue
                break;
            case "late":
                holder.status.setTextColor(Color.parseColor("#FF0000")); // Red
                break;
            case "pending":
                holder.status.setTextColor(Color.parseColor("#FFA500")); // Orange
                break;
            default:
                holder.status.setTextColor(Color.BLACK);
        }


        holder.itemView.setOnClickListener(v -> listener.onItemClick(currentItem));
    }

    @Override
    public int getItemCount() {
        return assignmentItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc, dueDate, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.assignmentTitle);
            desc = itemView.findViewById(R.id.assignmentDesc);
            dueDate = itemView.findViewById(R.id.assignmentDue);
            status = itemView.findViewById(R.id.assignmentStatus);
        }
    }
}