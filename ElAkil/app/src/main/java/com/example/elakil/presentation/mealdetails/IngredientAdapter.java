package com.example.elakil.presentation.mealdetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elakil.R;

import java.time.temporal.Temporal;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private List<String> ingredients ;
    private List<String> measurements ;


    public IngredientAdapter(List<String> ingredients, List<String> measurements) {
        this.ingredients = ingredients;
        this.measurements = measurements;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent , false);


        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.IngredientViewHolder holder, int position) {



    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewIngredient ;
        TextView textViewIngredient, textViewMeasurments ;
        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
