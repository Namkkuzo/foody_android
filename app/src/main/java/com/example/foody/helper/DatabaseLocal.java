package com.example.foody.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

import com.example.foody.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class DatabaseLocal extends SQLiteOpenHelper {


    public static class RecipeTable {
        public static final String TABLE_NAME = "recipe";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUMMARY = "summary";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_LIKE = "totalLike";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_VEGAN = "vegan";
        public static final String COLUMN_NAME_CHEAP = "cheap";
        public static final String COLUMN_NAME_DAIRY_FREE = "dairyFree";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_GLUTEN_FREE = "glutenFree";
        public static final String COLUMN_NAME_HEALTHY = "healthy";
        public static final String COLUMN_NAME_VEGETARIAN = "Vegetarian";
        public static final String ID_RECIPE = "id";

    }

    public static class RecipeIngredientTable {
        public static final String TABLE_NAME = "recipeIngredientTable";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_UNIT = "unit";
        public static final String COLUMN_NAME_WEIGHT = "weight";
        public static final String ID_RECIPE = "id";
    }

    public static class RecipeInstructionTable {
        public static final String TABLE_NAME = "recipeInstructionTable";
        public static final String COLUMN_NAME_STEP = "step";
        public static final String COLUMN_NAME_ACTION = "action";
        public static final String ID_RECIPE = "id";
    }



    private static final String SQL_CREATE_TABLE_RECIPE =
            "CREATE TABLE " + RecipeTable.TABLE_NAME + " (" +
                    RecipeTable.ID_RECIPE + " TEXT PRIMARY KEY," +
                    RecipeTable.COLUMN_NAME_TITLE + " TEXT," +

                    RecipeTable.COLUMN_NAME_TIME + " TEXT," +
                    RecipeTable.COLUMN_NAME_IMAGE + " BLOB," +
                    RecipeTable.COLUMN_NAME_VEGAN + " TEXT," +
                    RecipeTable.COLUMN_NAME_CHEAP + " TEXT," +
                    RecipeTable.COLUMN_NAME_LIKE + " TEXT, " +
                    RecipeTable.COLUMN_NAME_DAIRY_FREE + " TEXT, " +
                    RecipeTable.COLUMN_NAME_VEGETARIAN + " TEXT, " +
                    RecipeTable.COLUMN_NAME_DESCRIPTION + " TEXT, " +
                    RecipeTable.COLUMN_NAME_GLUTEN_FREE + " TEXT, " +
                    RecipeTable.COLUMN_NAME_HEALTHY + " TEXT, " +
                    RecipeTable.COLUMN_NAME_SUMMARY + " TEXT)";

    private static final String SQL_CREATE_TABLE_RECIPE_INGREDIENT =
            "CREATE TABLE " + RecipeIngredientTable.TABLE_NAME + " (" +
                    RecipeIngredientTable.ID_RECIPE + " TEXT ," +
                    RecipeIngredientTable.COLUMN_NAME_IMAGE + " BLOB," +
                    RecipeIngredientTable.COLUMN_NAME_UNIT + " TEXT," +
                    RecipeIngredientTable.COLUMN_NAME_WEIGHT + " TEXT," +
                    RecipeIngredientTable.COLUMN_NAME_NAME + " TEXT)";


    private static final String SQL_CREATE_TABLE_RECIPE_INSTRUCTION =
            "CREATE TABLE " + RecipeInstructionTable.TABLE_NAME + " (" +
                    RecipeInstructionTable.ID_RECIPE + " TEXT   , " +
                    RecipeInstructionTable.COLUMN_NAME_STEP + " TEXT , " +
                    RecipeInstructionTable.COLUMN_NAME_ACTION + "TEXT)";

    private static final String SQL_DELETE_TABLE_RECIPE =
            "DROP TABLE IF EXISTS " + RecipeTable.TABLE_NAME;
    private static final String SQL_DELETE_RECIPE_INGREDIENT =
            "DROP TABLE IF EXISTS " + RecipeIngredientTable.TABLE_NAME;
    private static final String SQL_DELETE_RECIPE_INSTRUCTION =
            "DROP TABLE IF EXISTS " + RecipeInstructionTable.TABLE_NAME;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "test";

    public DatabaseLocal( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_RECIPE);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_RECIPE_INGREDIENT);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_RECIPE_INSTRUCTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE_RECIPE);
        sqLiteDatabase.execSQL(SQL_DELETE_RECIPE_INGREDIENT);
        sqLiteDatabase.execSQL(SQL_DELETE_RECIPE_INSTRUCTION);
        onCreate(sqLiteDatabase);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static void addRecipe (SQLiteDatabase  db , Recipe recipe,byte[] bytesImage){
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(RecipeTable.ID_RECIPE, recipe.id);
        values.put(RecipeTable.COLUMN_NAME_IMAGE, bytesImage);
        values.put(RecipeTable.COLUMN_NAME_LIKE, new Integer(recipe.totalLike).toString());
        values.put(RecipeTable.COLUMN_NAME_TITLE, recipe.title);
        values.put(RecipeTable.COLUMN_NAME_SUMMARY, recipe.summary);
        values.put(RecipeTable.COLUMN_NAME_TIME, new Integer(recipe.totalTime).toString());
        values.put(RecipeTable.COLUMN_NAME_VEGAN,new  Boolean(recipe.vegan).toString());
        // Insert the new row, returning the primary key value of the new row
        db.insert(RecipeTable.TABLE_NAME, null, values);
    }


    public static void deleteRecipe (SQLiteDatabase  db ,@Nullable String id){
        if (id == null){
            db.delete(RecipeTable.TABLE_NAME, null, null);
            db.delete(RecipeInstructionTable.TABLE_NAME, null, null);
            db.delete(RecipeIngredientTable.TABLE_NAME, null, null);
        } else{
            db.delete(RecipeTable.TABLE_NAME, RecipeTable.ID_RECIPE + " = '" + id+"'", null);
            db.delete(RecipeInstructionTable.TABLE_NAME, RecipeInstructionTable.ID_RECIPE + " = '" + id+"'", null);
            db.delete(RecipeIngredientTable.TABLE_NAME, RecipeIngredientTable.ID_RECIPE + " = '" + id+"'", null);
        }


    }

    public static ArrayList<Recipe> getListRecipe(SQLiteDatabase db ){
        ArrayList listRecipeFavorite = new ArrayList<Recipe>();


        Cursor cursor = db.query(
                RecipeTable.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        while(cursor.moveToNext()) {

            String id  = cursor.getString(
                    cursor.getColumnIndexOrThrow(RecipeTable.ID_RECIPE));
            String title  = cursor.getString(
                    cursor.getColumnIndexOrThrow(RecipeTable.COLUMN_NAME_TITLE));
            String summary  = cursor.getString(
                    cursor.getColumnIndexOrThrow(RecipeTable.COLUMN_NAME_SUMMARY));
            Boolean vegan  = new Boolean(cursor.getString(
                    cursor.getColumnIndexOrThrow(RecipeTable.COLUMN_NAME_VEGAN)));
            int time  = new Integer(cursor.getString(
                    cursor.getColumnIndexOrThrow(RecipeTable.COLUMN_NAME_TIME)));
            int like  = new Integer(cursor.getString(
                    cursor.getColumnIndexOrThrow(RecipeTable.COLUMN_NAME_LIKE)));
            byte[] image  = cursor.getBlob(
                    cursor.getColumnIndexOrThrow(RecipeTable.COLUMN_NAME_IMAGE));
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            Recipe recipe = new Recipe();
            recipe.id = id;
            recipe.imageBitmap = bitmap;
            recipe.summary = summary;
            recipe.title = title;
            recipe.totalLike = like;
            recipe.totalTime = time;
            recipe.vegan = vegan;
            listRecipeFavorite.add(recipe);
        }
        cursor.close();
        return listRecipeFavorite;
    }

}
