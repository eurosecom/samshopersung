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


import com.eusecom.samfantozzi.models.BankItem;

import java.util.List;

public interface DocSearchMvpPresenter {

    void loadStudents();

    void loadSearchItems();

    void loadNext20SearchItems(int start, int end);

    void attachView(DocSearchMvpView view);

    void detachView();

    void getFirst20SearchItemsFromSql(String query);

    void getNext20SearchItemsFromSql(String query, int start, int end);

    void getForQueryFirst20SearchItemsFromSql(String query);

    void emitSearchString(String query);

}
