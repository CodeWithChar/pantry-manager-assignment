package com.example.smartpantrymanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//this is where the logic lies that basically says that only the recipes
//that will be suggested are the recipes that have a connection to what is
//in the pantry along with the quantity of that ingredient

public class SuggestedRecipesActivity extends AppCompatActivity {

    private DatabaseAssistant databaseAssistant;
    private RecipeConnector recipeConnector;
    private RecyclerView recyclerView;
    private RecipesAdapter adapter;
    private TextView noMatchesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggested_recipes);

        databaseAssistant = new DatabaseAssistant(this);
        recipeConnector = new RecipeConnector(databaseAssistant);

        recyclerView = findViewById(R.id.recyclerViewSuggested);
        noMatchesText = findViewById(R.id.textNoMatches);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSuggestedRecipes();
    }

    private void loadSuggestedRecipes() {
        List<Long> suggestedIds = recipeConnector.getSuggestedRecipeIds();
        List<Recipe> matchingRecipes = new ArrayList<>();
        for (Long id : suggestedIds) {
            Cursor cursor = databaseAssistant.getRecipeById(id);
            if (cursor.moveToFirst()) {
                long recipeId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseAssistant.COL_RECIPE_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAssistant.COL_RECIPE_NAME));
                String steps = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseAssistant.COL_RECIPE_STEPS));
                matchingRecipes.add(new Recipe(recipeId, name, steps));
            }
            cursor.close();
        }

        if (matchingRecipes.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            noMatchesText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noMatchesText.setVisibility(View.GONE);
        }

        if (adapter == null) {
            adapter = new RecipesAdapter(matchingRecipes, recipe -> {
                Intent intent = new Intent(SuggestedRecipesActivity.this, RecipeDetails.class);
                intent.putExtra("recipe_id", recipe.getId());
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateData(matchingRecipes);
        }
    }
}