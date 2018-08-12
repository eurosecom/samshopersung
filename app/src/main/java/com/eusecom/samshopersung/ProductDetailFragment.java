package com.eusecom.samshopersung;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.flowables.ConnectableFlowable;
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

        ConnectableFlowable<Object> tapEventEmitter = rxBus.asFlowable().publish();

        disposables
                .add(tapEventEmitter.subscribe(event -> {
                    if (event instanceof ProductDetailActivity.ClickFobEvent) {
                        Log.d("ProductDetailFragment  ", " fobClick ");



                    }
                    if (event instanceof ProductKt) {

                        String usnamex = ((ProductKt) event).getNat();
                        Log.d("ProductDetailFragment ",  usnamex);

                        if(((ProductKt) event).getPrm1().equals("101")){
                            ((ProductKt) event).setPrm1("1");
                            showAddToBasketDialog(((ProductKt) event));
                        }

                        if(((ProductKt) event).getPrm1().equals("111")){
                            ((ProductKt) event).setPrm1("11");
                            showAddToFavDialog(((ProductKt) event));
                        }


                    }

                }));

        disposables
                .add(tapEventEmitter.publish(stream ->
                        stream.buffer(stream.debounce(1, TimeUnit.SECONDS)))
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(taps -> {
                            ///_showTapCount(taps.size()); OK
                        }));

        disposables.add(tapEventEmitter.connect());


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

        mSubscription.add(getMyObservableSaveSumBasketToServer()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError(throwable -> { Log.e(TAG, "Error ProductDetailFragment " + throwable.getMessage());
                    hideProgressBar();
                    Toast.makeText(getActivity(), "Server not connected", Toast.LENGTH_SHORT).show();
                })
                .onErrorResumeNext(throwable -> empty())
                .subscribe(this::setSavedBasket));

        emitMyQueryProductsFromSqlServer("GetDetail" + mSharedPreferences.getString("edidok", ""));

    }

    private void setServerProducts(List<ProductKt> products) {

        mAdapter.setProductItems(products);
        hideProgressBar();
    }

    private void setSavedBasket(SumBasketKt sumbasket) {

        hideProgressBar();
        if(sumbasket.getSprm1().equals("0")) {
            rxBus.send(sumbasket);
            Toast.makeText(getActivity(), sumbasket.getBasketitems().get(0).getXnat() + " " + getString(R.string.savedtobasket), Toast.LENGTH_SHORT).show();
        }
        if(sumbasket.getSprm1().equals("1")) {
            Toast.makeText(getActivity(), sumbasket.getBasketitems().get(0).getXnat() + " " + getString(R.string.savedtofav), Toast.LENGTH_SHORT).show();
        }

    }

    private Observable<List<ProductKt>> getMyQueryProductsFromSqlServer() {
        return mViewModel.getMyQueryProductsFromSqlServer();
    }

    private void emitMyQueryProductsFromSqlServer(String query) {
        showProgressBar();
        mViewModel.emitMyQueryProductsFromSqlServer(query);
    }

    private Observable<SumBasketKt> getMyObservableSaveSumBasketToServer() {
        showProgressBar();
        return mViewModel.getMyObservableSaveSumBasketToServer();
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

    private void showAddToBasketDialog(@NonNull final ProductKt product){

        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.action_add_tobasket) + " " + product.getNat())
                .setPositiveButton(R.string.add,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                showProgressBar();
                                navigateToAddToBasket(product);

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

    private void navigateToAddToBasket(ProductKt product) {
        showProgressBar();
        mViewModel.emitMyObservableSaveSumBasketToServer(product);

    }

    private void showAddToFavDialog(@NonNull final ProductKt product){

        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.action_add_favourite) + " " + product.getNat())
                .setPositiveButton(R.string.add,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                showProgressBar();
                                navigateToAddToBasket(product);

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

    private void navigateToAddToFav(ProductKt product) {
        showProgressBar();
        mViewModel.emitMyObservableSaveSumBasketToServer(product);

    }


}
