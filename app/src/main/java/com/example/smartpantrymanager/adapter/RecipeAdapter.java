package com.example.smartpantrymanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.model.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final List<Recipe> recipeList;
    private final OnRecipeClickListener listener;

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public RecipeAdapter(
            List<Recipe> recipeList,
            OnRecipeClickListener listener
    ) {
        this.recipeList = recipeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecipeViewHolder holder,
            int position
    ) {

        Recipe recipe = recipeList.get(position);

        holder.textViewRecipeName.setText(recipe.getName());

        holder.itemView.setOnClickListener(v ->
                listener.onRecipeClick(recipe)
        );
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {

        TextView textViewRecipeName;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewRecipeName =
                    itemView.findViewById(R.id.textViewRecipeName);
        }
    }
}