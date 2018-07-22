package com.eusecom.samshopersung.mvvmdatamodel;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import io.realm.Realm;
import rx.Observable;
import com.eusecom.samshopersung.BasketKt;
import com.eusecom.samshopersung.CategoryKt;
import com.eusecom.samshopersung.CompanyKt;
import com.eusecom.samshopersung.ProductKt;
import com.eusecom.samshopersung.R;
import com.eusecom.samshopersung.SumBasketKt;
import com.eusecom.samshopersung.models.Album;
import com.eusecom.samshopersung.models.Employee;
import com.eusecom.samshopersung.models.InvoiceList;
import com.eusecom.samshopersung.realm.IRealmController;
import com.eusecom.samshopersung.realm.RealmDomain;
import com.eusecom.samshopersung.retrofit.ExampleInterceptor;
import com.eusecom.samshopersung.retrofit.ShopperRetrofitService;
import com.google.firebase.database.DatabaseReference;

public class ShopperDataModel implements ShopperIDataModel {

    DatabaseReference mFirebaseDatabase;
    ShopperRetrofitService mShopperRetrofitService;
    Resources mResources;
    Realm mRealm;
    ExampleInterceptor mInterceptor;
    IRealmController mRealmController;

    public ShopperDataModel(@NonNull final DatabaseReference databaseReference,
                                 @NonNull final ShopperRetrofitService shopperRetrofitService,
                                 @NonNull final Resources resources,
                                 @NonNull final Realm realm,
                                 @NonNull final ExampleInterceptor interceptor,
                                 @NonNull final IRealmController realmcontroller) {
        mFirebaseDatabase = databaseReference;
        mShopperRetrofitService = shopperRetrofitService;
        mResources = resources;
        mRealm = realm;
        mInterceptor = interceptor;
        mRealmController = realmcontroller;
    }


    //methods for ChooseCompanyActivity

    @Override
    public Observable<List<CompanyKt>> getCompaniesFromMysqlServer(String servername, String userhash, String userid) {

        setRetrofit(servername);
        return mShopperRetrofitService.getCompaniesFromServer(userhash, userid);

    }

    //save domains to realm
    @NonNull
    @Override
    public Observable<RealmDomain> saveDomainToRealm(@NonNull final RealmDomain domx) {

        //System.out.println("existRealmDomain " + domx.getDomain());
        //does exist invoice in Realm?
        RealmDomain domainexists = existRealmDomain( domx );

        if(domainexists != null){
            //System.out.println("existRealmDomain " + true);
            deleteRealmDomainData( domx );
        }else{
            //System.out.println("existRealmDomain " + false);
        }
        //save to realm and get String OK or ERROR
        setRealmDomainData( domx );

        return Observable.just(domx);

    }

    public RealmDomain existRealmDomain(@NonNull final RealmDomain domx) {

        //old version without realmcontroller
        //String dokx = domx.getDomain();
        //return mRealm.where(RealmDomain.class).equalTo("domain", dokx).findFirst();

        //new version with realmcontroller
        return mRealmController.existRealmDomain(domx);

    }

    private void setRealmDomainData(@NonNull final RealmDomain domx) {

        //old version without realmcontroller
        //mRealm.beginTransaction();
        //mRealm.copyToRealm(domx);
        //mRealm.commitTransaction();

        //new version with realmcontroller
        mRealmController.setRealmDomainData(domx);

    }

    private void deleteRealmDomainData(@NonNull final RealmDomain domx) {

        //old version without realmcontroller
        //String dokx = domx.getDomain();
        //mRealm.executeTransaction(new Realm.Transaction() {
        //    @Override
        //    public void execute(Realm realm) {
        //        RealmResults<RealmDomain> result = realm.where(RealmDomain.class).equalTo("domain", dokx).findAll();
        //        result.clear();
        //    }
        //});

        //new version with realmcontroller
        mRealmController.deleteRealmDomainData(domx);

    }
    //end methods for ChooseCompanyActivity

    //methods for DomainsActivity
    @NonNull
    @Override
    public Observable<List<RealmDomain>> getDomainsFromRealm() {

        Log.d("DomainsViewModelRealm ", mRealm.toString());
        List<RealmDomain> results = null;
        //old version without realmcontroller
        //results = mRealm.where(RealmDomain.class).findAll();

        //new version with realmcontroller
        results = mRealmController.getDomainsFromRealmDomain();


        return Observable.just(results);
    }
    //end methods for DomainsActivity


    //methods for OfferKtActivity

    //get products from MySql
    @Override
    public Observable<List<ProductKt>> getProductsFromMysqlServer(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String ucex, String umex, String dokx) {
        Log.d("GenDoc dokx", dokx);
        Log.d("GenDoc drh", drh);
        Log.d("GenDoc ucex", ucex);

        setRetrofit(servername);
        return mShopperRetrofitService.getProductsFromSqlServer(userhash, userid, fromfir, vyb_rok, drh, ucex, umex, dokx);

    }

