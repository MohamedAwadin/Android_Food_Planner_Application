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
import com.example.elakil.model.Ingredient;
import com.example.elakil.model.IngredientItem;

import java.time.temporal.Temporal;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<IngredientItem> ingredients ;

    public IngredientAdapter(List<IngredientItem> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent,false);

        return new IngredientViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.IngredientViewHolder holder, int position) {
        IngredientItem ingredient = ingredients.get(position);
        holder.textViewIngredientName.setText(ingredient.getName());
        holder.textViewMeasurement.setText(ingredient.getMeasurement());
        Glide.with(holder.imageViewIngredient.getContext())
                .load(ingredient.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.imageViewIngredient);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewIngredient ;
        TextView textViewIngredientName , textViewMeasurement ;
        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewIngredient = itemView.findViewById(R.id.imageViewIngredient);
            textViewIngredientName = itemView.findViewById(R.id.textViewIngredientName);
            textViewMeasurement = itemView.findViewById(R.id.textViewMeasurement);
        }
    }
}
