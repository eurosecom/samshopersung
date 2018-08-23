package com.eusecom.samshopersung.realm;

import android.support.annotation.NonNull;

import java.util.List;

import io.realm.Realm;

public interface IdcController {

    //get realm
    Realm getRealm();

    // methods for NewIdcActivity

    //to get ID from RealmInvoice
    public List<RealmInvoice> getMyIdcData(@NonNull final String fromact);

    //try if exist ID in RealmInvoice
    RealmInvoice existRealmInvoice(@NonNull final RealmInvoice icox);

    //to save ID into RealmInvoice
    void setRealmInvoiceData(@NonNull final RealmInvoice icox);

    //delete ID from RealmInvoice
    void deleteRealmInvoiceData(@NonNull final RealmInvoice icox);


}
