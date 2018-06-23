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

public interface BankMvpView {

    void showProgress();

    void hideProgress();

    void setItems(List<String> items);

    void setInvoiceItems(List<Invoice> items);

    void setBankItems(List<BankItem> items);

    void setSearchedBankItems(List<BankItem> items);

    void showMessage(String message);

    void showItemDialog(BankItem invoice);

    void setBalance(String statebalance);

    void setQueryToSearch(String query);
}
