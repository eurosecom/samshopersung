package com.eusecom.samshopersung.mvvmdatamodel;

import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import io.reactivex.Flowable;
import io.realm.Realm;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import com.eusecom.samshopersung.BasketKt;
import com.eusecom.samshopersung.CategoryKt;
import com.eusecom.samshopersung.CompanyKt;
import com.eusecom.samshopersung.IdCompanyKt;
import com.eusecom.samshopersung.Invoice;
import com.eusecom.samshopersung.ProductKt;
import com.eusecom.samshopersung.R;
import com.eusecom.samshopersung.SetImageServerResponse;
import com.eusecom.samshopersung.SumBasketKt;
import com.eusecom.samshopersung.models.Album;
import com.eusecom.samshopersung.models.EkassaRequestBackup;
import com.eusecom.samshopersung.models.Employee;
import com.eusecom.samshopersung.models.IShopperModelsFactory;
import com.eusecom.samshopersung.models.InvoiceList;
import com.eusecom.samshopersung.models.Product;
import com.eusecom.samshopersung.realm.IRealmController;
import com.eusecom.samshopersung.realm.IdcController;
import com.eusecom.samshopersung.realm.RealmDomain;
import com.eusecom.samshopersung.realm.RealmInvoice;
import com.eusecom.samshopersung.retrofit.ExampleInterceptor;
import com.eusecom.samshopersung.retrofit.ShopperRetrofitService;
import com.eusecom.samshopersung.retrofit.ShopperXmlRetrofitService;
import com.eusecom.samshopersung.roomdatabase.MyDatabase;
import com.eusecom.samshopersung.soap.soapekassa.EkassaRegisterReceiptResponseEnvelope;
import com.eusecom.samshopersung.soap.soapekassa.EkassaRequestEnvelope;
import com.eusecom.samshopersung.soap.soapekassa.EkassaResponseEnvelope;
import com.eusecom.samshopersung.soap.soaphello.HelloRequestEnvelope;
import com.eusecom.samshopersung.soap.soaphello.HelloResponseEnvelope;
import com.google.firebase.database.DatabaseReference;

public class ShopperDataModel implements ShopperIDataModel {

    DatabaseReference mFirebaseDatabase;
    ShopperRetrofitService mShopperRetrofitService;
    ShopperXmlRetrofitService mShopperXmlRetrofitService;
    Resources mResources;
    Realm mRealm;
    ExampleInterceptor mInterceptor;
    IRealmController mRealmController;
    IdcController mIdcController;
    MyDatabase mRoomDatabase;
    IShopperModelsFactory mModelsFactory;

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

    public ShopperDataModel(@NonNull final DatabaseReference databaseReference,
                             @NonNull final ShopperRetrofitService shopperRetrofitService,
                             @NonNull final Resources resources,
                             @NonNull final Realm realm,
                             @NonNull final ExampleInterceptor interceptor,
                             @NonNull final IdcController realmcontroller) {
        mFirebaseDatabase = databaseReference;
        mShopperRetrofitService = shopperRetrofitService;
        mResources = resources;
        mRealm = realm;
        mInterceptor = interceptor;
        mIdcController = realmcontroller;
    }

    public ShopperDataModel(@NonNull final ShopperRetrofitService shopperRetrofitService,
                            @NonNull final ExampleInterceptor interceptor,
                            @NonNull MyDatabase roomDatabase,
                            @NonNull IShopperModelsFactory modelsFactory) {
        mShopperRetrofitService = shopperRetrofitService;
        mInterceptor = interceptor;
        mRoomDatabase = roomDatabase;
        mModelsFactory = modelsFactory;
    }

    public ShopperDataModel(@NonNull final ShopperRetrofitService shopperRetrofitService,
                            @NonNull final ExampleInterceptor interceptor,
                            @NonNull MyDatabase roomDatabase,
                            @NonNull final ShopperXmlRetrofitService shopperXmlRetrofitService,
                            @NonNull IShopperModelsFactory modelsFactory) {
        mShopperRetrofitService = shopperRetrofitService;
        mInterceptor = interceptor;
        mRoomDatabase = roomDatabase;
        mShopperXmlRetrofitService = shopperXmlRetrofitService;
        mModelsFactory = modelsFactory;
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
        Log.d("GenDoc prodx", prodx);

        setRetrofit(servername);
        return mShopperRetrofitService.getSumBasketFromSqlServer(userhash, userid, fromfir, vyb_rok, drh, ucex, prodx, dokx);

    }


    //end methods for BasketKtActivity


    //methods for OrderListActivity

