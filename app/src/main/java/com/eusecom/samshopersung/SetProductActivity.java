package com.eusecom.samshopersung;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.eusecom.samshopersung.models.IShopperModelsFactory;
import com.eusecom.samshopersung.models.ShopperModelsFactory;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.content.ContentValues.TAG;
import static rx.Observable.empty;

/**
 * Created by Shaon on 12/3/2016.
 * by http://shaoniiuc.com/android/image-upload-retrofit-library/
 */


public class SetProductActivity extends BaseActivity {

    @Inject
    ShopperIMvvmViewModel mViewModel;

    @Inject
    SetImageAdapter mAdapter;

    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    IShopperModelsFactory mModelsFactory;

    @Inject
    Picasso mPicasso;

    @Inject
    public ImageUrl mImageUrl;

    Button btnUpload;
    LinearLayout itemlay;
    EditText itemx, namex, dphx, merx, cedx, catx;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private CompositeSubscription mSubscription;
    int whatactivity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.setproduct_activity);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        whatactivity = extras.getInt("whatactivity");

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        btnUpload = (Button) findViewById(R.id.upload);
        itemlay = (LinearLayout) findViewById(R.id.itemlay);
        itemx = (EditText) findViewById(R.id.itemx);
        namex = (EditText) findViewById(R.id.namex);
        dphx = (EditText) findViewById(R.id.dphx);
        catx = (EditText) findViewById(R.id.catx);
        merx = (EditText) findViewById(R.id.merx);
        cedx = (EditText) findViewById(R.id.cedx);

        String edidok = mSharedPreferences.getString("edidok", "");
        if (!edidok.equals("0") && !edidok.equals("") && !edidok.equals("FINDITEM")) {

            itemx.setEnabled(false);
            itemx.setFocusable(false);
            itemx.setFocusableInTouchMode(false);
        }else{

            itemx.setText("0");
            itemx.setEnabled(false);
            itemx.setFocusable(false);
            itemx.setFocusableInTouchMode(false);

        }

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgressBar();
                ProductKt prod = mModelsFactory.getProductKt();
                prod.setCis(itemx.getText().toString());
                prod.setNat(namex.getText().toString());
                prod.setDph(dphx.getText().toString());
                prod.setCat(catx.getText().toString());
                prod.setMer(merx.getText().toString());
                prod.setCed(cedx.getText().toString());
                prod.setPrm2(mSharedPreferences.getString("edidok", ""));
                mViewModel.emitSaveItemToServer(prod);
            }

        });


        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler = (RecyclerView) findViewById(R.id.recycler_view);
        mRecycler.setLayoutManager(mManager);
        mRecycler.setAdapter(mAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unBind();
    }

    @Override
    public void onResume() {
        super.onResume();
        bind();
    }

    @Override
    public void onPause() {
        super.onPause();
        unBind();
    }

    private void unBind() {

        mViewModel.clearSaveEanToServer();
        mViewModel.clearSaveItemToServer();
        mSubscription.clear();
    }

    private void bind() {

        mSubscription = new CompositeSubscription();

        mSubscription.add(mViewModel.getMyQueryProductsFromSqlServer()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    Log.e(TAG, "Error SetImageActivity " + throwable.getMessage());
                    hideProgressBar();
                    Toast.makeText(this, "Server not connected", Toast.LENGTH_SHORT).show();
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::setServerProducts));

        mSubscription.add(mViewModel.getObservableSaveEanToServer()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    Log.e(TAG, "Error SetImageActivity " + throwable.getMessage());
                    hideProgressBar();
                    Toast.makeText(this, "Server not connected", Toast.LENGTH_SHORT).show();
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::setSavedEan));

        mSubscription.add(mViewModel.getObservableSaveItemToServer()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    Log.e(TAG, "Error SetImageActivity " + throwable.getMessage());
                    hideProgressBar();
                    Toast.makeText(this, "Server not connected", Toast.LENGTH_SHORT).show();
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::setSavedItem));

        String edidok = mSharedPreferences.getString("edidok", "");
        Log.d("SetImageActivityLog ", edidok);
        if (!edidok.equals("0") && !edidok.equals("") && !edidok.equals("FINDITEM")) {
            showProgressBar();
            emitMyQueryProductsFromSqlServer("GetDetail" + mSharedPreferences.getString("edidok", ""));
        }

    }

    private void setSavedItem(List<ProductKt> products) {

        //mAdapter.setDataToAdapter(products);
        hideProgressBar();
        Toast.makeText(getApplicationContext(), getString(R.string.itemsaved), Toast.LENGTH_SHORT).show();
        String edidok = mSharedPreferences.getString("edidok", "");
        Log.d("SetImageActivityLog ", "saved " + products.get(0).getNat());
        if (!edidok.equals("0") && !edidok.equals("") && !edidok.equals("FINDITEM")) {
            showProgressBar();
            emitMyQueryProductsFromSqlServer("GetDetail" + mSharedPreferences.getString("edidok", ""));
        }
    }

    private void setSavedEan(List<ProductKt> products) {

        //mAdapter.setDataToAdapter(products);
        hideProgressBar();
        Toast.makeText(getApplicationContext(), getString(R.string.eansaved), Toast.LENGTH_SHORT).show();
        String edidok = mSharedPreferences.getString("edidok", "");
        Log.d("SetImageActivityLog ", "saved " + products.get(0).getNat());
        if (!edidok.equals("0") && !edidok.equals("") && !edidok.equals("FINDITEM")) {
            showProgressBar();
            emitMyQueryProductsFromSqlServer("GetDetail" + mSharedPreferences.getString("edidok", ""));
        }
    }

    private void setServerProducts(List<ProductKt> products) {

        mAdapter.setDataToAdapter(products);
        hideProgressBar();
        itemx.setText(products.get(0).getCis());
        namex.setText(products.get(0).getNat());
        merx.setText(products.get(0).getMer());
        dphx.setText(products.get(0).getDph());
        cedx.setText(products.get(0).getCed());
        catx.setText(products.get(0).getCat());

        //Log.d("SetImageActivityLog ", products.get(0).getNat());
    }

    private void emitMyQueryProductsFromSqlServer(String query) {
        showProgressBar();
        mViewModel.emitMyQueryProductsFromSqlServer(query);
    }




}