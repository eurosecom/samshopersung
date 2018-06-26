package com.eusecom.samshopersung;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import com.eusecom.samshopersung.mvvmdatamodel.ShopperIDataModel;
import com.eusecom.samshopersung.realm.RealmDomain;

import java.util.List;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.content.ContentValues.TAG;
import static rx.Observable.empty;

/**
 * ViewModel implementation that logs every count increment.
 */
public class DomainsViewModel extends DomainsBaseViewModel {

    private final DomainsClickInterceptor loggingInterceptor;
    ShopperIDataModel mDataModel;
    private CompositeSubscription mSubscription;

    public DomainsViewModel(DomainsClickInterceptor loggingInterceptor, ShopperIDataModel dataModel ) {
        this.loggingInterceptor = loggingInterceptor;
        this.mDataModel = dataModel;
        getDomainsLiveData();
    }

    @Override
    public void setCount(int count) {
        super.setCount(count);
        loggingInterceptor.intercept(count);
    }

    public void getDomainsLiveData(){

        mSubscription = new CompositeSubscription();
        mSubscription.add(mDataModel.getDomainsFromRealm()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> { Log.e(TAG, "Error DomainsViewModel " + throwable.getMessage());
                    //hideProgressBar();
                    //Toast.makeText(this, "Server not connected", Toast.LENGTH_SHORT).show();
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::savedDomainsInViewModel));
    }

    private void savedDomainsInViewModel(@NonNull final List<RealmDomain> domains) {

        Log.d("DomainsViewModel dom ", domains.get(0).getDomain());
        super.setLiveDomains(domains);
    }

    public void onDestroy() {

        mSubscription.unsubscribe();
        mSubscription.clear();
    }

    //get no saved doc from Realm
    public Observable<List<RealmDomain>> getSavedDomainFromRealm() {

        return mDataModel.getDomainsFromRealm();
    }
    //end get no saved doc from Realm

}
