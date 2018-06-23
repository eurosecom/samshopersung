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
import android.support.annotation.NonNull;
import android.util.Log;

import com.eusecom.samfantozzi.models.BankItem;
import com.eusecom.samfantozzi.models.BankItemList;
import com.eusecom.samfantozzi.retrofit.AbsServerService;
import com.eusecom.samfantozzi.retrofit.ExampleInterceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.content.ContentValues.TAG;
import static rx.Observable.empty;

public class DocSearchInteractorImpl implements DocSearchInteractor {

    AbsServerService mAbsServerService;
    ExampleInterceptor mInterceptor;
    private List<DocSearchStudent> studentList;
    private List<BankItem> bankitemList;

    public DocSearchInteractorImpl (@NonNull final AbsServerService absServerService,
                                    ExampleInterceptor interceptor) {
        mAbsServerService = absServerService;
        mInterceptor = interceptor;
    }

    //get students list
    @Override public void loadStudentsList(OnFinishedListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                listener.onFinishedStudents(createStudentsArrayList());
            }
        }, 2000);
    }

    private List<DocSearchStudent> createStudentsArrayList() {

        studentList = new ArrayList<DocSearchStudent>();
        for (int i = 1; i <= 20; i++) {
            studentList.add(new DocSearchStudent("Student " + i, "androidstudent" + i + "@gmail.com"));

        }
        return studentList;
    }

    //get bankitems list
    @Override public void loadSearchItemsList(OnFinishedListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                listener.onFinishedSearchItems(createSearchItemsList());
            }
        }, 1);
    }

    private List<BankItem> createSearchItemsList() {

        bankitemList = new ArrayList<BankItem>();
        for (int i = 1; i <= 20; i++) {
            bankitemList.add(new BankItem(" "," ","100" + i," "," "
                    ," "," "," "," "," ","120","pop" + i," "));

        }
        return bankitemList;
    }

    //get next 20 bankitems list
    @Override public void loadNext20SearchItemsList(OnFinishedListener listener, int start, int end) {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                listener.onFinishedNext20SearchItems(createNext20SearchItemsList(start, end));
            }
        }, 1);
    }

    private List<BankItem> createNext20SearchItemsList(int start, int end) {

        bankitemList = new ArrayList<BankItem>();
        for (int i = start + 1; i <= end; i++) {
            bankitemList.add(new BankItem(" "," ","100" + i," "," "
                    ," "," "," "," "," ","120","pop" + i," "));

        }
        return bankitemList;
    }

    //get search items from mysql server
    @Override public Observable<List<BankItem>> getSearchItemsFromSql(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String uce, String ume, String query, int start, int end) {

        setRetrofit(servername);
        return mAbsServerService.getSearchItemsFromSqlServer(userhash, userid, fromfir, vyb_rok, drh, uce, ume, query, start, end);

    }
    //end get search items from mysql server


    //set retrofit by runtime
    public void setRetrofit(String servername) {

        System.out.println("invxstring servername " + servername);
        String urlname = "http://" + servername;

        mInterceptor.setInterceptor(urlname);

    }
    //end set retrofit by runtime
}
