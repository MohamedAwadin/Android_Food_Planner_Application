package com.example.elakil.presentation.plan;

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

public class PlanMealAdapter extends RecyclerView.Adapter<PlanMealAdapter.PlanMealViewHolder> {
    private List<Meal> plannedMeals ;
    private List<String> days ;
    private OnPlanMealClickListener listener ;

    public interface OnPlanMealClickListener{
        void OnMealClick(Meal meal);
    }

    public PlanMealAdapter(List<Meal> plannedMeals, List<String> days, OnPlanMealClickListener listener) {
        this.plannedMeals = plannedMeals;
        this.days = days;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlanMealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan_meal,parent , false);

        return new PlanMealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanMealAdapter.PlanMealViewHolder holder, int position) {
        Meal meal = plannedMeals.get(position);
        String day = days.get(position);
        holder.textViewDay.setText(day);
        holder.textViewMealName.setText(meal.getStrMeal());
        Glide.with(holder.imageViewMeal.getContext())
                .load(meal.getStrMealThumb())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.imageViewMeal);
        holder.itemView.setOnClickListener(v -> listener.OnMealClick(meal));

    }

    @Override
    public int getItemCount() {
        return plannedMeals.size();
    }

    public class PlanMealViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDay , textViewMealName ;
        ImageView imageViewMeal ;
        public PlanMealViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDay = itemView.findViewById(R.id.textViewDay);
            textViewMealName = itemView.findViewById(R.id.textViewMealName);
            imageViewMeal = itemView.findViewById(R.id.imageViewMeal);
        }
    }
}