    @Override
    public Observable<InvoiceList> getOrdersFromMysqlServer(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String ucex, String umex, String dokx, String ustp) {

        System.out.println("order dokx " + dokx);
        System.out.println("order drh " + drh);
        System.out.println("order uce " + ucex);
        System.out.println("order ustp " + ustp);
        System.out.println("order umex " + umex);
        setRetrofit(servername);
        return mShopperRetrofitService.getOrdersFromSqlServer(userhash, userid, fromfir, vyb_rok, drh, ucex, umex, dokx, ustp);

    }


    @Override
    public Observable<List<Invoice>> getInvoicesFromMysqlServer(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String ucex, String umex, String dokx, String ustp) {
        Log.d("GenDoc dokx", dokx);
        Log.d("GenDoc drh", drh);
        Log.d("GenDoc ucex", ucex);
        Log.d("GenDoc ustp", ustp);

        setRetrofit(servername);
        return mShopperRetrofitService.getInvoicesFromSqlServer(userhash, userid, fromfir, vyb_rok, drh, ucex, umex, dokx, ustp);

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

    @NonNull
    @Override
    public Observable<Uri> getObservableUriDocPdf(Invoice invx, @NonNull final String firx
            , @NonNull final String rokx, @NonNull final String serverx, @NonNull final String adresx
            , String encrypted, @NonNull final String umex) {


        Log.d("DocPdf dokx ", invx.getDok());
        Log.d("DocPdf drhx ", invx.getDrh());
        Log.d("DocPdf ucex ", invx.getUce());
        Log.d("DocPdf icox ", invx.getIco());
        System.out.println("DocPdf userhash " + encrypted);

        Uri uri = null;
        if (invx.getDrh().equals("53")) {
            String drupoh = "1";

            uri = Uri.parse("http://" + serverx +
                    "/androidshopper/order_pdf.php?copern=1&drupoh="+ drupoh + "&page=1&zandroidu=1&anduct=1&kli_vume=1.2018"
                    + "&serverx=" + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx
                    + "&newfntz=1&h_drp=4&cislo_dok=" + invx.getDok() );

        }

        if (invx.getDrh().equals("54")) {
            String drupoh = "1";

            uri = Uri.parse("http://" + serverx +
                    "/faktury/vstf_pdf.php?cislo_dok=" + invx.getDok() + "&hladaj_dok=" + invx.getDok()
                    + "&mini=0&tlacitR=1&sysx=UCT&rozuct=NIE&zandroidu=1&anduct=1&h_razitko=1&copern=20&drupoh=1&page=1&serverx="
                    + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx + "&newfntz=1");

        }

        return Observable.just(uri);

    }

    @NonNull
    public Observable<String> getObservableExcp(String excp) {

        return Observable.just(excp);
    }

    //methods for NewIdcActivity
    @NonNull
    public Observable<List<IdCompanyKt>> getObservableIdModelCompany(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String queryx){

        setRetrofit(servername);
        return mShopperRetrofitService.controlIdCompanyOnSqlServer(userhash, userid, fromfir, vyb_rok, drh, queryx);
    }

    //save idc to realm
    @NonNull
    @Override
    public Observable<RealmInvoice> getIdcSavingToRealm(@NonNull final List<RealmInvoice> idcs) {

        RealmInvoice idcx = idcs.get(0);
        RealmInvoice idcexists = mIdcController.existRealmInvoice( idcx );

        if(idcexists != null){
            System.out.println("existRealmInvoice " + true);
            mIdcController.deleteRealmInvoiceData( idcx );
        }else{
            System.out.println("existRealmInvoice " + false);
        }
        //save to realm and get String OK or ERROR
        mIdcController.setRealmInvoiceData( idcx );

        return Observable.just(idcx);
    }
    //end save idc to realm

    //get my idc from realm
    @NonNull
    @Override
    public Observable<List<RealmInvoice>> getObservableNosavedDocRealm(String fromact) {

        return Observable.just(mIdcController.getMyIdcData(fromact));
    }
    //end get my idc from realm

    //get my idc from realm in basket
    @NonNull
    @Override
    public Observable<List<RealmInvoice>> getObservableMyIdc(String fromact) {

        return Observable.just(mRealmController.getMyIdcData(fromact));
    }
    //end get my idc from realm in basket

    //methods for RoomDemocActivity

    @Override
    public Flowable<List<Product>> loadProductsData() {

        return mRoomDatabase.productDao().getRxAll();

    }

    private Product mProduct;

    @Override
    public void insertOrUpdateProductData(String prodName) {

        mProduct = mModelsFactory.getProductModel();
        mProduct.setName(prodName);
        mProduct.setImageUrl("https://picsum.photos/500/500?image=14");
        mProduct.setPrice(1300);

        mRoomDatabase.productDao().insertProduct(mProduct);
    }

    @Override
    public void deleteRxProductByIdData(int prodId) {

        mRoomDatabase.productDao().deleteByUid(prodId);

    }

    //methods for SetImageActivity
    @NonNull
    public Observable<SetImageServerResponse> uploadImageToServer(String servername, MultipartBody.Part file, RequestBody desc){

        setRetrofit(servername);
        return mShopperRetrofitService.uploadImageFile(file, desc);
    }

    @NonNull
    public Observable<SetImageServerResponse> uploadImageWithMapToServer(String servername, MultipartBody.Part file
            , Map<String, RequestBody> partMap){

        setRetrofit(servername);
        return mShopperRetrofitService.uploadImageFileWithMap(file, partMap);
    }

    //test soap hello
    @NonNull
    public Observable<HelloResponseEnvelope> getEkassaSoapResponse(HelloRequestEnvelope requestEnvelope){

        setRetrofit("www.wsdl2code.com/");
        return mShopperXmlRetrofitService.getEkassaFromSoap(requestEnvelope);
    }

    @NonNull
    public Observable<EkassaResponseEnvelope> getEkassaRegisterReceiptXmlResponse(EkassaRequestEnvelope requestEnvelope){

        setRetrofit("www.eshoptest.sk/");
        return mShopperXmlRetrofitService.getRegisterReceiptEkassa(requestEnvelope);
    }

    @NonNull
    public Observable<EkassaRegisterReceiptResponseEnvelope> getEkassaRegisterReceiptResponse(EkassaRequestEnvelope requestEnvelope){

        setRetrofit("www.eshoptest.sk/");
        return mShopperXmlRetrofitService.getRegisterReceiptEkassaFromSoap(requestEnvelope);
    }

    //test method soap generic
    //try to create generic retrofit interface
    //it does not work by exception Error Throwable Parameter type must not include a type variable or wildcard
    public <EnvelopeType> Observable<HelloResponseEnvelope> getSoapResponse(EnvelopeType envelope){

        setRetrofit("www.wsdl2code.com/");
        return mShopperXmlRetrofitService.getResponseFromSoap(envelope);
    }

    //test class soap generic
    public class SoapEnvelopeWrapper<EnvelopeType> {

        private EnvelopeType envelope;

        public void print() {
            Log.d("EnvelopeString", envelope.toString());
        }

        public SoapEnvelopeWrapper() {
        }

        public SoapEnvelopeWrapper(EnvelopeType envelope) {
            this.envelope = envelope;
        }

        public void setEnvelope(EnvelopeType envelope) {
            this.envelope = envelope;
        }


    }

    //methods for OrpListKtActivity
    @Override
    public Flowable<List<EkassaRequestBackup>> loadEkasaRequestsData() {

        return mRoomDatabase.ekassaRequestBackupDao().getRxAllRequest();

    }
    private EkassaRequestBackup ekassareq;

    @Override
    public void insertOrUpdateEkassaReqData(String uuid, String daterequest, String count
            , String receipt, String pkpstring) {

        ekassareq = mModelsFactory.getEkassaRequestBackup();
        ekassareq.setRequestUuid(uuid);
        ekassareq.setRequestDate(daterequest);
        ekassareq.setSendingCount(Integer.valueOf(count));
        ekassareq.setReceiptNumber(receipt);
        ekassareq.setRequestStr(pkpstring);
        ekassareq.setResponseUuid("");
        ekassareq.setReceiptDataId("");
        Log.d("dsave requuid", uuid);

        mRoomDatabase.ekassaRequestBackupDao().insertEkassaRequestBackup(ekassareq);
    }

    @Override
    public void insertOrUpdateEkassaResponseData(String reqUuid, String resUuid, String procDate
            , String recid) {

        ekassareq = mModelsFactory.getEkassaRequestBackup();
        ekassareq.setRequestUuid(reqUuid);
        ekassareq.setResponseUuid(resUuid);
        ekassareq.setProcessDate(procDate);
        ekassareq.setReceiptDataId(recid);
        Log.d("dsave requuid", reqUuid);
        Log.d("dsave resuuid", resUuid);
        Log.d("dsave procDate", procDate);
        Log.d("dsave recid", recid);


        //insert changes all column, i want to change onlz response i have to use update query
        //mRoomDatabase.ekassaRequestBackupDao().insertEkassaRequestBackup(ekassareq);
        mRoomDatabase.ekassaRequestBackupDao().updateEkassaRequestBackup(reqUuid, resUuid, procDate, recid);
    }

    @Override
    public void insertOrUpdateMaxIdEkassaResponseData(String resUuid, String procDate
            , String recid) {


        mRoomDatabase.ekassaRequestBackupDao().updateMaxIdEkassaRequestBackup(resUuid, procDate, recid);
    }

    @Override
    public void deleteRxEkassaReqByIdData(int reqId) {

        mRoomDatabase.ekassaRequestBackupDao().deleteByUid(reqId);

    }

}
