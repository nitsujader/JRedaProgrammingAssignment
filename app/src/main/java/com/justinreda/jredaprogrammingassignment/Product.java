package com.justinreda.jredaprogrammingassignment;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

/**
 * Created by Justin on 10/19/2014.
 */
public class Product {

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

    int id;
    String name;
    String description;
    double regularPrice;
    double salePrice;
    String productImage;
    String[] colors;
    Hashtable<String, Integer> stores;

    public Product(JSONObject product) {
        try {
            this.id = product.getInt(FIELD_ID);
            this.name = product.getString(FIELD_NAME);
            this.description = product.getString(FIELD_DESCRIPTION);
            this.regularPrice = product.getDouble(FIELD_REGULAR_PRICE);
            this.salePrice = product.getDouble(FIELD_SALE_PRICE);
            this.productImage = product.getString(FIELD_PRODUCT_IMAGE);
            JSONArray colorz = product.getJSONArray(FIELD_COLORS);
            this.colors = new String[colorz.length()];
            for(int i=0; i<colorz.length(); i++){
                this.colors[i]=colorz.getString(i);
            }
            stores = new Hashtable<String, Integer>();
            JSONArray storez = product.getJSONArray(FIELD_STORES);
            for(int j=0; j<storez.length();j++){
                JSONObject storeObject = storez.getJSONObject(j);
                stores.put(storeObject.getString(FIELD_STORES_NUMBER),storeObject.getInt(FIELD_STORES_STOCK));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Product(String name, String description, double regularPrice, double salePrice, String productImage, int numColors, int numStores) {
        this.name = name;
        this.description = description;
        this.regularPrice = regularPrice;
        this.salePrice = salePrice;
        this.productImage = productImage;
    }

    public Product(Cursor cursor) {
        /*for (String name: cursor.getColumnNames()){
            Log.wtf("PRODUCT", "id CN "+cursor.moveToFirst());
            Log.wtf("PRODUCT", "name index "+cursor.getColumnIndexOrThrow(FIELD_NAME));
        }*/
        try {
            this.id = cursor.getInt(0);
            this.name = cursor.getString(cursor.getColumnIndexOrThrow(FIELD_NAME));
            this.description = cursor.getString(cursor.getColumnIndex(FIELD_DESCRIPTION));
            this.regularPrice = cursor.getDouble(cursor.getColumnIndex(FIELD_REGULAR_PRICE));
            this.salePrice = cursor.getDouble(cursor.getColumnIndex(FIELD_SALE_PRICE));
            this.productImage = cursor.getString(cursor.getColumnIndex(FIELD_PRODUCT_IMAGE));
            JSONArray colorz = new JSONArray(cursor.getString(cursor.getColumnIndex(FIELD_COLORS)));
            this.colors = new String[colorz.length()];
            for(int i=0; i<colorz.length(); i++){
                this.colors[i]=colorz.getString(i);
            }
            this.stores = new Hashtable<String, Integer>();
            JSONArray storez = new JSONArray(cursor.getString(cursor.getColumnIndex(FIELD_STORES)));
            //Log.wtf("PRODUCT","stores "+storez.toString());
            for(int j=0; j<storez.length();j++){
                JSONObject storeObject = storez.getJSONObject(j);
                this.stores.put(FIELD_STORES_NUMBER,storeObject.getInt(FIELD_STORES_STOCK));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(double regularPrice) {
        this.regularPrice = regularPrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String[] getColors() {
        return colors;
    }

    public void setColors(String[] colors) {
        this.colors = colors;
    }

    public Hashtable<String, Integer> getStores() {
        return stores;
    }

    public void setStores(Hashtable<String, Integer> stores) {
        this.stores = stores;
    }

    public JSONArray getStoresJSONArray() {

        try {
            JSONArray arrayOut = new JSONArray();
            for (String s : stores.keySet()) {
                JSONObject tempObject = new JSONObject();
                String storeName = s;
                tempObject.put(FIELD_STORES_NUMBER, storeName);
                tempObject.put(FIELD_STORES_STOCK, stores.get(storeName));
                arrayOut.put(tempObject);
            }

            Log.wtf("PRODUCT", "storez " + arrayOut.toString());
            return arrayOut;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", regularPrice=" + regularPrice +
                ", salePrice=" + salePrice +
                ", productImage='" + productImage + '\'' +
                '}';
    }
}
