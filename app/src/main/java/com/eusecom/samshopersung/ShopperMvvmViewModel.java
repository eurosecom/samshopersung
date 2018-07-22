package com.eusecom.samshopersung;


import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.util.Log;
import com.eusecom.samshopersung.models.Album;
import com.eusecom.samshopersung.models.Employee;
import com.eusecom.samshopersung.models.InvoiceList;
import com.eusecom.samshopersung.mvvmdatamodel.ShopperIDataModel;
import com.eusecom.samshopersung.mvvmschedulers.ISchedulerProvider;
import com.eusecom.samshopersung.realm.RealmDomain;
import java.util.List;
import java.util.Random;
import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * View model for the CompaniesMvvmActivity.
 */
public class ShopperMvvmViewModel implements ShopperIMvvmViewModel{

    //@Inject only by Base constructor injection, then i have got all provided dependencies in module DgFirebaseSubModule
    // injected in class DgAllEmpsAbsListFragment where i inject DgAllEmpsAbsMvvmViewModel
    // If i provide dependency DgAllEmpsAbsMvvmViewModel in DgFirebaseSubModule then i have got in DgAllEmpsAbsMvvmViewMode only dependencies in constructor
    ShopperIDataModel mDataModel;

    //@Inject only by Base constructor injection
    ISchedulerProvider mSchedulerProvider;

    //@Inject only by Base constructor injection
    SharedPreferences mSharedPreferences;


    @NonNull
    private CompositeSubscription mSubscription;
    @NonNull
    private ConnectivityManager mConnectivityManager;

    //@Inject only by Base constructor injection
    public ShopperMvvmViewModel(@NonNull final ShopperIDataModel dataModel,
                                     @NonNull final ISchedulerProvider schedulerProvider,
                                     @NonNull final SharedPreferences sharedPreferences,
                                     @NonNull final ConnectivityManager connectivityManager) {
        mDataModel = dataModel;
        mSchedulerProvider = schedulerProvider;
        mSharedPreferences = sharedPreferences;
        mConnectivityManager = connectivityManager;
    }


    /**
     * methods for ChooseCompanyActivity
     */

