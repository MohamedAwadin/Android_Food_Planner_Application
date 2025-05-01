package com.example.elakil.presentation.favorites;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.elakil.R;
import com.example.elakil.model.Meal;

import java.util.List;

public class FavoriteMealAdapter extends RecyclerView.Adapter<FavoriteMealAdapter.FavoriteMealViewHolder> {
    private List<Meal> favoriteMeals ;
    private OnFavoriteMealClickListener listener ;

    public interface OnFavoriteMealClickListener{
        void OnMealClick(Meal meal);
        void OnRemoveMeal(Meal meal);

    }

    public FavoriteMealAdapter(List<Meal> favoriteMeals, OnFavoriteMealClickListener listener) {
        this.favoriteMeals = favoriteMeals;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoriteMealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_meal , parent , false);

        return new FavoriteMealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteMealAdapter.FavoriteMealViewHolder holder, int position) {
        Meal meal = favoriteMeals.get(position);
        holder.textViewMealName.setText(meal.getStrMeal());
        Glide.with(holder.imageViewMeal.getContext())
                .load(meal.getStrMealThumb())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.imageViewMeal);

        holder.itemView.setOnClickListener(v -> listener.OnMealClick(meal));
        holder.buttonRemove.setOnClickListener(v -> listener.OnRemoveMeal(meal));

    }

    @Override
    public int getItemCount() {
        return favoriteMeals.size();
    }

    public class FavoriteMealViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewMeal ;
        TextView textViewMealName ;
        Button buttonRemove ;
        public FavoriteMealViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewMeal = itemView.findViewById(R.id.imageViewMeal);
            textViewMealName = itemView.findViewById(R.id.textViewMealName);
            buttonRemove = itemView.findViewById(R.id.buttonRemove);
        }
    }
}
