package com.example.elakil.presentation.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.bumptech.glide.Glide;
import com.example.elakil.R;
import com.example.elakil.model.Meal;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {

    private List<Meal> mealList ;
    private OnMealClickListener listener;

    private  boolean isLargeLayout ;


    public interface OnMealClickListener{
        void onMealClick(Meal meal);
    }

    public MealAdapter(List<Meal> mealList, OnMealClickListener listener , boolean isLargeLayout){
        this.mealList = mealList;
        this.listener =listener;
        this.isLargeLayout = isLargeLayout ;
    }

    @NonNull
    @Override
    public MealAdapter.MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = isLargeLayout ? R.layout.item_meal_large : R.layout.item_meal_small;
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealAdapter.MealViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.textViewMealName.setText(meal.getStrMeal());
        Glide.with(holder.imageViewMeal.getContext())
                .load(meal.getStrMealThumb())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.imageViewMeal)
        ;
        holder.itemView.setOnClickListener(v -> listener.onMealClick(meal));
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public class MealViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewMeal ;
        TextView textViewMealName ;
        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewMeal = itemView.findViewById(R.id.imageViewMeal);
            textViewMealName = itemView.findViewById(R.id.textViewMealName);
        }
    }
}
