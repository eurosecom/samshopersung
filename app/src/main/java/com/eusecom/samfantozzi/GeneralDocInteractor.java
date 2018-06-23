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

import com.eusecom.samfantozzi.models.BankItemList;

import java.util.List;

import rx.Observable;

public interface GeneralDocInteractor {

    interface OnFinishedListener {
        void onFinished(List<String> items);

    }

    Observable<BankItemList> findGeneralItemsWithBalance(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String uce, String ume, String dokx);

    Observable<BankItemList> getMyDocDelFromServer(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String uce, String ume, String dokx);

}
