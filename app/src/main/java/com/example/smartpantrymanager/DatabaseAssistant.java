package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//this file handles the creation of the database//
public class DatabaseAssistant extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    //these are the table names for the items
    public static final String TABLE_PANTRY = "pantry_items";
    public static final String TABLE_RECIPES = "recipes";
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";

    //these are the pantry item columns
    public static final String COL_PANTRY_ID = "id";
    public static final String COL_PANTRY_NAME = "name";
    public static final String COL_PANTRY_QUANTITY = "quantity";
    public static final String COL_PANTRY_UNIT = "unit";
    public static final String COL_PANTRY_EXPIRY = "expiry_date";

    //these are the recipes columns
    public static final String COL_RECIPE_ID = "id";
    public static final String COL_RECIPE_NAME = "name";
    public static final String COL_RECIPE_STEPS = "steps";

    //these are the recipe ingredients columns
    public static final String COL_RI_ID = "id";
    public static final String COL_RI_RECIPE_ID = "recipe_id";
    public static final String COL_RI_INGREDIENT_NAME = "ingredient_name";
    public static final String COL_RI_QUANTITY = "required_quantity";
    public static final String COL_RI_UNIT = "unit";

    public DatabaseAssistant(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createPantryTable = "CREATE TABLE " + TABLE_PANTRY + " ("
                + COL_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_PANTRY_NAME + " TEXT NOT NULL, "
                + COL_PANTRY_QUANTITY + " REAL NOT NULL, "
                + COL_PANTRY_UNIT + " TEXT, "
                + COL_PANTRY_EXPIRY + " TEXT"
                + ")";

        String createRecipesTable = "CREATE TABLE " + TABLE_RECIPES + " ("
                + COL_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_RECIPE_NAME + " TEXT NOT NULL, "
                + COL_RECIPE_STEPS + " TEXT"
                + ")";

        String createRecipeIngredientsTable = "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " ("
                + COL_RI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_RI_RECIPE_ID + " INTEGER NOT NULL, "
                + COL_RI_INGREDIENT_NAME + " TEXT NOT NULL, "
                + COL_RI_QUANTITY + " REAL NOT NULL, "
                + COL_RI_UNIT + " TEXT, "
                + "FOREIGN KEY(" + COL_RI_RECIPE_ID + ") REFERENCES " + TABLE_RECIPES + "(" + COL_RECIPE_ID + ")"
                + ")";

        db.execSQL(createPantryTable);
        db.execSQL(createRecipesTable);
        db.execSQL(createRecipeIngredientsTable);

        db.execSQL(createRecipeIngredientsTable);

        seedRecipes(db);

        //the below is recipes from different cultures and cuisines ensuring that
        //some recipes include the same ingredient for when a user want to add or edit a recipe//

        private void seedRecipes(SQLiteDatabase db) {

            //Recipe 1: Italian - Penne Arrabbiata
            long r1 = insertRecipeRow(db, "Penne Arrabbiata",
                    "1. Boil penne in salted water until al dente. 2. Sauté garlic and red chili flakes in olive oil until fragrant. "
                            + "3. Add crushed canned tomatoes and simmer 10 minutes. 4. Toss pasta with sauce and sprinkle fresh parsley.");
            insertIngredientRow(db, r1, "penne pasta", 200, "grams");
            insertIngredientRow(db, r1, "canned crushed tomatoes", 400, "grams");
            insertIngredientRow(db, r1, "garlic", 3, "cloves");
            insertIngredientRow(db, r1, "red chili flakes", 1, "tsp");
            insertIngredientRow(db, r1, "extra virgin olive oil", 2, "tbsp");

            //Recipe 2: Japanese - Tamagoyaki on Rice
            long r2 = insertRecipeRow(db, "Tamagoyaki on Rice",
                    "1. Whisk eggs with soy sauce, mirin, and dashi stock. 2. Roll layered omelette sheets in a rectangular pan. "
                            + "3. Slice into thick pieces. 4. Serve hot over steamed sushi rice.");
            insertIngredientRow(db, r2, "egg", 3, "pieces");
            insertIngredientRow(db, r2, "sushi rice", 200, "grams");
            insertIngredientRow(db, r2, "soy sauce", 1, "tbsp");
            insertIngredientRow(db, r2, "mirin", 1, "tbsp");
            insertIngredientRow(db, r2, "butter", 1, "tbsp");

            //Recipe 3: Thai - Thai Basil Chicken (Pad Krapow)
            long r3 = insertRecipeRow(db, "Thai Basil Chicken",
                    "1. Fry minced garlic and bird's eye chilies in high heat oil. 2. Stir fry ground chicken breast until cooked through. "
                            + "3. Add soy sauce, fish sauce, and fresh basil leaves until wilted. 4. Serve hot with jasmine rice.");
            insertIngredientRow(db, r3, "ground chicken breast", 300, "grams");
            insertIngredientRow(db, r3, "thai basil", 1, "cup");
            insertIngredientRow(db, r3, "bird's eye chili", 2, "pieces");
            insertIngredientRow(db, r3, "fish sauce", 1, "tbsp");
            insertIngredientRow(db, r3, "jasmine rice", 200, "grams");

            //Recipe 4: French - Provençal Vegetable Soup
            long r4 = insertRecipeRow(db, "Provençal Vegetable Soup",
                    "1. Sauté leek, carrot, and zucchini in olive oil. 2. Pour in vegetable stock and diced canned tomatoes. "
                            + "3. Simmer 20 minutes until tender. 4. Stir in fresh basil pesto and serve with crusty bread.");
            insertIngredientRow(db, r4, "carrot", 2, "pieces");
            insertIngredientRow(db, r4, "zucchini", 1, "pieces");
            insertIngredientRow(db, r4, "leek", 1, "pieces");
            insertIngredientRow(db, r4, "vegetable stock", 500, "ml");
            insertIngredientRow(db, r4, "basil pesto", 2, "tbsp");

            //Recipe 5: South African - Boerewors Roll with Chakalaka
            long r5 = insertRecipeRow(db, "Boerewors Roll with Chakalaka",
                    "1. Pan-fry or braai boerewors until browned and cooked through. 2. Sauté onion, bell pepper, and curry powder to make chakalaka. "
                            + "3. Slice roll open, place boerewors inside, and top with spicy chakalaka relish.");
            insertIngredientRow(db, r5, "boerewors sausage", 250, "grams");
            insertIngredientRow(db, r5, "hot dog roll", 2, "pieces");
            insertIngredientRow(db, r5, "onion", 1, "pieces");
            insertIngredientRow(db, r5, "bell pepper", 1, "pieces");
            insertIngredientRow(db, r5, "curry powder", 1, "tbsp");

            //Recipe 6: Chinese - Egg & Scallion Fried Rice
            long r6 = insertRecipeRow(db, "Egg & Scallion Fried Rice",
                    "1. Heat sesame oil in a wok. 2. Scramble eggs softly and push aside. "
                            + "3. Toss in cold leftover jasmine rice, chopped scallions, and carrots. 4. Season with soy sauce and stir fry on high heat.");
            insertIngredientRow(db, r6, "cooked jasmine rice", 300, "grams");
            insertIngredientRow(db, r6, "egg", 2, "pieces");
            insertIngredientRow(db, r6, "scallion", 3, "stalks");
            insertIngredientRow(db, r6, "sesame oil", 1, "tbsp");

            //Recipe 7: American / Breakfast - Fluffy Banana Pancakes
            long r7 = insertRecipeRow(db, "Fluffy Banana Pancakes",
                    "1. Mash ripe banana and whisk with egg yolks, milk, and flour. 2. Fold whipped egg whites into batter. "
                            + "3. Cook covered on low heat, flip once, and top with maple syrup.");
            insertIngredientRow(db, r7, "ripe banana", 2, "pieces");
            insertIngredientRow(db, r7, "egg", 2, "pieces");
            insertIngredientRow(db, r7, "all-purpose flour", 100, "grams");
            insertIngredientRow(db, r7, "maple syrup", 2, "tbsp");

            //Recipe 8: Italian - Classic Caprese Salad
            long r8 = insertRecipeRow(db, "Classic Caprese Salad",
                    "1. Slice fresh heirloom tomatoes and buffalo mozzarella. 2. Arrange alternating slices on a serving platter. "
                            + "3. Drizzle generously with extra virgin olive oil and balsamic glaze, top with fresh basil leaves.");
            insertIngredientRow(db, r8, "heirloom tomato", 2, "pieces");
            insertIngredientRow(db, r8, "buffalo mozzarella", 150, "grams");
            insertIngredientRow(db, r8, "extra virgin olive oil", 1, "tbsp");
            insertIngredientRow(db, r8, "balsamic glaze", 1, "tbsp");
            insertIngredientRow(db, r8, "fresh basil", 8, "leaves");

            //Recipe 9: South African - Cape Malay Sugar Bean Curry
            long r9 = insertRecipeRow(db, "Cape Malay Sugar Bean Curry",
                    "1. Sauté onion, garlic, and Cape Malay curry powder in oil. 2. Add sugar beans, diced tomato, and a pinch of sugar. "
                            + "3. Simmer for 15 minutes until rich and fragrant. 4. Serve hot with yellow rice or roti.");
            insertIngredientRow(db, r9, "canned sugar beans", 400, "grams");
            insertIngredientRow(db, r9, "onion", 1, "pieces");
            insertIngredientRow(db, r9, "garlic", 2, "cloves");
            insertIngredientRow(db, r9, "cape malay curry powder", 1, "tbsp");
            insertIngredientRow(db, r9, "canned diced tomatoes", 200, "grams");

            //Recipe 10: Spanish - Paprika Roasted Potato Wedges
            long r10 = insertRecipeRow(db, "Spanish Paprika Patatas Wedges",
                    "1. Cut russet potatoes into wedges. 2. Toss with olive oil, smoked paprika, garlic powder, and sea salt. "
                            + "3. Bake at 200°C for 30 minutes until crispy and golden.");
            insertIngredientRow(db, r10, "russet potato", 4, "pieces");
            insertIngredientRow(db, r10, "smoked paprika", 1, "tbsp");
            insertIngredientRow(db, r10, "olive oil", 2, "tbsp");
            insertIngredientRow(db, r10, "garlic powder", 1, "tsp");

            //Recipe 11: French - Herb & Goat Cheese Omelette
            long r11 = insertRecipeRow(db, "French Herb & Goat Cheese Omelette",
                    "1. Whisk eggs with chopped fresh chives and parsley. 2. Pour into melted butter pan on low heat. "
                            + "3. Crumble goat cheese across the center, fold gently, and serve immediately.");
            insertIngredientRow(db, r11, "egg", 3, "pieces");
            insertIngredientRow(db, r11, "goat cheese", 50, "grams");
            insertIngredientRow(db, r11, "fresh chives", 1, "tbsp");
            insertIngredientRow(db, r11, "butter", 1, "tbsp");

            //Recipe 12: Indian - Garlic Butter Jeera Rice
            long r12 = insertRecipeRow(db, "Garlic Jeera Rice",
                    "1. Heat ghee in a pot, sizzle cumin seeds and sliced garlic until golden. "
                            + "2. Add basmati rice, water, and sea salt. 3. Simmer covered on low heat for 15 minutes.");
            insertIngredientRow(db, r12, "basmati rice", 250, "grams");
            insertIngredientRow(db, r12, "garlic", 4, "cloves");
            insertIngredientRow(db, r12, "ghee", 2, "tbsp");
            insertIngredientRow(db, r12, "cumin seeds", 1, "tbsp");

            //Recipe 13: Greek - Lemon Chicken Soup (Avgolemono)
            long r13 = insertRecipeRow(db, "Greek Lemon Chicken Soup",
                    "1. Simmer chicken breast in chicken stock with diced carrots and celery. "
                            + "2. Whisk lemon juice into beaten egg yolk, slowly temper with hot broth. 3. Return mixture to pot and stir until silky.");
            insertIngredientRow(db, r13, "chicken breast", 250, "grams");
            insertIngredientRow(db, r13, "chicken stock", 600, "ml");
            insertIngredientRow(db, r13, "lemon juice", 3, "tbsp");
            insertIngredientRow(db, r13, "egg yolk", 2, "pieces");

            //Recipe 14: Middle Eastern - Toast with Tahini & Honey Banana
            long r14 = insertRecipeRow(db, "Tahini & Honey Banana Toast",
                    "1. Toast thick brioche or pita bread. 2. Spread creamy tahini generously across toast. "
                            + "3. Layer sliced banana, drizzle with honey, and sprinkle toasted sesame seeds.");
            insertIngredientRow(db, r14, "brioche bread", 2, "slices");
            insertIngredientRow(db, r14, "tahini", 2, "tbsp");
            insertIngredientRow(db, r14, "banana", 1, "pieces");
            insertIngredientRow(db, r14, "honey", 1, "tbsp");

            //Recipe 15: Mediterranean - Bruschetta Toast
            long r15 = insertRecipeRow(db, "Mediterranean Bruschetta Toast",
                    "1. Toast ciabatta slices and rub with a raw garlic clove. 2. Toss diced Roma tomatoes with olive oil and chopped basil. "
                            + "3. Top toast with tomato mixture and crumbled feta cheese.");
            insertIngredientRow(db, r15, "ciabatta bread", 2, "slices");
            insertIngredientRow(db, r15, "roma tomato", 2, "pieces");
            insertIngredientRow(db, r15, "feta cheese", 60, "grams");
            insertIngredientRow(db, r15, "extra virgin olive oil", 1, "tbsp");

            //Recipe 16: Vietnamese - Garlic Chili Stir Fry
            long r16 = insertRecipeRow(db, "Garlic Chili Vegetable Stir Fry",
                    "1. Heat peanut oil in a hot wok. 2. Sauté minced garlic, ginger, and shallots until fragrant. "
                            + "3. Toss in bok choy, carrots, and bell peppers, splash with tamari soy sauce, and serve fast.");
            insertIngredientRow(db, r16, "shallot", 2, "pieces");
            insertIngredientRow(db, r16, "garlic", 3, "cloves");
            insertIngredientRow(db, r16, "bok choy", 2, "heads");
            insertIngredientRow(db, r16, "carrot", 1, "pieces");
            insertIngredientRow(db, r16, "tamari soy sauce", 1, "tbsp");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        onCreate(db);
    }

    //this Adds a new pantry item and then returns the new row's ID or will return a -1 if
    //the insert failed for some reason//
    public long addPantryItem(String name, double quantity, String unit, String expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PANTRY_NAME, name);
        values.put(COL_PANTRY_QUANTITY, quantity);
        values.put(COL_PANTRY_UNIT, unit);
        values.put(COL_PANTRY_EXPIRY, expiryDate);

        long newRowId = db.insert(TABLE_PANTRY, null, values);
        db.close();
        return newRowId;
    }

    //this reads every pantry item currently stored ordered by name and then
    //returns a cursor which the pantry's  list screen will loop through to build its list//
    public Cursor getAllPantryItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PANTRY + " ORDER BY " + COL_PANTRY_NAME, null);
    }

    //this updates an existing pantry item's details then returns to true if a row was actually changed//
    public boolean updatePantryItem(int id, String name, double quantity, String unit, String expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_PANTRY_NAME, name);
        values.put(COL_PANTRY_QUANTITY, quantity);
        values.put(COL_PANTRY_UNIT, unit);
        values.put(COL_PANTRY_EXPIRY, expiryDate);

        int rowsAffected = db.update(TABLE_PANTRY, values, COL_PANTRY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    //this deletes a pantry item by ID and then returns true if a row was actually removed.//
    public boolean deletePantryItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_PANTRY, COL_PANTRY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    //this adds a new recipe then goes on to return the new recipe's ID.//
    public long addRecipe(String name, String steps) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_RECIPE_NAME, name);
        values.put(COL_RECIPE_STEPS, steps);

        long newRecipeId = db.insert(TABLE_RECIPES, null, values);
        db.close();
        return newRecipeId;
    }

    //this adds one required ingredient to a recipe//
    public long addRecipeIngredient(long recipeId, String ingredientName, double quantity, String unit) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_RI_RECIPE_ID, recipeId);
        //this is so that when comparing later it is easier to identify against the other
        //ingredient in the pantry
        values.put(COL_RI_INGREDIENT_NAME, ingredientName.toLowerCase().trim());
        values.put(COL_RI_QUANTITY, quantity);
        values.put(COL_RI_UNIT, unit);

        long newId = db.insert(TABLE_RECIPE_INGREDIENTS, null, values);
        db.close();
        return newId;
    }

    //this reads every recipe in ordered by name.//
    public Cursor getAllRecipes() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RECIPES + " ORDER BY " + COL_RECIPE_NAME, null);
    }

    //this reads a single recipe's full details by its ID //
    public Cursor getRecipeById(long recipeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RECIPES + " WHERE " + COL_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)});
    }

    //this reads every ingredient that a recipe may need //
    public Cursor getIngredientsForRecipe(long recipeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RECIPE_INGREDIENTS + " WHERE " + COL_RI_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)});
    }


}