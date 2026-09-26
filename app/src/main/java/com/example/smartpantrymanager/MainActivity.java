package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smartpantrymanager.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);
        androidx.appcompat.widget.Toolbar toolbar =
                findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        DatabaseHelper databaseHelper =
                new DatabaseHelper(this);

        databaseHelper.getWritableDatabase();


        // Open Pantry button
        Button buttonOpenPantry =
                findViewById(R.id.buttonOpenPantry);

        buttonOpenPantry.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            PantryActivity.class
                    );

            startActivity(intent);
        });


        // View Pantry button
        Button buttonViewPantry =
                findViewById(R.id.buttonViewPantry);

        buttonViewPantry.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            PantryListActivity.class
                    );

            startActivity(intent);
        });


        // Suggested Recipes button
        Button buttonSuggestedRecipes =
                findViewById(R.id.buttonSuggestedRecipes);

        buttonSuggestedRecipes.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            SuggestedRecipesActivity.class
                    );

            startActivity(intent);
        });


        // Settings button
        Button buttonSettings =
                findViewById(R.id.buttonSettings);

        buttonSettings.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            SettingsActivity.class
                    );

            startActivity(intent);
        });


        // Handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );
    }


    // =========================================================
    // CREATE NAVIGATION MENU
    // =========================================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(
                R.menu.navigation_menu,
                menu
        );

        return true;
    }


    // =========================================================
    // NAVIGATION MENU ACTIONS
    // =========================================================

    @Override
    public boolean onOptionsItemSelected(
            MenuItem item
    ) {

        int itemId = item.getItemId();


        if (itemId == R.id.nav_home) {

            return true;
        }


        if (itemId == R.id.nav_pantry) {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            PantryListActivity.class
                    );

            startActivity(intent);

            return true;
        }


        if (itemId == R.id.nav_recipes) {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            SuggestedRecipesActivity.class
                    );

            startActivity(intent);

            return true;
        }


        if (itemId == R.id.nav_settings) {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            SettingsActivity.class
                    );

            startActivity(intent);

            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}