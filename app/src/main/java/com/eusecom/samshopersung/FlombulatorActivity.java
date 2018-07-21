package com.eusecom.samshopersung;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;
import dagger.android.AndroidInjection;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import static android.content.ContentValues.TAG;
import static rx.Observable.empty;

public class FlombulatorActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();

    @Inject
    FlombulatorI flombulator;

    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    ShopperIMvvmViewModel mViewModel;

    @NonNull
    private CompositeSubscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flumborator);

        String flumx = flumbolate();
        Log.d("Flombulated text", flumx );

        //Toast.makeText(this, textFromPref(), Toast.LENGTH_SHORT).show();
        Log.d("Flombulated text", textFromPref() );

        //Toast.makeText(this, textFromMvvm(), Toast.LENGTH_SHORT).show();
        Log.d("Flombulated text", textFromMvvm() );

        bind();

    }

    private void bind() {

        mSubscription = new CompositeSubscription();

        mSubscription.add(getRxString()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> { Log.e(TAG, "Error SupplierListFragment " + throwable.getMessage());
                    Toast.makeText(this, "Server not connected", Toast.LENGTH_SHORT).show();
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::setRxStringFromMvvm));
    }

    private void setRxStringFromMvvm(List<String> rxstringfrommvvm) {

        Log.d("Flombulated text", rxstringfrommvvm.get(0) );
    }

    private void unBind() {

        mSubscription.unsubscribe();
        mSubscription.clear();
    }

    public String flumbolate() {
        return flombulator.flombulateMe();
    }

    public String textFromPref() {
        String textpref = "From act " + mSharedPreferences.getString("servername", "");
        return textpref;
    }

    public String textFromMvvm() {
        String textmvvm = mViewModel.getStringFromMvvm();
        return textmvvm;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unBind();
    }

    protected Observable<List<String>> getRxString() {

        return mViewModel.getRxStringFromMvvm();
    }


}
