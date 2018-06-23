package com.eusecom.samfantozzi;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eusecom.samfantozzi.realm.RealmInvoice;
import com.jakewharton.rxbinding.view.RxView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import static android.content.ContentValues.TAG;
import static rx.Observable.empty;

/**
 * Edit info about commpany and create new commpany
 * Called from IdcListFragment.java
 */

public class NewIdcActivity extends BaseListActivity {


    EditText inputIco;
    EditText inputDic;
    EditText inputIcd;
    EditText inputNai;
    EditText inputUli;
    EditText inputMes;
    EditText inputPsc;
    EditText inputTel;
    EditText inputMail;
    EditText inputIb1;
    EditText inputSw1;
    Button _btnSave;
    Button _btnTel;

    private static final String TAG_NEWX = "newx";
    private static final String TAG_ICOX = "icox";

    String newx;
    String icox;

    @NonNull
    private CompositeSubscription mSubscription;
    private Subscription subscriptionSave;

    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    DgAllEmpsAbsMvvmViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newidc_activity);

        ((SamfantozziApp) getApplication()).dgaeacomponent().inject(this);

        // getting product details from intent
        Intent i = getIntent();

        Bundle extras = i.getExtras();
        icox = extras.getString(TAG_ICOX);
        newx = extras.getString(TAG_NEWX);

        if(newx.equals("0")){
            getSupportActionBar().setTitle(getString(R.string.editico) + " " + icox);
        }else{
            getSupportActionBar().setTitle(getString(R.string.newico));
        }

        inputIco = (EditText) findViewById(R.id.inputIco);
        inputDic = (EditText) findViewById(R.id.inputDic);
        inputIcd = (EditText) findViewById(R.id.inputIcd);
        inputNai = (EditText) findViewById(R.id.inputNai);
        inputUli = (EditText) findViewById(R.id.inputUli);
        inputMes = (EditText) findViewById(R.id.inputMes);
        inputPsc = (EditText) findViewById(R.id.inputPsc);
        inputTel = (EditText) findViewById(R.id.inputTel);
        inputMail = (EditText) findViewById(R.id.inputMail);
        inputIb1 = (EditText) findViewById(R.id.inputIb1);
        inputSw1 = (EditText) findViewById(R.id.inputSw1);


        // save button
        _btnSave = (Button) findViewById(R.id.btnSave);

        if (newx.equals("0")) {

            inputIco.setEnabled(false);
            inputIco.setFocusable(false);
            inputIco.setFocusableInTouchMode(false);
        }

        // tel button
        _btnTel = (Button) findViewById(R.id.btnTel);
        _btnTel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                PackageManager packageManager = getBaseContext().getPackageManager();
                boolean jetel = packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);

                if (jetel) {
                    EditText num = (EditText) findViewById(R.id.inputTel);
                    String number = "tel:" + num.getText().toString().trim();
                    number = number.replace('/', ' ');
                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                    if (ActivityCompat.checkSelfPermission(NewIdcActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    //startActivity(callIntent);
                }else{
            			
            			
            		}

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

        mViewModel.clearObservableIdModelCompany();
        mViewModel.clearObservableIdcSaveToServer();
        mViewModel.clearObservableIdcSaveToRealm();
        mSubscription.clear();
        subscriptionSave.unsubscribe();

    }

    private void bind() {

        mSubscription = new CompositeSubscription();

        if(newx.equals("0")) {

            showProgressDialog();
            mSubscription.add(mViewModel.getMyObservableIdModelCompany()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                    .doOnError(throwable -> { Log.e(TAG, "Error NewIdcActivity " + throwable.getMessage());
                        hideProgressDialog();
                        Toast.makeText(this, "Server not connected", Toast.LENGTH_SHORT).show();
                    })
                    .onErrorResumeNext(throwable -> empty())
                    .subscribe(this::setEditedIco));

            mSubscription.add(mViewModel.getMyObservableIdcToServer()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                    .doOnError(throwable -> {
                        Log.e(TAG, "Error NewIdcActivity " + throwable.getMessage());
                        showProgressDialog();
                        Toast.makeText(this, "Server not connected", Toast.LENGTH_SHORT).show();
                    })
                    .onErrorResumeNext(throwable -> empty())
                    .subscribe(this::savedInvoiceToServer));

            mViewModel.emitMyObservableIdModelCompany(icox);

        }else{

            mSubscription.add(mViewModel.getDataIdcSavedToRealm()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                    .subscribe(this::dataSavedToRealm));
        }

        subscriptionSave = RxView.clicks(_btnSave)
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

                        Log.d("NewInvDoc", "Clicked save ");
                        Toast.makeText(NewIdcActivity.this, "Clicked save", Toast.LENGTH_SHORT).show();



                        if( newx.equals("1")) {

                            List<RealmInvoice> realminvoices = new ArrayList<>();
                            RealmInvoice realminvoice = new RealmInvoice();
                            realminvoice.setDrh("99");
                            realminvoice.setUce("");
                            realminvoice.setDok(inputIco.getText().toString());
                            realminvoice.setDat(inputNai.getText().toString());
                            realminvoice.setIco(inputIco.getText().toString());
                            realminvoice.setNai(inputNai.getText().toString());
                            realminvoice.setHod(inputMes.getText().toString());
                            realminvoice.setZk0(inputDic.getText().toString());
                            realminvoice.setZk1(inputIcd.getText().toString());
                            realminvoice.setZk2(inputUli.getText().toString());
                            realminvoice.setDn1(inputPsc.getText().toString());
                            realminvoice.setDn2(inputMes.getText().toString());
                            realminvoice.setPoz(inputTel.getText().toString());
                            realminvoice.setDas(inputIb1.getText().toString());
                            realminvoice.setDaz(inputSw1.getText().toString());
                            realminvoice.setFak("0");
                            realminvoice.setKto(inputMail.getText().toString());
                            realminvoice.setPoh("0");
                            realminvoice.setSaved("false");
                            realminvoices.add(realminvoice);

                            Log.d("NewIdc ", realminvoice.getDok());
                            mViewModel.emitRealmIdcToRealm(realminvoices);
                        }else{

                            List<RealmInvoice> realminvoices = new ArrayList<>();
                            RealmInvoice realminvoice = new RealmInvoice();
                            realminvoice.setDrh("99");
                            realminvoice.setUce("");
                            realminvoice.setDok(inputIco.getText().toString());
                            realminvoice.setDat(inputNai.getText().toString());
                            realminvoice.setIco(inputIco.getText().toString());
                            realminvoice.setNai(inputNai.getText().toString());
                            realminvoice.setHod(inputMes.getText().toString());
                            realminvoice.setZk0(inputDic.getText().toString());
                            realminvoice.setZk1(inputIcd.getText().toString());
                            realminvoice.setZk2(inputUli.getText().toString());
                            realminvoice.setDn1(inputPsc.getText().toString());
                            realminvoice.setDn2(inputMes.getText().toString());
                            realminvoice.setPoz(inputTel.getText().toString());
                            realminvoice.setDas(inputIb1.getText().toString());
                            realminvoice.setDaz(inputSw1.getText().toString());
                            realminvoice.setFak("0");
                            realminvoice.setKto(inputMail.getText().toString());
                            realminvoice.setPoh("0");
                            realminvoice.setSaved("false");
                            realminvoices.add(realminvoice);

                            Log.d("EditedIdc ", realminvoice.getDok());
                            mViewModel.emitMyObservableIdcToServer(realminvoice);
                        }

                    }
                });

    }

    private void setEditedIco(@NonNull final List<IdCompanyKt> idc) {

        if( idc.size() > 0 ){
           inputIco.setText(idc.get(0).getIco());
           inputDic.setText(idc.get(0).getDic());
           inputIcd.setText(idc.get(0).getIcd());
           inputNai.setText(idc.get(0).getNai());
           inputUli.setText(idc.get(0).getUli());
           inputPsc.setText(idc.get(0).getPsc());
           inputMes.setText(idc.get(0).getMes());
           inputTel.setText(idc.get(0).getTel());
           inputMail.setText(idc.get(0).getEmail());
           inputIb1.setText(idc.get(0).getIb1());
           inputSw1.setText(idc.get(0).getSw1());
        }
        hideProgressDialog();
    }

    private void dataSavedToRealm(@NonNull final RealmInvoice invoice) {
        mViewModel.clearObservableIdcSaveToRealm();

        Toast.makeText(this, "Saved doc " + invoice.getDok(), Toast.LENGTH_SHORT).show();
        finish();
    }

    private void savedInvoiceToServer(List<Invoice> saveds) {

        Log.d("invxstring saved ", saveds.get(0).getDok());
        Log.d("invxstring  ", saveds.get(0).getNai());
        mViewModel.clearObservableIdcSaveToServer();

        Log.d("invxstring ", "saved ");
        finish();

    }



}