package com.justinreda.jredaprogrammingassignment;

import android.provider.BaseColumns;

/**
 * Created by Justin on 10/19/2014.
 */
public class ProductDatabase {

    static String FIELD_ID = "id";
    static String FIELD_NAME = "name";
    static String FIELD_DESCRIPTION = "description";
    static String FIELD_REGULAR_PRICE = "regular_price";
    static String FIELD_SALE_PRICE = "sale_price";
    static String FIELD_PRODUCT_IMAGE = "product_image";
    static String FIELD_COLORS = "colors";
    static String FIELD_STORES = "stores";
    static String FIELD_STORES_NUMBER = "store_number";
    static String FIELD_STORES_STOCK = "store_stock";

    final static String TEXT_TYPE = " TEXT";
    final static String INT_TYPE = " INTEGER";
    final static String DOUBLE_TYPE = " DOUBLE";
    final static String COMMA_SEP = ",";

    final static String SQL_DROP_ENTRIES = "DROP TABLE IF EXISTS " + ProductEntry.TABLE_NAME;
    final static String SQL_CHECK_ENTRIES = "SELECT name FROM sqlite_master WHERE type=\'TABLE\' AND name=\'" + ProductEntry.TABLE_NAME + "\'";

    final static String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ProductEntry.TABLE_NAME + " (" +
                    ProductEntry._ID + " INTEGER PRIMARY KEY," +
                    ProductEntry.COLUMN_NAME_PRODUCT_ID + INT_TYPE + COMMA_SEP +
                    ProductEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    ProductEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    ProductEntry.COLUMN_NAME_REG_PRICE + DOUBLE_TYPE + COMMA_SEP +
                    ProductEntry.COLUMN_NAME_SALE_PRICE + DOUBLE_TYPE + COMMA_SEP +
                    ProductEntry.COLUMN_NAME_PRODUCT_IMAGE + TEXT_TYPE + COMMA_SEP +
                    ProductEntry.COLUMN_NAME_COLORS + TEXT_TYPE + COMMA_SEP +
                    ProductEntry.COLUMN_NAME_STORES + TEXT_TYPE + " )";

    public ProductDatabase(){}

    public static abstract class ProductEntry implements BaseColumns{
        public static final String TABLE_NAME = "product";
        public static final String COLUMN_NAME_PRODUCT_ID = FIELD_ID;
        public static final String COLUMN_NAME_DESCRIPTION = FIELD_DESCRIPTION;
        public static final String COLUMN_NAME_NAME = FIELD_NAME;
        public static final String COLUMN_NAME_REG_PRICE = FIELD_REGULAR_PRICE;
        public static final String COLUMN_NAME_SALE_PRICE = FIELD_SALE_PRICE;
        public static final String COLUMN_NAME_PRODUCT_IMAGE = FIELD_PRODUCT_IMAGE;
        public static final String COLUMN_NAME_COLORS = FIELD_COLORS;
        public static final String COLUMN_NAME_STORES = FIELD_STORES;
        public static final String COLUMN_NAME_STORES_NAME = FIELD_STORES_NUMBER;
        public static final String COLUMN_NAME_STORES_STOCK = FIELD_STORES_STOCK;
    }



}