    //get categories from MySql
    @Override
    public Observable<List<CategoryKt>> getCatFromMysqlServer(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String ucex, String umex, String dokx) {
        Log.d("GenDoc dokx", dokx);
        Log.d("GenDoc drh", drh);
        Log.d("GenDoc ucex", ucex);

        setRetrofit(servername);
        return mShopperRetrofitService.getCatFromSqlServer(userhash, userid, fromfir, vyb_rok, drh, ucex, umex, dokx);

    }

    public Observable<List<Album>> prepareAlbumsList() {

        List<Album> albumList = new ArrayList<>();;
        int[] covers = new int[]{
                R.drawable.album1,
                R.drawable.album2,
                R.drawable.album3,
                R.drawable.album4,
                R.drawable.album5,
                R.drawable.album6,
                R.drawable.album7,
                R.drawable.album8,
                R.drawable.album9,
                R.drawable.album10,
                R.drawable.album11};

        Album a = new Album("True Romance", 13, covers[0]);
        albumList.add(a);

        a = new Album("Xscpae", 8, covers[1]);
        albumList.add(a);

        a = new Album("Maroon 5*", 11, covers[2]);
        albumList.add(a);

        a = new Album("Born to Die", 12, covers[3]);
        albumList.add(a);

        a = new Album("Honeymoon", 14, covers[4]);
        albumList.add(a);

        a = new Album("I Need a Doctor", 1, covers[5]);
        albumList.add(a);

        a = new Album("Loud", 11, covers[6]);
        albumList.add(a);

        a = new Album("Legend", 14, covers[7]);
        albumList.add(a);

        a = new Album("Hello", 11, covers[8]);
        albumList.add(a);

        a = new Album("Greatest Hits", 17, covers[9]);
        albumList.add(a);

        return Observable.just(albumList);
    }


    //end methods for OfferKtActivity

    //methods for BasketKtActivity

    //get basket from MySql
    @Override
    public Observable<List<BasketKt>> getBasketFromMysqlServer(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String ucex, String prodx, String dokx) {
        Log.d("GenDoc dokx", dokx);
        Log.d("GenDoc drh", drh);
        Log.d("GenDoc ucex", ucex);

        setRetrofit(servername);
        return mShopperRetrofitService.getBasketFromSqlServer(userhash, userid, fromfir, vyb_rok, drh, ucex, prodx, dokx);

    }

    //get sumbasket from MySql
    @Override
    public Observable<SumBasketKt> getSumBasketFromMysqlServer(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String ucex, String prodx, String dokx) {
        Log.d("GenDoc dokx", dokx);
        Log.d("GenDoc drh", drh);
        Log.d("GenDoc ucex", ucex);

        setRetrofit(servername);
        return mShopperRetrofitService.getSumBasketFromSqlServer(userhash, userid, fromfir, vyb_rok, drh, ucex, prodx, dokx);

    }


    //end methods for BasketKtActivity


    //methods for OrderListActivity

    @Override
    public Observable<InvoiceList> getCashDocsFromMysqlServer(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String ucex, String umex, String dokx) {

        setRetrofit(servername);
        return mShopperRetrofitService.getCashDocsFromSqlServer(userhash, userid, fromfir, vyb_rok, drh, ucex, umex, dokx);

    }

    //end methods for OrderListActivity

    //methods for MapActivity
    public Observable<List<Employee>> prepareEmployeeList() {

        List<Employee> employeeList = new ArrayList<>();

        Employee emp = new Employee("username1", "1");
        emp.setLati("48.6880712");
        emp.setLongi("17.3694098");
        emp.setUsatw("1");
        employeeList.add(emp);

        Employee emp2 = new Employee("username2", "2");
        emp2.setLati("48.5810712");
        emp2.setLongi("17.3594098");
        emp2.setUsatw("1");
        employeeList.add(emp2);

        Employee emp3 = new Employee("username3", "3");
        emp3.setLati("48.6990712");
        emp3.setLongi("17.3994098");
        emp3.setUsatw("1");
        employeeList.add(emp3);


        return Observable.just(employeeList);
    }
    //end methods for MapActivity

    //methods for FlombulatorActivity
    public String getStringFromDataModel() {

        return "Flombulated Real String from DataModel";
    }

    public Observable<List<String>> getRxStringFromDataModel() {

        List<String> liststr = new ArrayList<>();
        liststr.add("Flombulated Real RxString from DataModel");
        return Observable.just(liststr);
    }

    //end methods for FlombulatorActivity


    //set retrofit by runtime
    public void setRetrofit(String servername) {

        System.out.println("invxstring servername " + servername);
        String urlname = "http://" + servername;

        mInterceptor.setInterceptor(urlname);

    }
    //end set retrofit by runtime

}
