/*
 *
 *  * Copyright (C) 2014 Antonio Leiva Gordillo.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.eusecom.samfantozzi;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.eusecom.samfantozzi.models.BankItem;
import com.eusecom.samfantozzi.models.BankItemList;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;
import static android.content.ContentValues.TAG;
import static rx.Observable.empty;

public class BankMvpPresenterImpl implements BankMvpPresenter, BankFindItemsInteractor.OnFinishedListener{

    private BankMvpView mainView;
    private BankFindItemsInteractor findItemsInteractor;
    private CompositeSubscription mSubscription;
    private SharedPreferences mSharedPreferences;
    private String searchQuery = "";

    public BankMvpPresenterImpl(BankMvpView mainView, SharedPreferences sharedPreferences,
                                BankFindItemsInteractor findItemsInteractor) {
        this.mainView = mainView;
        this.mSharedPreferences = sharedPreferences;
        this.findItemsInteractor = findItemsInteractor;
    }

    @Override
    public void attachView(BankMvpView view) {
        this.mainView = view;

        Log.d("BankMvpPresenter ", "attachView " + searchQuery);
        if (mainView != null) {
            mainView.setQueryToSearch(searchQuery);
        }
    }

    @Override
    public void detachView() {
        this.mainView = null;
        Log.d("BankMvpPresenter ", "detachView " + searchQuery);
    }

    @Override public void onResume() {
        if (mainView != null) {
            mainView.showProgress();
        }

        //delayed example
        findItemsInteractor.findItems(this);

    }

    @Override public void onDestroy() {

        mainView = null;
        clearObservableItemDelFromServer();
        mSubscription.unsubscribe();
        mSubscription.clear();
    }

    @Override
    public void onStart(){
        if (mainView != null) {
            mainView.showProgress();

            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString("drupoh", "1").apply();
            editor.putString("newdok", "1").apply();
            editor.putString("edidok", "0").apply();
            editor.commit();
            String serverx = mSharedPreferences.getString("servername", "");

        mSubscription = new CompositeSubscription();
        mSubscription.add(findItemsInteractor.findCompanies(serverx)
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> { Log.e(TAG, "Error InvoiceListFragment " + throwable.getMessage());
                    mainView.hideProgress();
                    mainView.showMessage("Server not connected");
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::onFinishedInvoice));
        }

        //http://www.eshoptest.sk/androidfantozzi/get_accountitem.php?userhash=7d074740465344b6cb3cd1eea75d88c73664d4ea912e15d5c7d851d328046849cd0325dd6778ae40da3c4d73ac441e34f03ad6ff3cd7f926df466e3a660c8740
        // &userid=6.49580023480085&vyb_rok=2017&fromfir=301&drh=2&uce=32100&ume=1.2017
        //String userhash, String userid, String fromfir, String vyb_rok, String drh, String uce, String ume, String dokx
        Random r = new Random();
        double d = 10.0 + r.nextDouble() * 20.0;
        String ds = String.valueOf(d);

        String usuidx = mSharedPreferences.getString("usuid", "");
        String userxplus =  ds + "/" + usuidx + "/" + ds;

        MCrypt mcrypt = new MCrypt();
        String encrypted2 = "";
        try {
            encrypted2 = mcrypt.bytesToHex( mcrypt.encrypt(userxplus) );
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String drh = "4";
        String dodx = mSharedPreferences.getString("doduce", "");
        if (drh.equals("1")) {
            dodx = mSharedPreferences.getString("odbuce", "");
        }
        if (drh.equals("3")) {
            dodx = mSharedPreferences.getString("pokluce", "");
        }
        if (drh.equals("4")) {
            dodx = mSharedPreferences.getString("bankuce", "");
        }
        String umex = mSharedPreferences.getString("ume", "");
        String edidok = mSharedPreferences.getString("edidok", "");
        String serverx = mSharedPreferences.getString("servername", "");

        //mSubscription.add(findItemsInteractor.findBankItems("7d074740465344b6cb3cd1eea75d88c73664d4ea912e15d5c7d851d328046849cd0325dd6778ae40da3c4d73ac441e34f03ad6ff3cd7f926df466e3a660c8740"
        //        , "6.49580023480085", "301"
        //        , "2017", "4"
        //        , "22100", "01.2017", "0")

        //mSubscription.add(findItemsInteractor.findBankItems(encrypted2, ds, firx, rokx, drh, dodx, umex, edidok)
        //        .subscribeOn(Schedulers.computation())
        //        .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
        //        .doOnError(throwable -> { Log.e(TAG, "Error BankMvpPresenter " + throwable.getMessage());
        //            mainView.hideProgress();
        //            mainView.showMessage("Server not connected");
        //        })
        //       .onErrorResumeNext(throwable -> empty())
        //        .subscribe(this::onFinishedBankItems));

        mSubscription.add(findItemsInteractor.findBankItemsWithBalance(serverx, encrypted2, ds, firx, rokx, drh, dodx, umex, edidok)
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> { Log.e(TAG, "Error BankMvpPresenter " + throwable.getMessage());
                    mainView.hideProgress();
                    mainView.showMessage("Server not connected");
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::onFinishedBankItemList));

    }


    @Override public void onItemClicked(int position) {
        if (mainView != null) {
            mainView.showMessage(String.format("Position %d clicked", position + 1));
        }
    }


    @Override public void onFinished(List<String> items) {
        if (mainView != null) {
            mainView.setItems(items);
            mainView.hideProgress();
        }
    }

    @Override public void onFinishedInvoice(List<Invoice> invoices) {
        if (mainView != null) {
            //Log.d("BankMvpPresenter ", items.get(0).getDok());
            //mainView.setInvoiceItems(invoices);
            //mainView.hideProgress();
        }
    }

    @Override public void onFinishedBankItems(List<BankItem> bankitems) {
        //if (mainView != null) {
        //    //Log.d("BankMvpPresenter ", bankitems.get(0).getDok());
        //    mAdapter = new AccountItemAdapter(this);
        //    mainView.setBankItems(mAdapter, bankitems);
        //    mainView.setBalance(bankitems.get(0).getBal());
        //    mainView.hideProgress();
        //}
    }

    @Override public void onFinishedBankItemList(BankItemList bankitems) {
        //Log.d("Presenter bankitems", bankitems.getBalance());
        if (mainView != null) {
            //Log.d("BankMvpPresenter ", bankitems.get(0).getDok());
            mainView.setBankItems(bankitems.getBankitem());
            mainView.setBalance(bankitems.getBalance());
            mainView.hideProgress();
        }
    }

    public BankMvpView getMainView() {
        return mainView;
    }


    public void deleteDoc(BankItem item){
        Log.d("deleteDoc", item.getHod());
        mSubscription.add(getMyItemDelFromServer()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> { Log.e(TAG, "Error BankMvpPresenter " + throwable.getMessage());
                    if (mainView != null) {
                    mainView.hideProgress();
                    mainView.showMessage("Server not connected");
                    }
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::deletedInvoice));
        if (mainView != null) {
            mainView.showProgress();
        }
        emitDelItemFromServer(item);

    }

    //emit delete Item from Mysql
    public void emitDelItemFromServer(BankItem invx) {

        mObservableItemDelFromServer.onNext(invx);
    }

    @NonNull
    private BehaviorSubject<BankItem> mObservableItemDelFromServer = BehaviorSubject.create();

    @NonNull
    public Observable<BankItemList> getMyItemDelFromServer() {

        Random r = new Random();
        double d = -10.0 + r.nextDouble() * 20.0;
        String ds = String.valueOf(d);

        String usuidx = mSharedPreferences.getString("usuid", "");
        String userxplus =  ds + "/" + usuidx + "/" + ds;

        MCrypt mcrypt = new MCrypt();
        String encrypted2 = "";
        try {
            encrypted2 = mcrypt.bytesToHex( mcrypt.encrypt(userxplus) );
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String drh = "4";
        String dodx = mSharedPreferences.getString("doduce", "");
        if (drh.equals("1")) {
            dodx = mSharedPreferences.getString("odbuce", "");
        }
        if (drh.equals("3")) {
            dodx = mSharedPreferences.getString("pokluce", "");
        }
        if (drh.equals("4")) {
            dodx = mSharedPreferences.getString("bankuce", "");
        }
        String umex = mSharedPreferences.getString("ume", "");
        String serverx = mSharedPreferences.getString("servername", "");

        Log.d("NewCashLog del fir ", firx);

        final String encryptedf=encrypted2;
        final String dodxf=dodx;

        return mObservableItemDelFromServer
                .observeOn(Schedulers.computation())
                .flatMap(invx -> findItemsInteractor.getMyDocDelFromServer(serverx, encryptedf, ds, firx, rokx, drh, dodxf, umex, invx.getDok() ));
    }

    public void clearObservableItemDelFromServer() {

        mObservableItemDelFromServer = BehaviorSubject.create();

    }
    //end delete Invoice from Mysql

    private void deletedInvoice(BankItemList item) {
        //Log.d("deleted Item", item.get(0).getDok());
        if (mainView != null) {
            //mAdapter = new AccountItemAdapter(this);
            mainView.setBankItems(item.getBankitem());
            mainView.hideProgress();
            mainView.setBalance(item.getBalance());
        }
    }

    public void showSearchResult(List<BankItem> result) {

        if (result.isEmpty()) {
            mainView.setSearchedBankItems(Collections.<BankItem>emptyList());
        } else {
            mainView.setSearchedBankItems(result);
        }
    }

    //emit BankList search query
    public void emitSearchString(String queryx){

        searchQuery=queryx;

    }

    private void setQueryString(String querystring) {

        Log.d("Presenter query ", querystring);
        if (mainView != null) {
            mainView.setQueryToSearch(querystring);
        }

    }

    //end emit BankList search query


}
