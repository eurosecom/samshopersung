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
import android.os.Handler;
import android.util.Log;

import com.eusecom.samfantozzi.models.BankItem;
import com.eusecom.samfantozzi.models.BankItemList;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.content.ContentValues.TAG;
import static rx.Observable.empty;

public class DocSearchMvpPresenterImpl implements DocSearchMvpPresenter, DocSearchInteractor.OnFinishedListener  {

    private DocSearchMvpView mainView;
    private DocSearchInteractor docSearchInteractor;
    private CompositeSubscription mSubscription;
    private SharedPreferences mSharedPreferences;
    private String searchQuery = "";


    public DocSearchMvpPresenterImpl(DocSearchMvpView mainView, SharedPreferences sharedPreferences,
                                DocSearchInteractor mDocSearchInteractor) {
        this.mainView = mainView;
        this.mSharedPreferences = sharedPreferences;
        this.docSearchInteractor = mDocSearchInteractor;
    }

    @Override
    public void attachView(DocSearchMvpView view) {
        this.mainView = view;

        mSubscription = new CompositeSubscription();

        Log.d("DocSearchMvpPresenter ", "attachView " + searchQuery);
        if (mainView != null) {
            mainView.setQueryToSearch(searchQuery);
        }
    }

    @Override
    public void detachView() {
        this.mainView = null;
        Log.d("DocSearchMvpPresenter ", "detachView " + searchQuery);
        mSubscription.clear();
        mSubscription.unsubscribe();
    }


    //load students list
    @Override
    public void loadStudents() {
        if (mainView != null) {
            mainView.showProgress();
        }
        docSearchInteractor.loadStudentsList(this);
    }

    @Override public void onFinishedStudents(List<DocSearchStudent> items) {
        if (mainView != null) {
            mainView.setStudents(items);
            mainView.hideProgress();
        }
    }


    //load bankitems list
    @Override
    public void loadSearchItems() {
        if (mainView != null) {
            mainView.showProgress();
        }
        docSearchInteractor.loadSearchItemsList(this);
    }

    @Override public void onFinishedSearchItems(List<BankItem> items) {
        if (mainView != null) {
           mainView.setSearchItems(items);
           mainView.hideProgress();
        }
    }

    //load next 20 bankitems list
    @Override
    public void loadNext20SearchItems(int start, int end) {
        if (mainView != null) {
            //do not use activity progressbar but progressbar in adapter
            //mainView.showProgress();
        }
        docSearchInteractor.loadNext20SearchItemsList(this, start, end);
    }

    @Override public void onFinishedNext20SearchItems(List<BankItem> items) {
        if (mainView != null) {
            mainView.setNext20SearchItems(items);
            //do not use activity progressbar but progressbar in adapter
            //mainView.hideProgress();
        }
    }



