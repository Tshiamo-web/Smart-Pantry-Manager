package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantrymanager.database.DatabaseHelper;
import com.example.smartpantrymanager.model.PantryItem;

public class EditPantryActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextQuantity;
    private EditText editTextUnit;
    private EditText editTextExpiryDate;
    private Button buttonSaveChanges;

    private DatabaseHelper databaseHelper;

    private int pantryItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_pantry);

        editTextName = findViewById(R.id.editTextEditName);
        editTextQuantity = findViewById(R.id.editTextEditQuantity);
        editTextUnit = findViewById(R.id.editTextEditUnit);
        editTextExpiryDate = findViewById(R.id.editTextEditExpiryDate);
        buttonSaveChanges = findViewById(R.id.buttonSaveChanges);

        Button buttonBackHome = findViewById(R.id.buttonBackHome);

        buttonBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(
                    EditPantryActivity.this,
                    MainActivity.class
            );

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });


        databaseHelper = new DatabaseHelper(this);

        pantryItemId = getIntent().getIntExtra("pantry_item_id", -1);

        loadPantryItem();

        buttonSaveChanges.setOnClickListener(v -> updatePantryItem());
    }

    private void loadPantryItem() {

        for (PantryItem item : databaseHelper.getAllPantryItems()) {

            if (item.getId() == pantryItemId) {

                editTextName.setText(item.getName());
                editTextQuantity.setText(String.valueOf(item.getQuantity()));
                editTextUnit.setText(item.getUnit());
                editTextExpiryDate.setText(item.getExpiryDate());

                break;
            }
        }
    }

    private void updatePantryItem() {

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
        } catch (NumberFormatException e) {
            editTextQuantity.setError("Please enter a valid quantity");
            editTextQuantity.requestFocus();
            return;
        }

        PantryItem updatedItem = new PantryItem(
                pantryItemId,
                name,
                quantity,
                unit,
                expiryDate
        );

        int result = databaseHelper.updatePantryItem(updatedItem);

        if (result > 0) {

            Toast.makeText(
                    this,
                    "Pantry item updated successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Failed to update pantry item",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}