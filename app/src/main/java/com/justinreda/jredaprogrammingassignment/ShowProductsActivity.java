package com.justinreda.jredaprogrammingassignment;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Justin on 10/20/2014.
 */
public class ShowProductsActivity extends Activity {

    String TAG = "ShowProductsActivity";

    private List<Product> mProductList;
    private ListAdapter mAdapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); //animation to make it look nice
        setContentView(R.layout.activity_show_products);

        ProductDatabaseHelper productDatabaseHelper = new ProductDatabaseHelper(getApplicationContext());
        SQLiteDatabase db = productDatabaseHelper.getWritableDatabase();
        mProductList = ProductDatabaseHelper.getWholeDatabase(db);

        mListView = (ListView) findViewById(R.id.show_products_LV);
        mAdapter = new ListAdapter();
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle baggage = new Bundle();
                Serializable prod;
                prod = mProductList.get(position);
                baggage.putSerializable("product", prod);
                Intent intent = new Intent(getApplicationContext(), ProductDetailsActivity.class);
                intent.putExtra("data", baggage);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ProductDatabaseHelper productDatabaseHelper = new ProductDatabaseHelper(getApplicationContext());
        SQLiteDatabase db = productDatabaseHelper.getWritableDatabase();
        mProductList = ProductDatabaseHelper.getWholeDatabase(db);

        mListView = (ListView) findViewById(R.id.show_products_LV);
        mAdapter = new ListAdapter();
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle baggage = new Bundle();
                Serializable prod;
                prod = mProductList.get(position);
                baggage.putSerializable("product", prod);
                Intent intent = new Intent(getApplicationContext(), ProductDetailsActivity.class);
                intent.putExtra("data", baggage);
                startActivity(intent);
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

    class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mProductList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.item_list, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            Product item = mProductList.get(position);
            HashMap<String, Integer> drawableHashMap;
            drawableHashMap = FirstActivity.getImageNamesRes(getApplicationContext());

            String imageName = item.getProductImage();
            holder.iv_icon.setImageResource(drawableHashMap.get(imageName));
            holder.tv_name.setText(item.getName());
//            holder.notify();
            return convertView;
        }

        class ViewHolder {
            ImageView iv_icon;
            TextView tv_name;

            public ViewHolder(View view) {
                iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                view.setTag(this);
            }
        }
    }
}
