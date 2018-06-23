package com.eusecom.samfantozzi;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.eusecom.samfantozzi.rxbus.RxBus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import static android.content.ContentValues.TAG;
import static android.content.Context.SEARCH_SERVICE;
import static rx.Observable.empty;


public class InvoiceListFragment extends Fragment {

    public InvoiceListFragment() {

    }
    private CompositeDisposable _disposables;
    private SupplierAdapter mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private ProgressBar mProgressBar;
    private Disposable mDisposable;
    protected SupplierSearchEngine mSupplierSearchEngine;

    @NonNull
    private CompositeSubscription mSubscription;

    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    DgAllEmpsAbsMvvmViewModel mViewModel;

    @Inject
    RxBus _rxBus;

    AlertDialog dialog = null;

    //searchview
    private SearchView searchView;
    private SearchView.OnQueryTextListener onQueryTextListener = null;
    SearchManager searchManager;
    private List<Invoice> invoiceszal = Collections.<Invoice>emptyList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SamfantozziApp) getActivity().getApplication()).dgaeacomponent().inject(this);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_suppliers, container, false);

        mRecycler = (RecyclerView) rootView.findViewById(R.id.list);
        mRecycler.setHasFixedSize(true);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String umex = mSharedPreferences.getString("ume", "");
        mAdapter = new SupplierAdapter(_rxBus);
        mAdapter.setAbsserver(Collections.<Invoice>emptyList());
        mSupplierSearchEngine = new SupplierSearchEngine(Collections.<Invoice>emptyList());
        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);
        mRecycler.setAdapter(mAdapter);

        //String serverx = "From fragment " + mSharedPreferences.getString("servername", "");
        //Toast.makeText(getActivity(), serverx, Toast.LENGTH_SHORT).show();


    }//end of onActivityCreated

    @Override
    public void onDestroy() {
        super.onDestroy();
        unBind();
    }

    @Override
    public void onResume() {
        super.onResume();
        bind();
    }

    @Override
    public void onPause() {
        super.onPause();
        unBind();
    }

    private void bind() {

        _disposables = new CompositeDisposable();

        ConnectableFlowable<Object> tapEventEmitter = _rxBus.asFlowable().publish();

        _disposables
                .add(tapEventEmitter.subscribe(event -> {
                    if (event instanceof InvoiceListFragment.ClickFobEvent) {
                        Log.d("InvoiceListFragment  ", " fobClick ");

                        Intent is = new Intent(getActivity(), NewInvoiceDocKtActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("fromact", "1");
                        extras.putString("drupoh", "1");
                        extras.putString("newdok", "1");
                        extras.putString("edidok", "0");
                        is.putExtras(extras);
                        startActivity(is);

                    }
                    if (event instanceof Invoice) {

                        String usnamex = ((Invoice) event).getDok();


                        Log.d("InvoiceListFragment ",  usnamex);
                        getInvoiceDialog(((Invoice) event));
                        //String serverx = "DgAeaListFragment " + usnamex;
                        //Toast.makeText(getActivity(), serverx, Toast.LENGTH_SHORT).show();


                    }

                }));

        _disposables
                .add(tapEventEmitter.publish(stream ->
                        stream.buffer(stream.debounce(1, TimeUnit.SECONDS)))
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(taps -> {
                            ///_showTapCount(taps.size()); OK
                        }));

        _disposables.add(tapEventEmitter.connect());


        mSubscription = new CompositeSubscription();

        showProgressBar();
        mSubscription.add(mViewModel.getMyInvoicesFromSqlServer("1")
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> { Log.e(TAG, "Error InvoiceListFragment " + throwable.getMessage());
                    hideProgressBar();
                    Toast.makeText(getActivity(), "Server not connected", Toast.LENGTH_SHORT).show();
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(it -> setServerInvoices(it)));

        mSubscription.add(mViewModel.getMyInvoiceDelFromServer()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> { Log.e(TAG, "Error InvoiceListFragment " + throwable.getMessage());
                    Toast.makeText(getActivity(), "Server not connected", Toast.LENGTH_SHORT).show();
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::deletedInvoice));

        mSubscription.add(mViewModel.getMyObservableCashListQuery()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> { Log.e(TAG, "Error InvoiceListFragment " + throwable.getMessage());
                    Toast.makeText(getActivity(), "Server not connected", Toast.LENGTH_SHORT).show();
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::setQueryString));

        ActivityCompat.invalidateOptionsMenu(getActivity());
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mSharedPreferences.getString("ume", "") + " "
                + mSharedPreferences.getString("odbuce", "") + " " +  getString(R.string.customers));
 }

    private void unBind() {

        mViewModel.clearObservableInvoiceDelFromServer();
        mViewModel.clearObservableCashListQuery();
        mSubscription.unsubscribe();
        mSubscription.clear();
        if( mDisposable != null ) {mDisposable.dispose();}
        _disposables.dispose();

        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog=null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        hideProgressBar();

    }

    private void setQueryString(String querystring) {

        if( querystring.equals("")){

        }else {
            searchView.setQuery(querystring, false);
        }

    }

    private void deletedInvoice(@NonNull final List<Invoice> invoice) {

        //List<Invoice> invoiceszalnew = Collections.<Invoice>emptyList();
        List<Invoice> invoiceszalnew = new ArrayList();

        for (int i = 0; i < invoiceszal.size(); i++) {
            if (!invoiceszal.get(i).getDok().contains(invoice.get(0).getDok())) {
                invoiceszalnew.add(invoiceszal.get(i));
                Log.d("undeleted dok ", invoiceszal.get(i).getDok());
            }
        }

        invoiceszal = invoiceszalnew;
        mAdapter.setAbsserver(invoiceszalnew);
        nastavResultAs(invoiceszalnew);

        Log.d("deleted dok ", invoice.get(0).getDok());
        hideProgressBar();
    }

    private void setServerInvoices(@NonNull final List<Invoice> invoices) {

        Log.d("searchModel ","in setServerInvoices");
        //String serverx = invoices.get(0).getNai();
        //Toast.makeText(getActivity(), serverx, Toast.LENGTH_SHORT).show();
        invoiceszal = invoices;
        if (invoices.isEmpty()) {
            //Toast.makeText(getActivity(), R.string.nothing_found, Toast.LENGTH_SHORT).show();
            mAdapter.setAbsserver(Collections.<Invoice>emptyList());
        } else {
            //Log.d("showResultAs ", resultAs.get(0).dmna);
            mAdapter.setAbsserver(invoices);
        }
        nastavResultAs(invoices);
        hideProgressBar();
    }

    protected void showResultAs(List<Invoice> resultAs) {

        if (resultAs.isEmpty()) {
            //Toast.makeText(getActivity(), R.string.nothing_found, Toast.LENGTH_SHORT).show();
            mAdapter.setAbsserver(Collections.<Invoice>emptyList());
        } else {
            //Log.d("showResultAs ", resultAs.get(0).dmna);
            mAdapter.setAbsserver(resultAs);
        }
    }

    protected void nastavResultAs(List<Invoice> resultAs) {
        mSupplierSearchEngine = new SupplierSearchEngine(resultAs);
    }

    public static class ClickFobEvent {}

    protected void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    protected void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    //listener to searchview
    private void getObservableSearchViewText() {

        Observable<String> searchViewChangeStream = createSearchViewTextChangeObservable();

        mDisposable = searchViewChangeStream
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        showProgressBar();
                    }
                })
                .observeOn(io.reactivex.schedulers.Schedulers.io())
                .map(  query -> mSupplierSearchEngine.searchModel(query) )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Invoice>>() {
                    @Override
                    public void accept(List<Invoice> result) {
                        hideProgressBar();
                        showResultAs(result);
                    }
                });
    }

    private Observable<String> createSearchViewTextChangeObservable() {
        Observable<String> searchViewTextChangeObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {

                onQueryTextListener = new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        // use this method when query submitted
                        //Toast.makeText(getActivity(), "submit " + query, Toast.LENGTH_SHORT).show();
                        emitter.onNext(query.toString());
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        // use this method for auto complete search process
                        //Toast.makeText(getActivity(), "change " + newText, Toast.LENGTH_SHORT).show();
                        emitter.onNext(newText.toString());
                        mViewModel.emitMyObservableCashListQuery(newText.toString());
                        return false;
                    }





                };

                searchView.setOnQueryTextListener(onQueryTextListener);

                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        searchView.setOnQueryTextListener(null);
                    }
                });
            }
        });

        return searchViewTextChangeObservable
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String query) throws Exception {
                        return query.length() >= 3 || query.equals("");
                    }
                }).debounce(300, TimeUnit.MILLISECONDS);  // add this line
    }


    private void getInvoiceDialog(@NonNull final Invoice invoice) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View textenter = inflater.inflate(R.layout.invoice_edit_dialog, null);

        final TextView valuex = (TextView) textenter.findViewById(R.id.valuex);
        valuex.setText(invoice.getHod());

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(textenter).setTitle(getString(R.string.document) + " " + invoice.getDok());

        builder.setItems(new CharSequence[]
                        {getString(R.string.pdf), getString(R.string.edit), getString(R.string.delete)},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:

                                Intent is = new Intent(getActivity(), ShowPdfActivity.class);
                                Bundle extras = new Bundle();
                                extras.putString("fromact", "1");
                                extras.putString("drhx", invoice.getDrh());
                                extras.putString("ucex", invoice.getUce());
                                extras.putString("dokx", invoice.getDok());
                                extras.putString("icox", invoice.getIco());
                                is.putExtras(extras);
                                startActivity(is);

                                break;
                            case 1:

                                Intent ie = new Intent(getActivity(), NewInvoiceDocKtActivity.class);
                                Bundle extrase = new Bundle();
                                extrase.putString("drupoh", "1");
                                extrase.putString("newdok", "0");
                                extrase.putString("edidok", invoice.getDok());
                                ie.putExtras(extrase);
                                startActivity(ie);

                                break;
                            case 2:
                                deleteDialog(invoice);
                                break;
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        builder.show();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // Retrieve the SearchView and plug it into SearchManager
        inflater.inflate(R.menu.menu_listdoc, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        getObservableSearchViewText();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {

            Intent is = new Intent(getActivity(), SettingsActivity.class);
            startActivity(is);
            return true;
        }

        if (id == R.id.action_setmonth) {

            Intent is = new Intent(getActivity(), ChooseMonthActivity.class);
            startActivity(is);
            return true;
        }

        if (id == R.id.action_setaccount) {

            Intent is = new Intent(getActivity(), ChooseAccountActivity.class);
            Bundle extras = new Bundle();
            extras.putString("fromact", "1");
            is.putExtras(extras);
            startActivity(is);
            return true;
        }

        if (id == R.id.action_nosaveddoc) {

            Intent is = new Intent(getActivity(), NoSavedDocActivity.class);
            Bundle extras = new Bundle();
            extras.putString("fromact", "3");
            is.putExtras(extras);
            startActivity(is);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteDialog(@NonNull final Invoice invoice){

        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.deletedoc) + " " + invoice.getDok())
                .setPositiveButton(R.string.delete,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                showProgressBar();
                                mViewModel.emitDelInvFromServer(invoice);

                            }
                        })
                .setNegativeButton(R.string.close,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {


                            }
                        })
                .show();

    }



}
