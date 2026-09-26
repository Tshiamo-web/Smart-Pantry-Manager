package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import android.graphics.Color;
import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.model.PantryItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.List;

public class PantryListActivity extends AppCompatActivity {

    private LinearLayout pantryItemsContainer;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pantry_list);

        pantryItemsContainer = findViewById(R.id.pantryItemsContainer);

        databaseHelper = new DatabaseHelper(this);

        displayPantryItems();

        Button buttonBackHome = findViewById(R.id.buttonBackHome);

        buttonBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(PantryListActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        displayPantryItems();
    }
    private void displayPantryItems() {

        pantryItemsContainer.removeAllViews();

        List<PantryItem> pantryItems = databaseHelper.getAllPantryItems();

        if (pantryItems.isEmpty()) {
            TextView emptyMessage = new TextView(this);

            emptyMessage.setText(
                    "Your pantry is empty. 🥫\n\n" +
                            "Add ingredients to your pantry to start " +
                            "getting recipe suggestions. 💗"
            );

            emptyMessage.setTextSize(18);
            emptyMessage.setTextColor(Color.rgb(142, 106, 143));
            emptyMessage.setGravity(android.view.Gravity.CENTER);
            emptyMessage.setPadding(24, 40, 24, 40);

            pantryItemsContainer.addView(emptyMessage);
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(this);

        for (PantryItem item : pantryItems) {

            View itemView = inflater.inflate(
                    R.layout.item_pantry,
                    pantryItemsContainer,
                    false
            );

            TextView textViewItemName =
                    itemView.findViewById(R.id.textViewItemName);

            TextView textViewItemQuantity =
                    itemView.findViewById(R.id.textViewItemQuantity);

            TextView textViewItemExpiry =
                    itemView.findViewById(R.id.textViewItemExpiry);

            TextView textViewExpiryStatus =
                    itemView.findViewById(R.id.textViewExpiryStatus);

            Button buttonEdit =
                    itemView.findViewById(R.id.buttonEdit);

            Button buttonDelete =
                    itemView.findViewById(R.id.buttonDelete);

            textViewItemName.setText(
                    "Item: " + item.getName()
            );

            textViewItemQuantity.setText(
                    "Quantity: " + item.getQuantity() + " " + item.getUnit()
            );

            textViewItemExpiry.setText(
                    "Expiry Date: " + item.getExpiryDate()
            );
            String expiryDateText = item.getExpiryDate();

            if (expiryDateText == null || expiryDateText.isEmpty()) {

                textViewExpiryStatus.setText(
                        "Expiry Status: No expiry date"
                );

            } else {

                try {

                    SimpleDateFormat dateFormat =
                            new SimpleDateFormat("yyyy-MM-dd");

                    dateFormat.setLenient(false);

                    Date expiryDate = dateFormat.parse(expiryDateText);

                    Date today = new Date();

                    String todayText = dateFormat.format(today);

                    Date todayDate = dateFormat.parse(todayText);

                    long differenceInMillis =
                            expiryDate.getTime() - todayDate.getTime();

                    long daysUntilExpiry =
                            TimeUnit.MILLISECONDS.toDays(differenceInMillis);
                    if (daysUntilExpiry < 0) {

                        textViewExpiryStatus.setText(
                                "Expiry Status: Expired"
                        );

                        textViewExpiryStatus.setTextColor(
                                Color.RED
                        );

                    } else if (daysUntilExpiry <= 7) {

                        textViewExpiryStatus.setText(
                                "Expiry Status: Expiring Soon"
                        );

                        textViewExpiryStatus.setTextColor(
                                Color.rgb(255, 152, 0)
                        );

                    } else {

                        textViewExpiryStatus.setText(
                                "Expiry Status: Good"
                        );

                        textViewExpiryStatus.setTextColor(
                                Color.GREEN
                        );
                    }

                } catch (ParseException e) {

                    textViewExpiryStatus.setText(
                            "Expiry Status: Invalid date"
                    );
                }
            }

            buttonEdit.setOnClickListener(v -> {

                android.content.Intent intent =
                        new android.content.Intent(
                                PantryListActivity.this,
                                EditPantryActivity.class
                        );

                intent.putExtra("pantry_item_id", item.getId());

                startActivity(intent);
            });

            buttonDelete.setOnClickListener(v -> {

                new AlertDialog.Builder(this)
                        .setTitle("Delete Pantry Item?")
                        .setMessage("Are you sure you want to delete this item?")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Delete", (dialog, which) -> {

                            databaseHelper.deletePantryItem(item.getId());

                            Toast.makeText(
                                    this,
                                    "Pantry item deleted",
                                    Toast.LENGTH_SHORT
                            ).show();

                            displayPantryItems();
                        })
                        .show();
            });

            pantryItemsContainer.addView(itemView);
        }
    }
}