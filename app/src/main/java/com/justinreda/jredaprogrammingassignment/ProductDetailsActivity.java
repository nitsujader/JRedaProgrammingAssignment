package com.justinreda.jredaprogrammingassignment;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by Justin on 10/20/2014.
 */
public class ProductDetailsActivity extends Activity {

    String TAG = "ProductDetailsActivity";

    ImageView smallProductETIV;
    ImageView smallProductTVIV;
    ImageView bigProductIV;

    Button updateBUT;
    Button saveBUT;
    Button deleteTVBUT;
    Button deleteETBUT;

    EditText productNameET;
    TextView productNameTV;
    EditText productDescriptionET;
    TextView productDescriptionTV;
    EditText productRegPriceET;
    TextView productRegPriceTV;
    EditText productSalePriceET;
    TextView productSalePriceTV;

    LinearLayout productDetailsLL;
    LinearLayout productDetailsTVLL;
    LinearLayout productDetailsETLL;
    LinearLayout productImageLL;

    Product theProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); //animation to make it look nice
        setContentView(R.layout.activity_product_details);

        theProduct = (Product) getIntent().getBundleExtra("data").getSerializable("product");

        setupLayout();
        populateLayout(this, theProduct);

    }

    private void setupLayout() {

        productDetailsLL = (LinearLayout) findViewById(R.id.product_details_LL);
        productDetailsETLL = (LinearLayout) findViewById(R.id.product_details_ETLL);
        productDetailsTVLL = (LinearLayout) findViewById(R.id.product_details_TVLL);

        productImageLL = (LinearLayout) findViewById(R.id.product_image_LL);

        smallProductETIV = (ImageView) findViewById(R.id.small_product_image_ETIV);
        smallProductTVIV = (ImageView) findViewById(R.id.small_product_image_TVIV);
        bigProductIV = (ImageView) findViewById(R.id.big_product_image_IV);

        productNameET = (EditText) findViewById(R.id.product_details_name_ET);
        productNameTV = (TextView) findViewById(R.id.product_details_name_TV);

        productDescriptionET = (EditText) findViewById(R.id.product_details_description_ET);
        productDescriptionTV = (TextView) findViewById(R.id.product_details_description_TV);

        productRegPriceET = (EditText) findViewById(R.id.product_details_reg_price_ET);
        productRegPriceTV = (TextView) findViewById(R.id.product_details_reg_price_TV);

        productSalePriceET = (EditText) findViewById(R.id.product_details_sale_price_ET);
        productSalePriceTV = (TextView) findViewById(R.id.product_details_sale_price_TV);

        updateBUT = (Button) findViewById(R.id.product_details_update_BUT);
        saveBUT = (Button) findViewById(R.id.product_details_save_BUT);
        deleteTVBUT = (Button) findViewById(R.id.product_details_delete_TVBUT);
        deleteETBUT = (Button) findViewById(R.id.product_details_delete_ETBUT);

    }

    private void populateLayout(final Context context, Product product) {


        productNameET.setText(product.getName());
        productNameTV.setText(product.getName());

        productDescriptionET.setText("[" + product.getDescription() + "] " + getResources().getString(R.string.lorem_ipsum));
        productDescriptionTV.setText("[" + product.getDescription() + "] " + getResources().getString(R.string.lorem_ipsum));

        String regPrice = Utilities.readDoubleAsCurrency(product.getRegularPrice());
        String salePrice = Utilities.readDoubleAsCurrency(product.getSalePrice());
        productRegPriceET.setText(regPrice);
        productRegPriceTV.setText(regPrice);
        productSalePriceET.setText(salePrice);
        productSalePriceTV.setText(salePrice);

        HashMap<String, Integer> drawableHashMap;
        drawableHashMap = FirstActivity.getImageNamesRes(getApplicationContext());
        String imageName = product.getProductImage();
        smallProductETIV.setImageResource(drawableHashMap.get(imageName));
        smallProductTVIV.setImageResource(drawableHashMap.get(imageName));
        bigProductIV.setImageResource(drawableHashMap.get(imageName));
        //bigProductIV.setScaleType(ImageView.ScaleType.FIT_XY);

        smallProductTVIV.setOnClickListener(new View.OnClickListener() {
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

        ProductDatabaseHelper productDatabaseHelper = new ProductDatabaseHelper(getApplicationContext());
        final SQLiteDatabase db = productDatabaseHelper.getWritableDatabase();

        updateBUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productDetailsTVLL.setVisibility(View.GONE);
                productDetailsETLL.setVisibility(View.VISIBLE);
            }
        });

        saveBUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productDetailsTVLL.setVisibility(View.VISIBLE);
                productDetailsETLL.setVisibility(View.GONE);

                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
            }
        });
        final Activity activity = this;
        deleteTVBUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: ask are you sure
                Toast.makeText(activity.getApplicationContext(), "delete", Toast.LENGTH_SHORT).show();
                ProductDatabaseHelper.deleteItemFromDB(theProduct, db);
                activity.finish();
            }
        });
        deleteTVBUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: ask are you sure
                Toast.makeText(activity.getApplicationContext(), "delete", Toast.LENGTH_SHORT).show();
                ProductDatabaseHelper.deleteItemFromDB(theProduct, db);
                activity.finish();
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
