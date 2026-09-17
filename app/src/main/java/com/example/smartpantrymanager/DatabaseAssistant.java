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