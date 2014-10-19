package com.justinreda.jredaprogrammingassignment;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
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
    Button showFileContentsBUT;
    Button createDatabaseBUT;

    TextView addProductOptionsTV;

    LinearLayout initialButtonsRowLL;
    LinearLayout createProductMethodLL;
    LinearLayout addProductMethodOptionsLL;
    LinearLayout fromFileOptionsLL;
    LinearLayout internalSystemOptionsLL;
    LinearLayout enterManuallyLL;
    LinearLayout fromSQLLL;
    LinearLayout[] importOptionsLLs;

    RadioGroup createProductsMethodsRG;

    LinearLayout showProductLL;

    Button showFileBUT;
    LinearLayout fromFileLoadFileOptionsButtonRowLL;
    ScrollView showFileSV;

    Button loadFromAssetsBUT;
    Button loadFromWebBUT;
    TextView showContentsTV;
    EditText numEntriesET;
    EditText numStoresET;
    Button runBUT;

    boolean isDataLoaded = false;
    String dataString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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

        addProductOptionsTV = (TextView) findViewById(R.id.add_product_method_options_title_TV);
        showContentsTV = (TextView) findViewById(R.id.from_file_show_contents_TV);

        initialButtonsRowLL = (LinearLayout) findViewById(R.id.initial_buttons_row_LL);
        createProductMethodLL = (LinearLayout) findViewById(R.id.create_product_method_LL);
        addProductMethodOptionsLL = (LinearLayout) findViewById(R.id.add_product_method_options_LL);
        showProductLL = (LinearLayout) findViewById(R.id.show_product_LL);
        fromFileLoadFileOptionsButtonRowLL = (LinearLayout) findViewById(R.id.from_file_load_file_options_LL);
        fromFileOptionsLL = (LinearLayout) findViewById(R.id.from_file_options_LL);
        internalSystemOptionsLL = (LinearLayout) findViewById(R.id.internal_randomizer_LL);
        enterManuallyLL = (LinearLayout) findViewById(R.id.initial_buttons_row_LL);
        fromSQLLL = (LinearLayout) findViewById(R.id.initial_buttons_row_LL);

        showFileSV = (ScrollView) findViewById(R.id.from_file_show_contents_SV);

        numEntriesET = (EditText) findViewById(R.id.randomizer_entries_ET);
        numStoresET = (EditText) findViewById(R.id.randomizer_stores_ET);

        importOptionsLLs = new LinearLayout[2];
        importOptionsLLs[0] = fromFileOptionsLL;
        importOptionsLLs[1] = internalSystemOptionsLL;
       /* //TODO
        importOptionsLLs[2]=enterManuallyLL;
        //TODO
        importOptionsLLs[3]=fromSQLLL;*/

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
                                        Toast.makeText(getApplicationContext(), "Loaded!", Toast.LENGTH_SHORT).show();
                                        showFileBUT.setEnabled(true);
                                        showContentsTV.setText(dataString);
                                        createDatabaseBUT.setVisibility(View.VISIBLE);


                                        dataString = (runRandomizer(4, 3)).toString();
                                        if (dataString != null) {
                                            isDataLoaded = true;
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
                                        Toast.makeText(getApplicationContext(), "Loaded!", Toast.LENGTH_SHORT).show();
                                        showFileBUT.setEnabled(true);
                                        showContentsTV.setText(dataString);
                                        createDatabaseBUT.setVisibility(View.VISIBLE);

                                        dataString = (runRandomizer(4, 3)).toString();
                                        if (dataString != null) {
                                            isDataLoaded = true;
                                            createDatabaseBUT.setVisibility(View.VISIBLE);
                                            Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                                        }
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
                                    Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        break;
                   /* case 2: // enter manually
                        break;
                    case 3: // saved sql db
                        break;*/
                }
            }
        });


        createProductBUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //real functionality
                if (showProductLL.getVisibility() == View.VISIBLE) {
                    showProductLL.setVisibility(View.GONE);
                }
                createProductMethodLL.setVisibility(View.VISIBLE);
            }
        });


        showProductBUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open show products activity
            }
        });

        createDatabaseBUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.writeDownloadsSTRFile(MOCK_DATA_FILENAME, dataString);
                showProductBUT.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_SHORT).show();
            }
        });


        /*Dictionary<String, Integer> storesDictionary;
        storesDictionary = new Hashtable<String, Integer>();
        storesDictionary.*/
    }

    private JSONArray runRandomizer(int numEntries, int numStores) {
        try {

            double priceMin = 15.00;
            double priceMax = 99.99;
            int stockMin = 0;
            int stockMax = 15;

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

                item.put(FIELD_PRODUCT_IMAGE, "Image Name " + i);

                JSONArray colorsArray = new JSONArray();
                colorsArray.put("Red");
                colorsArray.put("Blue");
                colorsArray.put("Green");
                colorsArray.put("Black");
                colorsArray.put("Yellow");
                colorsArray.put("Orange");
                if (i % 2 == 0) colorsArray.put("Gold");
                if (i % 2 == 0) colorsArray.put("Silver");
                if (i % 2 == 0) colorsArray.put("Chrome");
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

    @Override
    protected void onSaveInstanceState(Bundle outgoingState) {
        super.onSaveInstanceState(outgoingState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
    }
}
