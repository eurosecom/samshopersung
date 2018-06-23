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

import android.support.annotation.NonNull;
import com.eusecom.samfantozzi.models.BankItemList;
import com.eusecom.samfantozzi.retrofit.AbsServerService;
import com.eusecom.samfantozzi.retrofit.ExampleInterceptor;

import rx.Observable;

public class GeneralDocInteractorImpl implements GeneralDocInteractor {

    AbsServerService mAbsServerService;
    ExampleInterceptor mInterceptor;

    public GeneralDocInteractorImpl (@NonNull final AbsServerService absServerService,
                                     ExampleInterceptor interceptor) {
        mAbsServerService = absServerService;
        mInterceptor = interceptor;
    }

    //find BankItemsList from Mysql
    @Override public Observable<BankItemList> findGeneralItemsWithBalance(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String uce, String ume, String dokx) {

        setRetrofit(servername);
        return mAbsServerService.getBankItemsFromSqlServerWithBalance(userhash, userid, fromfir, vyb_rok, drh, uce, ume, dokx);

    }
    //end find BankItemsList from Mysql

    //delete General Doc from Mysql
    @Override public Observable<BankItemList> getMyDocDelFromServer(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String uce, String ume, String dokx) {

        System.out.println("invxstring userhash " + userhash);
        System.out.println("invxstring userid " + userid);
        System.out.println("invxstring fromfir " + fromfir);
        System.out.println("invxstring vyb_rok " + vyb_rok);
        System.out.println("invxstring drh " + drh);
        System.out.println("invxstring dok " + dokx);

        setRetrofit(servername);
        return mAbsServerService.deleteBankDocFromSqlServer(userhash, userid, fromfir, vyb_rok, drh, uce, ume, dokx);

    }
    //end delete General Doc from Mysql

    //set retrofit by runtime
    public void setRetrofit(String servername) {

        System.out.println("invxstring servername " + servername);
        String urlname = "http://" + servername;

        mInterceptor.setInterceptor(urlname);

    }
    //end set retrofit by runtime

}
