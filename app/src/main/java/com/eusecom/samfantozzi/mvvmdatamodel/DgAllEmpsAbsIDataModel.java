package com.eusecom.samfantozzi.mvvmdatamodel;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.eusecom.samfantozzi.Account;
import com.eusecom.samfantozzi.CalcVatKt;
import com.eusecom.samfantozzi.CompanyKt;
import com.eusecom.samfantozzi.Connected;
import com.eusecom.samfantozzi.IdCompanyKt;
import com.eusecom.samfantozzi.Invoice;
import com.eusecom.samfantozzi.Month;
import com.eusecom.samfantozzi.models.Attendance;
import com.eusecom.samfantozzi.models.Employee;
import com.eusecom.samfantozzi.models.InvoiceList;
import com.eusecom.samfantozzi.realm.RealmDomain;
import com.eusecom.samfantozzi.realm.RealmEmployee;
import com.eusecom.samfantozzi.realm.RealmInvoice;

import java.util.List;

import io.reactivex.Flowable;
import rx.Observable;

public interface DgAllEmpsAbsIDataModel {

    //recyclerview methods for DgAeaActivity

    @NonNull
    Observable<List<Employee>> getObservableFBusersRealmEmployee(String usicox, String usuid, int lenmoje);

    @NonNull
    public Observable<List<Attendance>> getAbsencesFromMysqlServer(String servername, String fromfir);

    @NonNull
    public Observable<List<Attendance>> getAbsencesFromMock(String fromfir);

    //recyclerview method for ChooseMonthActivity
    @NonNull
    public Observable<List<Month>> getMonthForYear(String rokx);

    //recyclerview method for ChooseAccountActivity
    @NonNull
    public Observable<List<Account>> getAccountsFromMysqlServer(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh);

    @NonNull
    public Observable<List<Account>> getAccounts(String rokx);

    //recyclerview method for ChooseCompanyActivity
    @NonNull
    public Observable<List<CompanyKt>> getCompaniesFromMysqlServer(String servername, String userhash, String userid);

    @NonNull
    public Observable<RealmDomain> saveDomainToRealm(RealmDomain domx);

    //recyclerview method for SupplierListActivity
    @NonNull
    public Observable<List<Attendance>> getInvoicesFromServer(String servername, String fromfir);

    @NonNull
    public Observable<List<Invoice>> getInvoicesFromMysqlServer(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String ucex, String umex, String dokx);

    //recyclerview method for CashListKtActivity
    @NonNull
    public Observable<InvoiceList> getCashDocsFromMysqlServer(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String ucex, String umex, String dokx);

    @NonNull
    public Observable<List<Invoice>> getObservableInvoiceDelFromMysql(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, Invoice invx);

    @NonNull
    Observable<List<Attendance>> getObservableAbsencesFromFB(@NonNull final String dokx, @NonNull final String umex
            , @NonNull final String usicox
            , String usuid, String ustype);

    Observable<Uri> getObservableUriDocPdf(Invoice invx, @NonNull final String firx
            , @NonNull final String rokx, @NonNull final String serverx, @NonNull final String adresx
            , String encrypted, @NonNull final String umex);

    @NonNull
    public Observable<String> getObservableCashListQuery(String queryx);

    //method for NewCashDocKtActivity
    @NonNull
    public Observable<Boolean> getObservableIdCompany(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String queryx);

    @NonNull
    public Observable<List<IdCompanyKt>> getObservableIdModelCompany(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String queryx);

    @NonNull
    public Observable<List<Account>> getReceiptsExpensesFromSql(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String drupoh, String ucto);

    @NonNull
    public Observable<List<Account>> getReceiptsExpensesFromRealm(String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String drupoh, String ucto);

    @NonNull
    public Observable<List<Account>> saveReceiptsExpensesToRealm(List<Account> recexp, String drh);

    @NonNull
    public Observable<CalcVatKt> getObservableRecountFromRealm(CalcVatKt calcx);

    @NonNull
    public Observable<Invoice> saveCashDocToRealm(Invoice invx);

    @NonNull
    Observable<RealmInvoice> getInvoiceSavingToRealm(@NonNull final List<RealmInvoice> invoices);

    //recyclerview method for TypesKtActivity

    @NonNull
    public Observable<List<IdCompanyKt>> getAllIdcFromMysqlServer(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh);

    @NonNull
    public Observable<List<IdCompanyKt>> saveIdCompaniesToRealm(List<IdCompanyKt> companies, String drh);

    @NonNull
    public Observable<List<IdCompanyKt>> getIdCompaniesFromRealm(String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String drupoh, String ucto);


    //recyclerview method for NoSavedDocActivity

    @NonNull
    public Observable<List<RealmInvoice>> getObservableNosavedDocRealm(String fromact);

    @NonNull
    public Observable<List<RealmInvoice>> deleteInvoiceFromRealm(RealmInvoice invoicex);

    @NonNull
    public Observable<List<RealmInvoice>> deleteAllInvoicesFromRealm(RealmInvoice invoicex);

    @NonNull
    public Observable<List<Invoice>> getObservableInvoiceToMysql(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, RealmInvoice invx, String edidok, String firduct);

    @NonNull
    public Observable<RealmInvoice> getIdcSavingToRealm(@NonNull final List<RealmInvoice> invoices);

    @NonNull
    public Observable<RealmInvoice> saveRealmOneIdcData(@NonNull final RealmInvoice invoices);

    //SaldoListKtFragment

    @NonNull
    public Observable<List<Invoice>> getSaldoFromSql(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, int drh, String ucex, String ucto, int salico, String recount);

    @NonNull
    public Observable<List<Invoice>> getObservableReminderToMysql(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, Invoice invx, String edidok, String firduct);

    //recyclerview method for TaxPaymentsActivity
    @NonNull
    public Observable<List<Account>> getTaxPayFromMysqlServer(String servername, String userhash, String userid, String fromfir
            , String vyb_rok, String drh, String serverx);


    //recyclerview method for DomainsActivity
    @NonNull
    public Observable<List<RealmDomain>> getDomainsFromRealm();

}
