package com.eusecom.samshopersung;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.eusecom.samshopersung.rxbus.RxBus;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.disposables.CompositeDisposable;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import static android.content.ContentValues.TAG;
import static rx.Observable.empty;

public class ProductDetailFragment extends Fragment {

    @Inject
    SharedPreferences mSharedPreferences;
    @Inject
    RxBus rxBus;
    @Inject
    Picasso mPicasso;
    @Inject
    ShopperIMvvmViewModel mViewModel;

    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private ProductDetailAdapter mAdapter;
    private CompositeDisposable disposables;
    private CompositeSubscription mSubscription;
    private ProgressBar mProgressBar;

    public static ProductDetailFragment newInstance() {
        Bundle args = new Bundle();
        ProductDetailFragment fragment = new ProductDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_prodetail, container, false);

        mRecycler = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecycler.setHasFixedSize(true);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new ProductDetailAdapter(getActivity(), rxBus, mPicasso );
        mAdapter.setProductItems(Collections.<ProductKt>emptyList());
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);
        mRecycler.setAdapter(mAdapter);

        //String serverx = "From fragment " + mSharedPreferences.getString("edidok", "");
        //Toast.makeText(getActivity(), serverx, Toast.LENGTH_SHORT).show();

    }

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

    private void unBind() {

        //mViewModel.clearObservableInvoiceDelFromServer();
        mSubscription.unsubscribe();
        mSubscription.clear();
        disposables.dispose();
        hideProgressBar();

    }

    private void bind() {

        disposables = new CompositeDisposable();
        mSubscription = new CompositeSubscription();

        mSubscription.add(getMyQueryProductsFromSqlServer()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> { Log.e(TAG, "Error ProductDetailFragment " + throwable.getMessage());
                    hideProgressBar();
                    Toast.makeText(getActivity(), "Server not connected", Toast.LENGTH_SHORT).show();
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::setServerProducts));

        emitMyQueryProductsFromSqlServer("GetDetail" + mSharedPreferences.getString("edidok", ""));

    }

    private void setServerProducts(List<ProductKt> products) {

        mAdapter.setProductItems(products);
        hideProgressBar();
    }

    protected Observable<List<ProductKt>> getMyQueryProductsFromSqlServer() {
        return mViewModel.getMyQueryProductsFromSqlServer();
    }

    protected void emitMyQueryProductsFromSqlServer(String query)  {
        showProgressBar();
        mViewModel.emitMyQueryProductsFromSqlServer(query);
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    protected void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    protected void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }


}
