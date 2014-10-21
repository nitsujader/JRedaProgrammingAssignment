package com.justinreda.jredaprogrammingassignment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by Justin on 10/19/2014.
 */
public class FirstActivity extends Activity {

    String FIELD_ID = "id";
    String FIELD_NAME = "name";
    String FIELD_DESCRIPTION = "description";
    String FIELD_REGULAR_PRICE = "regular_price";
    String FIELD_SALE_PRICE = "sale_price";
    String FIELD_PRODUCT_IMAGE = "product_image";
    String FIELD_COLORS = "colors";
    String FIELD_STORES = "stores";
    String FIELD_STORES_NUMBER = "store_number";
    String FIELD_STORES_STOCK = "store_stock";
    String MOCK_DATA_FILENAME = "ExampleData.json";

    Button createProductBUT;
    Button showProductBUT;
    Button loadFileBUT;
    Button showContentsBUT;
    Button createDatabaseBUT;
    Button showFileBUT;
    Button loadFromAssetsBUT;
    Button loadFromWebBUT;
    Button runBUT;
    Button meAddProductBUT, meNewProductBUT;

    TextView addProductOptionsTV;
    TextView showContentsTV;

    LinearLayout initialButtonsRowLL;
    LinearLayout createProductMethodLL;
    LinearLayout addProductMethodOptionsLL;
    LinearLayout fromFileOptionsLL;
    LinearLayout internalSystemOptionsLL;
    LinearLayout enterManuallyLL;
    LinearLayout showProductLL;
    LinearLayout fromFileLoadFileOptionsButtonRowLL;
    LinearLayout[] importOptionsLLs;

    RadioGroup createProductsMethodsRG;

    ScrollView showFileSV;

    EditText numEntriesET;
    EditText numStoresET;
    //manual entry fields
    EditText meNameET, meDescriptionET, meRegPriceET, meSalePriceET, meNumColorsET, meNumStoresET;

    boolean isDataLoaded = false;
    String dataString;
    JSONArray manualEntries;

    LinkedList<Product> productsList = new LinkedList<Product>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getApplicationContext().deleteDatabase("ProductsDatabase.db");

