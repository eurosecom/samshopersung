package com.eusecom.samshopersung;

import android.net.Uri;
import android.support.annotation.NonNull;
import com.eusecom.samshopersung.models.Album;
import com.eusecom.samshopersung.models.Employee;
import com.eusecom.samshopersung.models.InvoiceList;
import com.eusecom.samshopersung.models.Product;
import com.eusecom.samshopersung.proxy.CommandExecutorProxyImpl;
import com.eusecom.samshopersung.realm.RealmDomain;
import com.eusecom.samshopersung.realm.RealmInvoice;
import com.eusecom.samshopersung.soap.soapekassa.EkassaRegisterReceiptResponseEnvelope;
import com.eusecom.samshopersung.soap.soaphello.HelloRequestEnvelope;
import com.eusecom.samshopersung.soap.soaphello.HelloResponseEnvelope;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
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
    public Observable<List<Invoice>> getMyInvoicesFromSqlServer(String drh);

    @NonNull
    public void emitGetPdfOrder(Invoice order);

    @NonNull
    public void emitGetPdfInvoice(Invoice order);

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

    @NonNull
    public void emitDetailOrder(Invoice invx);

    @NonNull
    public Observable<InvoiceList> getObservableDetailOrder();

    @NonNull
    public void clearObservableDetailOrder();

    @NonNull
    public void emitSaveDetailOrder(Invoice invx);

    @NonNull
    public Observable<InvoiceList> getObservableSaveDetailOrder();

    @NonNull
    public void clearObservableSaveDetailOrder();

    @NonNull
    public void emitDeleteInvoice(Invoice invx);

    @NonNull
    public Observable<List<Invoice>> getObservableDeleteInvoice();

    @NonNull
    public void clearObservableDeleteInvoice();

    @NonNull
    public void emitOrderToInv(Invoice invx);

    @NonNull
    public Observable<InvoiceList> getObservableOrderToInv();

    @NonNull
    public void clearObservableOrderToInv();

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


    //methods for RoomDemocActivity

    @NonNull
    public Flowable<List<Product>> loadProducts();

    @NonNull
    public Completable updateProductName(final String prodName);

    @NonNull
    public Completable deleteRxProductById(final int prodId);


    //methods for SetImageActivity

    @NonNull
    public void emitUploadImageToServer(ProductKt prodx);

    @NonNull
    public Observable<SetImageServerResponse> getUploadImageToServer();

    @NonNull
    public void clearUploadImageToServe();

    @NonNull
    public void emitSaveEanToServer(String eancis);

    @NonNull
    public Observable<List<ProductKt>> getObservableSaveEanToServer();

    @NonNull
    public void clearSaveEanToServer();

    public boolean callCommandExecutorProxy(CommandExecutorProxyImpl.PermType perm , CommandExecutorProxyImpl.ReportTypes reportType
            , CommandExecutorProxyImpl.ReportName tableName);

    @NonNull
    public void emitSaveItemToServer(ProductKt prod);

    @NonNull
    public Observable<List<ProductKt>> getObservableSaveItemToServer();

    @NonNull
    public void clearSaveItemToServer();


    //test soap hello
    @NonNull
    public void emitSoapHello(Invoice order);

    @NonNull
    public Observable<HelloResponseEnvelope> getObservableHelloSoapResponse();

    @NonNull
    public void clearObservableSoapResponse();

    //soap Ekassa
    @NonNull
    public void emitSoapEkassa(Invoice order);

    @NonNull
    public Observable<HelloResponseEnvelope> getObservableSoapEkassaResponse();

    @NonNull
    public void clearObservableSoapEkassaResponse();

    @NonNull
    public void emitRegisterReceiptEkassa(Invoice order);

    @NonNull
    public Observable<EkassaRegisterReceiptResponseEnvelope> getObservableRegisterReceiptEkassaResponse();

    @NonNull
    public void clearObservableRegisterReceiptEkassaResponse();


}
