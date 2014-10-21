package com.justinreda.jredaprogrammingassignment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Justin on 10/20/2014.
 */
public class ProductDetailsActivity extends Activity {

    String TAG = "ProductDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); //animation to make it look nice
        setContentView(R.layout.activity_product_details);

        Product product = (Product) getIntent().getBundleExtra("data").getSerializable("product");
        Log.wtf(TAG, "name = " + product.getName());
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); //animation to make it look nice
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); //animation to make it look nice
        return true;
    }
}
