package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.adapter.RecipeAdapter;
import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.model.PantryItem;
import com.example.smartpantrymanager.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerViewRecipes;
    private TextView textViewNoRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_suggested_recipes);

        databaseHelper = new DatabaseHelper(this);

        recyclerViewRecipes =
                findViewById(R.id.recyclerViewRecipes);

        textViewNoRecipes =
                findViewById(R.id.textViewNoRecipes);


        Button buttonBackHome = findViewById(R.id.buttonBackHome);

        buttonBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(
                    SuggestedRecipesActivity.this,
                    MainActivity.class
            );

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        recyclerViewRecipes.setLayoutManager(
                new LinearLayoutManager(this)
        );

        loadSuggestedRecipes();
    }


    // =========================================================
    // LOAD SUGGESTED RECIPES
    // =========================================================

    private void loadSuggestedRecipes() {

        List<PantryItem> pantryItems =
                databaseHelper.getAllPantryItems();

        List<Recipe> allRecipes =
                databaseHelper.getAllRecipes();

        List<Recipe> matchingRecipes =
                new ArrayList<>();


        for (Recipe recipe : allRecipes) {

            if (canMakeRecipe(recipe, pantryItems)) {

                matchingRecipes.add(recipe);
            }
        }


        // No matching recipes
        if (matchingRecipes.isEmpty()) {

            recyclerViewRecipes.setVisibility(
                    RecyclerView.GONE
            );

            textViewNoRecipes.setText(
                    "No recipes match your pantry yet.\n\n" +
                            "Add more ingredients to discover recipes! 💗"
            );

            textViewNoRecipes.setVisibility(
                    TextView.VISIBLE
            );

        } else {

            recyclerViewRecipes.setVisibility(
                    RecyclerView.VISIBLE
            );

            textViewNoRecipes.setVisibility(
                    TextView.GONE
            );


            RecipeAdapter adapter =
                    new RecipeAdapter(
                            matchingRecipes,
                            recipe -> {

                                Intent intent =
                                        new Intent(
                                                SuggestedRecipesActivity.this,
                                                RecipeDetailActivity.class
                                        );

                                intent.putExtra(
                                        "recipe_id",
                                        recipe.getId()
                                );

                                startActivity(intent);
                            }
                    );


            recyclerViewRecipes.setAdapter(adapter);
        }
    }


    // =========================================================
    // CHECK WHETHER RECIPE CAN BE MADE
    // =========================================================

    private boolean canMakeRecipe(
            Recipe recipe,
            List<PantryItem> pantryItems
    ) {

        String ingredients =
                recipe.getIngredients();

        String[] requiredIngredients =
                ingredients.split(",");


        /*
         * EVERY required ingredient must be available
         * in the required quantity and a compatible unit.
         */
        for (String requiredIngredient :
                requiredIngredients) {

            requiredIngredient =
                    requiredIngredient.trim();


            /*
             * New format:
             *
             * ingredient:quantity:unit
             *
             * Example:
             *
             * egg:2:pcs
             */
            String[] parts =
                    requiredIngredient.split(":");


            if (parts.length != 3) {

                return false;
            }


            String requiredName =
                    normalizeIngredientName(
                            parts[0]
                    );


            double requiredQuantity;

            try {

                requiredQuantity =
                        Double.parseDouble(
                                parts[1]
                        );

            } catch (NumberFormatException e) {

                return false;
            }


            String requiredUnit =
                    normalizeUnit(
                            parts[2]
                    );


            boolean ingredientFound =
                    false;


            // Search pantry for the required ingredient
            for (PantryItem pantryItem :
                    pantryItems) {

                String pantryName =
                        normalizeIngredientName(
                                pantryItem.getName()
                        );


                if (!pantryName.equals(
                        requiredName
                )) {

                    continue;
                }


                String pantryUnit =
                        normalizeUnit(
                                pantryItem.getUnit()
                        );


                /*
                 * Convert pantry quantity into
                 * the recipe's unit.
                 */
                double convertedQuantity =
                        convertQuantity(
                                pantryItem.getQuantity(),
                                pantryUnit,
                                requiredUnit
                        );


                /*
                 * -1 means the units are incompatible.
                 */
                if (convertedQuantity < 0) {

                    continue;
                }


                /*
                 * Pantry quantity must be at least
                 * the quantity required by the recipe.
                 */
                if (convertedQuantity >=
                        requiredQuantity) {

                    ingredientFound = true;
                    break;
                }
            }


            /*
             * If even ONE required ingredient
             * is missing, the recipe cannot be made.
             */
            if (!ingredientFound) {

                return false;
            }
        }


        /*
         * Every ingredient passed the check.
         */
        return true;
    }


    // =========================================================
    // NORMALIZE INGREDIENT NAMES
    // =========================================================

    private String normalizeIngredientName(
            String ingredientName
    ) {

        String name =
                ingredientName
                        .trim()
                        .toLowerCase();


        /*
         * Simple plural handling.
         *
         * Example:
         * eggs -> egg
         * potatoes -> potato
         * tomatoes -> tomato
         * berries -> berry
         */

        if (name.endsWith("ies")
                && name.length() > 3) {

            name =
                    name.substring(
                            0,
                            name.length() - 3
                    ) + "y";

        } else if (name.endsWith("oes")
                && name.length() > 3) {

            name =
                    name.substring(
                            0,
                            name.length() - 2
                    );

        } else if (name.endsWith("es")
                && name.length() > 2) {

            name =
                    name.substring(
                            0,
                            name.length() - 2
                    );

        } else if (name.endsWith("s")
                && name.length() > 1) {

            name =
                    name.substring(
                            0,
                            name.length() - 1
                    );
        }


        return name;
    }


    // =========================================================
    // NORMALIZE UNITS
    // =========================================================

    private String normalizeUnit(
            String unit
    ) {

        String normalized =
                unit
                        .trim()
                        .toLowerCase();


        // Pieces
        if (normalized.equals("piece")
                || normalized.equals("pieces")
                || normalized.equals("pc")
                || normalized.equals("pcs")
                || normalized.equals("item")
                || normalized.equals("items")) {

            return "pcs";
        }


        // Cups
        if (normalized.equals("cup")
                || normalized.equals("cups")) {

            return "cup";
        }


        // Tablespoons
        if (normalized.equals("tbsp")
                || normalized.equals("tablespoon")
                || normalized.equals("tablespoons")) {

            return "tbsp";
        }


        // Teaspoons
        if (normalized.equals("tsp")
                || normalized.equals("teaspoon")
                || normalized.equals("teaspoons")) {

            return "tsp";
        }


        // Kilograms
        if (normalized.equals("kg")
                || normalized.equals("kilogram")
                || normalized.equals("kilograms")) {

            return "kg";
        }


        // Grams
        if (normalized.equals("g")
                || normalized.equals("gram")
                || normalized.equals("grams")) {

            return "g";
        }


        // Litres
        if (normalized.equals("l")
                || normalized.equals("litre")
                || normalized.equals("litres")
                || normalized.equals("liter")
                || normalized.equals("liters")) {

            return "l";
        }


        // Millilitres
        if (normalized.equals("ml")
                || normalized.equals("millilitre")
                || normalized.equals("millilitres")
                || normalized.equals("milliliter")
                || normalized.equals("milliliters")) {

            return "ml";
        }


        // Slices
        if (normalized.equals("slice")
                || normalized.equals("slices")) {

            return "slice";
        }


        return normalized;
    }


    // =========================================================
    // UNIT CONVERSION
    // =========================================================

    private double convertQuantity(
            double quantity,
            String fromUnit,
            String toUnit
    ) {

        /*
         * Same unit.
         */
        if (fromUnit.equals(toUnit)) {

            return quantity;
        }


        // =====================================================
        // WEIGHT
        // =====================================================

        if (fromUnit.equals("kg")
                && toUnit.equals("g")) {

            return quantity * 1000;
        }


        if (fromUnit.equals("g")
                && toUnit.equals("kg")) {

            return quantity / 1000;
        }


        // =====================================================
        // LIQUID
        // =====================================================

        if (fromUnit.equals("l")
                && toUnit.equals("ml")) {

            return quantity * 1000;
        }


        if (fromUnit.equals("ml")
                && toUnit.equals("l")) {

            return quantity / 1000;
        }


        // =====================================================
        // VOLUME
        // =====================================================

        /*
         * 1 cup = 16 tablespoons
         */
        if (fromUnit.equals("cup")
                && toUnit.equals("tbsp")) {

            return quantity * 16;
        }


        if (fromUnit.equals("tbsp")
                && toUnit.equals("cup")) {

            return quantity / 16;
        }


        /*
         * 1 tablespoon = 3 teaspoons
         */
        if (fromUnit.equals("tbsp")
                && toUnit.equals("tsp")) {

            return quantity * 3;
        }


        if (fromUnit.equals("tsp")
                && toUnit.equals("tbsp")) {

            return quantity / 3;
        }


        /*
         * Direct cup -> teaspoon.
         *
         * 1 cup = 48 teaspoons
         */
        if (fromUnit.equals("cup")
                && toUnit.equals("tsp")) {

            return quantity * 48;
        }


        if (fromUnit.equals("tsp")
                && toUnit.equals("cup")) {

            return quantity / 48;
        }


        // =====================================================
        // INCOMPATIBLE UNITS
        // =====================================================

        return -1;
    }


    // =========================================================
    // REFRESH WHEN RETURNING TO SCREEN
    // =========================================================

    @Override
    protected void onResume() {

        super.onResume();

        loadSuggestedRecipes();
    }
}
