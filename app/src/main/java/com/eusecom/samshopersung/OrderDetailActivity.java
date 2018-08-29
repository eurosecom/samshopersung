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
import java.util.ArrayList;
import java.util.Calendar;
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
    protected ArrayAdapter<Account> mAdapter;

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

        inputIco.setText(mSharedPreferences.getString("mfir", ""));
        inputNai.setText(mSharedPreferences.getString("mfirnaz", ""));

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

        ArrayList<Account> pohybys = new ArrayList<>();
        pohybys.add(new Account("Payment 1", "1", "", "", "", "","true"));
        pohybys.add(new Account("Payment 2", "2", "", "", "", "","true"));
        pohybys.add(new Account("Payment 3", "3", "", "", "", "","true"));
        pohybys.add(new Account("Payment 4", "4", "", "", "", "","true"));
        pohybys.add(new Account("Payment 5", "5", "", "", "", "","true"));
        setPaySpinner(pohybys);


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
                        //Toast.makeText(NewIdcActivity.this, "Clicked save", Toast.LENGTH_SHORT).show();

                        List<RealmInvoice> realminvoices = new ArrayList<>();
                        RealmInvoice realminvoice = new RealmInvoice();
                        realminvoice.setDrh("99");
                        realminvoice.setUce("");
                        realminvoice.setDok(inputIco.getText().toString());
                        realminvoice.setIco(inputIco.getText().toString());
                        realminvoice.setNai(inputNai.getText().toString());
                        realminvoice.setZk0(inputEid.getText().toString());
                        realminvoice.setFak("0");
                        realminvoice.setPoh("0");
                        realminvoice.setSaved("false");
                        realminvoices.add(realminvoice);

                        Log.d("NewIdc ", realminvoice.getDok());

                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putString("mfir", inputIco.getText().toString()).apply();
                        editor.putString("mfirnaz", inputNai.getText().toString()).apply();
                        editor.commit();

                        //mViewModel.emitSaveOrderDetailToSql(realminvoices);

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

    private void setDetailOrder(@NonNull final InvoiceList detail) {

        Log.d("order detail", detail.getInvoice().get(0).getIco());
        Invoice detx = detail.getInvoice().get(0);
        if( detail.getInvoice().size() > 0 ){

            inputEid.setText(detx.getZk1());
            datex.setText(detx.getZk0());
            //spinnPay.setSelection(2);
            spinnPay.setSelection(getIndex(spinnPay, "4 Payment 4"));




        }
        hideProgressBar();
    }

    //private method of your class
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }


    private DatePickerDialog getDatePicker(String datumx) {

        String datumx2=datumx;
        if(datumx2.equals("")){ datumx2 = "01.08.2018";}
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

            mAdapter = new ArrayAdapter<Account>(this, android.R.layout.simple_spinner_item, pohybys);
            mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnPay.setAdapter(mAdapter);
            //if(_inputPoh.getText().toString().equals("")) { _inputPoh.setText(pohyby.get(0).getAccnumber()); }
            //if(_inputPoh.getText().toString().equals("0")) { _inputPoh.setText(pohyby.get(0).getAccnumber()); }
            spinnPay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view,
                                           int position, long id) {
                    // Here you get the current item (a User object) that is selected by its position
                    Account account = mAdapter.getItem(position);
                    //_inputPoh.setText(account.getAccnumber());
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapter) {  }
            });

        }

    }


}