        initializeUI();
    }

    /**
     * Initializes the UI elements, initializes variables, adds listeners
     */
    private void initializeUI() {

        createProductBUT = (Button) findViewById(R.id.add_product_BUT);
        showProductBUT = (Button) findViewById(R.id.show_product_BUT);
        createDatabaseBUT = (Button) findViewById(R.id.create_database_BUT);
        loadFileBUT = (Button) findViewById(R.id.from_file_load_file_BUT);
        showFileBUT = (Button) findViewById(R.id.from_file_show_contents_BUT);
        loadFromAssetsBUT = (Button) findViewById(R.id.load_from_file_assets_BUT);
        loadFromWebBUT = (Button) findViewById(R.id.load_from_file_web_server_BUT);
        runBUT = (Button) findViewById(R.id.run_randomizer_BUT);
        meAddProductBUT = (Button) findViewById(R.id.manual_product_add_BUT);
        meNewProductBUT = (Button) findViewById(R.id.manual_product_new_BUT);

        addProductOptionsTV = (TextView) findViewById(R.id.add_product_method_options_title_TV);
        showContentsTV = (TextView) findViewById(R.id.from_file_show_contents_TV);

        initialButtonsRowLL = (LinearLayout) findViewById(R.id.initial_buttons_row_LL);
        createProductMethodLL = (LinearLayout) findViewById(R.id.create_product_method_LL);
        addProductMethodOptionsLL = (LinearLayout) findViewById(R.id.add_product_method_options_LL);
        showProductLL = (LinearLayout) findViewById(R.id.show_product_LL);
        fromFileLoadFileOptionsButtonRowLL = (LinearLayout) findViewById(R.id.from_file_load_file_options_LL);
        fromFileOptionsLL = (LinearLayout) findViewById(R.id.from_file_options_LL);
        internalSystemOptionsLL = (LinearLayout) findViewById(R.id.internal_randomizer_LL);
        enterManuallyLL = (LinearLayout) findViewById(R.id.enter_manually_LL);

        showFileSV = (ScrollView) findViewById(R.id.from_file_show_contents_SV);

        numEntriesET = (EditText) findViewById(R.id.randomizer_entries_ET);
        numStoresET = (EditText) findViewById(R.id.randomizer_stores_ET);
        //Manual entry fields
        meNameET = (EditText) findViewById(R.id.manual_product_name_ET);
        meDescriptionET = (EditText) findViewById(R.id.manual_product_description_ET);
        meRegPriceET = (EditText) findViewById(R.id.manual_product_reg_price_ET);
        meSalePriceET = (EditText) findViewById(R.id.manual_product_sale_price_ET);
        meNumColorsET = (EditText) findViewById(R.id.manual_product_num_colors_ET);
        meNumStoresET = (EditText) findViewById(R.id.manual_product_num_stores_ET);

        importOptionsLLs = new LinearLayout[3];
        importOptionsLLs[0] = fromFileOptionsLL;
        importOptionsLLs[1] = internalSystemOptionsLL;
        importOptionsLLs[2] = enterManuallyLL;


        createProductsMethodsRG = (RadioGroup) findViewById(R.id.add_product_method_RG);
        createProductsMethodsRG.clearCheck();
        createProductsMethodsRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                //Hide all the other views
                for (LinearLayout layout : importOptionsLLs) {
                    if (layout.getVisibility() == View.VISIBLE) {
                        layout.setVisibility(View.GONE);
                    }
                }

                if (showFileSV.getVisibility() == View.VISIBLE) {
                    showFileSV.setVisibility(View.GONE);
                }

                fromFileLoadFileOptionsButtonRowLL.setVisibility(View.GONE);

                if (createDatabaseBUT.getVisibility() == View.VISIBLE) {
                    createDatabaseBUT.setVisibility(View.GONE);
                    dataString = null;
                    isDataLoaded = false;
                }

                switch (checkedId) {
                    case R.id.apm_from_file_RB: // from file
                        addProductOptionsTV.setText("Select option");
                        fromFileOptionsLL.setVisibility(View.VISIBLE);

                        if (isDataLoaded) {
                            showFileBUT.setEnabled(true);
                        } else {
                            showFileBUT.setEnabled(false);
                        }

                        loadFileBUT.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (showFileSV.getVisibility() == View.VISIBLE) {
                                    showFileSV.setVisibility(View.GONE);
                                }
                                fromFileLoadFileOptionsButtonRowLL.setVisibility(View.VISIBLE);

                                loadFromAssetsBUT.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //Toast.makeText(getApplicationContext(), "Loaded!", Toast.LENGTH_SHORT).show();

                                        dataString = Utilities.getStringFromAsset(getApplicationContext(), "" + MOCK_DATA_FILENAME);
                                        if (dataString != null) {
                                            showContentsTV.setText(dataString);
                                            isDataLoaded = true;
                                            showFileBUT.setEnabled(true);
                                            createDatabaseBUT.setVisibility(View.VISIBLE);
                                            Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                loadFromWebBUT.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //Toast.makeText(getApplicationContext(), "Loaded!", Toast.LENGTH_SHORT).show();

                                        Thread thread = new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //TODO: show loading dialog
                                                dataString = Utilities.getStringFromURL("https://www.dropbox.com/s/nb0he3qki83ql73/ExampleData.json?dl=1");

                                                if (dataString != null) {
                                                    showContentsTV.setText(dataString);
                                                    isDataLoaded = true;
                                                    Handler handler = new Handler(getMainLooper());
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            showFileBUT.setEnabled(true);
                                                            createDatabaseBUT.setVisibility(View.VISIBLE);
                                                            Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                } else {
                                                    Handler handler = new Handler(getMainLooper());
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                        thread.start();
                                    }
                                });

                            }
                        });

                        showFileBUT.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (fromFileLoadFileOptionsButtonRowLL.getVisibility() == View.VISIBLE) {
                                    fromFileLoadFileOptionsButtonRowLL.setVisibility(View.GONE);
                                }
                                if (showFileSV.getVisibility() == View.VISIBLE) {
                                    showFileSV.setVisibility(View.GONE);
                                } else {
                                    showFileSV.setVisibility(View.VISIBLE);
                                }

                                if (isDataLoaded) {
                                    showContentsTV.setText(dataString);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Data not loaded!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        clearManualFields();
                        break;
                    case R.id.apm_internal_system_RB: // internal randomizer
                        addProductOptionsTV.setText("Please enter values below");
                        internalSystemOptionsLL.setVisibility(View.VISIBLE);

                        runBUT.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Utilities.hideKeyboard(getApplicationContext(), runBUT);

                                int entries = 3;
                                if (Utilities.etNotNullNotEmpty(numEntriesET)) {
                                    entries = Integer.parseInt(numEntriesET.getText().toString());
                                } else {
                                    numEntriesET.setText("" + entries);
                                }

                                int stores = 3;
                                if (Utilities.etNotNullNotEmpty(numStoresET)) {
                                    stores = Integer.parseInt(numStoresET.getText().toString());
                                } else {
                                    numStoresET.setText("" + stores);
                                }

                                dataString = (runRandomizer(entries, stores)).toString();
                                if (dataString != null) {
                                    isDataLoaded = true;
                                    createDatabaseBUT.setVisibility(View.VISIBLE);
                                    createDatabaseBUT.setEnabled(true);
                                    Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        clearManualFields();
                        break;

                    case R.id.apm_enter_manually_RB: // enter manually
                        addProductOptionsTV.setText("Please enter values below");
                        enterManuallyLL.setVisibility(View.VISIBLE);

                        break;

                }
            }
        });


        createProductBUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productsList = null;//null out any old info
                if (showProductLL.getVisibility() == View.VISIBLE) {
                    showProductLL.setVisibility(View.GONE);
                }
                createProductMethodLL.setVisibility(View.VISIBLE);
            }
        });


        showProductBUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.hideKeyboard(getApplicationContext(), showProductBUT);
                //open show products activity
                Bundle baggage = new Bundle();
                baggage.putString("datastr", dataString);
                Intent intent = new Intent(getApplicationContext(), ShowProductsActivity.class);
                intent.putExtra("dataset", baggage);
                startActivity(intent);
            }
        });

        createDatabaseBUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataString == null) {
                    if (manualEntries != null) {
                        dataString = manualEntries.toString();
                    }
                }
                Utilities.hideKeyboard(getApplicationContext(), createDatabaseBUT);
                createDatabase(dataString);
                showProductBUT.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Database created!", Toast.LENGTH_SHORT).show();
            }
        });

        meAddProductBUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkManualFields()) {
                    JSONObject newJSONProduct = hybridCreateProduct(
                            Integer.parseInt(meNumColorsET.getText().toString()),
                            Integer.parseInt(meNumStoresET.getText().toString()));

                    Product newProduct = new Product(newJSONProduct);
                    if (manualEntries == null) {
                        manualEntries = new JSONArray();
                    }
                    if (productsList == null) {
                        productsList = new LinkedList<Product>();
                        productsList.add(newProduct);
                        manualEntries.put(newJSONProduct);
                        createDatabaseBUT.setEnabled(true);
                        createDatabaseBUT.setVisibility(View.VISIBLE);

                    } else {
                        boolean foundMatch = false;
                        for (Product product : productsList) {
                            if (product.getName().matches(newProduct.getName())) {
                                foundMatch = true;
                            }
                        }
                        if (!foundMatch) {
                            productsList.add(newProduct);
                            manualEntries.put(newJSONProduct);
                            createDatabaseBUT.setEnabled(true);
                            createDatabaseBUT.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(getApplicationContext(), "Entry already in database!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        meNewProductBUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearManualFields();
            }
        });
    }

    private void clearManualFields() {
        meNameET.setText("");
        meDescriptionET.setText("");
        meRegPriceET.setText("");
        meSalePriceET.setText("");
        meNumColorsET.setText("");
        meNumStoresET.setText("");
    }

    private boolean checkManualFields() {

        if (Utilities.etNotNullNotEmpty(meNameET)) {
            if (Utilities.etNotNullNotEmpty(meDescriptionET)) {
                if (Utilities.etNotNullNotEmpty(meRegPriceET)) {
                    if (Utilities.etNotNullNotEmpty(meSalePriceET)) {
                        if (Utilities.etNotNullNotEmpty(meNumColorsET)) {
                            if (Utilities.etNotNullNotEmpty(meNumStoresET)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        Toast.makeText(getApplicationContext(), "All fields required!", Toast.LENGTH_SHORT).show();
        return false;
    }

    private LinkedList<Product> createDatabase(String dataIn) {
        LinkedList<Product> database = null;
        try {
            JSONArray productsArray = new JSONArray(dataIn);
            int numFoundProducts = productsArray.length();

            ProductDatabaseHelper productDatabaseHelper = new ProductDatabaseHelper(getApplicationContext());
            SQLiteDatabase db = productDatabaseHelper.getWritableDatabase();

            for (int i=0;i<numFoundProducts;i++){
                Product newProduct = new Product(productsArray.getJSONObject(i));
                //Log.wtf("FirstActivity","adding "+newProduct.toString());
                ProductDatabaseHelper.insertItemIntoDB(newProduct, db);
            }

            database = ProductDatabaseHelper.getWholeDatabase(db);
            LinkedList<Product> badEntries = new LinkedList<Product>();
            for(Product product: database){
                if (product.getName() != null) {
                    Log.wtf("FirstActivity123", product.toString());
                } else {
                    badEntries.add(product);
                    Log.wtf("FirstActivity123", "removing null entry");
                }
            }
            for (Product product : badEntries) {
                database.remove(product);
            }

            //at this point database is the winner, all good entries hopefully.

        } catch (JSONException e) {
            e.printStackTrace();
        }
        productsList = database;
        return database;
    }

    private LinkedList<String> getImageNames(){
        LinkedList<String> imageNames = new LinkedList<String>();
        imageNames.add("1");
        imageNames.add("2");
        imageNames.add("3");
        imageNames.add("4");
        imageNames.add("5");
        imageNames.add("6");
        imageNames.add("7");
        imageNames.add("8");
        imageNames.add("9");
        imageNames.add("0");

        return imageNames;
    }

    private JSONArray runRandomizer(int numEntries, int numStores) {
        try {

            double priceMin = 15.00;
            double priceMax = 99.99;
            int stockMin = 0;
            int stockMax = 15;

            LinkedList<String> imageNames = getImageNames();

            JSONArray itemsArray = new JSONArray();
            for (int i = 0; i < numEntries; i++) {
                JSONObject item = new JSONObject();

                item.put(FIELD_ID, i);
                item.put(FIELD_NAME, "Item " + i);
                item.put(FIELD_DESCRIPTION, "Description " + i);

                double randomRegular = Utilities.getRandomDoubleInRange(priceMin, priceMax);
                randomRegular = Utilities.truncateDouble(randomRegular, 2);
                double randomSale = randomRegular * 0.75;
                randomSale = Utilities.truncateDouble(randomSale, 2);
                item.put(FIELD_REGULAR_PRICE, randomRegular);
                item.put(FIELD_SALE_PRICE, randomSale);
                int randomPicID = Utilities.getRandomIntInRange(0, 9);
                item.put(FIELD_PRODUCT_IMAGE, "imagename_" + randomPicID);

                int numColorsToPick = 4;
                String[] colors = {"Red","Blue","Green","Black","Yellow","Orange","Gold","Silver","Chrome"};
                JSONArray colorsArray = new JSONArray();
                LinkedList<Integer> pickedColors = new LinkedList<Integer>();
                for(int j=0;j<numColorsToPick;j++){
                    int pickedNum = Utilities.getRandomIntInRange(0,8);
                    while(pickedColors.contains(pickedNum)){
                        pickedNum = Utilities.getRandomIntInRange(0,8);
                    }
                    pickedColors.add(pickedNum);
                    colorsArray.put(colors[pickedNum]);
                }
                item.put(FIELD_COLORS, colorsArray);

                JSONArray storesArray = new JSONArray();
                for (int j = 0; j < numStores; j++) {
                    JSONObject store = new JSONObject();
                    store.put(FIELD_STORES_NUMBER, "Store " + j);
                    int randomStock = Utilities.getRandomIntInRange(stockMin, stockMax);
                    store.put(FIELD_STORES_STOCK, randomStock);
                    storesArray.put(store);
                }
                item.put(FIELD_STORES, storesArray);

                itemsArray.put(item);
            }

            dataString = itemsArray.toString();
            isDataLoaded = true;
            return itemsArray;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject hybridCreateProduct(int numColors, int numStores) {
        try {

            String name = meNameET.getText().toString();
            String description = meDescriptionET.getText().toString();
            Double regPrice = Utilities.truncateDouble(Double.parseDouble(meRegPriceET.getText().toString()), 2);
            Double salePrice = Utilities.truncateDouble(Double.parseDouble(meSalePriceET.getText().toString()), 2);

            int stockMin = 0;
            int stockMax = 15;

            LinkedList<String> imageNames = getImageNames();
            int randomPicID = Utilities.getRandomIntInRange(0, 9);
            int startId = 0;
            if (productsList != null) {
                startId = productsList.size();
            }
            JSONObject item = new JSONObject();

            item.put(FIELD_ID, startId);
            item.put(FIELD_NAME, name);
            item.put(FIELD_DESCRIPTION, description);

            item.put(FIELD_REGULAR_PRICE, regPrice);
            item.put(FIELD_SALE_PRICE, salePrice);
            item.put(FIELD_PRODUCT_IMAGE, "imagename_" + randomPicID);

            String[] colors = {"Red", "Blue", "Green", "Black", "Yellow", "Orange", "Gold", "Silver", "Chrome"};
            JSONArray colorsArray = new JSONArray();
            LinkedList<Integer> pickedColors = new LinkedList<Integer>();
            for (int j = 0; j < numColors; j++) {
                int pickedNum = Utilities.getRandomIntInRange(0, 8);
                while (pickedColors.contains(pickedNum)) {
                    pickedNum = Utilities.getRandomIntInRange(0, 8);
                }
                pickedColors.add(pickedNum);
                colorsArray.put(colors[pickedNum]);
            }
            item.put(FIELD_COLORS, colorsArray);

            JSONArray storesArray = new JSONArray();
            for (int j = 0; j < numStores; j++) {
                JSONObject store = new JSONObject();
                store.put(FIELD_STORES_NUMBER, "Store " + j);
                int randomStock = Utilities.getRandomIntInRange(stockMin, stockMax);
                store.put(FIELD_STORES_STOCK, randomStock);
                storesArray.put(store);
            }
            item.put(FIELD_STORES, storesArray);

            return item;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outgoingState) {
        super.onSaveInstanceState(outgoingState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
    }
}
