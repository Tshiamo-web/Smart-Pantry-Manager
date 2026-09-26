package com.example.smartpantrymanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.smartpantrymanager.model.PantryItem;
import com.example.smartpantrymanager.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry_manager.db";
    private static final int DATABASE_VERSION = 4;

    // =========================
    // PANTRY TABLE
    // =========================

    public static final String TABLE_PANTRY = "pantry_items";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_EXPIRY_DATE = "expiry_date";

    private static final String CREATE_PANTRY_TABLE =
            "CREATE TABLE " + TABLE_PANTRY + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_QUANTITY + " REAL NOT NULL, " +
                    COLUMN_UNIT + " TEXT NOT NULL, " +
                    COLUMN_EXPIRY_DATE + " TEXT" +
                    ")";


    // =========================
    // RECIPE TABLE
    // =========================

    public static final String TABLE_RECIPES = "recipes";

    public static final String COLUMN_RECIPE_ID = "recipe_id";
    public static final String COLUMN_RECIPE_NAME = "recipe_name";
    public static final String COLUMN_RECIPE_INGREDIENTS = "ingredients";
    public static final String COLUMN_RECIPE_STEPS = "steps";

    private static final String CREATE_RECIPE_TABLE =
            "CREATE TABLE " + TABLE_RECIPES + " (" +
                    COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                    COLUMN_RECIPE_INGREDIENTS + " TEXT NOT NULL, " +
                    COLUMN_RECIPE_STEPS + " TEXT NOT NULL" +
                    ")";


    // =========================
    // CONSTRUCTOR
    // =========================

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // =========================
    // DATABASE CREATION
    // =========================

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_PANTRY_TABLE);
        db.execSQL(CREATE_RECIPE_TABLE);

        seedRecipes(db);
    }


    // =========================
    // DATABASE UPGRADE
    // =========================

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        /*
         * Version 2 introduced the recipe table.
         */
        if (oldVersion < 2) {
            db.execSQL(CREATE_RECIPE_TABLE);
        }

        /*
         * Version 4 replaces the old repetitive recipes
         * with the new varied recipe collection.
         *
         * Pantry data is NOT deleted.
         */
        if (oldVersion < 4) {

            db.delete(
                    TABLE_RECIPES,
                    null,
                    null
            );

            seedRecipes(db);
        }
    }


    // =========================================================
    // PANTRY METHODS
    // =========================================================

    public long addPantryItem(PantryItem item) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(
                COLUMN_NAME,
                item.getName()
        );

        values.put(
                COLUMN_QUANTITY,
                item.getQuantity()
        );

        values.put(
                COLUMN_UNIT,
                item.getUnit()
        );

        values.put(
                COLUMN_EXPIRY_DATE,
                item.getExpiryDate()
        );

        long id = db.insert(
                TABLE_PANTRY,
                null,
                values
        );

        db.close();

        return id;
    }


    public List<PantryItem> getAllPantryItems() {

        List<PantryItem> pantryItems =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_PANTRY,
                null,
                null,
                null,
                null,
                null,
                COLUMN_ID + " ASC"
        );

        if (cursor.moveToFirst()) {

            do {

                int id =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_ID
                                )
                        );

                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_NAME
                                )
                        );

                double quantity =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_QUANTITY
                                )
                        );

                String unit =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_UNIT
                                )
                        );

                String expiryDate =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_EXPIRY_DATE
                                )
                        );

                PantryItem item =
                        new PantryItem(
                                id,
                                name,
                                quantity,
                                unit,
                                expiryDate
                        );

                pantryItems.add(item);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return pantryItems;
    }


    public int updatePantryItem(PantryItem item) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COLUMN_NAME,
                item.getName()
        );

        values.put(
                COLUMN_QUANTITY,
                item.getQuantity()
        );

        values.put(
                COLUMN_UNIT,
                item.getUnit()
        );

        values.put(
                COLUMN_EXPIRY_DATE,
                item.getExpiryDate()
        );

        int rowsUpdated =
                db.update(
                        TABLE_PANTRY,
                        values,
                        COLUMN_ID + " = ?",
                        new String[]{
                                String.valueOf(
                                        item.getId()
                                )
                        }
                );

        db.close();

        return rowsUpdated;
    }


    public int deletePantryItem(int id) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        int rowsDeleted =
                db.delete(
                        TABLE_PANTRY,
                        COLUMN_ID + " = ?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        db.close();

        return rowsDeleted;
    }


    // =========================================================
    // RECIPE METHODS
    // =========================================================

    public long addRecipe(Recipe recipe) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COLUMN_RECIPE_NAME,
                recipe.getName()
        );

        values.put(
                COLUMN_RECIPE_INGREDIENTS,
                recipe.getIngredients()
        );

        values.put(
                COLUMN_RECIPE_STEPS,
                recipe.getSteps()
        );

        long id =
                db.insert(
                        TABLE_RECIPES,
                        null,
                        values
                );

        db.close();

        return id;
    }


    public List<Recipe> getAllRecipes() {

        List<Recipe> recipes =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.query(
                        TABLE_RECIPES,
                        null,
                        null,
                        null,
                        null,
                        null,
                        COLUMN_RECIPE_ID + " ASC"
                );

        if (cursor.moveToFirst()) {

            do {

                int id =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_RECIPE_ID
                                )
                        );

                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_RECIPE_NAME
                                )
                        );

                String ingredients =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_RECIPE_INGREDIENTS
                                )
                        );

                String steps =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_RECIPE_STEPS
                                )
                        );

                Recipe recipe =
                        new Recipe(
                                id,
                                name,
                                ingredients,
                                steps
                        );

                recipes.add(recipe);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recipes;
    }


    public Recipe getRecipeById(int id) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.query(
                        TABLE_RECIPES,
                        null,
                        COLUMN_RECIPE_ID + " = ?",
                        new String[]{
                                String.valueOf(id)
                        },
                        null,
                        null,
                        null
                );

        Recipe recipe = null;

        if (cursor.moveToFirst()) {

            String name =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    COLUMN_RECIPE_NAME
                            )
                    );

            String ingredients =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    COLUMN_RECIPE_INGREDIENTS
                            )
                    );

            String steps =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    COLUMN_RECIPE_STEPS
                            )
                    );

            recipe =
                    new Recipe(
                            id,
                            name,
                            ingredients,
                            steps
                    );
        }

        cursor.close();
        db.close();

        return recipe;
    }

    // =========================================================
