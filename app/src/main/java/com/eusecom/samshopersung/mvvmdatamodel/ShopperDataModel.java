/**
 * Copyright 2018 EuroSecon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * EXTERNAL COMPONENTS
 *
 * Apache PDFBox includes a number of components with separate copyright notices
 * and license terms. Your use of these components is subject to the terms and
 * conditions of the following licenses.
 *
 * Contributions made to the original PDFBox and FontBox projects:
 *
 * Copyright (c) 2002-2007, www.pdfbox.org
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of pdfbox; nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * Adobe Font Metrics (AFM) for PDF Core 14 Fonts
 *
 * This file and the 14 PostScript(R) AFM files it accompanies may be used,
 * copied, and distributed for any purpose and without charge, with or without
 * modification, provided that all copyright notices are retained; that the
 * AFM files are not distributed without this file; that all modifications
 * to this file or any of the AFM files are prominently noted in the modified
 * file(s); and that this paragraph is not modified. Adobe Systems has no
 * responsibility or obligation to support the use of the AFM files.
 *
 * CMaps for PDF Fonts (http://opensource.adobe.com/wiki/display/cmap/Downloads)
 *
 * Copyright 1990-2009 Adobe Systems Incorporated.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of Adobe Systems Incorporated nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 *
 * PaDaF PDF/A preflight (http://sourceforge.net/projects/padaf)
 *
 * Copyright 2010 Atos Worldline SAS
 *
 * Licensed by Atos Worldline SAS under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * Atos Worldline SAS licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eusecom.samshopersung.mvvmdatamodel;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import com.eusecom.samshopersung.models.EkassaSettings;
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
import com.eusecom.samshopersung.soap.EkasaPdfDoc;
import com.eusecom.samshopersung.soap.IEkasaPdfDoc;
import com.eusecom.samshopersung.soap.soapekassa.EkassaRegisterReceiptResponseEnvelope;
import com.eusecom.samshopersung.soap.soapekassa.EkassaRequestEnvelope;
import com.eusecom.samshopersung.soap.soapekassa.EkassaResponseEnvelope;
import com.eusecom.samshopersung.soap.soaphello.HelloRequestEnvelope;
import com.eusecom.samshopersung.soap.soaphello.HelloResponseEnvelope;
import com.google.firebase.database.DatabaseReference;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream;
import com.tom_roush.pdfbox.pdmodel.font.PDFont;
import com.tom_roush.pdfbox.pdmodel.font.PDType1Font;
import com.tom_roush.pdfbox.pdmodel.graphics.image.JPEGFactory;
import com.tom_roush.pdfbox.pdmodel.graphics.image.LosslessFactory;
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject;

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
    AssetManager mAssetManager;

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
                            @NonNull IShopperModelsFactory modelsFactory,
                            @NonNull AssetManager assetManager) {
        mShopperRetrofitService = shopperRetrofitService;
        mInterceptor = interceptor;
        mRoomDatabase = roomDatabase;
        mShopperXmlRetrofitService = shopperXmlRetrofitService;
        mModelsFactory = modelsFactory;
        mAssetManager = assetManager;
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

    @NonNull
    @Override
    public Observable<File> getObservableUriEkasaPdf(Invoice invx, EkassaSettings ekasaset) {

        //return createEkasaPdf(invx);
        IEkasaPdfDoc ekasapdfdoc = new EkasaPdfDoc(ekasaset, mAssetManager);
        return ekasapdfdoc.getEkasaPdf(invx);

    }

    @NonNull
    @Override
    public File getFileEkasaPdf(Invoice invx, EkassaSettings ekasaset) {

        //return createEkasaPdf(invx);
        IEkasaPdfDoc ekasapdfdoc = new EkasaPdfDoc(ekasaset, mAssetManager);
        return ekasapdfdoc.getFileEkasaPdf(invx);

    }

    private Observable<File> createEkasaPdf(Invoice invx) {

        File root = android.os.Environment.getExternalStorageDirectory();

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        // Create a new font object selecting one of the PDF base fonts
        PDFont font = PDType1Font.HELVETICA;
        // Or a custom font
//		try {
//			PDType0Font font = PDType0Font.load(document, assetManager.open("MyFontFile.TTF"));
//		} catch(IOException e) {
//			e.printStackTrace();
//		}

        PDPageContentStream contentStream;

        try {
            // Define a content stream for adding to the PDF
            contentStream = new PDPageContentStream(document, page);

            // Write Hello World in blue text
            contentStream.beginText();
            contentStream.setNonStrokingColor(15, 38, 192);
            contentStream.setFont(font, 12);
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Hello World");
            contentStream.endText();

            // Load in the images
            InputStream in = mAssetManager.open("falcon.jpg");
            InputStream alpha = mAssetManager.open("trans.png");

            // Draw a green rectangle
            contentStream.addRect(5, 500, 100, 100);
            contentStream.setNonStrokingColor(0, 255, 125);
            contentStream.fill();

            // Draw the falcon base image
            PDImageXObject ximage = JPEGFactory.createFromStream(document, in);
            contentStream.drawImage(ximage, 20, 20);

            // Draw the red overlay image
            Bitmap alphaImage = BitmapFactory.decodeStream(alpha);
            PDImageXObject alphaXimage = LosslessFactory.createFromImage(document, alphaImage);
            contentStream.drawImage(alphaXimage, 20, 20);

            // Make sure that the content stream is closed:
            contentStream.close();

            // Save the final pdf document to a file
            String path = root.getAbsolutePath() + "/Download/ekasa" + invx.getDok() + ".pdf";
            document.save(path);
            document.close();
            //tv.setText("Successfully wrote PDF to " + path);
            File externalFile = new File(path);
            return Observable.just(externalFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    //methods for OrpSettingsActivity
    //get ekasa settings
    @Override
    public Flowable<List<EkassaSettings>> loadEkasaSettings() {

        return mRoomDatabase.ekassaSettingsDao().getRxAllSettings();

    }

    @Override
    public void saveEkassaSetData(String id, String compico, String compname, String compdic
            , String compicd, String headq, String dkp, String shop, String orsr, String pata1, String pata2) {

        EkassaSettings ekassaset = mModelsFactory.getEkassaSettings();
        ekassaset.setId(Integer.valueOf(id));
        ekassaset.setCompidc(Integer.valueOf(compico));
        ekassaset.setCompname(compname);
        ekassaset.setCompdic(compdic);
        ekassaset.setCompicd(compicd);
        ekassaset.setHeadquarters(headq);
        ekassaset.setDkp(dkp);
        ekassaset.setShopadress(shop);
        ekassaset.setOrsr(orsr);
        ekassaset.setPata1(pata1);
        ekassaset.setPata2(pata2);
        Log.d("dsave settid", id);

        mRoomDatabase.ekassaSettingsDao().insertEkassaSettings(ekassaset);
    }

    //get request by Dok
    @Override
    public Flowable<List<EkassaRequestBackup>> loadEkasaRequestForDok(String dokx) {

        return mRoomDatabase.ekassaRequestBackupDao().findRequestByDok(dokx);

    }

}
