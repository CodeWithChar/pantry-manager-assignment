package com.example.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//this handles both of the adding a new item in the pantry and editing one thats already there//

public class AddandEditIngredientActivity extends AppCompatActivity {

    private DatabaseAssistant databaseAssistant;

    private EditText editName, editQuantity, editUnit, editExpiry;
    private TextView errorText;
    private Button deleteButton;

    private int editingItemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        databaseAssistant = new DatabaseAssistant(this);

        editName = findViewById(R.id.editName);
        editQuantity = findViewById(R.id.editQuantity);
        editUnit = findViewById(R.id.editUnit);
        editExpiry = findViewById(R.id.editExpiry);
        errorText = findViewById(R.id.textError);
        Button saveButton = findViewById(R.id.buttonSave);
        deleteButton = findViewById(R.id.buttonDelete);

        editingItemId = getIntent().getIntExtra("item_id", -1);

        if (editingItemId != -1) {
            loadExistingItem(editingItemId);
            deleteButton.setVisibility(android.view.View.VISIBLE);
        }

        saveButton.setOnClickListener(v -> saveItem());
        deleteButton.setOnClickListener(v -> deleteItem());
    }

    //this gets the items data that is already in the database and fills the form//
    private void loadExistingItem(int id) {
        Cursor cursor = databaseAssistant.getAllPantryItems();
        if (cursor.moveToFirst()) {
            do {
                int currentId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseAssistant.COL_PANTRY_ID));
                if (currentId == id) {
                    editName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAssistant.COL_PANTRY_NAME)));
                    editQuantity.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseAssistant.COL_PANTRY_QUANTITY))));
                    editUnit.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAssistant.COL_PANTRY_UNIT)));
                    editExpiry.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAssistant.COL_PANTRY_EXPIRY)));
                    break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void saveItem() {
        String name = editName.getText().toString().trim();
        String quantityStr = editQuantity.getText().toString().trim();
        String unit = editUnit.getText().toString().trim();
        String expiry = editExpiry.getText().toString().trim();

        if (name.isEmpty()) {
            errorText.setText("Ingredient name is required.");
            return;
        }

        if (quantityStr.isEmpty()) {
            errorText.setText("Quantity is required.");
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(quantityStr);
        } catch (NumberFormatException e) {
            errorText.setText("Quantity must be a number.");
            return;
        }

        if (quantity <= 0) {
            errorText.setText("Quantity must be greater than zero.");
            return;
        }

        if (editingItemId == -1) {
            databaseAssistant.addPantryItem(name, quantity, unit, expiry.isEmpty() ? null : expiry);
        } else {
            databaseAssistant.updatePantryItem(editingItemId, name, quantity, unit, expiry.isEmpty() ? null : expiry);
        }

        finish();
    }

    private void deleteItem() {
        if (editingItemId != -1) {
            databaseAssistant.deletePantryItem(editingItemId);
            finish();
        }
    }
}