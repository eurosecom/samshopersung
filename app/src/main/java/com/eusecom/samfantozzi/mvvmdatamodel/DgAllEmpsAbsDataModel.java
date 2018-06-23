package com.eusecom.samfantozzi.mvvmdatamodel;

import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import com.eusecom.samfantozzi.Account;
import com.eusecom.samfantozzi.CalcVatKt;
import com.eusecom.samfantozzi.CompanyKt;
import com.eusecom.samfantozzi.IdCompanyKt;
import com.eusecom.samfantozzi.Invoice;
import com.eusecom.samfantozzi.Month;
import com.eusecom.samfantozzi.R;
import com.eusecom.samfantozzi.models.Attendance;
import com.eusecom.samfantozzi.models.Employee;
import com.eusecom.samfantozzi.models.InvoiceList;
import com.eusecom.samfantozzi.realm.RealmAccount;
import com.eusecom.samfantozzi.realm.RealmDomain;
import com.eusecom.samfantozzi.realm.RealmIdCompany;
import com.eusecom.samfantozzi.realm.RealmInvoice;
import com.eusecom.samfantozzi.retrofit.AbsServerService;
import com.eusecom.samfantozzi.retrofit.ExampleInterceptor;
import com.eusecom.samfantozzi.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;


public class DgAllEmpsAbsDataModel implements DgAllEmpsAbsIDataModel {

    DatabaseReference mFirebaseDatabase;
    AbsServerService mAbsServerService;
    Resources mResources;
    Realm mRealm;
    ExampleInterceptor mInterceptor;

    public DgAllEmpsAbsDataModel(@NonNull final DatabaseReference databaseReference,
                                 @NonNull final AbsServerService absServerService,
                                 @NonNull final Resources resources,
                                 @NonNull final Realm realm,
                                 @NonNull final ExampleInterceptor interceptor) {
        mFirebaseDatabase = databaseReference;
        mAbsServerService = absServerService;
        mResources = resources;
        mRealm = realm;
        mInterceptor = interceptor;
    }


    //recyclerview datamodel for DgAeaActivity


    @NonNull
    @Override
    public Observable<List<Attendance>> getAbsencesFromMysqlServer(String servername, String fromfir) {

        setRetrofit(servername);
        return mAbsServerService.getAbsServer(fromfir);


    }

    @NonNull
    @Override
    public Observable<List<Attendance>> getAbsencesFromMock(String fromfir) {

        return Observable.just(getMockAttendance());

    }


