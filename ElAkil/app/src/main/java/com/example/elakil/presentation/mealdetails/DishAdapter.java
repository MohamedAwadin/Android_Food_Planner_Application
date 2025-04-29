package com.example.elakil.presentation.mealdetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.elakil.R;
import com.example.elakil.model.Meal;

import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {
    private List<Meal> dishes;
    private OnDishClickListener listener;

    public interface OnDishClickListener{
        void OnDishClick(Meal meal);
    }

    public DishAdapter(List<Meal> dishes, OnDishClickListener listener) {
        this.dishes = dishes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dish_card , parent ,false);

        return new DishViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull DishAdapter.DishViewHolder holder, int position) {
        Meal meal = dishes.get(position);
        holder.textViewDishName.setText(meal.getStrMeal());
        Glide.with(holder.imageViewDish.getContext())
                .load(meal.getStrMealThumb())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.imageViewDish);

        holder.itemView.setOnClickListener(v -> listener.OnDishClick(meal));

    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    public class DishViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewDish;
        TextView textViewDishName;
        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewDish = itemView.findViewById(R.id.imageViewDish);
            textViewDishName = itemView.findViewById(R.id.textViewDishName);
        }
    }
}
