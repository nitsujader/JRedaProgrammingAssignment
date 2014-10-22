package com.justinreda.jredaprogrammingassignment;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.LinkedList;

/**
 * Created by Justin on 10/19/2014.
 *
 * Product class for each item in the database
 */
public class Product implements Serializable {

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

    /**
     * Product constructor, build product object based on a JSONObject
     *
     * @param product JSONObject of the product that should be created.
     */
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
            for (int i = 0; i < colorz.length(); i++) {
                this.colors[i] = colorz.getString(i);
            }
            stores = new Hashtable<String, Integer>();
            JSONArray storez = product.getJSONArray(FIELD_STORES);
            for (int j = 0; j < storez.length(); j++) {
                JSONObject storeObject = storez.getJSONObject(j);
                stores.put(storeObject.getString(FIELD_STORES_NUMBER), storeObject.getInt(FIELD_STORES_STOCK));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Product constructor, form a product object given an input pointer from a SQLLite DB
     *
     * @param cursor cursor containing the Product object
     */
    public Product(Cursor cursor) {

        try {
            this.id = cursor.getInt(0);
            this.name = cursor.getString(cursor.getColumnIndexOrThrow(FIELD_NAME));
            this.description = cursor.getString(cursor.getColumnIndex(FIELD_DESCRIPTION));
            this.regularPrice = cursor.getDouble(cursor.getColumnIndex(FIELD_REGULAR_PRICE));
            this.salePrice = cursor.getDouble(cursor.getColumnIndex(FIELD_SALE_PRICE));
            this.productImage = cursor.getString(cursor.getColumnIndex(FIELD_PRODUCT_IMAGE));
            JSONArray colorz = new JSONArray(cursor.getString(cursor.getColumnIndex(FIELD_COLORS)));
            this.colors = new String[colorz.length()];
            for (int i = 0; i < colorz.length(); i++) {
                this.colors[i] = colorz.getString(i);
            }
            this.stores = new Hashtable<String, Integer>();
            JSONArray storez = new JSONArray(cursor.getString(cursor.getColumnIndex(FIELD_STORES)));
            for (int j = 0; j < storez.length(); j++) {
                JSONObject storeObject = storez.getJSONObject(j);
                this.stores.put(storeObject.getString(FIELD_STORES_NUMBER), storeObject.getInt(FIELD_STORES_STOCK));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
/*
    public void removeStore(String storeName) {
        if (this.stores.containsKey(storeName)) {
            this.stores.remove(storeName);
        }
    }

    public void updateStore(String storeName, int newValue) {
        if (this.stores.containsKey(storeName)) {
            this.stores.put(storeName, newValue);
        }
    }

    public void addStore(String storeName, int newValue) {
        this.stores.put(storeName, newValue);
    }

    public void addColor(String colorName) {
        String[] newColors = new String[this.colors.length + 1];
        for (int i = 0; i < this.colors.length; i++) {
            newColors[i] = colors[i];
        }
        newColors[this.colors.length] = colorName;
        setColors(newColors);
    }*/

    /**
     * Remove a color from the products list of available colors
     *
     * @param colorName String name of color to be removed
     * @return Updated string array of the product colors
     */
    public String[] removeColor(String colorName) {
        String[] currentColors = this.getColors();
        LinkedList<String> newArrayLL = new LinkedList<String>();
        for (String currColor : currentColors) {
            if (Utilities.strNotNullNotEmpty(colorName) && Utilities.strNotNullNotEmpty(currColor)) {
                if (!colorName.matches(currColor)) {
                    newArrayLL.add(currColor);
                }
            }
        }

        int sizeOfArray = newArrayLL.size();
        String[] newColorArray = new String[sizeOfArray];
        for (int i = 0; i < sizeOfArray; i++) {
            if (newArrayLL.getFirst() != null) {
                newColorArray[i] = newArrayLL.removeFirst();
            }
        }

        setColors(newColorArray);
        return newColorArray;
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

    /**
     * Get the store list and stock in JSON form
     *
     * @return the JSONArray form of the stores for the selected product
     */
    public JSONArray getStoresJSONArray() {

        try {
            JSONArray arrayOut = new JSONArray();
            for (String s : stores.keySet()) {
                JSONObject tempObject = new JSONObject();
                tempObject.put(FIELD_STORES_NUMBER, s);
                tempObject.put(FIELD_STORES_STOCK, stores.get(s));
                arrayOut.put(tempObject);
            }

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
