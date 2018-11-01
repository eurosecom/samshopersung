package com.eusecom.samshopersung;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.eusecom.samshopersung.models.Account;
import com.eusecom.samshopersung.models.IShopperModelsFactory;
import com.eusecom.samshopersung.models.InvoiceList;
import com.eusecom.samshopersung.realm.RealmInvoice;
import com.jakewharton.rxbinding.view.RxView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import dagger.android.AndroidInjection;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import static android.content.ContentValues.TAG;
import static rx.Observable.empty;

public class OrderDetailActivity extends BaseActivity {


    EditText inputIco, inputEid, inputNai, datex;
    Spinner spinnState, spinnPay, spinnTrans;
    String order="0";
    Button btnSave, btnIco, datebutton;
    protected ArrayAdapter<Account> mPayAdapter, mTransAdapter, mOstateAdapter;
    private int xpay, xtrans, xstate;

    @NonNull
    private CompositeSubscription mSubscription;
    private Subscription subscriptionSave;

    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    ShopperIMvvmViewModel mViewModel;

    @Inject
    IShopperModelsFactory mModelsFactory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderdetail_activity);

        AndroidInjection.inject(this);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        order = extras.getString("order");

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        inputIco = (EditText) findViewById(R.id.inputIco);
        inputNai = (EditText) findViewById(R.id.inputNai);
        inputEid = (EditText) findViewById(R.id.inputEid);
        datex = (EditText) findViewById(R.id.datex);
        spinnPay = (Spinner) findViewById(R.id.spinnPay);
        spinnTrans = (Spinner) findViewById(R.id.spinnTrans);
        spinnState = (Spinner) findViewById(R.id.spinnState);

        //inputIco.setText(mSharedPreferences.getString("mfir", ""));
        //inputNai.setText(mSharedPreferences.getString("mfirnaz", ""));

        // save button
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setText(String.format(getResources().getString(R.string.ordersave), order));


        //ico button
        btnIco = (Button) findViewById(R.id.btnIco);
        btnIco.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                showProgressBar();
                mViewModel.emitMyObservableIdModelCompany(inputIco.getText().toString());

            }
        });


        datebutton = (Button) findViewById(R.id.datebutton);
        datebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDatePicker(datex.getText().toString()).show();

            }
        });

        ArrayList<Account> pays = new ArrayList<>();
        String[] payArray = getResources().getStringArray(R.array.payment);
        for(int ix = 0;ix<payArray.length; ix++)
        {

            pays.add(new Account( payArray[ix], ix + "", "", "", "", "","true"));

        }

        setPaySpinner(pays);

        ArrayList<Account> transes = new ArrayList<>();
        String[] transArray = getResources().getStringArray(R.array.transport);
        for(int ix = 0;ix<transArray.length; ix++)
        {

            transes.add(new Account( transArray[ix], ix + "", "", "", "", "","true"));

        }

        setTransSpinner(transes);

        ArrayList<Account> states = new ArrayList<>();
        String[] stateArray = getResources().getStringArray(R.array.orderstate);
        for(int ix = 0;ix<stateArray.length; ix++)
        {

            states.add(new Account( stateArray[ix], ix + "", "", "", "", "","true"));

        }

        setOstateSpinner(states);
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

        mViewModel.clearObservableIdModelCompany();
        mViewModel.clearObservableSaveDetailOrder();
        mSubscription.clear();
        subscriptionSave.unsubscribe();

    }

    private void bind() {

        mSubscription = new CompositeSubscription();

        //get Nai by Ico from MySql after click on btnIco
        mSubscription.add(mViewModel.getMyObservableIdModelCompany()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> { Log.e(TAG, "Error OrderDetailActivity " + throwable.getMessage());
                    hideProgressBar();
                    Toast.makeText(this, "Server not connected", Toast.LENGTH_SHORT).show();
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::setClickedIco));

        //get Order Detail from MySql emited in onCreate
        mSubscription.add(mViewModel.getObservableDetailOrder()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> { Log.e(TAG, "Error OrderDetailActivity " + throwable.getMessage());
                    hideProgressBar();
                    Toast.makeText(this, "Server not connected", Toast.LENGTH_SHORT).show();
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::setDetailOrder));

        //saved Order Detail from MySql emited in onCreate
        mSubscription.add(mViewModel.getObservableSaveDetailOrder()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> { Log.e(TAG, "Error OrderDetailActivity " + throwable.getMessage());
                    hideProgressBar();
                    Toast.makeText(this, "Server not connected", Toast.LENGTH_SHORT).show();
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::setSavedDetailOrder));

        //save orders detail to MySql
        subscriptionSave = RxView.clicks(btnSave)
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    public int mCount;

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Object o) {

                        //Log.d("NewInvDoc", "Clicked save ");

                        Invoice invoice = mModelsFactory.getInvoice();
                        invoice.setDrh("9");
                        invoice.setDok(order);
                        invoice.setIco(inputIco.getText().toString());
                        invoice.setNai(inputNai.getText().toString());
                        invoice.setZk0(datex.getText().toString());
                        invoice.setZk1(inputEid.getText().toString());
                        invoice.setZk2(String.valueOf(xpay));
                        invoice.setDn1(String.valueOf(xstate));
                        invoice.setDn2(String.valueOf(xtrans));

                        mViewModel.emitSaveDetailOrder(invoice);

                    }
                });

        Invoice invx = mModelsFactory.getInvoice();
        invx.setDrh("8");
        invx.setDok(order);

        mViewModel.emitDetailOrder(invx);

    }

    private void setClickedIco(@NonNull final List<IdCompanyKt> idc) {

        if( idc.size() > 0 ){
           inputIco.setText(idc.get(0).getIco());
           inputNai.setText(idc.get(0).getNai());

        }
        hideProgressBar();
    }

    private void setSavedDetailOrder(@NonNull final InvoiceList detail) {


        Toast.makeText(this, String.format(getResources().getString(R.string.savedorderdetails), order), Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setDetailOrder(@NonNull final InvoiceList detail) {

        Log.d("order detail", detail.getInvoice().get(0).getIco());
        Invoice detx = detail.getInvoice().get(0);
        if( detail.getInvoice().size() > 0 ){

            inputIco.setText(detx.getIco());
            inputNai.setText(detx.getNai());
            inputEid.setText(detx.getZk1());
            datex.setText(detx.getZk0());
            spinnPay.setSelection(Integer.valueOf(detx.getZk2()));
            spinnTrans.setSelection(Integer.valueOf(detx.getDn2()));
            spinnState.setSelection(Integer.valueOf(detx.getDn1()));
            xpay = Integer.valueOf(detx.getZk2());
            xstate = Integer.valueOf(detx.getDn1());
            xtrans = Integer.valueOf(detx.getDn2());




        }
        hideProgressBar();
    }


    private DatePickerDialog getDatePicker(String datumx) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String currentDateandTime = sdf.format(new Date());

        String datumx2=datumx;
        if(datumx2.trim().equals("00.00.0000")){ datumx2 = currentDateandTime;}
        if(datumx2.trim().equals("")){ datumx2 = currentDateandTime;}
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        //dd = 38;

        //String datumx = "12.12.2017";

        String delims = "[.]+";
        String[] datumxxx = datumx2.split(delims);

        String ddx = datumxxx[0];
        String mmx = datumxxx[1];
        String yyx = datumxxx[2];

        int ddi = Integer.parseInt(ddx);
        int mmi = Integer.parseInt(mmx);
        int yyi = Integer.parseInt(yyx);
        dd=ddi; mm=mmi-1; yy=yyi;

        DatePickerDialog dpd = new DatePickerDialog(this, null, yy, mm, dd);
        //dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Button Neg Text", dpd);
        dpd.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.datedialogpos), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {

                    int dayx = dpd.getDatePicker().getDayOfMonth();
                    int monthx = dpd.getDatePicker().getMonth();
                    int yearx = dpd.getDatePicker().getYear();
                    int monthy = monthx + 1;

                    datex.setText(dayx + "." + monthy + "." + yearx);
                }
            }

        });


        return dpd;
    }

    private void setPaySpinner(@NonNull final List<Account> pohyby) {

        if (pohyby.size() > 0) {

            ArrayList<Account> pohybys = new ArrayList<>();

            for (int i = 0; i < pohyby.size(); i++) {
                pohybys.add(new Account(pohyby.get(i).getAccname(), pohyby.get(i).getAccnumber(), "", "", "", "","true"));
            }

            mPayAdapter = new ArrayAdapter<Account>(this, android.R.layout.simple_spinner_item, pohybys);
            mPayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnPay.setAdapter(mPayAdapter);
            spinnPay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view,
                                           int position, long id) {
                    // Here you get the current item (a User object) that is selected by its position
                    Account account = mPayAdapter.getItem(position);
                    xpay = Integer.valueOf(account.getAccnumber());
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapter) {  }
            });

        }

    }

    private void setTransSpinner(@NonNull final List<Account> pohyby) {

        if (pohyby.size() > 0) {

            ArrayList<Account> pohybys = new ArrayList<>();

            for (int i = 0; i < pohyby.size(); i++) {
                pohybys.add(new Account(pohyby.get(i).getAccname(), pohyby.get(i).getAccnumber(), "", "", "", "","true"));
            }

            mTransAdapter = new ArrayAdapter<Account>(this, android.R.layout.simple_spinner_item, pohybys);
            mTransAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnTrans.setAdapter(mTransAdapter);
            spinnTrans.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view,
                                           int position, long id) {
                    // Here you get the current item (a User object) that is selected by its position
                    Account account = mTransAdapter.getItem(position);
                    xtrans = Integer.valueOf(account.getAccnumber());
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapter) {  }
            });

        }

    }

    private void setOstateSpinner(@NonNull final List<Account> pohyby) {

        if (pohyby.size() > 0) {

            ArrayList<Account> pohybys = new ArrayList<>();

            for (int i = 0; i < pohyby.size(); i++) {
                pohybys.add(new Account(pohyby.get(i).getAccname(), pohyby.get(i).getAccnumber(), "", "", "", "","true"));
            }

            mOstateAdapter = new ArrayAdapter<Account>(this, android.R.layout.simple_spinner_item, pohybys);
            mOstateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnState.setAdapter(mOstateAdapter);
            spinnState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view,
                                           int position, long id) {
                    // Here you get the current item (a User object) that is selected by its position
                    Account account = mOstateAdapter.getItem(position);
                    xstate = Integer.valueOf(account.getAccnumber());
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapter) {  }
            });

        }

    }


}