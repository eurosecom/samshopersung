package com.eusecom.samshopersung.mvvmdatamodel;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.eusecom.samshopersung.BasketKt;
import com.eusecom.samshopersung.CategoryKt;
import com.eusecom.samshopersung.CompanyKt;
import com.eusecom.samshopersung.IdCompanyKt;
import com.eusecom.samshopersung.Invoice;
import com.eusecom.samshopersung.ProductKt;
import com.eusecom.samshopersung.SetImageServerResponse;
import com.eusecom.samshopersung.SumBasketKt;
import com.eusecom.samshopersung.models.Album;
import com.eusecom.samshopersung.models.EkassaRequestBackup;
import com.eusecom.samshopersung.models.Employee;
import com.eusecom.samshopersung.models.InvoiceList;
import com.eusecom.samshopersung.models.Product;
import com.eusecom.samshopersung.realm.RealmDomain;
import com.eusecom.samshopersung.realm.RealmInvoice;
import com.eusecom.samshopersung.soap.soapekassa.EkassaRegisterReceiptResponseEnvelope;
import com.eusecom.samshopersung.soap.soapekassa.EkassaRequestEnvelope;
import com.eusecom.samshopersung.soap.soapekassa.EkassaResponseEnvelope;
import com.eusecom.samshopersung.soap.soaphello.HelloRequestEnvelope;
import com.eusecom.samshopersung.soap.soaphello.HelloResponseEnvelope;
import java.util.List;
import java.util.Map;
import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
            , String vyb_rok, String drh, String ucex, String umex, String dokx, String ustp);

    @NonNull
    public Observable<List<Invoice>> getInvoicesFromMysqlServer(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String ucex, String umex, String dokx, String ustp);

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


    //methods for RoomDemocActivity

    @NonNull
    public Flowable<List<Product>> loadProductsData();

    @NonNull
    public void insertOrUpdateProductData(String prodName);

    @NonNull
    public void deleteRxProductByIdData(int prodId);


    //methods for SetImageActivity

    @NonNull
    public Observable<SetImageServerResponse> uploadImageToServer(String servername, MultipartBody.Part file, RequestBody desc);

    @NonNull
    public Observable<SetImageServerResponse> uploadImageWithMapToServer(String servername, MultipartBody.Part file
            , Map<String, RequestBody> partMap);

    //soap
    @NonNull
    public Observable<HelloResponseEnvelope> getEkassaSoapResponse(HelloRequestEnvelope requestEnvelope);

    public Observable<EkassaResponseEnvelope> getEkassaRegisterReceiptXmlResponse(EkassaRequestEnvelope requestEnvelope);

    @NonNull
    public Observable<EkassaRegisterReceiptResponseEnvelope> getEkassaRegisterReceiptResponse(EkassaRequestEnvelope requestEnvelope);

    public <EnvelopeType> Observable<HelloResponseEnvelope> getSoapResponse(EnvelopeType envelope);

    //methods for OrpListKtActivity
    public Flowable<List<EkassaRequestBackup>> loadEkasaRequestsData();

    @NonNull
    public void insertOrUpdateEkassaReqData(String uuid, String daterequest, String count
            , String receipt, String pkpstring);

    @NonNull
    public void deleteRxEkassaReqByIdData(int reqId);

    @NonNull
    public void insertOrUpdateEkassaResponseData(String reqUuid, String resUuid, String procDate, String recid);

    @NonNull
    public void insertOrUpdateMaxIdEkassaResponseData(String resUuid, String procDate, String recid);

    @NonNull
    public Observable<Uri> getObservableUriEkasaPdf(Invoice invx, @NonNull final String firx
            , @NonNull final String rokx, @NonNull final String serverx, @NonNull final String adresx
            , String encrypted, @NonNull final String umex);

}
