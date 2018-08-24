package com.eusecom.samshopersung;

import android.net.Uri;
import android.support.annotation.NonNull;
import com.eusecom.samshopersung.models.Album;
import com.eusecom.samshopersung.models.Employee;
import com.eusecom.samshopersung.models.InvoiceList;
import com.eusecom.samshopersung.realm.RealmDomain;
import com.eusecom.samshopersung.realm.RealmInvoice;

import java.util.List;
import rx.Observable;

public interface ShopperIMvvmViewModel {

    //methods for ChooseCompanyActivity
    @NonNull
    public Observable<List<CompanyKt>> getMyCompaniesFromServer();

    @NonNull
    public void emitSaveDomainToRealm(RealmDomain domx);

    @NonNull
    public Observable<RealmDomain> getMyObservableSaveDomainToRealm();

    public void clearObservableSaveDomainToRealm();

    //methods for OfferKtActivity
    @NonNull
    public Observable<List<Album>> getMyObservableAlbumsFromList();

    @NonNull
    public Observable<List<ProductKt>> getMyProductsFromSqlServer(String drh);

    @NonNull
    public Observable<List<CategoryKt>> getMyCatsFromSqlServer(String drh);

    @NonNull
    public void emitMyObservableSaveBasketToServer(ProductKt invx);

    @NonNull
    public Observable<List<BasketKt>> getMyObservableSaveBasketToServer();

    @NonNull
    public void clearMyObservableSaveBasketToServer();

    @NonNull
    public void emitMyObservableSaveSumBasketToServer(ProductKt invx);

    @NonNull
    public Observable<SumBasketKt> getMyObservableSaveSumBasketToServer();

    @NonNull
    public void clearMyObservableSaveSumBasketToServer();

    @NonNull
    public void emitMyCatProductsFromSqlServer(String cat);

    @NonNull
    public Observable<List<ProductKt>> getMyCatProductsFromSqlServer();

    @NonNull
    public void clearMyCatProductsFromSqlServe();

    @NonNull
    public List<ProductKt> getQueryListProduct(String query);

    @NonNull
    public void emitMyQueryProductsFromSqlServer(String query);

    @NonNull
    public Observable<List<ProductKt>> getMyQueryProductsFromSqlServer();

    @NonNull
    public void clearMyQueryProductsFromSqlServe();

    //methods for BasketKtActivity
    @NonNull
    public Observable<List<BasketKt>> getMyBasketFromSqlServer();

    @NonNull
    public Observable<SumBasketKt> getMySumBasketFromSqlServer();

    //methods for OrderListActivity
    @NonNull
    public Observable<InvoiceList> getMyOrdersFromSqlServer(String drh);

    @NonNull
    public void emitGetPdfOrder(Invoice order);

    @NonNull
    public void emitDocumentPdfUri(Invoice invx);

    @NonNull
    public Observable<Uri> getObservableDocPdf();

    @NonNull
    public void clearObservableDocPDF();

    @NonNull
    public Observable<String> getObservableException();

    @NonNull
    public void clearObservableException();

    @NonNull
    public void emitDeleteOrder(Invoice invx);

    @NonNull
    public Observable<InvoiceList> getObservableDeleteOrder();

    @NonNull
    public void clearObservableDeleteOrder();

    //methods for MapActivity
    @NonNull
    public Observable<List<Employee>> getMyObservableEmployeesFromList();

    //methods for FlombulatorActivity
    @NonNull
    public String getStringFromMvvm();

    @NonNull
    public Observable<List<String>> getRxStringFromMvvm();

    //methods for NewIdcActivity
    @NonNull
    public void emitMyObservableIdModelCompany(String queryx);

    @NonNull
    public Observable<List<IdCompanyKt>> getMyObservableIdModelCompany();

    @NonNull
    public void clearObservableIdModelCompany();

    @NonNull
    public void emitRealmIdcToRealm(List<RealmInvoice> invoice);

    @NonNull
    public Observable<RealmInvoice> getDataIdcSavedToRealm();

    @NonNull
    public void clearObservableIdcSaveToRealm();

    @NonNull
    public Observable<List<RealmInvoice>> getNoSavedDocFromRealm(String fromact);

    @NonNull
    public Observable<List<RealmInvoice>> getMyIdcData(String fromact);


}