    //get companies from MySql server
    public Observable<List<CompanyKt>> getMyCompaniesFromServer() {

        Random r = new Random();
        double d = -10.0 + r.nextDouble() * 20.0;
        String ds = String.valueOf(d);

        String usuidx = mSharedPreferences.getString("usuid", "");
        String userxplus =  ds + "/" + usuidx + "/" + ds;

        MCrypt mcrypt = new MCrypt();
        String encrypted = "";
        try {
            encrypted = mcrypt.bytesToHex(mcrypt.encrypt(userxplus));
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        Log.d("userxplus ", encrypted + " " + ds);
        	/* Decrypt */
        //String decrypted = new String( mMcrypt.decrypt( encrypted ) );
        String serverx = mSharedPreferences.getString("servername", "");

        return mDataModel.getCompaniesFromMysqlServer(serverx, encrypted, ds);
    }
    //end get companies from MySql server

    //emit SaveDomain ToRealm
    public void emitSaveDomainToRealm(RealmDomain domx) {
        mObservableSaveDomainToRealm.onNext(domx);
    }

    @NonNull
    private BehaviorSubject<RealmDomain> mObservableSaveDomainToRealm = BehaviorSubject.create();

    @NonNull
    public Observable<RealmDomain> getMyObservableSaveDomainToRealm() {

        return mObservableSaveDomainToRealm
                .observeOn(mSchedulerProvider.ui())
                .flatMap(domx -> mDataModel.saveDomainToRealm(domx ));

    }

    public void clearObservableSaveDomainToRealm() {

        mObservableSaveDomainToRealm = BehaviorSubject.create();

    }
    //end emit Save DomainToRealm

    /**
     * end methods for ChooseCompanyActivity
     */


    /**
     * methods for OfferKtActivity
     */

    //get products from MySql server
    public Observable<List<ProductKt>> getMyProductsFromSqlServer(String drh) {

        Random r = new Random();
        double d = -10.0 + r.nextDouble() * 20.0;
        String ds = String.valueOf(d);

        String usuidx = mSharedPreferences.getString("usuid", "");
        String userxplus =  ds + "/" + usuidx + "/" + ds;

        MCrypt mcrypt = new MCrypt();
        String encrypted = "";
        try {
            encrypted = mcrypt.bytesToHex(mcrypt.encrypt(userxplus));
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String dodx = mSharedPreferences.getString("doduce", "");
        if (drh.equals("1")) {
            dodx = mSharedPreferences.getString("odbuce", "");
        }
        if (drh.equals("3")) {
            dodx = mSharedPreferences.getString("pokluce", "");
        }
        if (drh.equals("4")) {
            dodx = mSharedPreferences.getString("bankuce", "");
        }
        String umex = mSharedPreferences.getString("ume", "");
        String serverx = mSharedPreferences.getString("servername", "");

        return mDataModel.getProductsFromMysqlServer(serverx, encrypted, ds, firx, rokx, drh, dodx, umex, "0");
    }
    //end get products from MySql server

    //get categories from MySql server
    public Observable<List<CategoryKt>> getMyCatsFromSqlServer(String drh) {

        Random r = new Random();
        double d = -10.0 + r.nextDouble() * 20.0;
        String ds = String.valueOf(d);

        String usuidx = mSharedPreferences.getString("usuid", "");
        String userxplus =  ds + "/" + usuidx + "/" + ds;

        MCrypt mcrypt = new MCrypt();
        String encrypted = "";
        try {
            encrypted = mcrypt.bytesToHex(mcrypt.encrypt(userxplus));
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String dodx = mSharedPreferences.getString("doduce", "");
        if (drh.equals("1")) {
            dodx = mSharedPreferences.getString("odbuce", "");
        }
        if (drh.equals("3")) {
            dodx = mSharedPreferences.getString("pokluce", "");
        }
        if (drh.equals("4")) {
            dodx = mSharedPreferences.getString("bankuce", "");
        }
        String umex = mSharedPreferences.getString("ume", "");
        String serverx = mSharedPreferences.getString("servername", "");

        return mDataModel.getCatFromMysqlServer(serverx, encrypted, ds, firx, rokx, drh, dodx, umex, "0");
    }
    //end get categories from MySql server


    //emit save product to basket to Mysql
    public void emitMyObservableSaveBasketToServer(ProductKt invx) {

        mObservableBasketToServer.onNext(invx);
    }

    @NonNull
    private BehaviorSubject<ProductKt> mObservableBasketToServer = BehaviorSubject.create();

    @NonNull
    public Observable<List<BasketKt>> getMyObservableSaveBasketToServer() {

        Random r = new Random();
        double d = 10.0 + r.nextDouble() * 20.0;
        String ds = String.valueOf(d);

        String usuidx = mSharedPreferences.getString("usuid", "");
        String userxplus =  ds + "/" + usuidx + "/" + ds;

        MCrypt mcrypt = new MCrypt();
        String encrypted = "";
        try {
            encrypted = mcrypt.bytesToHex(mcrypt.encrypt(userxplus));
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String encrypted2=encrypted;

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String dodx = "1";
        String prodx = "";
        String serverx = mSharedPreferences.getString("servername", "");

        return mObservableBasketToServer
                .observeOn(mSchedulerProvider.computation())
                .flatMap(invx -> mDataModel.getBasketFromMysqlServer(serverx, encrypted2, ds, firx
                        , rokx, invx.getPrm1(), dodx, JsonFromProduct(invx), "0"));
    }

    public void clearMyObservableSaveBasketToServer() {

        mObservableBasketToServer = BehaviorSubject.create();

    }
    //end save product to basket to Mysql

    //emit save product to basket to Mysql
    public void emitMyObservableSaveSumBasketToServer(ProductKt invx) {

        mObservableSumBasketToServer.onNext(invx);
    }

    @NonNull
    private BehaviorSubject<ProductKt> mObservableSumBasketToServer = BehaviorSubject.create();

    @NonNull
    public Observable<SumBasketKt> getMyObservableSaveSumBasketToServer() {

        Random r = new Random();
        double d = 10.0 + r.nextDouble() * 20.0;
        String ds = String.valueOf(d);

        String usuidx = mSharedPreferences.getString("usuid", "");
        String userxplus =  ds + "/" + usuidx + "/" + ds;

        MCrypt mcrypt = new MCrypt();
        String encrypted = "";
        try {
            encrypted = mcrypt.bytesToHex(mcrypt.encrypt(userxplus));
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String encrypted2=encrypted;

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String dodx = "1";
        String prodx = "";
        String serverx = mSharedPreferences.getString("servername", "");

        return mObservableSumBasketToServer
                .observeOn(mSchedulerProvider.computation())
                .flatMap(invx -> mDataModel.getSumBasketFromMysqlServer(serverx, encrypted2, ds, firx
                        , rokx, invx.getPrm1(), dodx, JsonFromProduct(invx), "0"));
    }

    public void clearMyObservableSaveSumBasketToServer() {

        mObservableSumBasketToServer = BehaviorSubject.create();

    }
    //end save product to basket to Mysql

    //get Albums from List
    @NonNull
    public Observable<List<Album>> getMyObservableAlbumsFromList() {
        return mDataModel.prepareAlbumsList();
    }


    /**
     * end methods for OfferKtActivity
     */


    /**
     * methods for BasketKtActivity
     */

    //get basket from MySql server
    public Observable<List<BasketKt>> getMyBasketFromSqlServer() {

        Random r = new Random();
        double d = -10.0 + r.nextDouble() * 20.0;
        String ds = String.valueOf(d);

        String usuidx = mSharedPreferences.getString("usuid", "");
        String userxplus =  ds + "/" + usuidx + "/" + ds;

        MCrypt mcrypt = new MCrypt();
        String encrypted = "";
        try {
            encrypted = mcrypt.bytesToHex(mcrypt.encrypt(userxplus));
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String drh = "0";
        String dodx = "1";
        String prodx = "";
        String serverx = mSharedPreferences.getString("servername", "");

        return mDataModel.getBasketFromMysqlServer(serverx, encrypted, ds, firx, rokx, drh, dodx, prodx, "0");
    }
    //end get basket from MySql server


    //get sumbasket from MySql server
    public Observable<SumBasketKt> getMySumBasketFromSqlServer() {

        Random r = new Random();
        double d = -10.0 + r.nextDouble() * 20.0;
        String ds = String.valueOf(d);

        String usuidx = mSharedPreferences.getString("usuid", "");
        String userxplus =  ds + "/" + usuidx + "/" + ds;

        MCrypt mcrypt = new MCrypt();
        String encrypted = "";
        try {
            encrypted = mcrypt.bytesToHex(mcrypt.encrypt(userxplus));
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String drh = "0";
        String dodx = "1";
        String prodx = "";
        String serverx = mSharedPreferences.getString("servername", "");

        return mDataModel.getSumBasketFromMysqlServer(serverx, encrypted, ds, firx, rokx, drh, dodx, prodx, "0");
    }
    //end get sumbasket from MySql server


    /**
     * end methods for BasketKtActivity
     */


    /**
     * methods for OrderListActivity
     */


    //get cashdocs from MySql server
    public Observable<InvoiceList> getMyCashDocsFromSqlServer(String drh) {

        Random r = new Random();
        double d = -10.0 + r.nextDouble() * 20.0;
        String ds = String.valueOf(d);

        String usuidx = mSharedPreferences.getString("usuid", "");
        String userxplus =  ds + "/" + usuidx + "/" + ds;

        MCrypt mcrypt = new MCrypt();
        String encrypted = "";
        try {
            encrypted = mcrypt.bytesToHex(mcrypt.encrypt(userxplus));
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String dodx = "1";
        String umex = mSharedPreferences.getString("ume", "");
        String serverx = mSharedPreferences.getString("servername", "");

        return mDataModel.getCashDocsFromMysqlServer(serverx, encrypted, ds, firx, rokx, drh, dodx, umex, "0");
    }
    //end get cashdocs from MySql server

    /**
     * end methods for OrderListActivity
     */



    /**
     * methods for MapActivity
     */

    //get Employees from List
    @NonNull
    public Observable<List<Employee>> getMyObservableEmployeesFromList() {
        return mDataModel.prepareEmployeeList();
    }


    /**
     * end methods for MapActivity
     */


    /**
     * methods for FlombulatorActivity
     */
    public String getStringFromMvvm() {

        return mDataModel.getStringFromDataModel();
    }

    public Observable<List<String>> getRxStringFromMvvm() {

        return mDataModel.getRxStringFromDataModel();
    }
    /**
     * end methods for FlombulatorActivity
     */

    //JSON from ProductKt
    public String JsonFromProduct(ProductKt invx) {

        String jsonstring = "{" +
                "  \"cis\":" + "\"" + invx.getCis() + "\"" +
                ", \"nat\":" + "\"" + invx.getNat() + "\"" +
                ", \"mer\":" + "\"" + invx.getMer() + "\"" +
                ", \"cep\":" + "\"" + invx.getCep() + "\"" +
                ", \"ced\":" + "\"" + invx.getCed() + "\"" +
                ", \"dph\":" + "\"" + invx.getDph() + "\"" +
                ", \"zas\":" + "\"" + invx.getZas() + "\"" +
                ", \"cat\":" + "\"" + invx.getCat() + "\"" +
                ", \"cep1\":" + "\"" + invx.getCep1() + "\"" +
                ", \"ced1\":" + "\"" + invx.getCed1() + "\"" +
                ", \"prm1\":" + "\"" + invx.getPrm1() + "\"" +
                " }";

        return jsonstring;
    }
    //end JSON from Product

}
