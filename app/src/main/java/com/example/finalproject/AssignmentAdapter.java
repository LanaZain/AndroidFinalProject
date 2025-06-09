package com.example.finalproject;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.ViewHolder> {
    private Context context;
    private List<Assignment> assignments;
    private OnItemClickListener listener;

    // Interface for handling item clicks
    public interface OnItemClickListener {
        void onItemClick(Assignment assignment);
    }

    // Adapter constructor
    public AssignmentAdapter(Context context, List<Assignment> assignments, OnItemClickListener listener) {
        this.context = context;
        this.assignments = assignments;
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
        Assignment assignment = assignments.get(position);

        holder.title.setText(assignment.getTitle());
        holder.desc.setText(assignment.getDescription());
        holder.dueDate.setText("Due: " + assignment.getDueDate());
        holder.status.setText(assignment.getStatus());

        // Set color based on status
        switch (assignment.getStatus()) {
            case "Graded":
                holder.status.setTextColor(Color.parseColor("#008000")); // Green
                break;
            case "Pending":
                holder.status.setTextColor(Color.parseColor("#FFD700")); // Yellow
                break;
            case "Submitted":
                holder.status.setTextColor(Color.parseColor("#0000FF")); // Blue
                break;
            case "Late":
                holder.status.setTextColor(Color.parseColor("#FF0000")); // Red
                break;
            default:
                holder.status.setTextColor(Color.BLACK);
        }

        // Set click listener
        holder.itemView.setOnClickListener(v -> listener.onItemClick(assignment));
    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }

    // ViewHolder
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
