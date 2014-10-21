package com.justinreda.jredaprogrammingassignment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by Justin on 10/20/2014.
 */
public class ProductDetailsActivity extends Activity {

    String TAG = "ProductDetailsActivity";

    ImageView smallProductIV;
    ImageView bigProductIV;

    EditText productNameET;
    TextView productNameTV;
    EditText productDescriptionET;
    EditText productRegPriceET;
    EditText productSalePriceET;

    LinearLayout productDetailsLL;
    LinearLayout productImageLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); //animation to make it look nice
        setContentView(R.layout.activity_product_details);

        setupLayout();

        Product product = (Product) getIntent().getBundleExtra("data").getSerializable("product");
        Log.wtf(TAG, "name = " + product.getName());
        double price = 5.90;
        Log.wtf(TAG, "val = " + Utilities.readDoubleAsCurrency(price));

        populateLayout(product);



    }

    private void setupLayout() {

        productDetailsLL = (LinearLayout) findViewById(R.id.product_details_LL);
        productImageLL = (LinearLayout) findViewById(R.id.product_image_LL);

        smallProductIV = (ImageView) findViewById(R.id.small_product_image_IV);
        bigProductIV = (ImageView) findViewById(R.id.big_product_image_IV);

        productNameET = (EditText) findViewById(R.id.product_details_name_ET);
        productNameTV = (TextView) findViewById(R.id.product_details_name_TV);
        productDescriptionET = (EditText) findViewById(R.id.product_details_description_ET);
        productRegPriceET = (EditText) findViewById(R.id.product_details_reg_price_ET);
        productSalePriceET = (EditText) findViewById(R.id.product_details_sale_price_ET);
    }

    private void populateLayout(Product product) {

        productDetailsLL.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        productNameET.setText(product.getName());
        productNameET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    productNameTV.setText(productNameET.getText().toString());
                    productNameET.setVisibility(View.GONE);
                    productNameTV.setVisibility(View.VISIBLE);
                }
            }
        });
        productNameTV.setText(product.getName());
        productNameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productNameTV.setVisibility(View.GONE);
                productNameET.setVisibility(View.VISIBLE);
            }
        });
        productNameTV.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    productNameTV.setText(productNameET.getText().toString());
                    productNameET.setVisibility(View.GONE);
                    productNameTV.setVisibility(View.VISIBLE);
                }
            }
        });
        productDescriptionET.setText("[" + product.getDescription() + "] " + getResources().getString(R.string.lorem_ipsum));


        String regPrice = Utilities.readDoubleAsCurrency(product.getRegularPrice());
        String salePrice = Utilities.readDoubleAsCurrency(product.getSalePrice());
        productRegPriceET.setText(regPrice);
        productSalePriceET.setText(salePrice);

        HashMap<String, Integer> drawableHashMap;
        drawableHashMap = FirstActivity.getImageNamesRes(getApplicationContext());
        String imageName = product.getProductImage();
        smallProductIV.setImageResource(drawableHashMap.get(imageName));
        bigProductIV.setImageResource(drawableHashMap.get(imageName));
        //bigProductIV.setScaleType(ImageView.ScaleType.FIT_XY);

        smallProductIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productImageLL.setVisibility(View.VISIBLE);
                productDetailsLL.setVisibility(View.GONE);
            }
        });
        bigProductIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productDetailsLL.setVisibility(View.VISIBLE);
                productImageLL.setVisibility(View.GONE);
            }
        });
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
