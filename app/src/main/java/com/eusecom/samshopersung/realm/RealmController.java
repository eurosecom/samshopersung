package com.eusecom.samshopersung.realm;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;
import java.util.List;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * The Implementation of RealmController for Realm data operations.
 *
 * @author  eurosecom
 * @version 1.0
 * @since   2018-06-23
 */
public class RealmController implements IRealmController{
 
    private static RealmController instance;
    private final Realm realm;
 
    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController getInstance() {

        return instance;
    }

    //get realm
    public Realm getRealm() {
 
        return realm;
    }
 
    //Refresh the realm istance
    public void refresh() {
 
        realm.refresh();
    }


    //get domains from RealmDomain
    public List<RealmDomain> getDomainsFromRealmDomain() {

        Log.d("DomainsViewModelRealm", realm.toString());
        List<RealmDomain> domains = null;
        domains = realm.where(RealmDomain.class).findAll();
        return domains;
    }
    //end get domains from RealmDomain


    // methods for ChooseCompanyActivity

    /**
     * This method is used to try if exist domain domx in database table RealmDomain
     * @param domx String Name of domain to try if exist in database table RealmDomain
     * @return RealmDomain This returns RealmDomain domx, when exists in database table RealmDomain.
     * {@link  com.eusecom.samshopersung.realm.RealmDomain}
     * @see com.eusecom.samshopersung.realm.RealmDomain
     */
    public RealmDomain existRealmDomain(@NonNull final RealmDomain domx) {

        String dokx = domx.getDomain();
        return realm.where(RealmDomain.class).equalTo("domain", dokx).findFirst();

    }
    //end try if exist domain in RealmDomain

    //to save domain into RealmDomain
    public void setRealmDomainData(@NonNull final RealmDomain domx) {

        realm.beginTransaction();
        realm.copyToRealm(domx);
        realm.commitTransaction();

    }
    //end to save domain into RealmDomain

    //delete domain from RealmDomain
    public void deleteRealmDomainData(@NonNull final RealmDomain domx) {

        String dokx = domx.getDomain();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<RealmDomain> result = realm.where(RealmDomain.class).equalTo("domain", dokx).findAll();
                result.clear();
            }
        });

    }
    //end delete domain from RealmDomain

    // end methods for ChooseCompanyActivity

}