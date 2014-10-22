package com.justinreda.jredaprogrammingassignment;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

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
    LinearLayout productColorsTVLL;
    LinearLayout productColorsETLL;
    LinearLayout productStoresTVLL;
    LinearLayout productStoresETLL;

    Product theProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_product_details);

        theProduct = (Product) getIntent().getBundleExtra("data").getSerializable("product");

        setupLayout();
        populateLayout(theProduct);

    }

    /**
     * Assign all layout elements to their XML elements
     */
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

        productColorsETLL = (LinearLayout) findViewById(R.id.product_details_colors_ETLL);
        productColorsTVLL = (LinearLayout) findViewById(R.id.product_details_colors_TVLL);

        productStoresETLL = (LinearLayout) findViewById(R.id.product_details_stores_ETLL);
        productStoresTVLL = (LinearLayout) findViewById(R.id.product_details_stores_TVLL);
    }

    /**
     * Create the layout and populate all fields with teh data from the local product.
     * Can also be used an an update function (just pass in updated product)
     *
     * @param product the product in which to populate all of the fields.
     */
    private void populateLayout(Product product) {

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
        drawableHashMap = FirstActivity.getImageNamesRes();
        String imageName = product.getProductImage();
        smallProductETIV.setImageResource(drawableHashMap.get(imageName));
        smallProductTVIV.setImageResource(drawableHashMap.get(imageName));
        bigProductIV.setImageResource(drawableHashMap.get(imageName));

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

        int index = 0;
        for (final String color : theProduct.getColors()) {
            final LinearLayout colorETLL = new LinearLayout(getApplicationContext());
            final LinearLayout colorTVLL = new LinearLayout(getApplicationContext());
            colorETLL.setOrientation(LinearLayout.HORIZONTAL);
            colorTVLL.setOrientation(LinearLayout.HORIZONTAL);

            TextView colorNameTV = new TextView(getApplicationContext(), null, R.style.TextViewNormal);
            TextView colorNameET = new TextView(getApplicationContext(), null, R.style.TextViewNormal);
            android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
            colorNameTV.setLayoutParams(params);
            colorNameET.setLayoutParams(params);
            colorNameET.setText(color);
            colorNameET.setTextColor(getResources().getColor(R.color.black));
            colorNameTV.setText(color);
            colorNameTV.setTextColor(getResources().getColor(R.color.black));
            colorETLL.addView(colorNameET, 0);
            colorTVLL.addView(colorNameTV, 0);
            Button removeColorTV = new Button(getApplicationContext());
            removeColorTV.setText("Remove");
            removeColorTV.setBackgroundColor(getResources().getColor(R.color.off_white));
            removeColorTV.setTextColor(getResources().getColor(R.color.black));
            removeColorTV.setLayoutParams(params);
            removeColorTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    theProduct.removeColor(color);
                    productColorsETLL.removeView(colorETLL);
                    productColorsTVLL.removeView(colorTVLL);
                    theProduct.setColors(theProduct.removeColor(color));
                    updateProductInDB();
                }
            });
            Button removeColorET = new Button(getApplicationContext());
            removeColorET.setText("Remove");
            removeColorET.setBackgroundColor(getResources().getColor(R.color.off_white));
            removeColorET.setTextColor(getResources().getColor(R.color.black));
            removeColorET.setLayoutParams(params);
            removeColorET.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    theProduct.removeColor(color);
                    productColorsETLL.removeView(colorETLL);
                    productColorsTVLL.removeView(colorTVLL);
                    theProduct.setColors(theProduct.removeColor(color));
                    updateProductInDB();
                }
            });
            colorETLL.addView(removeColorET, 1);
            colorTVLL.addView(removeColorTV, 1);

            productColorsTVLL.addView(colorTVLL, index);
            index++;
        }

        Hashtable<String, Integer> storeHM = theProduct.getStores();
        Set<String> storesArray = theProduct.getStores().keySet();

        Log.wtf(TAG, "JSON = " + storeHM.size());
        for (String store : storesArray) {
            final LinearLayout storesTVLL = new LinearLayout(getApplicationContext());
            storesTVLL.setOrientation(LinearLayout.HORIZONTAL);
            TextView storeNameTV = new TextView(getApplicationContext(), null, R.style.TextViewNormal);
            android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
            storeNameTV.setLayoutParams(params);
            storeNameTV.setText(store);
            storeNameTV.setTextColor(getResources().getColor(R.color.black));
            storesTVLL.addView(storeNameTV, 0);

            TextView storeStockTV = new TextView(getApplicationContext(), null, R.style.TextViewNormal);
            storeStockTV.setLayoutParams(params);
            storeStockTV.setText("" + storeHM.get(store));
            storeStockTV.setTextColor(getResources().getColor(R.color.black));
            storesTVLL.addView(storeStockTV, 1);
            productStoresTVLL.addView(storesTVLL);

        }

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
                updateLocalProduct();
                populateLayout(theProduct);
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
            }
        });
        final Activity activity = this;
        deleteTVBUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity.getApplicationContext(), "delete", Toast.LENGTH_SHORT).show();
                ProductDatabaseHelper.deleteItemFromDB(theProduct, db);
                activity.finish();
            }
        });
        deleteETBUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity.getApplicationContext(), "delete", Toast.LENGTH_SHORT).show();
                ProductDatabaseHelper.deleteItemFromDB(theProduct, db);
                activity.finish();
            }
        });
    }

    /**
     * Update the local product from the editable fields.
     * Effectively the save data command. (will not update SQL)
     */
    private void updateLocalProduct() {

        String newName = productNameET.getText().toString();
        productNameTV.setText(newName);
        theProduct.setName(newName);
        String newDescription = productDescriptionET.getText().toString();
        productDescriptionTV.setText(newDescription);
        theProduct.setDescription(newDescription);

        String newRegPrice = productRegPriceET.getText().toString();
        if (newRegPrice.startsWith("$")) newRegPrice = newRegPrice.substring(1);
        Double newRegDouble = Double.parseDouble(newRegPrice);
        String newRegStr = Utilities.readDoubleAsCurrency(newRegDouble);
        productRegPriceTV.setText(newRegStr);
        productRegPriceET.setText(newRegStr);
        theProduct.setRegularPrice(newRegDouble);

        String newSalePrice = productSalePriceET.getText().toString();
        if (newSalePrice.startsWith("$")) newSalePrice = newSalePrice.substring(1);
        Double newSaleDouble = Double.parseDouble(newSalePrice);
        String newSaleStr = Utilities.readDoubleAsCurrency(newSaleDouble);
        productSalePriceTV.setText(newSaleStr);
        productSalePriceET.setText(newSaleStr);
        theProduct.setSalePrice(newSaleDouble);

        theProduct.setColors(theProduct.getColors());
        theProduct.setStores(theProduct.getStores());

        updateProductInDB();
    }

    /**
     * Update the current product in the database
     */
    private void updateProductInDB() {
        ProductDatabaseHelper productDatabaseHelper = new ProductDatabaseHelper(getApplicationContext());
        final SQLiteDatabase db = productDatabaseHelper.getWritableDatabase();
        ProductDatabaseHelper.updateItemInDB(theProduct, db);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return true;
    }

}
