package com.eusecom.samshopersung;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.eusecom.samshopersung.models.IShopperModelsFactory;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import dagger.android.AndroidInjection;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import static android.content.ContentValues.TAG;
import static rx.Observable.empty;

/**
 * Created by eurosecom.
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
    EditText itemx, namex, dphx, merx, cedx, ced1x, catx, descx;
    Spinner catSpinner, dphSpinner;
    private ArrayAdapter<CategoryKt> mSpinAdapter, mVatSpinAdapter;
    ArrayList<CategoryKt> categories;
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
        ced1x = (EditText) findViewById(R.id.ced1x);
        descx = (EditText) findViewById(R.id.descx);
        catSpinner = (Spinner) findViewById(R.id.catSpinner);
        dphSpinner = (Spinner) findViewById(R.id.dphSpinner);

        categories = new ArrayList<>();

        String edidok = mSharedPreferences.getString("edidok", "");
        if (!edidok.equals("0") && !edidok.equals("") && !edidok.equals("FINDITEM")) {

            itemx.setEnabled(false);
            itemx.setFocusable(false);
            itemx.setFocusableInTouchMode(false);
        } else {

            if(itemx.getText().toString().equals("")){
                itemx.setText("0");
            }
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
                prod.setCed1(ced1x.getText().toString());
                prod.setDesc(descx.getText().toString());
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

        ArrayList<CategoryKt> vats = new ArrayList<>();
        vats.add(new CategoryKt(mSharedPreferences.getString("firdph2", ""), "", ""));
        vats.add(new CategoryKt(mSharedPreferences.getString("firdph1", ""), "", ""));
        vats.add(new CategoryKt("0", "", ""));
        mVatSpinAdapter = new ArrayAdapter<CategoryKt>(this, android.R.layout.simple_spinner_item, vats);
        mVatSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dphSpinner.setAdapter(mVatSpinAdapter);
        if (dphx.getText().toString().equals("")) {
            dphx.setText(mSharedPreferences.getString("firdph2", ""));
        }
        if (dphx.getText().toString().equals("0")) {
            dphx.setText(mSharedPreferences.getString("firdph2", ""));
        }
        dphSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                CategoryKt dph = mVatSpinAdapter.getItem(position);
                dphx.setText(dph.getCat());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });


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

        showProgressBar();
        mSubscription.add(mViewModel.getMyCatsFromSqlServer("1")
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    Log.e(TAG, "Error SetProductActivity " + throwable.getMessage());
                    hideProgressBar();
                    Toast.makeText(this, "Server not connected", Toast.LENGTH_SHORT).show();
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::setServerCategories));

        mSubscription.add(mViewModel.getMyQueryProductsFromSqlServer()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    Log.e(TAG, "Error SetProductActivity " + throwable.getMessage());
                    hideProgressBar();
                    Toast.makeText(this, "Server not connected", Toast.LENGTH_SHORT).show();
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::setServerProducts));

        mSubscription.add(mViewModel.getObservableSaveEanToServer()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    Log.e(TAG, "Error SetProductActivity " + throwable.getMessage());
                    hideProgressBar();
                    Toast.makeText(this, "Server not connected", Toast.LENGTH_SHORT).show();
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::setSavedEan));

        mSubscription.add(mViewModel.getObservableSaveItemToServer()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    Log.e(TAG, "Error SetProductActivity " + throwable.getMessage());
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

    private void setServerCategories(List<CategoryKt> pohyby) {

        if (pohyby.size() > 0) {

            for (int i = 0; i < pohyby.size(); i++) {
                categories.add(new CategoryKt(pohyby.get(i).getCat(), pohyby.get(i).getNac(), ""));
            }

            mSpinAdapter = new ArrayAdapter<CategoryKt>(this, android.R.layout.simple_spinner_item, categories);
            mSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            catSpinner.setAdapter(mSpinAdapter);
            if (catx.getText().toString().equals("")) {
                catx.setText(pohyby.get(0).getCat());
            }
            if (catx.getText().toString().equals("0")) {
                catx.setText(pohyby.get(0).getCat());
            }
            catSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view,
                                           int position, long id) {
                    // Here you get the current item (a User object) that is selected by its position
                    CategoryKt cat = mSpinAdapter.getItem(position);
                    catx.setText(cat.getCat());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapter) {
                }
            });

        }

        hideProgressBar();
    }

    private CategoryKt searchModel(String query, List<CategoryKt> cats) {
        query = query.toLowerCase();
        CategoryKt resultAs = null;
        //Log.d("SetProductActivityLog", cats.get(0).getCat().toLowerCase() + " - 1.query " + query);
        for (int i = 0; i < cats.size(); i++) {
            //Log.d("SetProductActivityLog", cats.get(i).getCat().toLowerCase() + " - 2.query " + query);
            if (cats.get(i).getCat().toLowerCase().contains(query)) {
                //Log.d("SetProductActivityLog", cats.get(i).getCat().toLowerCase() + " - 3.query " + query);
                resultAs = cats.get(i);
            }
        }
        return resultAs;
    }


    private void setSavedItem(List<ProductKt> products) {

        mSharedPreferences.edit().putString("edidok", products.get(0).getCis()).commit();

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
        ced1x.setText(products.get(0).getCed1());
        descx.setText(products.get(0).getDesc());
        catx.setText(products.get(0).getCat());

        CategoryKt vatcompare = new CategoryKt(products.get(0).getDph(), "", "");
        int spinnerPosition = mVatSpinAdapter.getPosition(vatcompare);
        dphSpinner.setSelection(spinnerPosition);

        //andrejko to solve categories later initialize as products
            try {
            int spinnerPosition2 = mSpinAdapter.getPosition(searchModel(catx.getText().toString(), categories));
            catSpinner.setSelection(spinnerPosition2);
            } catch (NullPointerException e) {

            }

        //Log.d("SetImageActivityLog ", products.get(0).getNat());
    }

    private void emitMyQueryProductsFromSqlServer(String query) {
        showProgressBar();
        mViewModel.emitMyQueryProductsFromSqlServer(query);
    }


}