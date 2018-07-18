package com.eusecom.samshopersung

import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.eusecom.samshopersung.models.InvoiceList
import com.eusecom.samshopersung.rxbus.RxBus
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.AlertBuilder
import org.jetbrains.anko.support.v4.toast
import rx.Observable
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Kotlin fragment Recyclerview with classic XML itemlayout without Anko DSL
 * template samfantozzi CashListKtFragment.kt
 */

class OrderFragment : Fragment() {

    private var mAdapter: OrderAdapter? = null
    private var mRecycler: RecyclerView? = null
    private var mManager: LinearLayoutManager? = null
    private var balance: TextView? = null

    private var mProgressBar: ProgressBar? = null

    private var mSubscription: CompositeSubscription? = null
    private var _disposables = CompositeDisposable()

    @Inject
    lateinit var mSharedPreferences: SharedPreferences

    @Inject
    lateinit var mViewModel: ShopperIMvvmViewModel

    @Inject
    lateinit var  _rxBus: RxBus

    private lateinit var alert: AlertBuilder<DialogInterface>

    //searchview
    private var searchView: SearchView? = null
    private var onQueryTextListener: SearchView.OnQueryTextListener? = null
    private var mDisposable: Disposable? = null
    protected var mOrderSearchEngine: OrderSearchEngine? = null
    var searchManager: SearchManager? = null
    private var invoiceszal: MutableList<Invoice>  = mutableListOf<Invoice>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //andrejko (activity.application as SamfantozziApp).dgaeacomponent().inject(this)
        setHasOptionsMenu(true)

        Log.d("mViewModel frg ", mViewModel.toString())

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater!!.inflate(R.layout.fragment_order, container, false)

        balance = rootView.findViewById<View>(R.id.balance) as TextView
        mRecycler = rootView.findViewById<View>(R.id.list) as RecyclerView
        mRecycler?.setHasFixedSize(true)
        mProgressBar = rootView.findViewById<View>(R.id.progress_bar) as ProgressBar

        return rootView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mAdapter = OrderAdapter(_rxBus)
        mAdapter?.setAbsserver(emptyList())
        mOrderSearchEngine = OrderSearchEngine(emptyList())
        // Set up Layout Manager, reverse layout
        mManager = LinearLayoutManager(context)
        mManager?.setReverseLayout(true)
        mManager?.setStackFromEnd(true)
        mRecycler?.setLayoutManager(mManager)
        mRecycler?.setAdapter(mAdapter)

    }

    private fun bind() {

        _disposables = CompositeDisposable()

        val tapEventEmitter = _rxBus.asFlowable().publish()

        _disposables
                .add(tapEventEmitter.subscribe { event ->
                    if (event is OrderFragment.ClickFobEvent) {
                        //Log.d("OrderListActivity  ", " fobClick ")
                        //andrejko showNewCashDocDialog()

                    }
                    if (event is Invoice) {

                        val usnamex = event.nai

                        //Log.d("OrderFragment ", usnamex)
                        //andrejko getTodoDialog(event)


                    }

                })

        _disposables
                .add(tapEventEmitter.publish { stream -> stream.buffer(stream.debounce(1, TimeUnit.SECONDS)) }
                        .observeOn(AndroidSchedulers.mainThread()).subscribe { taps ->
                    ///_showTapCount(taps.size()); OK
                })

        _disposables.add(tapEventEmitter.connect())

        mSubscription = CompositeSubscription()

        showProgressBar()
        mSubscription?.add(mViewModel.getMyProductsFromSqlServer("1")
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OrderFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setServerProducts(it) })


        ActivityCompat.invalidateOptionsMenu(activity)
        //andrejko (activity as AppCompatActivity).supportActionBar!!.setTitle(mSharedPreferences.getString("ume", "") + " "
        //andrejko         + mSharedPreferences.getString("pokluce", ""))
    }

    private fun unBind() {

        mSubscription?.unsubscribe()
        mSubscription?.clear()
        _disposables.dispose()
        if (mDisposable != null) {
            mDisposable?.dispose()
        }
        hideProgressBar()

    }

    private fun setServerProducts(products: List<ProductKt>) {
        hideProgressBar()
        var serverx = "FromOrderfrg " + mSharedPreferences?.getString("servername", "")
        Toast.makeText(activity, serverx, Toast.LENGTH_SHORT).show()

        Log.d("FromOrderfrg ", mViewModel.toString())

    }


    private fun setQueryString(querystring: String) {

        //toast(" querystring " + querystring)
        if( querystring.equals("")){

        }else {
            searchView?.setQuery(querystring, false)
        }

    }


    private fun setServerCashDocs(invoices: InvoiceList) {

        //Log.d("searchModel ", "in setServerInvoices")
        //toast(" nai0 " + invoices.get(0).nai)
        invoiceszal = invoices.getInvoice().toMutableList()
        mAdapter?.setAbsserver(invoices.getInvoice())
        nastavResultAs(invoices.getInvoice())
        balance?.setText(invoices.getBalance())
        hideProgressBar()
    }

    protected fun showResultAs(resultAs: List<Invoice>) {

        if (resultAs.isEmpty()) {
            //Toast.makeText(getActivity(), R.string.nothing_found, Toast.LENGTH_SHORT).show();
            mAdapter?.setAbsserver(emptyList<Invoice>())
        } else {
            //Log.d("showResultAs ", resultAs.get(0).dmna);
            mAdapter?.setAbsserver(resultAs)
        }
    }

    protected fun nastavResultAs(resultAs: List<Invoice>) {
        mOrderSearchEngine = OrderSearchEngine(resultAs)
    }

    class ClickFobEvent

    protected fun showProgressBar() {
        mProgressBar?.setVisibility(View.VISIBLE)
    }

    protected fun hideProgressBar() {
        mProgressBar?.setVisibility(View.GONE)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("OrderFragment ", "onDestroy");
        unBind()

    }

    override fun onResume() {
        super.onResume()
        Log.d("OrderFragment ", "onResume");
        bind()
    }

    override fun onPause() {
        super.onPause()
        Log.d("OrderFragment ", "onPause");
        unBind()
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {

        // Retrieve the SearchView and plug it into SearchManager
        inflater!!.inflate(R.menu.menu_orderfragment, menu)
        searchView = MenuItemCompat.getActionView(menu!!.findItem(R.id.action_search)) as SearchView
        searchManager = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView?.setSearchableInfo(searchManager?.getSearchableInfo(activity.componentName))
        //andrejko getObservableSearchViewText()
    }

    override fun onDestroyOptionsMenu() {
        searchView?.setOnQueryTextListener(null)
        searchManager = null
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId

        if (id == R.id.action_settings) {

            val `is` = Intent(activity, SettingsActivity::class.java)
            startActivity(`is`)
            return true
        }



        return super.onOptionsItemSelected(item)
    }





}
