package com.justinreda.jredaprogrammingassignment;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.LinkedList;

/**
 * Created by Justin on 10/20/2014.
 */
public class ShowProductsActivity extends Activity {

    String TAG = "ShowProductsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_products);

        ProductDatabaseHelper productDatabaseHelper = new ProductDatabaseHelper(getApplicationContext());
        SQLiteDatabase db = productDatabaseHelper.getWritableDatabase();
        LinkedList<Product> products = ProductDatabaseHelper.getWholeDatabase(db);
    }

}
