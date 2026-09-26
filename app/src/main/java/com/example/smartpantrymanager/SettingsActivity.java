package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantrymanager.database.DatabaseHelper;

public class SettingsActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private TextView textViewRecipeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        databaseHelper = new DatabaseHelper(this);

        textViewRecipeCount = findViewById(R.id.textViewRecipeCount);

        int recipeCount = databaseHelper.getRecipeCount();

        textViewRecipeCount.setText(
                "Smart Pantry Manager • Mobile Application Development 700\n\n" +
                        "Recipe collection: " + recipeCount + " recipes available"
        );

        Button buttonBackHome = findViewById(R.id.buttonBackHome);

        buttonBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(
                    SettingsActivity.this,
                    MainActivity.class
            );

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

    }
    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null && textViewRecipeCount != null) {
            int recipeCount = databaseHelper.getRecipeCount();

            textViewRecipeCount.setText(
                    "Smart Pantry Manager • Mobile Application Development 700\n\n" +
                            "Recipe collection: " + recipeCount + " recipes available"
            );
        }
    }
}