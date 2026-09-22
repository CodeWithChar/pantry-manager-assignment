package com.example.smartpantrymanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
//this is the pantry lists homescreen//

public class MainActivity extends AppCompatActivity {

    private DatabaseAssistant databaseAssistant;
    private RecyclerView recyclerView;
    private PantryAdapter adapter;
    private TextView emptyStateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseAssistant = new DatabaseAssistant(this);

        recyclerView = findViewById(R.id.recyclerViewPantry);
        emptyStateText = findViewById(R.id.textEmptyState);
        Button addButton = findViewById(R.id.buttonAddItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddandEditIngredientActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantryItems();
    }

    private void loadPantryItems() {
        List<PantryItems> items = new ArrayList<>();

        Cursor cursor = databaseAssistant.getAllPantryItems();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseAssistant.COL_PANTRY_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAssistant.COL_PANTRY_NAME));
                double quantity = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseAssistant.COL_PANTRY_QUANTITY));
                String unit = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAssistant.COL_PANTRY_UNIT));
                String expiry = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAssistant.COL_PANTRY_EXPIRY));

                items.add(new PantryItems(id, name, quantity, unit, expiry));
            } while (cursor.moveToNext());
        }
        cursor.close();

        if (items.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyStateText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateText.setVisibility(View.GONE);
        }

        if (adapter == null) {
            adapter = new PantryAdapter(items, item -> {
                Intent intent = new Intent(MainActivity.this, AddandEditIngredientActivity.class);
                intent.putExtra("item_id", item.getId());
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateData(items);
        }
    }
}