package com.example.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//this is here to display the recipes list of all the ingredients
//and how to prepare

public class RecipeDetails extends AppCompatActivity {

    private DatabaseAssistant databaseAssistant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);

        databaseAssistant = new DatabaseAssistant(this);

        TextView nameText = findViewById(R.id.textDetailName);
        TextView ingredientsText = findViewById(R.id.textDetailIngredients);
        TextView stepsText = findViewById(R.id.textDetailSteps);

        long recipeId = getIntent().getLongExtra("recipe_id", -1);

        if (recipeId == -1) {
            nameText.setText("Recipe not found");
            return;
        }

        //this loads the recipes own name and the steps.
        Cursor recipeCursor = databaseAssistant.getRecipeById(recipeId);
        if (recipeCursor.moveToFirst()) {
            String name = recipeCursor.getString(recipeCursor.getColumnIndexOrThrow(DatabaseAssistant.COL_RECIPE_NAME));
            String steps = recipeCursor.getString(recipeCursor.getColumnIndexOrThrow(DatabaseAssistant.COL_RECIPE_STEPS));
            nameText.setText(name);
            stepsText.setText(steps);
        }
        recipeCursor.close();
        StringBuilder ingredientsList = new StringBuilder();
        Cursor ingredientCursor = databaseAssistant.getIngredientsForRecipe(recipeId);
        if (ingredientCursor.moveToFirst()) {
            do {
                String ingredientName = ingredientCursor.getString(
                        ingredientCursor.getColumnIndexOrThrow(DatabaseAssistant.COL_RI_INGREDIENT_NAME));
                double quantity = ingredientCursor.getDouble(
                        ingredientCursor.getColumnIndexOrThrow(DatabaseAssistant.COL_RI_QUANTITY));
                String unit = ingredientCursor.getString(
                        ingredientCursor.getColumnIndexOrThrow(DatabaseAssistant.COL_RI_UNIT));

                ingredientsList.append("• ").append(quantity).append(" ").append(unit)
                        .append(" ").append(ingredientName).append("\n");
            } while (ingredientCursor.moveToNext());
        }
        ingredientCursor.close();

        ingredientsText.setText(ingredientsList.toString());
    }
}