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
public class ProductDatabaseHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ProductsDatabase.db";
    public static SQLiteDatabase liteDatabase;

    public ProductDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //context.deleteDatabase(DATABASE_NAME);
    }

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

    public static long updateItemInDB(int id, ContentValues contentValues, SQLiteDatabase database) {

        String selection = ProductDatabase.ProductEntry.COLUMN_NAME_PRODUCT_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};

        return database.update(ProductDatabase.ProductEntry.TABLE_NAME, contentValues, selection, selectionArgs);//returns row id
    }

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

    public static void deleteItemFromDB(Product foresaken, SQLiteDatabase database) {
        String selection = ProductDatabase.ProductEntry.COLUMN_NAME_NAME + " LIKE ?";
        String[] selectionArgs = {String.valueOf(foresaken.getName())};
        database.delete(ProductDatabase.ProductEntry.TABLE_NAME, selection, selectionArgs);

    }

    private static JSONArray stringArrayToJSONArray(String[] in) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < in.length; i++) {
            jsonArray.put(in[i]);
        }
        return jsonArray;
    }

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
