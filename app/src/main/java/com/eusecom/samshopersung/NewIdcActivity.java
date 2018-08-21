package com.eusecom.samshopersung;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.eusecom.samshopersung.realm.RealmInvoice;
import com.jakewharton.rxbinding.view.RxView;
import java.util.ArrayList;
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


public class NewIdcActivity extends BaseActivity {

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

    @NonNull
    private CompositeSubscription mSubscription;
    private Subscription subscriptionSave;

    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    ShopperIMvvmViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newidc_activity);

        AndroidInjection.inject(this);

        //getSupportActionBar().setTitle(getString(R.string.action_setmfir));

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
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
        //mViewModel.clearObservableIdcSaveToServer();
        //mViewModel.clearObservableIdcSaveToRealm();
        mSubscription.clear();
        subscriptionSave.unsubscribe();

    }

    private void bind() {

        showProgressBar();
        mSubscription = new CompositeSubscription();

        mSubscription.add(mViewModel.getMyObservableIdModelCompany()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> { Log.e(TAG, "Error NewIdcActivity " + throwable.getMessage());
                    hideProgressBar();
                    Toast.makeText(this, "Server not connected", Toast.LENGTH_SHORT).show();
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::setEditedIco));

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
                            //mViewModel.emitMyObservableIdcToServer(realminvoice);

                    }
                });

        mViewModel.emitMyObservableIdModelCompany(mSharedPreferences.getString("mfir", ""));


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
        hideProgressBar();
    }


    private void savedInvoiceToServer(List<Invoice> saveds) {

        Log.d("invxstring saved ", saveds.get(0).getDok());
        Log.d("invxstring  ", saveds.get(0).getNai());
        //mViewModel.clearObservableIdcSaveToServer();

        Log.d("invxstring ", "saved ");
        finish();

    }



}