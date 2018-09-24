package com.eusecom.samshopersung;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import com.eusecom.samshopersung.models.Album;
import com.eusecom.samshopersung.models.Employee;
import com.eusecom.samshopersung.models.InvoiceList;
import com.eusecom.samshopersung.models.Product;
import com.eusecom.samshopersung.mvvmdatamodel.ShopperIDataModel;
import com.eusecom.samshopersung.mvvmschedulers.ISchedulerProvider;
import com.eusecom.samshopersung.proxy.CommandExecutorProxy;
import com.eusecom.samshopersung.proxy.CommandExecutorProxyImpl;
import com.eusecom.samshopersung.realm.RealmDomain;
import com.eusecom.samshopersung.realm.RealmInvoice;
import com.eusecom.samshopersung.soap.ISoapRequestFactory;
import com.eusecom.samshopersung.soap.SoapRequestFactory;
import com.eusecom.samshopersung.soap.soaphello.HelloRequestEnvelope;
import com.eusecom.samshopersung.soap.soaphello.HelloResponseEnvelope;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.inject.Inject;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    //@Inject only by Base constructor injection to activity
    ISchedulerProvider mSchedulerProvider;

    //@Inject only by Base constructor injection to activity
    SharedPreferences mSharedPreferences;

    //@Inject only by Base constructor injection to activity for example adapter to activity
    ISoapRequestFactory mSoapRequestFactory;

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

    public ShopperMvvmViewModel(@NonNull final ShopperIDataModel dataModel,
                                @NonNull final ISchedulerProvider schedulerProvider,
                                @NonNull final SharedPreferences sharedPreferences,
                                @NonNull final ConnectivityManager connectivityManager,
                                @NonNull ISoapRequestFactory soapRequestFactory) {
        mDataModel = dataModel;
        mSchedulerProvider = schedulerProvider;
        mSharedPreferences = sharedPreferences;
        mConnectivityManager = connectivityManager;
        mSoapRequestFactory = soapRequestFactory;
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

        //Log.d("userxplus ", encrypted + " " + ds);
        	/* Decrypt */
        //String decrypted = new String( mMcrypt.decrypt( encrypted ) );
        String serverx = mSharedPreferences.getString("servername", "");

        Log.d("WhatStr usuidx ", usuidx);
        Log.d("WhatStr serverx ", serverx);
        Log.d("WhatStr encrypted ", encrypted);
        Log.d("WhatStr ds ", ds);

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
        String dodx = "1";
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
        String dodx = "0";

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

    //emit product by cat from Mysql
    public void emitMyCatProductsFromSqlServer(String drhx) {

        mObservableCatProductsFromServer.onNext(drhx);
    }

    @NonNull
    private BehaviorSubject<String> mObservableCatProductsFromServer = BehaviorSubject.create();

    @NonNull
    public Observable<List<ProductKt>> getMyCatProductsFromSqlServer() {

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
        String encrypted3=encrypted;

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String dodx = "1";
        String umex = "";
        String serverx = mSharedPreferences.getString("servername", "");

        return mObservableCatProductsFromServer
                .observeOn(mSchedulerProvider.computation())
                .flatMap(drhx -> mDataModel.getProductsFromMysqlServer(serverx, encrypted3, ds, firx, rokx, drhx, dodx, umex, "0"));
    }

    public void clearMyCatProductsFromSqlServe() {

        mObservableCatProductsFromServer = BehaviorSubject.create();

    }
    //end emit product by cat from Mysql


    //emit product by query from Mysql
    public void emitMyQueryProductsFromSqlServer(String drhx) {

        mObservableQueryProductsFromServer.onNext(drhx);
    }

    @NonNull
    private BehaviorSubject<String> mObservableQueryProductsFromServer = BehaviorSubject.create();

    @NonNull
    public Observable<List<ProductKt>> getMyQueryProductsFromSqlServer() {

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
        String encrypted3=encrypted;

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String dodx = "1";
        String umex = "";
        String serverx = mSharedPreferences.getString("servername", "");

        return mObservableQueryProductsFromServer
                .observeOn(mSchedulerProvider.computation())
                .flatMap(drhx -> mDataModel.getProductsFromMysqlServer(serverx, encrypted3, ds, firx, rokx, drhx, dodx, umex, "1"));
    }

    public void clearMyQueryProductsFromSqlServe() {

        mObservableQueryProductsFromServer = BehaviorSubject.create();

    }
    //end emit product by query from Mysql


    public List<ProductKt> getQueryListProduct(String query) {

        List<ProductKt> listprod = new ArrayList<>();
        ProductKt prod = new ProductKt("999","Nat 999","", "","","","",""
                ,"","","4","","","");
        listprod.add(prod);


        return listprod;
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


    //get orders from MySql server
    public Observable<InvoiceList> getMyOrdersFromSqlServer(String drh) {

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
        String ustp = mSharedPreferences.getString("ustype", "");

        return mDataModel.getOrdersFromMysqlServer(serverx, encrypted, ds, firx, rokx, drh, dodx, umex, "0", ustp);
    }
    //end get orders from MySql server

    //get invoices from MySql server
    public Observable<List<Invoice>> getMyInvoicesFromSqlServer(String drh) {

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
        String dodx = mSharedPreferences.getString("odbuce", "");
        String umex = "01.2018";
        String serverx = mSharedPreferences.getString("servername", "");
        String ustp = mSharedPreferences.getString("ustype", "");

        return mDataModel.getInvoicesFromMysqlServer(serverx, encrypted, ds, firx, rokx, drh, dodx, umex, "0", ustp);
    }
    //end get invoices from MySql server

    //delete Order
    public void emitDeleteOrder(Invoice invx) {
        if (callCommandExecutorProxy(CommandExecutorProxyImpl.PermType.ADM, CommandExecutorProxyImpl.ReportTypes.PDF
                , CommandExecutorProxyImpl.ReportName.ORDER)) {
            System.out.println("command approved.");
            mObservableDelOrder.onNext(invx);
        }
    }

    @NonNull
    private BehaviorSubject<Invoice> mObservableDelOrder = BehaviorSubject.create();

    @NonNull
    public Observable<InvoiceList> getObservableDeleteOrder() {

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
        String encrypted2=encrypted;

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String dodx = "1";
        String umex = mSharedPreferences.getString("ume", "");
        String serverx = mSharedPreferences.getString("servername", "");
        String ustp = mSharedPreferences.getString("ustype", "");

        return mObservableDelOrder
                .observeOn(mSchedulerProvider.computation())
                .flatMap(invx ->
                        mDataModel.getOrdersFromMysqlServer(serverx, encrypted2, ds, firx, rokx, "4", dodx, umex, invx.getDok(), ustp));
    }

    public void clearObservableDeleteOrder() {

        mObservableDelOrder = BehaviorSubject.create();

    }
    //end delete Order

    //get Order Detail
    public void emitDetailOrder(Invoice invx) {
        if (callCommandExecutorProxy(CommandExecutorProxyImpl.PermType.LGN, CommandExecutorProxyImpl.ReportTypes.PDF
                , CommandExecutorProxyImpl.ReportName.ORDER)) {
            System.out.println("command approved.");
            mObservableDetailOrder.onNext(invx);
        }
    }

    @NonNull
    private BehaviorSubject<Invoice> mObservableDetailOrder = BehaviorSubject.create();

    @NonNull
    public Observable<InvoiceList> getObservableDetailOrder() {

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
        String encrypted2=encrypted;

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String dodx = "1";
        String umex = mSharedPreferences.getString("ume", "");
        String serverx = mSharedPreferences.getString("servername", "");
        String ustp = mSharedPreferences.getString("ustype", "");

        return mObservableDetailOrder
                .observeOn(mSchedulerProvider.computation())
                .flatMap(invx ->
                        mDataModel.getOrdersFromMysqlServer(serverx, encrypted2, ds, firx, rokx, "8", dodx, umex, invx.getDok(), ustp));
    }

    public void clearObservableDetailOrder() {

        mObservableDetailOrder = BehaviorSubject.create();

    }
    //end get Order Detail

    //save Order Detail
    public void emitSaveDetailOrder(Invoice invx) {
        if (callCommandExecutorProxy(CommandExecutorProxyImpl.PermType.LGN, CommandExecutorProxyImpl.ReportTypes.PDF
                , CommandExecutorProxyImpl.ReportName.ORDER)) {
            System.out.println("command approved.");
            mObservableSaveDetailOrder.onNext(invx);
        }
    }

    @NonNull
    private BehaviorSubject<Invoice> mObservableSaveDetailOrder = BehaviorSubject.create();

    @NonNull
    public Observable<InvoiceList> getObservableSaveDetailOrder() {

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
        String encrypted2=encrypted;

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String dodx = "1";
        String umex = mSharedPreferences.getString("ume", "");
        String serverx = mSharedPreferences.getString("servername", "");
        String ustp = mSharedPreferences.getString("ustype", "");

        return mObservableSaveDetailOrder
                .observeOn(mSchedulerProvider.computation())
                .flatMap(invx ->
                        mDataModel.getOrdersFromMysqlServer(serverx, encrypted2, ds, firx, rokx, "9", dodx, JsonFromInvoice(invx), invx.getDok(), ustp));
    }

    public void clearObservableSaveDetailOrder() {

        mObservableSaveDetailOrder = BehaviorSubject.create();

    }
    //end save Order Detail


    //delete Invoice
    public void emitDeleteInvoice(Invoice invx) {
        if (callCommandExecutorProxy(CommandExecutorProxyImpl.PermType.ADM, CommandExecutorProxyImpl.ReportTypes.PDF
                , CommandExecutorProxyImpl.ReportName.ORDER)) {
            System.out.println("command approved.");
            mObservableDelInvoice.onNext(invx);
        }
    }

    @NonNull
    private BehaviorSubject<Invoice> mObservableDelInvoice = BehaviorSubject.create();

    @NonNull
    public Observable<List<Invoice>> getObservableDeleteInvoice() {

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
        String encrypted2=encrypted;

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String dodx = mSharedPreferences.getString("odbuce", "");
        String umex = mSharedPreferences.getString("ume", "");
        String serverx = mSharedPreferences.getString("servername", "");
        String ustp = mSharedPreferences.getString("ustype", "");

        return mObservableDelInvoice
                .observeOn(mSchedulerProvider.computation())
                .flatMap(invx ->
                        mDataModel.getInvoicesFromMysqlServer(serverx, encrypted2, ds, firx, rokx, "4", dodx, umex, invx.getDok(), ustp));
    }

    public void clearObservableDeleteInvoice() {

        mObservableDelInvoice = BehaviorSubject.create();

    }
    //end delete Invoice


    //Order to Invoice
    public void emitOrderToInv(Invoice invx) {
        if (callCommandExecutorProxy(CommandExecutorProxyImpl.PermType.ADM, CommandExecutorProxyImpl.ReportTypes.PDF
                , CommandExecutorProxyImpl.ReportName.ORDER)) {
            System.out.println("command approved.");
            mObservableOrderToInv.onNext(invx);
        }
    }

    @NonNull
    private BehaviorSubject<Invoice> mObservableOrderToInv = BehaviorSubject.create();

    @NonNull
    public Observable<InvoiceList> getObservableOrderToInv() {

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
        String encrypted2=encrypted;

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String dodx = mSharedPreferences.getString("odbuce", "");
        String umex = mSharedPreferences.getString("ume", "");
        String serverx = mSharedPreferences.getString("servername", "");
        String ustp = mSharedPreferences.getString("ustype", "");

        return mObservableOrderToInv
                .observeOn(mSchedulerProvider.computation())
                .flatMap(invx ->
                        mDataModel.getOrdersFromMysqlServer(serverx, encrypted2, ds, firx, rokx, "6", dodx, umex, invx.getDok(), ustp));
    }

    public void clearObservableOrderToInv() {

        mObservableOrderToInv = BehaviorSubject.create();

    }
    //end Order to Invoice

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
                ", \"prm2\":" + "\"" + invx.getPrm2() + "\"" +
                ", \"desc\":" + "\"" + EncodeDesc(invx.getDesc()) + "\"" +
                " }";

        return jsonstring;
    }
    //end JSON from Product

    public String EncodeDesc(String desc) {

        String encode = desc.replace("\n", "$$$$");
        encode = encode.replace("\r", "%%%%");

        return encode;
    }

    //JSON from Invoice
    public String JsonFromInvoice(Invoice invx) {

        String jsonstring = "{" +
                "  \"drh\":" + "\"" + invx.getDrh() + "\"" +
                ", \"dok\":" + "\"" + invx.getDok() + "\"" +
                ", \"ico\":" + "\"" + invx.getIco() + "\"" +
                ", \"nai\":" + "\"" + invx.getNai() + "\"" +
                ", \"zk0\":" + "\"" + invx.getZk0() + "\"" +
                ", \"zk1\":" + "\"" + invx.getZk1() + "\"" +
                ", \"zk2\":" + "\"" + invx.getZk2() + "\"" +
                ", \"dn1\":" + "\"" + invx.getDn1() + "\"" +
                ", \"dn2\":" + "\"" + invx.getDn2() + "\"" +
                " }";

        return jsonstring;
    }
    //end JSON from Invoice

    //get PDF invoice
    public void emitGetPdfInvoice(Invoice order) {

        if(callCommandExecutorProxy(CommandExecutorProxyImpl.PermType.LGN, CommandExecutorProxyImpl.ReportTypes.PDF
                , CommandExecutorProxyImpl.ReportName.INVOICE)){
            System.out.println("command approved.");

            order.setDrh("54");

            emitDocumentPdfUri(order);
        }

    }

    //get PDF order
    public void emitGetPdfOrder(Invoice order) {

        if(callCommandExecutorProxy(CommandExecutorProxyImpl.PermType.LGN, CommandExecutorProxyImpl.ReportTypes.PDF
                , CommandExecutorProxyImpl.ReportName.ORDER)){
            System.out.println("command approved.");

            order.setDrh("53");

            emitDocumentPdfUri(order);
        }

    }
    //end get PDF order

    //get PDF Uri document
    public void emitDocumentPdfUri(Invoice invx) { mObservableDocPDF.onNext(invx); }

    @NonNull
    private BehaviorSubject<Invoice> mObservableDocPDF = BehaviorSubject.create();

    @NonNull
    public Observable<Uri> getObservableDocPdf() {

        String firx = mSharedPreferences.getString("fir", "");
        //String rokx = "2014";
        String rokx = mSharedPreferences.getString("rok", "");
        //String serverx = "www.eshoptest.sk";
        String serverx = mSharedPreferences.getString("servername", "");
        //String adresx = "www.eshoptest.sk/androiducto";
        String adresx = mSharedPreferences.getString("servername", "") + "/androiducto";

        String usuidx = mSharedPreferences.getString("usuid", "");

        String umex = mSharedPreferences.getString("ume", "");

        Random r = new Random();
        double d = -10.0 + r.nextDouble() * 20.0;
        String ds = String.valueOf(d);

        String userx = "Nick/test2345" + "/ID/1001" + "/PSW/cp41cs" + "/Doklad/" + ds;

        String userxplus = userx + "/" + usuidx;
        System.out.println("DocPdf userxplus " + userxplus);

        MCrypt mcrypt = new MCrypt();
        String encrypted = "";
        try {
            encrypted = mcrypt.bytesToHex(mcrypt.encrypt(userxplus));
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        String encrypted2 = encrypted;

        return mObservableDocPDF
                .observeOn(mSchedulerProvider.ui())
                .flatMap(invx ->
                        mDataModel.getObservableUriDocPdf(invx, firx, rokx, serverx, adresx, encrypted2, umex));
    }

    public void clearObservableDocPDF() {

        mObservableDocPDF = BehaviorSubject.create();

    }
    //end get PDF Uri document

    public boolean callCommandExecutorProxy(CommandExecutorProxyImpl.PermType perm , CommandExecutorProxyImpl.ReportTypes reportType
            , CommandExecutorProxyImpl.ReportName tableName) {

        boolean approved = false;

        CommandExecutorProxy executor = new CommandExecutorProxyImpl();

        executor.setUserParams(mSharedPreferences.getString("usuid", "0")
                , mSharedPreferences.getString("fir", "0"), mSharedPreferences.getString("usadmin", "0")
                , mSharedPreferences.getString("ustype", "0"));
        try {
            approved = executor.approveCommand(perm, reportType, tableName);
        } catch (Exception e ) {
            Log.d("Exc Message approved:", e.getMessage());
            if(e.getMessage().equals("ADM")) {
                emitProxyException("ADM");
                System.out.println("'" + perm + "' command not approved.");
            }
            if(e.getMessage().equals("LGN")) {
                emitProxyException("LGN");
                System.out.println("'" + perm + "' command not approved.");
            }
            if(e.getMessage().equals("CMP")) {
                emitProxyException("CMP");
                System.out.println("'" + perm + "' command not approved.");
            }
        }

        return approved;
    }

    //get exception
    public void emitProxyException(String excp) { mObservableException.onNext(excp); }

    @NonNull
    private BehaviorSubject<String> mObservableException = BehaviorSubject.create();

    @NonNull
    public Observable<String> getObservableException() {


        return mObservableException
                .flatMap(excp ->
                        mDataModel.getObservableExcp(excp));
    }

    public void clearObservableException() {

        mObservableException = BehaviorSubject.create();

    }
    //end get exception


    //emit Observable<IdCompanyKt> control IdCompany
    public void emitMyObservableIdModelCompany(String queryx) {
        //String querys = String.valueOf(queryx);
        mObservableIdModelCompany.onNext(queryx);
    }

    @NonNull
    private BehaviorSubject<String> mObservableIdModelCompany = BehaviorSubject.create();

    @NonNull
    public Observable<List<IdCompanyKt>> getMyObservableIdModelCompany() {

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

        String encrypted2 = encrypted;

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String drh = "2";

        Log.d("NewCashLog idc fir ", firx);
        String serverx = mSharedPreferences.getString("servername", "");

        return mObservableIdModelCompany
                .observeOn(mSchedulerProvider.computation())
                .flatMap(queryx -> mDataModel.getObservableIdModelCompany(serverx, encrypted2, ds, firx, rokx, drh, queryx ));
    }

    public void clearObservableIdModelCompany() {

        mObservableIdModelCompany = BehaviorSubject.create();

    }
    //end emit Observable<IdCompanyKt> control IdCompany

    //save idc to realm
    public void emitRealmIdcToRealm(List<RealmInvoice> invoice) {
        mIdcSaveToRealm.onNext(invoice);
    }

    @NonNull
    private BehaviorSubject<List<RealmInvoice>> mIdcSaveToRealm = BehaviorSubject.create();

    @NonNull
    public Observable<RealmInvoice> getDataIdcSavedToRealm() {
        return mIdcSaveToRealm
                .observeOn(mSchedulerProvider.ui())
                .flatMap(list -> mDataModel.getIdcSavingToRealm(list));
    }

    public void clearObservableIdcSaveToRealm() {

        mIdcSaveToRealm = BehaviorSubject.create();

    }
    //end save idc to realm

    //get saved idc from realm
    public Observable<List<RealmInvoice>> getNoSavedDocFromRealm(String fromact) {

        return mDataModel.getObservableNosavedDocRealm(fromact);
    }
    //end get saved idc from realm

    //get saved idc from realm
    public Observable<List<RealmInvoice>> getMyIdcData(String fromact) {

        return mDataModel.getObservableMyIdc(fromact);
    }
    //end get saved idc from realm

    //methods for RoomDemocActivity

    //get products
    public Flowable<List<Product>> loadProducts() {

        return mDataModel.loadProductsData();

    }

    /**
     * Insert product.
     * @param prodName the new product name
     * @return a {@link Completable} that completes when the user name is updated
     */
    public Completable updateProductName(final String prodName) {
        return Completable.fromAction(() -> {

            mDataModel.insertOrUpdateProductData(prodName);
        });
    }

    /**
     * Delete product by Uid.
     * @param prodId the uid of deleted product
     * @return a {@link Completable} that completes when the user name is updated
     */
    public Completable deleteRxProductById(final int prodId) {
        return Completable.fromAction(() -> {

            mDataModel.deleteRxProductByIdData(prodId);
        });
    }

    //end methods for RoomDemocActivity


    //methods for SetImageActivity

    //upload image to server
    public void emitUploadImageToServer(ProductKt prodx) {

        mObservableUploadImageToServer.onNext(prodx);
    }

    @NonNull
    private BehaviorSubject<ProductKt> mObservableUploadImageToServer = BehaviorSubject.create();

    @NonNull
    public Observable<SetImageServerResponse> getUploadImageToServer() {

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
        String encrypted3=encrypted;

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String dodx = "1";
        String umex = "";
        String serverx = mSharedPreferences.getString("servername", "");

        Map<String, RequestBody> params = new HashMap<>();
        params.put("title1", RequestBody.create(MediaType.parse("text/plain"), "1"));
        params.put("title2", RequestBody.create(MediaType.parse("text/plain"), "2"));
        params.put("title3", RequestBody.create(MediaType.parse("text/plain"), "3"));

        return mObservableUploadImageToServer
                .observeOn(mSchedulerProvider.computation())
                .flatMap(prodx -> mDataModel.uploadImageToServer(serverx, getFileToUpload(prodx.getPrm1()), getDesc(prodx.getCis())));
                //to solve php .flatMap(mediaPath -> mDataModel.uploadImageWithMapToServer(serverx, getFileToUpload(mediaPath), params));
    }

    private MultipartBody.Part getFileToUpload(String mediaPath) {
        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(mediaPath);

        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

        return fileToUpload;
    }


    private RequestBody getDesc(String cis) {

        RequestBody desc = RequestBody.create(MediaType.parse("text/plain"), cis);

        return desc;
    }

    public void clearUploadImageToServe() {

        mObservableUploadImageToServer = BehaviorSubject.create();

    }
    //end upload image to server


    //save ean to server
    public void emitSaveEanToServer(String eancis) { mObservableSaveEanToServer.onNext(eancis); }

    @NonNull
    private BehaviorSubject<String> mObservableSaveEanToServer = BehaviorSubject.create();

    @NonNull
    public Observable<List<ProductKt>> getObservableSaveEanToServer() {

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
        String encrypted3=encrypted;

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String dodx = "1";
        String umex = "";
        String serverx = mSharedPreferences.getString("servername", "");

        return mObservableSaveEanToServer
                .observeOn(mSchedulerProvider.computation())
                .flatMap(drhx -> mDataModel.getProductsFromMysqlServer(serverx, encrypted3, ds, firx, rokx, drhx, dodx, umex, "2"));
    }

    public void clearSaveEanToServer() {

        mObservableSaveEanToServer = BehaviorSubject.create();

    }
    //end save ean to server

    //save item to server
    public void emitSaveItemToServer(ProductKt prod) {

        mObservableSaveItemToServer.onNext(prod);
    }

    @NonNull
    private BehaviorSubject<ProductKt> mObservableSaveItemToServer = BehaviorSubject.create();

    @NonNull
    public Observable<List<ProductKt>> getObservableSaveItemToServer() {

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
        String encrypted3=encrypted;

        String firx = mSharedPreferences.getString("fir", "");
        String rokx = mSharedPreferences.getString("rok", "");
        String dodx = "1";
        String umex = "";
        String serverx = mSharedPreferences.getString("servername", "");

        return mObservableSaveItemToServer
                .observeOn(mSchedulerProvider.computation())
                .flatMap(prodx -> mDataModel.getProductsFromMysqlServer(serverx, encrypted3, ds, firx, rokx, JsonFromProduct(prodx), dodx, umex, "3"));
    }

    public void clearSaveItemToServer() {

        mObservableSaveItemToServer = BehaviorSubject.create();

    }
    //end save item to server

    //end methods for SetImageActivity


    //test SOAP hello
    //get Hello from SOAP
    public void emitSoapHello(Invoice order) {
        if (callCommandExecutorProxy(CommandExecutorProxyImpl.PermType.ADM, CommandExecutorProxyImpl.ReportTypes.PDF
                , CommandExecutorProxyImpl.ReportName.ORDER)) {
            System.out.println("command approved.");

            //ISoapRequestFactory isoapfactory = new SoapRequestFactory();
            HelloRequestEnvelope requestEnvelop = mSoapRequestFactory.getHelloRequestEnvelop(order);

            mObservableSoapResponse.onNext(requestEnvelop);
        }
    }

    @NonNull
    private BehaviorSubject<HelloRequestEnvelope> mObservableSoapResponse = BehaviorSubject.create();

    @NonNull
    public Observable<HelloResponseEnvelope> getObservableHelloSoapResponse() {

        return mObservableSoapResponse
                .observeOn(mSchedulerProvider.computation())
                .flatMap(envelop ->
                        mDataModel.getHelloSoapResponse(envelop));
    }

    public void clearObservableSoapResponse() {

        mObservableSoapResponse = BehaviorSubject.create();

    }
    //end get Hello from SOAP
    //end test SOAP hello


}
