package com.justinreda.jredaprogrammingassignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;

import java.util.LinkedList;

/**
 * Created by Justin on 10/19/2014.
 */
public class ProductDatabaseHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ProductsDatabase.db";

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
        Log.wtf("DB HELPER", "inserting " + newProduct.getName());
        return database.insert(ProductDatabase.ProductEntry.TABLE_NAME, null, contentValues);//returns row id
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
        //Log.wtf("PRODUCTDBHELPER","rows = "+cursor.getCount());

        LinkedList<Product> products = new LinkedList<Product>();
        //Log.wtf("PRODUCTDBHELPER","first "+cursor.moveToFirst());
        /*for (int i = 0; i<cursor.getCount();i++){
            Log.wtf("PRODUCTDBHELPER","Cursor POS1 "+cursor.getPosition());
            Log.wtf("PRODUCTDBHELPER","Cursor POS11 "+cursor.getColumnName(1));
            Log.wtf("PRODUCTDBHELPER","Cursor POS111 "+cursor.getString(1));

            Product product = new Product(cursor);
            products.add(product);
            //Log.wtf("FIRSTACTIVITY","name under cursor "+cursor.getString(cursor.getColumnIndex(ProductDatabase.ProductEntry.COLUMN_NAME_NAME)));
            //Log.wtf("FIRSTACTIVITY","next " +cursor.moveToNext());

            cursor.moveToPosition(i);
        }*/
        while (cursor.getPosition() < cursor.getCount()) {
            Product product = new Product(cursor);
            products.add(product);
            //Log.wtf("PRODUCTDBHELPER","Cursor POS1 "+cursor.getPosition());
            //Log.wtf("PRODUCTDBHELPER","Cursor POS11 "+cursor.getColumnName(1));
            //Log.wtf("PRODUCTDBHELPER","Cursor POS111 "+cursor.getString(1));
            cursor.moveToNext();
        }
        /*Log.wtf("PRODUCTDBHELPER","Cursor POS2 "+cursor.getPosition());
        Log.wtf("PRODUCTDBHELPER","Cursor POS22 "+cursor.getColumnName(1));
        Log.wtf("PRODUCTDBHELPER","Cursor POS222 "+cursor.getString(1));

        Product product2 = new Product(cursor);
        products.add(product2);*/
        /*Product product2 = new Product(cursor);
        products.add(product2);
        Log.wtf("FIRSTACTIVITY","name under cursor "+cursor.getString(cursor.getColumnIndex(ProductDatabase.ProductEntry.COLUMN_NAME_NAME)));
        Log.wtf("FIRSTACTIVITY","next " +cursor.moveToNext());*/

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

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ProductDatabase.SQL_DROP_ENTRIES);
        db.execSQL(ProductDatabase.SQL_CREATE_ENTRIES);
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
