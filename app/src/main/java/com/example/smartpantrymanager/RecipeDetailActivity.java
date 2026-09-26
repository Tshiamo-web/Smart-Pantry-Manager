package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.model.Recipe;

public class RecipeDetailActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private TextView textViewRecipeDetailName;
    private TextView textViewRecipeIngredients;
    private TextView textViewRecipeSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_detail);

        Button buttonBackHome = findViewById(R.id.buttonBackHome);

        buttonBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeDetailActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        textViewRecipeDetailName =
                findViewById(R.id.textViewRecipeDetailName);

        textViewRecipeIngredients =
                findViewById(R.id.textViewRecipeIngredients);

        textViewRecipeSteps =
                findViewById(R.id.textViewRecipeSteps);

        databaseHelper =
                new DatabaseHelper(this);

        int recipeId =
                getIntent().getIntExtra(
                        "recipe_id",
                        -1
                );

        if (recipeId == -1) {

            Toast.makeText(
                    this,
                    "Recipe could not be found.",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        loadRecipe(recipeId);
    }


    // =========================================================
    // LOAD RECIPE
    // =========================================================

    private void loadRecipe(int recipeId) {

        Recipe recipe =
                databaseHelper.getRecipeById(recipeId);

        if (recipe == null) {

            Toast.makeText(
                    this,
                    "Recipe could not be found.",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }


        textViewRecipeDetailName.setText(
                recipe.getName()
        );


        textViewRecipeIngredients.setText(
                formatIngredients(
                        recipe.getIngredients()
                )
        );


        textViewRecipeSteps.setText(
                recipe.getSteps()
        );
    }


    // =========================================================
    // FORMAT INGREDIENTS
    // =========================================================

    private String formatIngredients(
            String ingredients
    ) {

        String[] ingredientList =
                ingredients.split(",");

        StringBuilder formattedIngredients =
                new StringBuilder();


        for (String ingredient :
                ingredientList) {

            ingredient =
                    ingredient.trim();


            /*
             * Recipe format:
             *
             * ingredient:quantity:unit
             *
             * Example:
             * egg:2:pcs
             */

            String[] parts =
                    ingredient.split(":");


            if (parts.length == 3) {

                String name =
                        formatIngredientName(
                                parts[0]
                        );

                String quantity =
                        parts[1].trim();

                String unit =
                        formatUnit(
                                parts[2]
                        );


                formattedIngredients
                        .append("• ")
                        .append(name)
                        .append(" — ")
                        .append(quantity)
                        .append(" ")
                        .append(unit)
                        .append("\n");

            } else {

                /*
                 * Fallback in case an older recipe
                 * does not use the new format.
                 */
                formattedIngredients
                        .append("• ")
                        .append(ingredient)
                        .append("\n");
            }
        }


        return formattedIngredients
                .toString()
                .trim();
    }


    // =========================================================
    // FORMAT INGREDIENT NAME
    // =========================================================

    private String formatIngredientName(
            String name
    ) {

        name =
                name.trim()
                        .toLowerCase();


        if (name.isEmpty()) {
            return name;
        }


        /*
         * Capitalise the first letter.
         *
         * egg -> Egg
         * tomato -> Tomato
         * peanut butter -> Peanut butter
         */

        return name.substring(0, 1).toUpperCase()
                + name.substring(1);
    }


    // =========================================================
    // FORMAT UNIT
    // =========================================================

    private String formatUnit(
            String unit
    ) {

        String normalized =
                unit.trim().toLowerCase();


        if (normalized.equals("pcs")) {
            return "pcs";
        }


        if (normalized.equals("tbsp")) {
            return "tbsp";
        }


        if (normalized.equals("tsp")) {
            return "tsp";
        }


        if (normalized.equals("cup")) {
            return "cup";
        }


        if (normalized.equals("slice")) {
            return "slice";
        }


        if (normalized.equals("kg")) {
            return "kg";
        }


        if (normalized.equals("g")) {
            return "g";
        }


        if (normalized.equals("l")) {
            return "L";
        }


        if (normalized.equals("ml")) {
            return "ml";
        }


        return unit.trim();
    }
}
