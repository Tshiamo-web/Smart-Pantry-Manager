package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.model.PantryItem;

public class PantryActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextQuantity;
    private EditText editTextUnit;
    private EditText editTextExpiryDate;
    private Button buttonSave;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pantry);

        editTextName = findViewById(R.id.editTextName);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextUnit = findViewById(R.id.editTextUnit);
        editTextExpiryDate = findViewById(R.id.editTextExpiryDate);
        buttonSave = findViewById(R.id.buttonSave);

        databaseHelper = new DatabaseHelper(this);

        buttonSave.setOnClickListener(v -> savePantryItem());

        Button buttonBackHome = findViewById(R.id.buttonBackHome);

        buttonBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(PantryActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void savePantryItem() {

        String name = editTextName.getText().toString().trim();
        String quantityText = editTextQuantity.getText().toString().trim();
        String unit = editTextUnit.getText().toString().trim();
        String expiryDate = editTextExpiryDate.getText().toString().trim();

        if (name.isEmpty()) {
            editTextName.setError("Please enter an item name");
            editTextName.requestFocus();
            return;
        }

        if (quantityText.isEmpty()) {
            editTextQuantity.setError("Please enter a quantity");
            editTextQuantity.requestFocus();
            return;
        }

        if (unit.isEmpty()) {
            editTextUnit.setError("Please enter a unit");
            editTextUnit.requestFocus();
            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);

            if (quantity <= 0) {
                editTextQuantity.setError("Quantity must be greater than 0");
                editTextQuantity.requestFocus();
                return;
            }

        } catch (NumberFormatException e) {
            editTextQuantity.setError("Please enter a valid quantity");
            editTextQuantity.requestFocus();
            return;
        }
        PantryItem item = new PantryItem(
                name,
                quantity,
                unit,
                expiryDate
        );

        long result = databaseHelper.addPantryItem(item);

        if (result != -1) {

            Toast.makeText(
                    this,
                    "Pantry item saved successfully",
                    Toast.LENGTH_SHORT
            ).show();

            editTextName.setText("");
            editTextQuantity.setText("");
            editTextUnit.setText("");
            editTextExpiryDate.setText("");

        } else {

            Toast.makeText(
                    this,
                    "Failed to save pantry item",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}