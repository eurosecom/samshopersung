package com.eusecom.samshopersung;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.eusecom.samshopersung.database.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class RoomDemoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roomdemo_activity);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // run the sentence in a new thread
        new Thread(new Runnable() {
            @Override
            public void run() {

                List<Product> products = SamshopperApp.get().getDB().productDao().getAll();
                boolean force = SamshopperApp.get().isForceUpdate();
                if (force || products.isEmpty()) {
                    retrieveProducts();
                    Log.d("RoomDemo ", "retrieveProducts ");
                } else {
                    Log.d("RoomDemo ", "populateProducts ");
                    populateProducts(products);
                }
            }
        }).start();
    }

    private void retrieveProducts() {
        List<Product> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Product product = new Product();
            product.setName(getString(R.string.name_format, String.valueOf(i)));
            product.setImageUrl("https://picsum.photos/500/500?image=" + i);
            product.setPrice(i == 0 ? 50 : i * 100);
            list.add(product);
        }

        // insert product list into database
        SamshopperApp.get().getDB().productDao().insertAll(list);

        // disable flag for force update
        SamshopperApp.get().setForceUpdate(false);

        populateProducts(list);
    }

    private void populateProducts(final List<Product> products) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(new RoomDemoAdapter(products));
            }
        });
    }

}
