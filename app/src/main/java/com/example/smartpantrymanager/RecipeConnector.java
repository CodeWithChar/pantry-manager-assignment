package com.example.smartpantrymanager;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//this is where all of the apps necessary logic is as this helps the user to know
//which recipes they can make with whats already in their pantry//

public class RecipeConnector {

    private final DatabaseAssistant databaseAssistant;

    public RecipeConnector(DatabaseAssistant databaseAssistant) {
        this.databaseAssistant = databaseAssistant;
    }

    //this specifically ensures that the ids of every recipe is returned when it matches
    //what is in the pantry//

    public List<Long> getSuggestedRecipeIds() {
        Map<String, Double> pantryMap = buildPantryMap();
        List<Long> suggestedIds = new ArrayList<>();

        Cursor recipeCursor = databaseAssistant.getAllRecipes();
        if (recipeCursor.moveToFirst()) {
            do {
                long recipeId = recipeCursor.getLong(
                        recipeCursor.getColumnIndexOrThrow(DatabaseAssistant.COL_RECIPE_ID));

                if (recipeFullyMatchesPantry(recipeId, pantryMap)) {
                    suggestedIds.add(recipeId);
                }
            } while (recipeCursor.moveToNext());
        }
        recipeCursor.close();

        return suggestedIds;
    }

    private boolean recipeFullyMatchesPantry(long recipeId, Map<String, Double> pantryMap) {
        Cursor ingredientCursor = databaseAssistant.getIngredientsForRecipe(recipeId);

        boolean allIngredientsAvailable = true;

        if (ingredientCursor.moveToFirst()) {
            do {
                String requiredName = ingredientCursor.getString(
                        ingredientCursor.getColumnIndexOrThrow(DatabaseAssistant.COL_RI_INGREDIENT_NAME));
                double requiredQuantity = ingredientCursor.getDouble(
                        ingredientCursor.getColumnIndexOrThrow(DatabaseAssistant.COL_RI_QUANTITY));

                double availableQuantity = getPantryQuantity(pantryMap, requiredName);

                if (availableQuantity < requiredQuantity) {
                    // Found one missing/short ingredient - no point
                    // checking the rest, this recipe is disqualified.
                    allIngredientsAvailable = false;
                    break;
                }
            } while (ingredientCursor.moveToNext());
        }
        ingredientCursor.close();

        return allIngredientsAvailable;
    }

    //this is for the mathematic side of the app where it basically totals up
    //what is in the pantry if there are onions in one recipe and one in another
    //they are counted as separate ingredients as they are for different
    //recipes but added together also tallies up all the ingredients in the pantry

    private Map<String, Double> buildPantryMap() {
        Map<String, Double> pantryMap = new HashMap<>();

        Cursor pantryCursor = databaseAssistant.getAllPantryItems();
        if (pantryCursor.moveToFirst()) {
            do {
                String name = pantryCursor.getString(
                        pantryCursor.getColumnIndexOrThrow(DatabaseAssistant.COL_PANTRY_NAME));
                double quantity = pantryCursor.getDouble(
                        pantryCursor.getColumnIndexOrThrow(DatabaseAssistant.COL_PANTRY_QUANTITY));

                String normalizedName = normalize(name);
                double existing = pantryMap.getOrDefault(normalizedName, 0.0);
                pantryMap.put(normalizedName, existing + quantity);
            } while (pantryCursor.moveToNext());
        }
        pantryCursor.close();

        return pantryMap;
    }

    private double getPantryQuantity(Map<String, Double> pantryMap, String ingredientName) {
        String normalized = normalize(ingredientName);
        return pantryMap.getOrDefault(normalized, 0.0);
    }

    private String normalize(String ingredientName) {
        String result = ingredientName.toLowerCase().trim();

        //this part i included for when a recipe says tomatoes and another tomato
        //just for it to not be read as "separate"
        if (result.endsWith("es") && result.length() > 3) {
            result = result.substring(0, result.length() - 2);
        } else if (result.endsWith("s") && !result.endsWith("ss") && result.length() > 2) {
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }
}