package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//this connects a list of PantryItem objects to the RecyclerView on the Pantry List screen.//
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private List<PantryItems> pantryItems;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(PantryItems item);
    }

    public PantryAdapter(List<PantryItems> pantryItems, OnItemClickListener listener) {
        this.pantryItems = pantryItems;
        this.listener = listener;
    }

    //this is only called when a new row needs to be created
    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_row, parent, false);
        return new PantryViewHolder(view);
    }

    //this is called when a row that has been created needs to show data for a specific item
    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItems item = pantryItems.get(position);

        holder.textItemName.setText(item.getName());

        String details = item.getQuantity() + " " + item.getUnit();
        if (item.getExpiryDate() != null && !item.getExpiryDate().isEmpty()) {
            details += "  ·  Expires: " + item.getExpiryDate();
        }
        holder.textItemDetails.setText(details);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return pantryItems.size();
    }

    //this updates the data in the list in the pantry
    public void updateData(List<PantryItems> newItems) {
        this.pantryItems = newItems;
        notifyDataSetChanged();
    }

    static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView textItemName;
        TextView textItemDetails;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            textItemName = itemView.findViewById(R.id.textItemName);
            textItemDetails = itemView.findViewById(R.id.textItemDetails);
        }
    }
}