// RECIPE COUNT
// =========================================================

    public int getRecipeCount() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT COUNT(*) FROM " + TABLE_RECIPES,
                        null
                );

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return count;
    }

    // =========================================================
    // SEED 20 SIMPLE & VARIED RECIPES
    // =========================================================

    private void seedRecipes(SQLiteDatabase db) {

        // 1
        addSeedRecipe(
                db,
                "Egg Omelette",
                "egg:2:pcs, onion:1:pcs, tomato:1:pcs",
                "1. Beat the eggs in a bowl.\n" +
                        "2. Chop the onion and tomato.\n" +
                        "3. Fry the onion and tomato.\n" +
                        "4. Add the eggs and cook until set."
        );

        // 2
        addSeedRecipe(
                db,
                "Tomato Pasta",
                "pasta:2:cup, tomato:2:pcs, onion:1:pcs",
                "1. Cook the pasta.\n" +
                        "2. Chop the tomatoes and onion.\n" +
                        "3. Fry the onion and tomatoes.\n" +
                        "4. Mix with the cooked pasta."
        );

        // 3
        addSeedRecipe(
                db,
                "Cheese Sandwich",
                "bread:2:pcs, cheese:2:slice, butter:1:tbsp",
                "1. Butter the bread.\n" +
                        "2. Add the cheese.\n" +
                        "3. Close the sandwich.\n" +
                        "4. Toast until golden."
        );

        // 4
        addSeedRecipe(
                db,
                "Pancakes",
                "flour:2:cup, milk:1:cup, egg:1:pcs, sugar:1:tbsp",
                "1. Mix the flour, milk, egg and sugar.\n" +
                        "2. Heat a pan.\n" +
                        "3. Pour the batter into the pan.\n" +
                        "4. Cook both sides until golden."
        );

        // 5
        addSeedRecipe(
                db,
                "Fried Rice",
                "rice:2:cup, egg:1:pcs, onion:1:pcs, carrot:1:pcs",
                "1. Cook the rice.\n" +
                        "2. Fry the onion and carrot.\n" +
                        "3. Add the egg.\n" +
                        "4. Add the rice and stir-fry."
        );

        // 6
        addSeedRecipe(
                db,
                "Chicken Rice",
                "rice:2:cup, chicken:2:pcs, onion:1:pcs, carrot:1:pcs",
                "1. Cook the rice.\n" +
                        "2. Cook the chicken thoroughly.\n" +
                        "3. Fry the onion and carrot.\n" +
                        "4. Combine everything."
        );

        // 7
        addSeedRecipe(
                db,
                "Mashed Potatoes",
                "potato:3:pcs, butter:1:tbsp, milk:1:cup",
                "1. Boil the potatoes until soft.\n" +
                        "2. Drain and mash them.\n" +
                        "3. Add butter and milk.\n" +
                        "4. Mix until smooth."
        );

        // 8
        addSeedRecipe(
                db,
                "Tomato Soup",
                "tomato:3:pcs, onion:1:pcs, milk:1:cup",
                "1. Chop the tomatoes and onion.\n" +
                        "2. Cook until soft.\n" +
                        "3. Blend until smooth.\n" +
                        "4. Add milk and heat gently."
        );

        // 9
        addSeedRecipe(
                db,
                "Garden Salad",
                "lettuce:2:cup, tomato:1:pcs, cucumber:1:pcs, carrot:1:pcs",
                "1. Wash the vegetables.\n" +
                        "2. Chop the tomato, cucumber and carrot.\n" +
                        "3. Add the lettuce.\n" +
                        "4. Mix everything together."
        );

        // 10
        addSeedRecipe(
                db,
                "Banana Smoothie",
                "banana:1:pcs, milk:1:cup, yogurt:1:cup",
                "1. Peel the banana.\n" +
                        "2. Add banana, milk and yogurt to a blender.\n" +
                        "3. Blend until smooth.\n" +
                        "4. Serve immediately."
        );

        // 11
        addSeedRecipe(
                db,
                "Potato Wedges",
                "potato:3:pcs, oil:1:tbsp, salt:1:tsp",
                "1. Wash and cut the potatoes into wedges.\n" +
                        "2. Add oil and salt.\n" +
                        "3. Mix well.\n" +
                        "4. Bake until golden and crispy."
        );

        // 12
        addSeedRecipe(
                db,
                "Chicken Stir-Fry",
                "chicken:2:pcs, carrot:1:pcs, pepper:1:pcs, onion:1:pcs",
                "1. Cut the chicken into small pieces.\n" +
                        "2. Chop the vegetables.\n" +
                        "3. Cook the chicken in a hot pan.\n" +
                        "4. Add the vegetables and stir-fry."
        );

        // 13
        addSeedRecipe(
                db,
                "Tomato Cheese Toast",
                "bread:2:pcs, tomato:1:pcs, cheese:2:slice",
                "1. Slice the tomato.\n" +
                        "2. Place tomato and cheese on the bread.\n" +
                        "3. Toast until the cheese melts.\n" +
                        "4. Serve warm."
        );

        // 14
        addSeedRecipe(
                db,
                "Avocado Toast",
                "bread:2:pcs, avocado:1:pcs, salt:1:tsp",
                "1. Toast the bread.\n" +
                        "2. Mash the avocado.\n" +
                        "3. Spread avocado over the toast.\n" +
                        "4. Add a small amount of salt."
        );

        // 15
        addSeedRecipe(
                db,
                "Banana Toast",
                "bread:2:pcs, banana:1:pcs, butter:1:tbsp",
                "1. Toast the bread.\n" +
                        "2. Spread butter on the toast.\n" +
                        "3. Slice the banana.\n" +
                        "4. Place the banana on the toast."
        );

        // 16
        addSeedRecipe(
                db,
                "Vegetable Soup",
                "carrot:2:pcs, potato:2:pcs, onion:1:pcs, cabbage:1:cup",
                "1. Chop all the vegetables.\n" +
                        "2. Add the vegetables to a pot with water.\n" +
                        "3. Cook until tender.\n" +
                        "4. Season and serve."
        );

        // 17
        addSeedRecipe(
                db,
                "Egg Rice Bowl",
                "rice:1:cup, egg:2:pcs, carrot:1:pcs",
                "1. Cook the rice.\n" +
                        "2. Cook the eggs.\n" +
                        "3. Grate or chop the carrot.\n" +
                        "4. Place everything in a bowl."
        );

        // 18
        addSeedRecipe(
                db,
                "Potato Egg Hash",
                "potato:2:pcs, egg:2:pcs, onion:1:pcs",
                "1. Boil and chop the potatoes.\n" +
                        "2. Fry the onion.\n" +
                        "3. Add the potatoes.\n" +
                        "4. Add the eggs and cook until done."
        );

        // 19
        addSeedRecipe(
                db,
                "Banana Oatmeal",
                "oats:1:cup, banana:1:pcs, milk:1:cup",
                "1. Add oats and milk to a pot.\n" +
                        "2. Cook until the oats become soft.\n" +
                        "3. Slice the banana.\n" +
                        "4. Add the banana and serve."
        );

        // 20
        addSeedRecipe(
                db,
                "Vegetable Rice",
                "rice:1:cup, carrot:1:pcs, peas:1:cup, onion:1:pcs",
                "1. Cook the rice.\n" +
                        "2. Chop the onion and carrot.\n" +
                        "3. Fry the vegetables.\n" +
                        "4. Add the rice and peas and mix well."
        );
    }


    // =========================================================
    // ADD SEEDED RECIPE
    // =========================================================

    private void addSeedRecipe(
            SQLiteDatabase db,
            String name,
            String ingredients,
            String steps
    ) {

        ContentValues values =
                new ContentValues();

        values.put(
                COLUMN_RECIPE_NAME,
                name
        );

        values.put(
                COLUMN_RECIPE_INGREDIENTS,
                ingredients
        );

        values.put(
                COLUMN_RECIPE_STEPS,
                steps
        );

        db.insert(
                TABLE_RECIPES,
                null,
                values
        );
    }
}