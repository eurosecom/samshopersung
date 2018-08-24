package com.eusecom.samshopersung.mvvmdatamodel;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.eusecom.samshopersung.BasketKt;
import com.eusecom.samshopersung.CategoryKt;
import com.eusecom.samshopersung.CompanyKt;
import com.eusecom.samshopersung.IdCompanyKt;
import com.eusecom.samshopersung.Invoice;
import com.eusecom.samshopersung.ProductKt;
import com.eusecom.samshopersung.SumBasketKt;
import com.eusecom.samshopersung.models.Album;
import com.eusecom.samshopersung.models.Employee;
import com.eusecom.samshopersung.models.InvoiceList;
import com.eusecom.samshopersung.realm.RealmDomain;
import com.eusecom.samshopersung.realm.RealmInvoice;

import java.util.List;
import rx.Observable;

public interface ShopperIDataModel {

    //methods for ChooseCompanyActivity
    @NonNull
    public Observable<List<CompanyKt>> getCompaniesFromMysqlServer(String servername, String userhash, String userid);

    @NonNull
    public Observable<RealmDomain> saveDomainToRealm(RealmDomain domx);

    //methods for DomainsActivity
    @NonNull
    public Observable<List<RealmDomain>> getDomainsFromRealm();

    //methods for OfferKtActivity
    @NonNull
    public Observable<List<Album>> prepareAlbumsList();

    @NonNull
    public Observable<List<ProductKt>> getProductsFromMysqlServer(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String ucex, String umex, String dokx);

    @NonNull
    public Observable<List<CategoryKt>> getCatFromMysqlServer(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String ucex, String umex, String dokx);

    //methods for BasketKtActivity
    @NonNull
    public Observable<List<BasketKt>> getBasketFromMysqlServer(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String ucex, String prodx, String dokx);

    @NonNull
    public Observable<SumBasketKt> getSumBasketFromMysqlServer(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String ucex, String prodx, String dokx);

    //methods for OrderListActivity
    @NonNull
    public Observable<InvoiceList> getOrdersFromMysqlServer(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String ucex, String umex, String dokx);

    //methods for MapActivity
    public Observable<List<Employee>> prepareEmployeeList();

    //methods for FlombulatorActivity
    public String getStringFromDataModel();

    public Observable<List<String>> getRxStringFromDataModel();

    @NonNull
    public Observable<Uri> getObservableUriDocPdf(Invoice invx, @NonNull final String firx
            , @NonNull final String rokx, @NonNull final String serverx, @NonNull final String adresx
            , String encrypted, @NonNull final String umex);

    @NonNull
    public Observable<String> getObservableExcp(String excp);

    //methods for NewIdcActivity
    @NonNull
    public Observable<List<IdCompanyKt>> getObservableIdModelCompany(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String queryx);

    @NonNull
    public Observable<RealmInvoice> getIdcSavingToRealm(@NonNull final List<RealmInvoice> invoices);

    @NonNull
    public Observable<List<RealmInvoice>> getObservableNosavedDocRealm(String fromact);

    @NonNull
    public Observable<List<RealmInvoice>> getObservableMyIdc(String fromact);

}
