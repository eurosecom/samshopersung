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
import com.eusecom.samfantozzi.models.BankItem;
import com.eusecom.samfantozzi.models.GeneralDocPresenterState;
import com.eusecom.samfantozzi.retrofit.AbsServerService;
import com.eusecom.samfantozzi.retrofit.ExampleInterceptor;

import java.util.Collections;
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
import static android.content.Context.SEARCH_SERVICE;


public class GeneralDocFragment extends Fragment implements GeneralDocMvpView {

    public GeneralDocFragment() {

    }

    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private ProgressBar mProgressBar;
    private GeneralDocAdapter mAdapter;
    private GeneralDocAdapter.ClickOnItemListener listener;

    @Inject
    SharedPreferences mSharedPreferences;
    @Inject
    AbsServerService mAbsServerService;
    @Inject
    ExampleInterceptor mInterceptor;

    private GeneralDocMvpPresenter presenter;

    //searchview
    private SearchView searchView;
    private MenuItem menuItem;
    private SearchView.OnQueryTextListener onQueryTextListener = null;
    SearchManager searchManager;
    private String querystring = "";
    private Disposable mDisposable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SamfantozziApp) getActivity().getApplication()).dgaeacomponent().inject(this);
        setHasOptionsMenu(true);

        // restore previous status data if available
        GeneralDocPresenterState state = new GeneralDocPresenterState("","","");
        if(savedInstanceState != null) {
            state =
                    (GeneralDocPresenterState)savedInstanceState.getSerializable("PRESENTERSTATE");
            Log.d("MvpPresenter onGet ", "state " + state.getQuerystring());
        }
        if (presenter == null) {
            presenter = new GeneralDocMvpPresenterImpl(this, mSharedPreferences
                    , new GeneralDocInteractorImpl(mAbsServerService, mInterceptor));

        }
        presenter.attachView(this, state);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putSerializable("PRESENTERSTATE", presenter.getSerializedPresenter());
        Log.d("MvpPresenter onSave ", "state " + presenter.getSerializedPresenter().getQuerystring());

    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_generaldocs, container, false);

        mRecycler = (RecyclerView) rootView.findViewById(R.id.list);
        mRecycler.setHasFixedSize(true);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);


        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String umex = mSharedPreferences.getString("ume", "");
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        listener = (item, pos) -> {
            //Toast.makeText(getContext(), "Dok " + item.getDok(), Toast.LENGTH_SHORT).show();
            //Log.d("Frag onShortClick", item.getHod());
            getItemDialog(item);
        };

        mAdapter = new GeneralDocAdapter(listener);
        mRecycler.setAdapter(mAdapter);


    }//end of onActivityCreated

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        mDisposable.dispose();
        listener = null;

    }

    @Override public void onResume() {
        super.onResume();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("edidok", "0").apply();
        editor.commit();
        bind();
    }


    private void bind() {

        presenter.onStart();
        //presenter.getGeneralDocs();

        ActivityCompat.invalidateOptionsMenu(getActivity());
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mSharedPreferences.getString("ume", "") + " "
                +  getString(R.string.generaldoc));

    }

    @Override public void setGeneralItems(List<BankItem> bankitems) {

        mAdapter.setBankItems(bankitems);
        mRecycler.setAdapter(mAdapter);
        //andrejko nastavSearchEngine(bankitems);

    }

    //View implementation called from presenter
    @Override public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override public void hideProgress() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override public void setQueryToSearch(String querystringx) {

        //querystringx = "20";
        Log.d("MvpPresenter query ", "in View " + querystringx);
        if( querystringx.equals("")){
        }else {
            querystring = querystringx;

        }

    }


    private void getItemDialog(@NonNull final BankItem invoice) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View textenter = inflater.inflate(R.layout.invoice_edit_dialog, null);

        final TextView valuex = (TextView) textenter.findViewById(R.id.valuex);
        valuex.setText(invoice.getHod());

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(textenter).setTitle(getString(R.string.document) + " " + invoice.getDok());

        builder.setItems(new CharSequence[]
                        {getString(R.string.pdf), getString(R.string.edit), getString(R.string.deletedoc)},
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
                                extras.putString("ucex", mSharedPreferences.getString("genuce", ""));
                                extras.putString("dokx", invoice.getDok());
                                extras.putString("icox", invoice.getIco());
                                is.putExtras(extras);
                                startActivity(is);

                                break;
                            case 1:

                                Intent ie = new Intent(getActivity(), NewBankDocKtActivity.class);
                                Bundle extrase = new Bundle();
                                extrase.putString("drupoh", "2");
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
        new AlertDialog.Builder(getActivity())
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



    //options menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // Retrieve the SearchView and plug it into SearchManager
        inflater.inflate(R.menu.bankmvp_menu, menu);
        menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        if( querystring.equals("")){

        }else {
            searchView.setIconified(false);
            searchView.setQuery(querystring, false);
            menuItem.setVisible(true);
            presenter.emitSearchString(querystring);
        }

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
                        return presenter.searchModel(query);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<BankItem>>() {
                    @Override
                    public void accept(List<BankItem> result) {
                        hideProgress();
                        setGeneralItems(result);
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


}
