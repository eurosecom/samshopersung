package com.eusecom.samfantozzi;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import com.eusecom.samfantozzi.models.BankItem;
import com.eusecom.samfantozzi.retrofit.AbsServerService;
import com.eusecom.samfantozzi.retrofit.ExampleInterceptor;

import java.util.ArrayList;
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

import static java.util.Collections.emptyList;

/*
* How to Implement Load More in #RecyclerView
* by https://medium.com/@programmerasi/how-to-implement-load-more-in-recyclerview-3c6358297f4
*
*/

public class DocSearchActivity  extends AppCompatActivity implements DocSearchMvpView {

    private Toolbar toolbar;

    private TextView tvEmptyView, total;
    private TextSwitcher listed;
    private RecyclerView mRecyclerView;
    private DocSearchAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    protected Handler handler;
    private List<BankItem> searchitems;
    private ProgressDialog mProgressDialog;

    //MVP
    private DocSearchMvpPresenter presenter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.docsearch_activity);

        ((SamfantozziApp) getApplication()).dgaeacomponent().inject(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvEmptyView = (TextView) findViewById(R.id.empty_view);
        total = (TextView) findViewById(R.id.total);
        listed = (TextSwitcher) findViewById(R.id.listed);
        //listed = (TextView) findViewById(R.id.listed);

        searchitems = new ArrayList<BankItem>();
        searchitems = emptyList();

        // Set the ViewFactory of the TextSwitcher that will create TextView object when asked
        listed.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                // TODO Auto-generated method stub
                // create new textView and set the properties like colour, size, gravity etc
                TextView myText = new TextView(DocSearchActivity.this);
                myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                myText.setTextSize(18);
                myText.setTextColor(Color.BLACK);
                return myText;
            }
        });

        // Declare the in and out animations and initialize them
        Animation in = AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);

        // set the animation type of textSwitcher
        listed.setInAnimation(in);
        listed.setOutAnimation(out);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        handler = new Handler();
        total.setText("0");
        listed.setText("0");

        mAdapter = new DocSearchAdapter(searchitems, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        presenter = (DocSearchMvpPresenter) getLastCustomNonConfigurationInstance();
        if (presenter == null) {
            presenter = new DocSearchMvpPresenterImpl(this, mSharedPreferences
                    , new DocSearchInteractorImpl(mAbsServerService, mInterceptor));

        }
        presenter.attachView(this);

        getSupportActionBar().setTitle(getString(R.string.docsearch));

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //presenter.loadStudents();
        //presenter.loadSearchItems();
        presenter.getFirst20SearchItemsFromSql(querystring);

    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }

    @Override protected void onDestroy() {

        presenter.detachView();
        super.onDestroy();
    }


    @Override public void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    @Override public void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override public void setStudents(List<DocSearchStudent> studentList) {
        //Log.d("DocSearchMvp ", "student " + studentList.get(0).getName());
    }

    @Override public void setSearchItems(List<BankItem> first20searchitems) {
        //Log.d("DocSearchMvp ", "firstitems " + first20searchitems.get(0).getDok());
        Log.d("DocSearchMvp ", "firstitems ");

        //searchitems = new ArrayList<BankItem>();
        searchitems = first20searchitems;
        mAdapter = new DocSearchAdapter(searchitems, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        if (searchitems.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }

        mAdapter.setOnLoadMoreListener(new DocSearchOnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                //add null , so the adapter will check view_type and show progress bar at bottom
                searchitems.add(null);
                mAdapter.notifyItemInserted(searchitems.size() - 1);

                int start = searchitems.size()-1;
                int end = start + 20;

                //presenter.loadNext20SearchItems(start, end);
                presenter.getNext20SearchItemsFromSql(querystring, start, end);

            }
        });

        setItemsAmount(first20searchitems);

    }

    @Override public void setNext20SearchItems(List<BankItem> next20searchitems) {
        //Log.d("DocSearchMvp ", "nextitems " + next20searchitems.get(0).getDok());
        Log.d("DocSearchMvp ", "nextitems ");

        //hide bottom progressbar
        searchitems.remove(searchitems.size() - 1);
        mAdapter.notifyItemRemoved(searchitems.size());

        for (int i = 0; i < next20searchitems.size(); i++) {
            searchitems.add(next20searchitems.get(i));
        }
        mAdapter.setLoaded();
        mAdapter.notifyDataSetChanged();

        setItemsAmount(next20searchitems);

    }

    @Override public void setForQueryFirstSearchItems(List<BankItem> querysearchitems) {
        //Log.d("DocSearchMvp ", "nextitems " + next20searchitems.get(0).getDok());
        Log.d("DocSearchMvp ", "firstqueryitems ");

        listed.setText("0");
        //hide bottom progressbar
        searchitems.clear();
        mAdapter.notifyDataSetChanged();

        for (int i = 0; i < querysearchitems.size(); i++) {
            // Caused by: rx.exceptions.OnErrorNotImplementedException
            searchitems.add(querysearchitems.get(i));
        }
        mAdapter.setLoaded();
        mAdapter.notifyDataSetChanged();

        setItemsAmount(querysearchitems);
    }

    //option menu
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.docsearch_menu, menu);
        menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchManager = (SearchManager) this.getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        searchView.setQueryHint(getString(R.string.searchhint));
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

                    }
                })
                .observeOn(io.reactivex.schedulers.Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String query) {
                        if(!query.equals(querystring)) {
                            //java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()
                            //showProgress();
                            presenter.getForQueryFirst20SearchItemsFromSql(query); }
                        return query;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) {
                        querystring = result;
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
                }).debounce(1200, TimeUnit.MILLISECONDS);  // add this line
    }

    @Override public void setQueryToSearch(String querystringx) {

        Log.d("BankMvpPresenter query ", "in View " + querystringx);
        if( querystringx.equals("")){
        }else {
            querystring = querystringx;
        }

    }

    private void setItemsAmount(List<BankItem> items) {

        String amitems="";
        try {
            amitems = items.get(0).getBal();
        }catch(IndexOutOfBoundsException e){
            amitems="0";
        }
        total.setText(amitems);

        int listedi = items.size();
        TextView tvlisted = (TextView) listed.getCurrentView();
        int listepi = Integer.parseInt(tvlisted.getText().toString());
        int listesi = listepi + listedi;
        String listeds = listesi + "";
        listed.setText(listeds);

        if( querystring.equals("")){

        }else {
            searchView.setIconified(false);
            searchView.setQuery(querystring, false);
            menuItem.setVisible(true);
        }

    }


}