    //get first 20 items from sql
    @Override
    public void getFirst20SearchItemsFromSql(String query) {
        if (mainView != null) {
            mainView.showProgress();

            mSubscription = new CompositeSubscription();

            Random r = new Random();
            double d = 10.0 + r.nextDouble() * 20.0;
            String ds = String.valueOf(d);

            String usuidx = mSharedPreferences.getString("usuid", "");
            String userxplus = ds + "/" + usuidx + "/" + ds;

            MCrypt mcrypt = new MCrypt();
            String encrypted2 = "";
            try {
                encrypted2 = mcrypt.bytesToHex(mcrypt.encrypt(userxplus));
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

            mSubscription.add(docSearchInteractor.getSearchItemsFromSql(serverx, encrypted2, ds, firx, rokx, drh, dodx, umex, query, 0, 20)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                    .doOnError(throwable -> {
                        Log.e(TAG, "Error DocSearchPresenter " + throwable.getMessage());
                        mainView.hideProgress();
                        mainView.showMessage("Server not connected");
                    })
                    .onErrorResumeNext(throwable -> empty())
                    .subscribe(this::onFinishedSearchItemsFromSql));

        }
    }

    public void onFinishedSearchItemsFromSql(List<BankItem> searchtems) {
        //Log.d("DocSearchMvp bankitem0", searchtems.get(0).getDok());
        if (mainView != null) {
            mainView.setSearchItems(searchtems);
            mainView.hideProgress();
        }
    }
    //end get first 20 items from sql

    //get next 20 items from sql
    @Override
    public void getNext20SearchItemsFromSql(String query, int start, int end) {
        if (mainView != null) {
            //do not use activity progressbar but progressbar in adapter
            //mainView.showProgress();

            Random r = new Random();
            double d = 10.0 + r.nextDouble() * 20.0;
            String ds = String.valueOf(d);

            String usuidx = mSharedPreferences.getString("usuid", "");
            String userxplus = ds + "/" + usuidx + "/" + ds;

            MCrypt mcrypt = new MCrypt();
            String encrypted2 = "";
            try {
                encrypted2 = mcrypt.bytesToHex(mcrypt.encrypt(userxplus));
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

            Log.d("DocSearchMvp start end ", start + " " + end);
            String serverx = mSharedPreferences.getString("servername", "");

            mSubscription.add(docSearchInteractor.getSearchItemsFromSql(serverx, encrypted2, ds, firx, rokx, drh, dodx, umex, query, start, end)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                    .doOnError(throwable -> {
                        Log.e(TAG, "Error DocSearchPresenter " + throwable.getMessage());
                        mainView.hideProgress();
                        mainView.showMessage("Server not connected");
                    })
                    .onErrorResumeNext(throwable -> empty())
                    .subscribe(this::onFinishedNextSearchItemsFromSql));

        }
    }

    public void onFinishedNextSearchItemsFromSql(List<BankItem> searchtems) {
        //Log.d("DocSearchMvp bankitem0", searchtems.get(0).getDok());
        if (mainView != null) {
            mainView.setNext20SearchItems(searchtems);
            //do not use activity progressbar but progressbar in adapter
            //mainView.hideProgress();
        }
    }
    //end get next 20 items from sql

    //get for query first 20 items from sql
    @Override
    public void getForQueryFirst20SearchItemsFromSql(String query) {
        if (mainView != null) {

            //java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()
            //mainView.showProgress();

            mSubscription = new CompositeSubscription();

            Random r = new Random();
            double d = 10.0 + r.nextDouble() * 20.0;
            String ds = String.valueOf(d);

            String usuidx = mSharedPreferences.getString("usuid", "");
            String userxplus = ds + "/" + usuidx + "/" + ds;

            MCrypt mcrypt = new MCrypt();
            String encrypted2 = "";
            try {
                encrypted2 = mcrypt.bytesToHex(mcrypt.encrypt(userxplus));
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

            mSubscription.add(docSearchInteractor.getSearchItemsFromSql(serverx, encrypted2, ds, firx, rokx, drh, dodx, umex, query, 0, 20)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                    .doOnError(throwable -> {
                        Log.e(TAG, "Error DocSearchPresenter " + throwable.getMessage());
                        mainView.hideProgress();
                        mainView.showMessage("Server not connected");
                    })
                    .onErrorResumeNext(throwable -> empty())
                    .subscribe(this::onForQueryFinishedSearchItemsFromSql));

        }
    }

    public void onForQueryFinishedSearchItemsFromSql(List<BankItem> searchtems) {
        //Log.d("DocSearchMvp bankitem0", searchtems.get(0).getDok());
        if (mainView != null) {
            mainView.setForQueryFirstSearchItems(searchtems);
            mainView.hideProgress();
        }
    }
    //end get for query first 20 items from sql

    //emit DocSearchList search query
    public void emitSearchString(String queryx){

        Log.d("DocSearchMvp presenter ", queryx);
        searchQuery=queryx;

    }
    //end emit DocSearchList search query

}
