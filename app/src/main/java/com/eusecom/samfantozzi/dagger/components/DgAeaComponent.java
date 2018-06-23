package com.eusecom.samfantozzi.dagger.components;

import com.eusecom.samfantozzi.AccountReportsActivity;
import com.eusecom.samfantozzi.AutopohListKtFragment;
import com.eusecom.samfantozzi.BankMvpActivity;
import com.eusecom.samfantozzi.CashListKtActivity;
import com.eusecom.samfantozzi.CashListKtFragment;
import com.eusecom.samfantozzi.ChooseAccountActivity;
import com.eusecom.samfantozzi.ChooseCompanyActivity;
import com.eusecom.samfantozzi.ChooseMonthActivity;
import com.eusecom.samfantozzi.DocSearchActivity;
import com.eusecom.samfantozzi.DomainsViewModelActivity;
import com.eusecom.samfantozzi.GeneralDocActivity;
import com.eusecom.samfantozzi.GeneralDocFragment;
import com.eusecom.samfantozzi.IdcListKtFragment;
import com.eusecom.samfantozzi.InvoiceListFragment;
import com.eusecom.samfantozzi.InvoiceListKtActivity;
import com.eusecom.samfantozzi.NewBankDocFragment;
import com.eusecom.samfantozzi.NewBankDocKtActivity;
import com.eusecom.samfantozzi.NewCashDocFragment;
import com.eusecom.samfantozzi.NewCashDocKtActivity;
import com.eusecom.samfantozzi.NewIdcActivity;
import com.eusecom.samfantozzi.NewInvoiceDocFragment;
import com.eusecom.samfantozzi.NewInvoiceDocKtActivity;
import com.eusecom.samfantozzi.NoSavedDocActivity;
import com.eusecom.samfantozzi.SaldoKtActivity;
import com.eusecom.samfantozzi.SaldoListKtFragment;
import com.eusecom.samfantozzi.ShowPdfActivity;
import com.eusecom.samfantozzi.SupplierListActivity;
import com.eusecom.samfantozzi.SupplierListFragment;
import com.eusecom.samfantozzi.TaxPaymentsActivity;
import com.eusecom.samfantozzi.TypesKtActivity;
import com.eusecom.samfantozzi.dagger.modules.ApplicationModule;
import com.eusecom.samfantozzi.dagger.modules.DgAeaModule;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules={ApplicationModule.class, DgAeaModule.class})
public interface DgAeaComponent {
  void inject(SupplierListActivity supplierListActivity);
  void inject(SupplierListFragment supplierfragment);
  void inject(CashListKtActivity cashListKtActivity);
  void inject(TypesKtActivity typestKtActivity);
  void inject(NewCashDocKtActivity newCashDocKtActivity);
  void inject(NewBankDocKtActivity newBankDocKtActivity);
  void inject(NewCashDocFragment newCashDocFragment);
  void inject(NewBankDocFragment newBankDocFragment);
  void inject(NewInvoiceDocKtActivity newInvoiceDocKtActivity);
  void inject(NewInvoiceDocFragment newInvoiceDocFragment);
  void inject(CashListKtFragment cashListKtFragment);
  void inject(IdcListKtFragment idcListKtFragment);
  void inject(NewIdcActivity newidcactivity);
  void inject(AutopohListKtFragment autopohListKtFragment);
  void inject(InvoiceListFragment invoicefragment);
  void inject(InvoiceListKtActivity invoiceListKtActivity);
  void inject(ChooseMonthActivity chooseMonthActivity);
  void inject(ChooseCompanyActivity chooseCompanyActivity);
  void inject(ChooseAccountActivity chooseAccountActivity);
  void inject(NoSavedDocActivity noSavedDoctActivity);
  void inject(AccountReportsActivity accountReportsActivity);
  void inject(ShowPdfActivity showPdftActivity);
  void inject(BankMvpActivity bankMvptActivity);
  void inject(SaldoKtActivity saldoKtActivity);
  void inject(SaldoListKtFragment saldoListKtFragment);
  void inject(TaxPaymentsActivity taxPaymentsActivity);
  void inject(DocSearchActivity docSearchActivity);
  void inject(GeneralDocActivity generalDocActivity);
  void inject(GeneralDocFragment generalDocfragment);
  void inject(DomainsViewModelActivity domainsViewModelActivity);




}