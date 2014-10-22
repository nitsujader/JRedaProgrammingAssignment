package com.justinreda.jredaprogrammingassignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;

import java.util.LinkedList;

/**
 * Created by Justin on 10/19/2014.
 */
public class ProductDatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ProductsDatabase.db";
    public static SQLiteDatabase liteDatabase;

    /**
     * Default constructor
     *
     * @param context application context
     */
    public ProductDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Insert a product into a specific SQLite DB
     *
     * @param newProduct product object to be inserted into DB
     * @param database   database to insert the product into
     * @return id of new product row
     */
    public static long insertItemIntoDB(Product newProduct, SQLiteDatabase database) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductDatabase.FIELD_ID, newProduct.getId());
        contentValues.put(ProductDatabase.FIELD_NAME, newProduct.getName());
        contentValues.put(ProductDatabase.FIELD_DESCRIPTION, newProduct.getDescription());
        contentValues.put(ProductDatabase.FIELD_REGULAR_PRICE, newProduct.getRegularPrice());
        contentValues.put(ProductDatabase.FIELD_SALE_PRICE, newProduct.getSalePrice());
        contentValues.put(ProductDatabase.FIELD_PRODUCT_IMAGE, newProduct.getProductImage());
        contentValues.put(ProductDatabase.FIELD_COLORS, stringArrayToJSONArray(newProduct.getColors()).toString());
        contentValues.put(ProductDatabase.FIELD_STORES, newProduct.getStoresJSONArray().toString());
        //Log.wtf("DB HELPER", "inserting " + newProduct.getName());
        return database.insert(ProductDatabase.ProductEntry.TABLE_NAME, null, contentValues);//returns row id
    }

    /**
     * Update a product row in the database. Updates entire object
     *
     * @param newProduct the product to be updated (already updated)
     * @param database   the database to update the product in
     * @return the row id of the updated product
     */
    public static long updateItemInDB(Product newProduct, SQLiteDatabase database) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductDatabase.FIELD_ID, newProduct.getId());
        contentValues.put(ProductDatabase.FIELD_NAME, newProduct.getName());
        contentValues.put(ProductDatabase.FIELD_DESCRIPTION, newProduct.getDescription());
        contentValues.put(ProductDatabase.FIELD_REGULAR_PRICE, newProduct.getRegularPrice());
        contentValues.put(ProductDatabase.FIELD_SALE_PRICE, newProduct.getSalePrice());
        contentValues.put(ProductDatabase.FIELD_PRODUCT_IMAGE, newProduct.getProductImage());
        contentValues.put(ProductDatabase.FIELD_COLORS, stringArrayToJSONArray(newProduct.getColors()).toString());
        contentValues.put(ProductDatabase.FIELD_STORES, newProduct.getStoresJSONArray().toString());
        //Log.wtf("DB HELPER", "inserting " + newProduct.getName());
        String selection = ProductDatabase.ProductEntry.COLUMN_NAME_PRODUCT_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(newProduct.getId())};

        return database.update(ProductDatabase.ProductEntry.TABLE_NAME, contentValues, selection, selectionArgs);//returns row id
    }

    /**
     * Update an item in the database selectively. Will only update contentValue
     *
     * @param id            id of the row (product) to be updated
     * @param contentValues the field which should be updated
     * @param database      the database to update the product in
     * @return row id of the updated product
     */
    public static long updateItemInDB(int id, ContentValues contentValues, SQLiteDatabase database) {

        String selection = ProductDatabase.ProductEntry.COLUMN_NAME_PRODUCT_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};

        return database.update(ProductDatabase.ProductEntry.TABLE_NAME, contentValues, selection, selectionArgs);//returns row id
    }

    /**
     * Return all Product entries in the SQL database as a List
     *
     * @param database database to get all products from
     * @return LinkedList of Product objects from the SQLLite database
     */
    public static LinkedList<Product> getWholeDatabase(SQLiteDatabase database) {
        String[] columnsToReturn = {
                ProductDatabase.ProductEntry.COLUMN_NAME_PRODUCT_ID,
                ProductDatabase.ProductEntry.COLUMN_NAME_NAME,
                ProductDatabase.ProductEntry.COLUMN_NAME_DESCRIPTION,
                ProductDatabase.ProductEntry.COLUMN_NAME_REG_PRICE,
                ProductDatabase.ProductEntry.COLUMN_NAME_SALE_PRICE,
                ProductDatabase.ProductEntry.COLUMN_NAME_PRODUCT_IMAGE,
                ProductDatabase.ProductEntry.COLUMN_NAME_COLORS,
                ProductDatabase.ProductEntry.COLUMN_NAME_STORES};

        String sortOrder = ProductDatabase.ProductEntry.COLUMN_NAME_PRODUCT_ID + " DESC";
        Cursor cursor = null;
        cursor = database.query(
                ProductDatabase.ProductEntry.TABLE_NAME,
                columnsToReturn,
                null,//columns for where
                null,//values for where
                null,
                null,
                sortOrder);

        //add products while i can still find data
        LinkedList<Product> products = new LinkedList<Product>();
        while (cursor.getPosition() < cursor.getCount()) {
            Product product = new Product(cursor);
            products.add(product);
            cursor.moveToNext();
        }

        //remove header row if it came through
        LinkedList<Product> badEntries = new LinkedList<Product>();
        for (Product product : products) {
            if (product.getName() == null) {
                badEntries.add(product);
            }
        }
        for (Product product : badEntries) {
            products.remove(product);
        }

        //This will unreverse the list
        LinkedList<Product> tempReverse = new LinkedList<Product>();
        for (int i = products.size() - 1; i >= 0; i--) {
            tempReverse.add(products.get(i));
        }
        products = tempReverse;
        cursor.close();
        return products;
    }

    /**
     * Remove an item from the SQL database (will not update UI)
     *
     * @param foresaken the product to be deleted from the database
     * @param database  the database to delete a product from
     */
    public static void deleteItemFromDB(Product foresaken, SQLiteDatabase database) {
        String selection = ProductDatabase.ProductEntry.COLUMN_NAME_NAME + " LIKE ?";
        String[] selectionArgs = {String.valueOf(foresaken.getName())};
        database.delete(ProductDatabase.ProductEntry.TABLE_NAME, selection, selectionArgs);

    }

    /**
     * Given an input array of strings, will make return it in JSON array form.
     * This is useful for putting arrays into a DB.
     *
     * @param in the string array to convert to JSON
     * @return the JSON array equivalent of the input string array
     */
    private static JSONArray stringArrayToJSONArray(String[] in) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < in.length; i++) {
            jsonArray.put(in[i]);
        }
        return jsonArray;
    }

    /**
     * Destroy the database and all entries then recreates
     *
     * @param database the database to delete
     */
    public static void dropDB(SQLiteDatabase database) {
        if (database != null) {
            database.execSQL(ProductDatabase.SQL_DROP_ENTRIES);
            database.execSQL(ProductDatabase.SQL_CREATE_ENTRIES);
            liteDatabase = database;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ProductDatabase.SQL_DROP_ENTRIES);
        db.execSQL(ProductDatabase.SQL_CREATE_ENTRIES);
        liteDatabase = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ProductDatabase.SQL_DROP_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
