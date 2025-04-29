package com.example.elakil.presentation.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.elakil.R;
import com.example.elakil.model.FilterItem;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {
    private List<FilterItem> filterItems ;
    private nFilterClickListener listener ;

    public interface nFilterClickListener{
        void nFilterClick(FilterItem filterItem);
    }

    public FilterAdapter(List<FilterItem> filterItems, nFilterClickListener listener) {
        this.filterItems = filterItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter_card , parent, false);
        return new FilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterAdapter.FilterViewHolder holder, int position) {

        FilterItem filterItem = filterItems.get(position);
        holder.textViewFilterName.setText(filterItem.getName());

        if (filterItem.getType().equals("area")){
            int resourceId = holder.itemView.getContext().getResources().getIdentifier(filterItem.getImageUrl(),"drawable", holder.itemView.getContext().getPackageName());
            if (resourceId != 0){
                holder.imageViewFilter.setImageResource(resourceId);
            }
            else {
                holder.imageViewFilter.setImageResource(android.R.drawable.ic_menu_help);
            }

        }else {
            Glide.with(holder.imageViewFilter.getContext())
                    .load(filterItem.getImageUrl())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(holder.imageViewFilter);
        }
        holder.itemView.setOnClickListener(v -> listener.nFilterClick(filterItem));
    }

    @Override
    public int getItemCount() {
        return filterItems.size();
    }

    public class FilterViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFilterName ;
        ImageView imageViewFilter;

        public FilterViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFilter = itemView.findViewById(R.id.imageViewFilter);
            textViewFilterName = itemView.findViewById(R.id.textViewFilterName);
        }
    }
}
