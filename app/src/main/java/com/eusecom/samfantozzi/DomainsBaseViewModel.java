package com.eusecom.samfantozzi;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.eusecom.samfantozzi.realm.RealmDomain;
import java.util.List;

/**
 * Basic view model implementation to illustrate the ViewModel functionality.
 */

public class DomainsBaseViewModel extends ViewModel {

    private int count;

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }


    private MutableLiveData<List<RealmDomain>> liveDomains = new MutableLiveData<>();

    public void setLiveDomains(List<RealmDomain> domains) {
        liveDomains.setValue(domains);
    }

    public MutableLiveData<List<RealmDomain>> getLiveDomains() {
        return liveDomains;
    }

}
