package com.eusecom.samshopersung

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.support.v4.toast
import rx.Observable
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class ProductDetailKtFragment : Fragment() {

    @Inject
    lateinit var mViewModel: ShopperIMvvmViewModel
    @Inject
    lateinit var mSharedPreferences: SharedPreferences
    @Inject
    lateinit var mAdapter: ProductDetail2Adapter
    @Inject
    lateinit var mImageUrl: ImageUrl

    private var mRecycler: RecyclerView? = null
    private var mManager: LinearLayoutManager? = null
    private var mProgressBar: ProgressBar? = null
    private var mSubscription: CompositeSubscription? = null
    private var disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater!!.inflate(R.layout.fragment_prodetail, container, false)

        mRecycler = rootView.findViewById<View>(R.id.recycler_view) as RecyclerView
        mRecycler?.setHasFixedSize(true)
        mProgressBar = rootView.findViewById<View>(R.id.progress_bar) as ProgressBar

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //toast("From fragment 2" + mSharedPreferences?.getString("edidok", ""))
        mAdapter.setProductItems(emptyList<ProductKt>())
        mManager = LinearLayoutManager(context)
        mManager?.setReverseLayout(true)
        mManager?.setStackFromEnd(true)
        mRecycler?.setLayoutManager(mManager)
        mRecycler?.setAdapter(mAdapter)

    }

    override fun onDestroy() {
        super.onDestroy()
        unBind()
    }

    override fun onResume() {
        super.onResume()
        bind()
    }

    override fun onPause() {
        super.onPause()
        unBind()
    }

    private fun bind() {

        mSubscription = CompositeSubscription()

        //showProgressBar()
        mSubscription?.add(getMyQueryProductsFromSqlServer()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("ProductDetailKtFragment", "Error Throwable " + throwable.message)
                    //hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setServerProducts(it) })

        emitMyQueryProductsFromSqlServer("GetDetail" + mSharedPreferences.getString("edidok", "")!!)

    }

    private fun setServerProducts(products: List<ProductKt>) {

        mAdapter.setProductItems(products)
        //hideProgressBar()
    }

    private fun getMyQueryProductsFromSqlServer(): Observable<List<ProductKt>> {
        return mViewModel.myQueryProductsFromSqlServer
    }

    private fun emitMyQueryProductsFromSqlServer(query: String) {
        //showProgressBar()
        mViewModel.emitMyQueryProductsFromSqlServer(query)
    }

    private fun unBind() {

        mSubscription?.unsubscribe()
        mSubscription?.clear()
        disposables?.dispose()
        //hideProgressBar()

    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

}