    @NonNull
    @Override
    public Observable<List<Employee>> getObservableFBusersRealmEmployee(String usicox, String usuid, int lenmoje) {

        Query usersQuery = mFirebaseDatabase.child("users").orderByChild("usico").equalTo(usicox);

        return RxFirebaseDatabase.getInstance().observeValueEvent(usersQuery)
                .flatMap(dataSnapshot ->{
                    List<Employee> blogPostEntities = new ArrayList<>();
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        String keys = childDataSnapshot.getKey();
                        //System.out.println("keys " + keys);
                        Employee resultx = childDataSnapshot.getValue(Employee.class);
                        resultx.setKeyf(keys);
                        blogPostEntities.add(resultx);
                    }
                    return Observable.just(blogPostEntities);
                });

    }


    public List<Attendance> getMockAttendance() {


        List<Attendance> mockAttendance = new ArrayList<>();

        Attendance newAttendance = new Attendance("44551142", "usid", "10.2017", "506",
                "Mock Dovolena", "1506549600", "1506549600", "2",
                "4", "0", "0", "1506549600", "1", "andrejd" );

        mockAttendance.add(newAttendance);

        return mockAttendance;

    }


    //recyclerview method for ChooseMonthActivity

    public Observable<List<Month>> getMonthForYear(String rokx) {

        List<Month> mymonths = new ArrayList<>();
        Month newmonth = new Month(mResources.getString(R.string.january), "01." + rokx, "1");
        mymonths.add(newmonth);
        newmonth = new Month(mResources.getString(R.string.february), "02." + rokx, "2");
        mymonths.add(newmonth);
        newmonth = new Month(mResources.getString(R.string.march), "03." + rokx, "3");
        mymonths.add(newmonth);
        newmonth = new Month(mResources.getString(R.string.april), "04." + rokx, "4");
        mymonths.add(newmonth);
        newmonth = new Month(mResources.getString(R.string.may), "05." + rokx, "5");
        mymonths.add(newmonth);
        newmonth = new Month(mResources.getString(R.string.june), "06." + rokx, "6");
        mymonths.add(newmonth);
        newmonth = new Month(mResources.getString(R.string.july), "07." + rokx, "7");
        mymonths.add(newmonth);
        newmonth = new Month(mResources.getString(R.string.august), "08." + rokx, "8");
        mymonths.add(newmonth);
        newmonth = new Month(mResources.getString(R.string.september), "09." + rokx, "9");
        mymonths.add(newmonth);
        newmonth = new Month(mResources.getString(R.string.october), "10." + rokx, "10");
        mymonths.add(newmonth);
        newmonth = new Month(mResources.getString(R.string.november), "11." + rokx, "11");
        mymonths.add(newmonth);
        newmonth = new Month(mResources.getString(R.string.december), "12." + rokx, "12");
        mymonths.add(newmonth);

        return Observable.just(mymonths);
    }


    //recyclerview method for ChooseCompanyuActivity

    @Override
    public Observable<List<CompanyKt>> getCompaniesFromMysqlServer(String servername, String userhash, String userid) {

        setRetrofit(servername);
        return mAbsServerService.getCompaniesFromServer(userhash, userid);

    }

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

        String dokx = domx.getDomain();
        return mRealm.where(RealmDomain.class).equalTo("domain", dokx).findFirst();
    }

    private void setRealmDomainData(@NonNull final RealmDomain domx) {

            mRealm.beginTransaction();
            mRealm.copyToRealm(domx);
            mRealm.commitTransaction();

    }

    private void deleteRealmDomainData(@NonNull final RealmDomain domx) {

        String dokx = domx.getDomain();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<RealmDomain> result = realm.where(RealmDomain.class).equalTo("domain", dokx).findAll();
                result.clear();
            }
        });

    }

    //recyclerview method for ChooseAccountActivity
    @Override
    public Observable<List<Account>> getAccountsFromMysqlServer(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh) {

        //Log.d("ChooseAccount drh ", drh);
        setRetrofit(servername);
        return mAbsServerService.getAccountsFromSqlServer(userhash, userid, fromfir, vyb_rok, drh);
        //return mAbsServerService.controlIdCompanyOnSqlServer(userhash, userid, fromfir, vyb_rok, drh, "xxx");

    }


    @Override
    public Observable<List<Account>> getAccounts(String rokx) {

        List<Account> mymonths = new ArrayList<>();
        Account newmonth = new Account("Dodavatelia", "32100", "830001", "0", "2", "0","true");
        mymonths.add(newmonth);


        return Observable.just(mymonths);
    }

    //recyclerview datamodel for SupplierListActivity

    @NonNull
    @Override
    public Observable<List<Attendance>> getInvoicesFromServer(String servername, String fromfir) {

        setRetrofit(servername);
        return mAbsServerService.getInvoicesFromServer(fromfir);

    }

    @Override
    public Observable<List<Invoice>> getInvoicesFromMysqlServer(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String ucex, String umex, String dokx) {
        Log.d("GenDoc dokx", dokx);
        Log.d("GenDoc drh", drh);
        Log.d("GenDoc ucex", ucex);

        setRetrofit(servername);
        return mAbsServerService.getInvoicesFromSqlServer(userhash, userid, fromfir, vyb_rok, drh, ucex, umex, dokx);

    }

    //recyclerview method for CashListKtActivity
    @Override
    public Observable<InvoiceList> getCashDocsFromMysqlServer(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String ucex, String umex, String dokx) {

        setRetrofit(servername);
        return mAbsServerService.getCashDocsFromSqlServer(userhash, userid, fromfir, vyb_rok, drh, ucex, umex, dokx);

    }

    @NonNull
    public Observable<List<Invoice>> getObservableInvoiceDelFromMysql(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, Invoice invx){

        List<IdCompanyKt> myidc = new ArrayList<>();
        IdCompanyKt newidc = new IdCompanyKt("31414466", "", "", "Firma xyz", "ulixyz",
                "Mesto", "", "", true, "", "", "", "");
        myidc.add(newidc);

        //Log.d("userhash ", userhash);
        System.out.println("invx.dok " + invx.getDok());
        System.out.println("invx.hod " + invx.getHod());

        String invxstring = JSsonFromInvoice(invx);

        System.out.println("invxstring " + invxstring);

        setRetrofit(servername);
        return mAbsServerService.deleteInvoiceFromMysqlPost(userhash, userid, fromfir, vyb_rok, drh, invxstring);
    }

    @NonNull
    @Override
    public Observable<List<Attendance>> getObservableAbsencesFromFB(@NonNull final String dokx, @NonNull final String umex
            , @NonNull final String usicox, String usuid, String ustype) {

        int lenmoje=1;
        if (ustype.equals("99")) {
            lenmoje=0;
        }else{

        }
        String umexy = umex;
        if (dokx.equals("0")) {
            umexy="0";
        }
        Query usersQuery = mFirebaseDatabase.child("company-absences").child(usicox).orderByChild("ume").equalTo(umexy);
        if( lenmoje == 1 ){
            usersQuery = mFirebaseDatabase.child("user-absences").child(usuid).orderByChild("ume").equalTo(umexy);
        }

        return RxFirebaseDatabase.getInstance().observeValueEvent(usersQuery)
                .flatMap(dataSnapshot ->{
                    List<Attendance> blogPostEntities = new ArrayList<>();
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        String keys = childDataSnapshot.getKey();
                        //System.out.println("keys " + keys);
                        Attendance resultx = childDataSnapshot.getValue(Attendance.class);
                        resultx.setRok(keys);
                        blogPostEntities.add(resultx);
                    }
                    return Observable.just(blogPostEntities);
                });

    }

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
        if (invx.getDrh().equals("31")){
            String drupoh = "1";
            uri = Uri.parse("http://" + serverx +
                    "/ucto/vspk_pdf.php?cislo_dok=" + invx.getDok() + "&hladaj_dok=" + invx.getDok()
                    + "&sysx=UCT&rozuct=ANO&zandroidu=1&anduct=1&copern=20&drupoh="+ drupoh + "&page=1&serverx="
                    + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx + "&newfntz=1");
        }
        if (invx.getDrh().equals("32")){
            String drupoh = "1";
            uri = Uri.parse("http://" + serverx +
                    "/ucto/vspk_pdf.php?cislo_dok=" + invx.getDok() + "&hladaj_dok=" + invx.getDok()
                    + "&sysx=UCT&rozuct=ANO&zandroidu=1&anduct=1&copern=20&drupoh="+ drupoh + "&page=1&serverx="
                    + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx + "&newfntz=1");
        }
        if (invx.getDrh().equals("1")){
            String drupoh = "1";
            uri = Uri.parse("http://" + serverx +
                    "/faktury/vstf_pdf.php?cislo_dok=" + invx.getDok() + "&hladaj_dok=" + invx.getDok()
                    + "&mini=1&tlacitR=1&sysx=UCT&rozuct=ANO&zandroidu=1&anduct=1&h_razitko=1&copern=20&drupoh=1&page=1&serverx="
                    + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx + "&newfntz=1");
        }
        if (invx.getDrh().equals("2")){
            String drupoh = "1";
            uri = Uri.parse("http://" + serverx +
                    "/faktury/vstf_pdf.php?cislo_dok=" + invx.getDok() + "&hladaj_dok=" + invx.getDok()
                    + "&mini=1&tlacitR=1&sysx=UCT&rozuct=ANO&zandroidu=1&anduct=1&h_razitko=1&copern=20&drupoh=2&page=1&serverx="
                    + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx + "&newfntz=1");
        }
        if (invx.getDrh().equals("4")){
            String drupoh = "4";
            uri = Uri.parse("http://" + serverx +
                    "/ucto/vspk_pdf.php?cislo_dok=" + invx.getDok() + "&hladaj_dok=" + invx.getDok()
                    + "&sysx=UCT&rozuct=ANO&zandroidu=1&anduct=1&copern=20&drupoh="+ drupoh + "&page=1&serverx="
                    + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx + "&newfntz=1");
        }
        if (invx.getDrh().equals("5")){
            String drupoh = "5";
            uri = Uri.parse("http://" + serverx +
                    "/ucto/vspk_pdf.php?cislo_dok=" + invx.getDok() + "&hladaj_dok=" + invx.getDok()
                    + "&sysx=UCT&rozuct=ANO&zandroidu=1&anduct=1&copern=20&drupoh="+ drupoh + "&page=1&serverx="
                    + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx + "&newfntz=1");
        }
        if (invx.getDrh().equals("41")) {
            String drupoh = "1";
            //String umex = "1.2017";

            uri = Uri.parse("http://" + serverx +
                    "/ucto/penden.php?copern=10&drupoh="+ drupoh + "&page=1&typ=PDF&zandroidu=1&anduct=1&kli_vume="+ umex
                    + "&serverx=" + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx + "&newfntz=1" );

        }

        if (invx.getDrh().equals("42")) {
            String drupoh = "1";
            //String umex = "1.2017";

            uri = Uri.parse("http://" + serverx +
                    "/ucto/penden2013.php?copern=10&drupoh="+ drupoh + "&page=1&typ=PDF&zandroidu=1&anduct=1&kli_vume="+ umex
                    + "&serverx=" + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx + "&newfntz=1" );

        }

        if (invx.getDrh().equals("43")) {
            String drupoh = "1";
            //String umex = "1.2017";

            uri = Uri.parse("http://" + serverx +
                    "/ucto/vprivyd2014.php?copern=10&drupoh="+ drupoh + "&page=1&typ=PDF&zandroidu=1&anduct=1&kli_vume="+ umex
                    + "&serverx=" + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx + "&newfntz=1" );

        }

        if (invx.getDrh().equals("44")) {
            String drupoh = "1";
            //String umex = "1.2017";

            uri = Uri.parse("http://" + serverx +
                    "/ucto/vmajzav2014.php?copern=10&drupoh="+ drupoh + "&page=1&typ=PDF&zandroidu=1&anduct=1&kli_vume="+ umex
                    + "&serverx=" + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx + "&newfntz=1" );

        }

        if (invx.getDrh().equals("51")) {
            String drupoh = "1";
            //String umex = "1.2017";

            //uzavierka
            //window.open('../ucto/vprivyd2014.php?copern=10&drupoh=1&h_zos=&h_sch=&h_drp=1&page=1&uzav=1&celeeura=1'

            uri = Uri.parse("http://" + serverx +
                    "/ucto/vprivyd2014.php?copern=10&drupoh="+ drupoh + "&page=1&typ=PDF&zandroidu=1&anduct=1&kli_vume="+ umex
                    + "&serverx=" + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx
                    + "&newfntz=1&h_sch=&h_zos=&h_drp=1&uzav=1&celeeura=0" );

        }

        if (invx.getDrh().equals("52")) {
            String drupoh = "1";
            //String umex = "1.2017";

            //uri = Uri.parse("http://" + serverxxx[0] +
            //"/ucto/kniha_faktur.php?copern=1&drupoh=1&page=1&zandroidu=1&anduct=1&h_drp=3&kli_vume=" + kli_vume + "&serverx="
            //+ serverx + "&userhash=" + encrypted + "&rokx=" + allxxx[3] + "&firx=" + allxxx[1] );

            uri = Uri.parse("http://" + serverx +
                    "/ucto/kniha_faktur.php?copern=1&drupoh="+ drupoh + "&page=1&zandroidu=1&anduct=1&kli_vume="+ umex
                    + "&serverx=" + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx
                    + "&newfntz=1&h_drp=3" );

        }

        if (invx.getDrh().equals("53")) {
            String drupoh = "1";
            //String umex = "1.2017";

            //uri = Uri.parse("http://" + serverxxx[0] +
            //"/ucto/kniha_faktur.php?copern=1&drupoh=1&page=1&zandroidu=1&anduct=1&h_drp=3&kli_vume=" + kli_vume + "&serverx="
            //+ serverx + "&userhash=" + encrypted + "&rokx=" + allxxx[3] + "&firx=" + allxxx[1] );

            uri = Uri.parse("http://" + serverx +
                    "/ucto/kniha_faktur.php?copern=1&drupoh="+ drupoh + "&page=1&zandroidu=1&anduct=1&kli_vume="+ umex
                    + "&serverx=" + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx
                    + "&newfntz=1&h_drp=4" );

        }


        if (invx.getDrh().equals("54")) {
            String drupoh = "1";

            uri = Uri.parse("http://" + serverx +
                    "/ucto/prizdph2018.php?copern=10&pdfand=1&drupoh=1&page=1&zandroidu=1&anduct=1&kli_vume="+ umex
                    + "&fir_uctx01=1&h_drp=1&h_arch=0&anduct=1&kli_vduj=9"
                    + "&serverx=" + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx
                    + "&newfntz=1" );

        }

        if (invx.getDrh().equals("55")) {
            String drupoh = "1";

            //uri = Uri.parse("http://" + serverxxx[0] +
            //"/ucto/priznanie_fob2015.php?copern=10&drupoh=1&page=1&zandroidu=1&anduct=1
            //&kli_vume=" + kli_vume + "&serverx="
            //+ serverx + "&userhash=" + encrypted + "&rokx=" + allxxx[3] + "&firx=" + allxxx[1] );

            uri = Uri.parse("http://" + serverx +
                    "/ucto/priznanie_fob2017_pdf.php?copern=11&drupoh=1&page=1&zandroidu=1&anduct=1&kli_vume="+ umex
                    + "&serverx=" + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx
                    + "&newfntz=1" );

        }

        if (invx.getDrh().equals("61")) {
            String drupoh = "2";
            String delims = "[.]+";
            String[] umexxx = umex.split(delims);

            String mesx = umexxx[0];

            //http@ //www.eshoptest.sk/ucto/juknihapoh.php?h_obdp=1&h_obdk=1&copern=11&drupoh=2&page=1&typ=HTML#

            uri = Uri.parse("http://" + serverx +
                    "/ucto/juknihapoh.php?copern=11&drupoh="+ drupoh + "&page=1&zandroidu=1&anduct=1&kli_vume="+ umex
                    + "&serverx=" + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx
                    + "&newfntz=1&h_obdp=" + mesx + "&h_obdk=" + mesx + "&typ=PDF" );

        }

        if (invx.getDrh().equals("71")) {
            String drupoh = "2";
            String delims = "[.]+";
            String[] umexxx = umex.split(delims);

            String mesx = umexxx[0];
            String ucex = invx.getUce();
            String icox = invx.getIco();

            //"/ucto/saldo_pdf.php?copern=11&drupoh=1&page=1&h_uce=" + ucex + "&h_ico=0&zandroidu=1&anduct=1&serverx="
            // serverx + "&userhash=" + encrypted + "&rokx=" + allxxx[3] + "&firx=" + allxxx[1] );

            uri = Uri.parse("http://" + serverx +
                    "/ucto/saldo_pdf.php?copern=11&drupoh=1&page=1&h_uce=" + ucex + "&zandroidu=1&anduct=1"
                    + "&serverx=" + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx
                    + "&newfntz=1&typ=PDF&h_ico=" + icox);

        }

        if (invx.getDrh().equals("72")) {
            String drupoh = "2";
            String delims = "[.]+";
            String[] umexxx = umex.split(delims);

            String mesx = umexxx[0];
            String ucex = invx.getUce();
            String icox = invx.getIco();

            //"/ucto/saldo_pdf.php?copern=11&drupoh=1&page=1&h_uce=" + ucex + "&h_ico=0&zandroidu=1&anduct=1&serverx="
            // serverx + "&userhash=" + encrypted + "&rokx=" + allxxx[3] + "&firx=" + allxxx[1] );

            uri = Uri.parse("http://" + serverx +
                    "/ucto/saldo_pdf.php?copern=11&drupoh=1&page=1&h_uce=" + ucex + "&zandroidu=1&anduct=1"
                    + "&serverx=" + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx
                    + "&newfntz=1&typ=PDF&h_ico=" + icox );

        }

        if (invx.getDrh().equals("73")) {
            String drupoh = "2";
            String delims = "[.]+";
            String[] umexxx = umex.split(delims);

            String mesx = umexxx[0];
            String ucex = invx.getUce();
            String icox = invx.getIco();

            uri = Uri.parse("http://" + serverx +
                    "/ucto/saldo_pdf.php?copern=14&drupoh=4&page=1&h_uce=" + ucex + "&zandroidu=1&anduct=1"
                    + "&serverx=" + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx
                    + "&newfntz=1&typ=PDF&h_ico=" + icox );

        }

        if (invx.getDrh().equals("74")) {
            String drupoh = "2";
            String delims = "[.]+";
            String[] umexxx = umex.split(delims);

            String mesx = umexxx[0];
            String ucex = invx.getUce();
            String icox = invx.getIco();

            uri = Uri.parse("http://" + serverx +
                    "/ucto/saldo_pdf.php?copern=14&drupoh=4&page=1&h_uce=" + ucex + "&zandroidu=1&anduct=1"
                    + "&serverx=" + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx
                    + "&newfntz=1&typ=PDF&h_ico=" + icox );

        }

        if (invx.getDrh().equals("81")) {
            String drupoh = "2";
            String delims = "[.]+";
            String[] umexxx = umex.split(delims);

            String mesx = umexxx[0];
            String ucex = invx.getUce();
            String icox = invx.getIco();

            uri = Uri.parse("http://" + serverx +
                    "/ucto/upomienka.php?copern=20&drupoh=1&page=1&h_pen=0&h_ppe=0&h_uce=" + ucex + "&zandroidu=1&anduct=1"
                    + "&serverx=" + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx
                    + "&newfntz=1&typ=PDF&h_ico=" + icox + "&cislo_ico=" + icox + "&cislo_fak=0&cislo_strana=1" );

        }

        if (invx.getDrh().equals("82")) {
            String drupoh = "2";
            String delims = "[.]+";
            String[] umexxx = umex.split(delims);

            String mesx = umexxx[0];
            String ucex = invx.getUce();
            String icox = invx.getIco();
            String dokx = invx.getDok();

            uri = Uri.parse("http://" + serverx +
                    "/ucto/upomienka.php?copern=10&drupoh=1&page=1&h_pen=0&h_ppe=0&h_uce=" + ucex + "&zandroidu=1&anduct=1"
                    + "&serverx=" + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx
                    + "&newfntz=1&typ=PDF&h_ico=" + icox + "&cislo_ico=" + icox + "&cislo_fak=" + dokx + "&cislo_strana=1" );

        }

        if (invx.getDrh().equals("91")) {

            uri = Uri.parse("http://www.edcom.sk/ram1/" + invx.getDok() + ".pdf");

        }

        //double accounts reports

        if (invx.getDrh().equals("101")) {

            uri = Uri.parse("http://" + serverx +
                    "/ucto/uobrat.php?copern=1&drupoh=1&page=1&zandroidu=1&anduct=1&kli_vume="+ umex
                    + "&serverx=" + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx
                    + "&newfntz=1" );

        }

        if (invx.getDrh().equals("102")) {

            uri = Uri.parse("http://" + serverx +
                    "/ucto/udennik.php?copern=1&drupoh=1&page=1&zandroidu=1&anduct=1&kli_vume="+ umex
                    + "&serverx=" + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx
                    + "&newfntz=1" );

        }

        if (invx.getDrh().equals("103")) {

            uri = Uri.parse("http://" + serverx +
                    "/ucto/suv_mala.php?copern=10&drupoh=1&page=1&zandroidu=1&anduct=1&kli_vume="+ umex
                    + "&serverx=" + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx
                    + "&newfntz=1" );

        }

        if (invx.getDrh().equals("104")) {

            uri = Uri.parse("http://" + serverx +
                    "/ucto/vys_mala.php?copern=10&drupoh=1&page=1&zandroidu=1&anduct=1&kli_vume="+ umex
                    + "&serverx=" + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx
                    + "&newfntz=1" );

        }

        if (invx.getDrh().equals("105")) {

            uri = Uri.parse("http://" + serverx +
                    "/ucto/hlkniha_polpdf.php?copern=10&drupoh=1&page=1&zandroidu=1&anduct=1&kli_vume="+ umex
                    + "&serverx=" + adresx + "&userhash=" + encrypted + "&rokx=" + rokx + "&firx=" + firx
                    + "&newfntz=1" );

        }

        return Observable.just(uri);

    }

    @NonNull
    @Override
    public Observable<String> getObservableCashListQuery(@NonNull final String queryx) {

        return Observable.just(queryx);
    }

    //method for NewCashDocKtActivity
    @NonNull
    public Observable<Boolean> getObservableIdCompany(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String queryx){

        return Observable.just(true);
        //return mAbsServerService.controlIdCompanyOnSqlServer(userhash, userid, fromfir, vyb_rok, drh, queryx);
    }

    @NonNull
    public Observable<List<IdCompanyKt>> getObservableIdModelCompany(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String queryx){

        List<IdCompanyKt> myidc = new ArrayList<>();
        IdCompanyKt newidc = new IdCompanyKt("31414466", "", "", "Firma xyz", "ulixyz",
                "Mesto", "", "", true, "", "", "", "");
        myidc.add(newidc);

        Log.d("NewCashLog data queryx ", queryx);

        setRetrofit(servername);
        return mAbsServerService.controlIdCompanyOnSqlServer(userhash, userid, fromfir, vyb_rok, drh, queryx);
    }

    @Override
    public Observable<List<Account>> getReceiptsExpensesFromSql(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String drupoh, String ucto) {

        setRetrofit(servername);
        return mAbsServerService.getReceiptExpensesFromSqlServer(userhash, userid, fromfir, vyb_rok, drh, drupoh, ucto);

    }

    @NonNull
    public Observable<List<Account>> saveReceiptsExpensesToRealm(List<Account> recexp, String drh){

        //clear all items in table
        //mRealm.beginTransaction();
        //mRealm.clear(RealmAccount.class);
        //mRealm.commitTransaction();

        String typex = recexp.get(0).getAcctype();
        if(drh.equals("100")) {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<RealmAccount> result = realm.where(RealmAccount.class).equalTo("accdoc", "0").findAll();
                    result.clear();
                }
            });
        }else{
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<RealmAccount> result = realm.where(RealmAccount.class).equalTo("acctype", typex).findAll();
                    result.clear();
                }
            });
        }

        for (Account b : recexp) {

            RealmAccount realmacc = new RealmAccount();

            long unixTime = System.currentTimeMillis() / 1000L;
            String unixTimes = unixTime + "";

            realmacc.setAccname(b.getAccname());
            realmacc.setAccnumber(b.getAccnumber());
            realmacc.setAccdoc(b.getAccdoc());
            realmacc.setAccdov(b.getAccdov());
            realmacc.setAcctype(b.getAcctype());
            // to get time from sql realmacc.setDatm(b.getDatm());
            //to get current time
            realmacc.setDatm(unixTimes);
            realmacc.setLogprx(b.getLogprx());

            System.out.println("save RealmAccount " + b.getAccnumber() + " " + b.getAcctype() + " " + unixTimes);

            mRealm.beginTransaction();
            mRealm.copyToRealm(realmacc);
            mRealm.commitTransaction();
        }

        return Observable.just(recexp);
    }

    @Override
    public Observable<List<Account>> getReceiptsExpensesFromRealm(String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String drupoh, String ucto) {

        List<RealmAccount> results = null;
        if(drh.equals("100")) {
            results = mRealm.where(RealmAccount.class).equalTo("accdoc", "0").findAll();
        }else{
            results = mRealm.where(RealmAccount.class).equalTo("acctype", drupoh).findAll();
        }

        List<Account> myaccounts = new ArrayList<>();
        for (RealmAccount b : results) {

            Account account = new Account("rm " + b.getAccname(), b.getAccnumber(), b.getAccdoc()
                    ,b.getAccdov(), b.getAcctype(), b.getDatm(), b.getLogprx() );
            myaccounts.add(account);

            System.out.println("get RealmAccount " + drh + " " + b.getAccnumber() + " " + b.getAcctype() + " " + b.getDatm());

        }

        return Observable.just(myaccounts);

    }

    @NonNull
    @Override
    public Observable<CalcVatKt> getObservableRecountFromRealm(CalcVatKt calcx) {

        calcx.setSumnod();
        return Observable.just(calcx);
    }

    @NonNull
    @Override
    public Observable<Invoice> saveCashDocToRealm(Invoice invx) {

        return Observable.just(invx);
    }

    @NonNull
    @Override
    public Observable<RealmInvoice> getInvoiceSavingToRealm(@NonNull final List<RealmInvoice> invoices) {

        //does exist invoice in Realm?
        RealmInvoice invoiceexists = existRealmInvoice( invoices );

        if(invoiceexists != null){
            //System.out.println("existRealmInvoice " + true);
            //System.out.println("existRealmInvoice " + true);
            deleteRealmInvoiceData( invoices );
        }else{
            //System.out.println("existRealmInvoice " + false);
        }
        //save to realm and get String OK or ERROR
        setRealmInvoiceData( invoices );

        return Observable.just(invoices.get(0));

    }

    public RealmInvoice existRealmInvoice(@NonNull final List<RealmInvoice> invoices) {

        String dokx = invoices.get(0).getDok();
        return mRealm.where(RealmInvoice.class).equalTo("dok", dokx).findFirst();
    }

    private void setRealmInvoiceData(@NonNull final List<RealmInvoice> invoices) {

        //clear all items in table
        //mRealm.beginTransaction();
        //mRealm.clear(RealmInvoice.class);
        //mRealm.commitTransaction();


        for (RealmInvoice b : invoices) {
            // Persist your data easily
            mRealm.beginTransaction();
            mRealm.copyToRealm(b);
            mRealm.commitTransaction();
        }

    }

    private void deleteRealmInvoiceData(@NonNull final List<RealmInvoice> invoices) {

        String dokx = invoices.get(0).getDok();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<RealmInvoice> result = realm.where(RealmInvoice.class).equalTo("dok", dokx).findAll();
                result.clear();
            }
        });

    }


    //methods for TypesKtActivity
    @Override
    public Observable<List<IdCompanyKt>> getAllIdcFromMysqlServer(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh) {

        setRetrofit(servername);
        return mAbsServerService.getAllIdCompanyOnSqlServer(userhash, userid, fromfir, vyb_rok, drh, "xxx");

    }

    @NonNull
    public Observable<List<IdCompanyKt>> saveIdCompaniesToRealm(List<IdCompanyKt> companies, String drh){

            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<RealmIdCompany> result = realm.where(RealmIdCompany.class).findAll();
                    result.clear();
                }
            });


        for (IdCompanyKt b : companies) {

            RealmIdCompany realmacc = new RealmIdCompany();

            long unixTime = System.currentTimeMillis() / 1000L;
            String unixTimes = unixTime + "";

            realmacc.setIco(b.getIco());
            realmacc.setDic(b.getDic());
            realmacc.setIcd(b.getIcd());
            realmacc.setNai(b.getNai());
            realmacc.setUli(b.getUli());
            realmacc.setMes(b.getMes());
            realmacc.setPsc(b.getPsc());
            realmacc.setTel(b.getTel());
            realmacc.setLogprx(String.valueOf(b.getLogprx()));
            realmacc.setDatm(unixTimes);

            System.out.println("save RealmIdCompany " + b.getIco() + " " + b.getNai() + " " + unixTimes);

            mRealm.beginTransaction();
            mRealm.copyToRealm(realmacc);
            mRealm.commitTransaction();
        }

        return Observable.just(companies);
    }

    @Override
    public Observable<List<IdCompanyKt>> getIdCompaniesFromRealm(String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String drupoh, String ucto) {

        List<RealmIdCompany> results =  mRealm.where(RealmIdCompany.class).findAll();


        List<IdCompanyKt> myaccounts = new ArrayList<>();
        for (RealmIdCompany b : results) {

            //data class IdCompanyKt(var ico : String, var dic : String, var icd : String,  var nai: String
            //        , var uli: String, var mes: String, var psc: String, var tel: String
            //        , var logprx: Boolean, var datm: String )

            IdCompanyKt account = new IdCompanyKt(b.getIco(), b.getDic(), b.getIcd(), "rm " + b.getNai()
                    ,b.getUli(), b.getMes(), b.getPsc()
                    ,b.getTel(), true, b.getDatm(), b.getEmail(), b.getIb1(), b.getSw1() );
            myaccounts.add(account);

            long unixTime = System.currentTimeMillis() / 1000L;
            String unixTimes = unixTime + "";
            System.out.println("get RealmIdCompany " + b.getIco() + " " + b.getNai() + " " + b.getDatm() + " " + unixTimes);

        }

        return Observable.just(myaccounts);

    }


    //end methods for TypesKtActivity


    //recyclerview method for NoSavedDocActivity

    @NonNull
    @Override
    public Observable<List<RealmInvoice>> getObservableNosavedDocRealm(String fromact) {

        List<RealmInvoice> results = null;
        String drhx = fromact;

        results = mRealm.where(RealmInvoice.class).equalTo("saved", "false").findAll();

        return Observable.just(results);
    }

    @NonNull
    @Override
    public Observable<List<RealmInvoice>> deleteInvoiceFromRealm(RealmInvoice invoicex) {

        String docx = invoicex.getDok();
        String fromact = invoicex.getDrh();
        //System.out.println("deleteInvoiceFromRealm " + docx);

        mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<RealmInvoice> result = realm.where(RealmInvoice.class).equalTo("drh", fromact).equalTo("dok", docx).findAll();
                    result.clear();
                }
         });

        List<RealmInvoice> results = mRealm.where(RealmInvoice.class).equalTo("saved", "false").findAll();
        return Observable.just(results);
    }

    @NonNull
    @Override
    public Observable<List<RealmInvoice>> deleteAllInvoicesFromRealm(RealmInvoice invoicex) {

        String docx = invoicex.getDok();
        String fromact = invoicex.getDrh();
        //System.out.println("fromact " + fromact);

        mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<RealmInvoice> result = realm.where(RealmInvoice.class).equalTo("drh", fromact).findAll();
                    result.clear();
                }
         });


        List<RealmInvoice> results = mRealm.where(RealmInvoice.class).equalTo("saved", "false").findAll();

        return Observable.just(results);
    }

    @NonNull
    public Observable<List<Invoice>> getObservableInvoiceToMysql(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, RealmInvoice invx, String edidok, String firduct){

        if(invx.getDrh().toString().equals("99999")){
            //andrejko
            saveRealmOneIdcData(invx);
        }

        //Log.d("userhash ", userhash);
        System.out.println("invx.dok " + invx.getDok());
        System.out.println("invx.hod " + invx.getHod());

        String invxstring = JSsonFromRealmInvoice(invx);

        System.out.println("invxstring userhash " + userhash);
        System.out.println("invxstring userid " + userid);
        System.out.println("invxstring fromfir " + fromfir);
        System.out.println("invxstring vyb_rok " + vyb_rok);
        System.out.println("invxstring drh " + drh);
        System.out.println("invxstring " + invxstring);
        System.out.println("invxstring edidok" + edidok);
        System.out.println("invxstring firduct" + firduct);

        setRetrofit(servername);
        return mAbsServerService.saveInvoiceToMysqlPost(userhash, userid, fromfir, vyb_rok, drh, invxstring, edidok, firduct);
    }

    //JSON from Invoice
    public String JSsonFromInvoice(Invoice invx) {


        String jsonstring = "{" +
                "  \"drh\":" + "\"" + invx.getDrh() + "\"" +
                ", \"uce\":" + "\"" + invx.getUce() + "\"" +
                ", \"dok\":" + "\"" + invx.getDok() + "\"" +
                ", \"ico\":" + "\"" + invx.getIco() + "\"" +
                ", \"nai\":" + "\"" + invx.getNai() + "\"" +
                ", \"kto\":" + "\"" + invx.getKto() + "\"" +
                ", \"fak\":" + "\"" + invx.getFak() + "\"" +
                ", \"ksy\":" + "\"" + invx.getKsy() + "\"" +
                ", \"ssy\":" + "\"" + invx.getSsy() + "\"" +
                ", \"ume\":" + "\"" + invx.getUme() + "\"" +
                ", \"dat\":" + "\"" + invx.getDat() + "\"" +
                ", \"daz\":" + "\"" + invx.getDaz() + "\"" +
                ", \"das\":" + "\"" + invx.getDas() + "\"" +
                ", \"poz\":" + "\"" + invx.getPoz() + "\"" +
                ", \"poh\":" + "\"" + invx.getPoh() + "\"" +
                ", \"zk0\":" + "\"" + invx.getZk0() + "\"" +
                ", \"zk1\":" + "\"" + invx.getZk1() + "\"" +
                ", \"dn1\":" + "\"" + invx.getDn1() + "\"" +
                ", \"zk2\":" + "\"" + invx.getZk2() + "\"" +
                ", \"dn2\":" + "\"" + invx.getDn2() + "\"" +
                ", \"saved\":" + "\"" + invx.getSaved() + "\"" +
                ", \"datm\":" + "\"" + invx.getDatm() + "\"" +
                ", \"uzid\":" + "\"" + invx.getUzid() + "\"" +
                ", \"tel\":" + "\"" + invx.getTel() + "\"" +
                " }";

        return jsonstring;
    }
    //end JSON from Invoice

    //JSON from RealmInvoice
    public String JSsonFromRealmInvoice(RealmInvoice invx) {


        String jsonstring = "{" +
                "  \"drh\":" + "\"" + invx.getDrh() + "\"" +
                ", \"uce\":" + "\"" + invx.getUce() + "\"" +
                ", \"dok\":" + "\"" + invx.getDok() + "\"" +
                ", \"ico\":" + "\"" + invx.getIco() + "\"" +
                ", \"nai\":" + "\"" + invx.getNai() + "\"" +
                ", \"kto\":" + "\"" + invx.getKto() + "\"" +
                ", \"fak\":" + "\"" + invx.getFak() + "\"" +
                ", \"ksy\":" + "\"" + invx.getKsy() + "\"" +
                ", \"ssy\":" + "\"" + invx.getSsy() + "\"" +
                ", \"ume\":" + "\"" + invx.getUme() + "\"" +
                ", \"dat\":" + "\"" + invx.getDat() + "\"" +
                ", \"daz\":" + "\"" + invx.getDaz() + "\"" +
                ", \"das\":" + "\"" + invx.getDas() + "\"" +
                ", \"poz\":" + "\"" + invx.getPoz() + "\"" +
                ", \"poh\":" + "\"" + invx.getPoh() + "\"" +
                ", \"zk0\":" + "\"" + invx.getZk0() + "\"" +
                ", \"zk1\":" + "\"" + invx.getZk1() + "\"" +
                ", \"dn1\":" + "\"" + invx.getDn1() + "\"" +
                ", \"zk2\":" + "\"" + invx.getZk2() + "\"" +
                ", \"dn2\":" + "\"" + invx.getDn2() + "\"" +
                ", \"saved\":" + "\"" + invx.getSaved() + "\"" +
                ", \"datm\":" + "\"" + invx.getDatm() + "\"" +
                ", \"uzid\":" + "\"" + invx.getUzid() + "\"" +
                " }";

        return jsonstring;
    }
    //end JSON from RealmInvoice


    //NewIdcActivity
    //save idc to realm
    @NonNull
    @Override
    public Observable<RealmInvoice> getIdcSavingToRealm(@NonNull final List<RealmInvoice> invoices) {

        if(invoices.get(0).getDrh().toString().equals("99")){
            saveRealmOneIdcData(invoices.get(0));
        }

        //does exist invoice in Realm?
        RealmInvoice idcexists = existRealmIdc( invoices );

        if(idcexists != null){
            //System.out.println("existRealmInvoice " + true);
            //System.out.println("existRealmInvoice " + true);
            deleteRealmIdcData( invoices );
        }else{
            //System.out.println("existRealmInvoice " + false);
        }
        //save to realm and get String OK or ERROR
        setRealmIdcData( invoices );

        return Observable.just(invoices.get(0));

    }

    public RealmInvoice existRealmIdc(@NonNull final List<RealmInvoice> invoices) {

        String dokx = invoices.get(0).getDok();
        return mRealm.where(RealmInvoice.class).equalTo("dok", dokx).findFirst();
    }

    private void setRealmIdcData(@NonNull final List<RealmInvoice> invoices) {

        //clear all items in table
        //mRealm.beginTransaction();
        //mRealm.clear(RealmInvoice.class);
        //mRealm.commitTransaction();


        for (RealmInvoice b : invoices) {
            // Persist your data easily
            mRealm.beginTransaction();
            mRealm.copyToRealm(b);
            mRealm.commitTransaction();
        }

    }

    private void deleteRealmIdcData(@NonNull final List<RealmInvoice> invoices) {

        String dokx = invoices.get(0).getDok();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<RealmInvoice> result = realm.where(RealmInvoice.class).equalTo("dok", dokx).findAll();
                result.clear();
            }
        });

    }

    public Observable<RealmInvoice> saveRealmOneIdcData(@NonNull final RealmInvoice invoices) {

        if(invoices.getDrh().equals("99")) {

            List<IdCompanyKt> companies = new ArrayList<>();
            //{  "drh":"99", "uce":"", "dok":"12345678", "ico":"12345678", "nai":"JUCTO F301 xo",
            // "kto":"email@email.com", "fak":"0", "ksy":"null", "ssy":"null", "ume":"null", "dat":"JUCTO F301 xo",
            // "daz":"null", "das":"null", "poz":"0999/123457", "poh":"0", "zk0":"", "zk1":"1077135345", "dn1":"90501",
            // "zk2":"Dlha 23", "dn2":"Senica", "saved":"false", "datm":"null", "uzid":"null" }
            IdCompanyKt newidc = new IdCompanyKt(invoices.getIco(), invoices.getZk1(), invoices.getZk0()
                    , invoices.getNai(), invoices.getZk2(),
                    invoices.getDn2(), invoices.getDn1(), invoices.getPoz()
                    , true, "", invoices.getKto(), "", "");
            companies.add(newidc);


            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<RealmIdCompany> result = realm.where(RealmIdCompany.class).equalTo("ico", companies.get(0).getIco()).findAll();
                    result.clear();
                }
            });


            for (IdCompanyKt b : companies) {

                RealmIdCompany realmacc = new RealmIdCompany();

                long unixTime = System.currentTimeMillis() / 1000L;
                String unixTimes = unixTime + "";

                realmacc.setIco(b.getIco());
                realmacc.setDic(b.getDic());
                realmacc.setIcd(b.getIcd());
                realmacc.setNai(b.getNai());
                realmacc.setUli(b.getUli());
                realmacc.setMes(b.getMes());
                realmacc.setPsc(b.getPsc());
                realmacc.setTel(b.getTel());
                realmacc.setLogprx(String.valueOf(b.getLogprx()));
                realmacc.setDatm(unixTimes);

                System.out.println("save OneRealmIdCompany " + b.getIco() + " " + b.getNai() + " " + unixTimes);

                mRealm.beginTransaction();
                mRealm.copyToRealm(realmacc);
                mRealm.commitTransaction();
            }

        }

        return Observable.just(invoices);

    }


    //SaldoListKtFragment

    @Override
    public Observable<List<Invoice>> getSaldoFromSql(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, int drh, String ucex, String ucto, int salico, String recount) {

        //System.out.println("getSaldo recount " + recount);
        setRetrofit(servername);
        return mAbsServerService.getSaldoFromSqlServer(userhash, userid, fromfir, vyb_rok, drh, ucex, ucto, salico, recount);

    }

    @NonNull
    public Observable<List<Invoice>> getObservableReminderToMysql(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, Invoice invx, String edidok, String firduct){


        //Log.d("userhash ", userhash);
        System.out.println("invx.dok " + invx.getDok());
        System.out.println("invx.hod " + invx.getHod());

        String invxstring = JSsonFromInvoice(invx);

        System.out.println("invxstring userhash " + userhash);
        System.out.println("invxstring userid " + userid);
        System.out.println("invxstring fromfir " + fromfir);
        System.out.println("invxstring vyb_rok " + vyb_rok);
        System.out.println("invxstring drh " + drh);
        System.out.println("invxstring " + invxstring);
        System.out.println("invxstring edidok" + edidok);
        System.out.println("invxstring firduct" + firduct);

        setRetrofit(servername);
        return mAbsServerService.saveReminderToMysqlPost(userhash, userid, fromfir, vyb_rok, drh, invxstring, edidok, firduct);
    }


    //recyclerview method for TaxPaymentsActivity
    @Override
    public Observable<List<Account>> getTaxPayFromMysqlServer(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String serverx) {

        //Log.d("mAbsServerService ", mAbsServerService.toString());

        setRetrofit(servername);
        return mAbsServerService.getTaxPayFromSqlServer(userhash, userid, fromfir, vyb_rok, drh, serverx
                , "1", "1", "1");

    }

    //recyclerview method for DomainsActivity
    @NonNull
    @Override
    public Observable<List<RealmDomain>> getDomainsFromRealm() {

        Log.d("DomainsViewModel dom ", "read Realm");
        List<RealmDomain> results = null;
        results = mRealm.where(RealmDomain.class).findAll();

        return Observable.just(results);
    }


    //set retrofit by runtime
    public void setRetrofit(String servername) {

        System.out.println("invxstring servername " + servername);
        String urlname = "http://" + servername;

        mInterceptor.setInterceptor(urlname);

    }
    //end set retrofit by runtime

}
