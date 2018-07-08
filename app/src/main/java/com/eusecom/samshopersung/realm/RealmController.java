package com.eusecom.samshopersung.realm;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;
import io.realm.Realm;
import io.realm.RealmResults;
 
 
public class RealmController {
 
    private static RealmController instance;
    private final Realm realm;
 
    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController getInstance() {

        return instance;
    }
 
    public Realm getRealm() {
 
        return realm;
    }
 
    //Refresh the realm istance
    public void refresh() {
 
        realm.refresh();
    }
 

}