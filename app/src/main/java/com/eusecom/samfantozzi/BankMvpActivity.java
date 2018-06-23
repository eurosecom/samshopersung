/*
 *
 *  * Copyright (C) 2014 Antonio Leiva Gordillo.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.eusecom.samfantozzi;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.eusecom.samfantozzi.models.BankItem;
import com.eusecom.samfantozzi.retrofit.AbsServerService;
import com.eusecom.samfantozzi.retrofit.ExampleInterceptor;

import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/*
* MVP pattern for Bank and Universal Documents List
* by https://github.com/antoniolg/androidmvp
*
* save Presenter state by https://medium.com/@trionkidnapper/android-mvp-keeping-presenters-alive-a91b9e080761
*/

public class BankMvpActivity extends AppCompatActivity implements BankMvpView, AdapterView.OnItemClickListener, AccountItemAdapter.KlikaciListener  {

    private ListView listView;
    private ProgressBar progressBar;
    private BankMvpPresenter presenter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private TextView balance;
    private AccountItemAdapter mAdapter;

    @Inject
    AbsServerService mAbsServerService;
    @Inject
    SharedPreferences mSharedPreferences;
    @Inject
    ExampleInterceptor mInterceptor;

    //searchview
    private SearchView searchView;
    private MenuItem menuItem;
    private SearchView.OnQueryTextListener onQueryTextListener = null;
    SearchManager searchManager;
    protected BankItemSearchEngine mBankItemSearchEngine;
    private Disposable mDisposable;
    private String querystring = "";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SamfantozziApp) getApplication()).dgaeacomponent().inject(this);

        setContentView(R.layout.bankmvp_activity);
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);

        balance = (TextView) findViewById(R.id.balance);
        mRecycler = (RecyclerView) findViewById(R.id.rvlist);
        mRecycler.setHasFixedSize(true);
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);
        mAdapter = new AccountItemAdapter(this);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        //presenter = new BankMvpPresenterImpl(this, mSharedPreferences, new BankFindItemsInteractorImpl(mAbsServerService));

        presenter = (BankMvpPresenter) getLastCustomNonConfigurationInstance();
        if (presenter == null) {
            presenter = new BankMvpPresenterImpl(this, mSharedPreferences
                    , new BankFindItemsInteractorImpl(mAbsServerService, mInterceptor));

        }
        presenter.attachView(this);

        Log.d("BankMvpPresenter act ", presenter.toString());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v -> {

            Intent is = new Intent(this, NewBankDocKtActivity.class);
            Bundle extras = new Bundle();
            extras.putString("fromact", "1");
            extras.putString("drupoh", "1");
            extras.putString("newdok", "1");
            extras.putString("edidok", "0");
            is.putExtras(extras);
            startActivity(is);

                }
        );

    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }

    //call presenter
    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.onItemClicked(position);
    }

    @Override protected void onResume() {
        super.onResume();
        getSupportActionBar().setTitle(mSharedPreferences.getString("ume", "") + " "
                + mSharedPreferences.getString("bankuce", "") + " " +  getString(R.string.bank));
        //presenter.onResume();
        ActivityCompat.invalidateOptionsMenu(this);

        presenter.onStart();
    }

    @Override protected void onDestroy() {
        //presenter.onDestroy();
        presenter.detachView();
        super.onDestroy();
        if( mDisposable != null ) {mDisposable.dispose();}
    }

    //View implementation called from presenter
    @Override public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.INVISIBLE);
    }

    @Override public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.VISIBLE);
    }

    @Override public void setItems(List<String> items) {
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items));
    }

    @Override public void setInvoiceItems(List<Invoice> items) {
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items));
    }

    @Override public void setBankItems(List<BankItem> bankitems) {

        mAdapter.setBankItems(bankitems);
        mRecycler.setAdapter(mAdapter);
        nastavSearchEngine(bankitems);

        if( querystring.equals("")){

        }else {
            searchView.setIconified(false);
            searchView.setQuery(querystring, false);
            menuItem.setVisible(true);
        }
    }

    @Override public void setSearchedBankItems(List<BankItem> searchedbankitems) {

        mAdapter.setBankItems(searchedbankitems);
        mRecycler.setAdapter(mAdapter);

    }

    protected void nastavSearchEngine(List<BankItem> result) {
        mBankItemSearchEngine = new BankItemSearchEngine(result);
    }

    @Override public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override public void showItemDialog(BankItem invoice) {

        getItemDialog(invoice);
    }

    @Override public void setBalance(String statebalance) {

        balance.setText(statebalance);
    }

    private void getItemDialog(@NonNull final BankItem invoice) {

        LayoutInflater inflater = LayoutInflater.from(BankMvpActivity.this);
        final View textenter = inflater.inflate(R.layout.invoice_edit_dialog, null);

        final TextView valuex = (TextView) textenter.findViewById(R.id.valuex);
        valuex.setText(invoice.getHod());

        final AlertDialog.Builder builder = new AlertDialog.Builder(BankMvpActivity.this);
        builder.setView(textenter).setTitle(getString(R.string.document) + " " + invoice.getDok());

        builder.setItems(new CharSequence[]
                        {getString(R.string.pdf), getString(R.string.edit), getString(R.string.deletedoc)},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:

                                Intent is = new Intent(BankMvpActivity.this, ShowPdfActivity.class);
                                Bundle extras = new Bundle();
                                extras.putString("fromact", "1");
                                extras.putString("drhx", invoice.getDrh());
                                extras.putString("ucex", mSharedPreferences.getString("bankuce", ""));
                                extras.putString("dokx", invoice.getDok());
                                extras.putString("icox", invoice.getIco());
                                is.putExtras(extras);
                                startActivity(is);

                                break;
                            case 1:

                                Intent ie = new Intent(BankMvpActivity.this, NewBankDocKtActivity.class);
                                Bundle extrase = new Bundle();
                                extrase.putString("drupoh", "1");
                                extrase.putString("newdok", "0");
                                extrase.putString("edidok", invoice.getDok());
                                ie.putExtras(extrase);
                                startActivity(ie);

                                break;
                            case 2:
                                deleteItemDialog(invoice, 1);
                                break;
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        builder.show();

    }

    private void deleteItemDialog(@NonNull final BankItem invoice, int all){

        String title = "";
        if( all == 0 ) {
            title = getString(R.string.deletedoc) + " " + invoice.getDok()
                    + " " + getString(R.string.value) + " " + invoice.getHod();
        }else{
            title = getString(R.string.deletewholedoc) + " " + invoice.getDok();
        }
        new AlertDialog.Builder(BankMvpActivity.this)
                .setTitle(title)
                .setPositiveButton(R.string.delete,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                presenter.deleteDoc(invoice);

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

    //option menu
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bankmvp_menu, menu);
        menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchManager = (SearchManager) this.getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        getObservableSearchViewText();

        return true;
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {

            Intent is = new Intent(this, SettingsActivity.class);
            startActivity(is);
            return true;
        }

        if (id == R.id.action_setmonth) {

            Intent is = new Intent(this, ChooseMonthActivity.class);
            startActivity(is);
            return true;
        }

        if (id == R.id.action_setaccount) {

            Intent is = new Intent(this, ChooseAccountActivity.class);
            Bundle extras = new Bundle();
            extras.putString("fromact", "4");
            is.putExtras(extras);
            startActivity(is);
            return true;
        }

        if (id == R.id.action_nosaveddoc) {

            Intent is = new Intent(this, NoSavedDocActivity.class);
            Bundle extras = new Bundle();
            extras.putString("fromact", "3");
            is.putExtras(extras);
            startActivity(is);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //listener to searchview
    private void getObservableSearchViewText() {

        Observable<String> searchViewChangeStream = createSearchViewTextChangeObservable();

        mDisposable = searchViewChangeStream
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        showProgress();
                    }
                })
                .observeOn(io.reactivex.schedulers.Schedulers.io())
                .map(new Function<String, List<BankItem>>() {
                    @Override
                    public List<BankItem> apply(String query) {
                        return mBankItemSearchEngine.searchModel(query);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<BankItem>>() {
                    @Override
                    public void accept(List<BankItem> result) {
                        hideProgress();
                        presenter.showSearchResult(result);
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
                        emitter.onNext(query.toString());
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        // use this method for auto complete search process
                        //Toast.makeText(getActivity(), "change " + newText, Toast.LENGTH_SHORT).show();
                        emitter.onNext(newText.toString());
                        presenter.emitSearchString(newText.toString());
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

    @Override public void setQueryToSearch(String querystringx) {

        Log.d("BankMvpPresenter query ", "in View " + querystringx);
        if( querystringx.equals("")){
        }else {
            querystring = querystringx;
        }

    }

    public void klikolSomItem(BankItem item){
        Log.d("onShortClickListAdapt", item.getHod());
        showItemDialog(item);

    }


}
