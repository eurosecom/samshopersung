package com.eusecom.samshopersung

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import com.eusecom.samshopersung.models.InvoiceList
import com.eusecom.samshopersung.rxbus.RxBus
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.yesButton
import rx.Observable
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Kotlin fragment Recyclerview with classic XML itemlayout without Anko DSL
 * template samfantozzi CashListKtFragment.kt
 */

class OrderFragment : BaseKtFragment() {

    private var mAdapter: OrderAdapter? = null
    private var mRecycler: RecyclerView? = null
    private var mManager: LinearLayoutManager? = null
    private var balance: TextView? = null
    private var mSubscription: CompositeSubscription? = null
    private var _disposables = CompositeDisposable()
    private var mDisposable: Disposable? = null

    @Inject
    lateinit var mSharedPreferences: SharedPreferences

    @Inject
    lateinit var  _rxBus: RxBus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        Log.d("Fromfrg Order ", mViewModel.toString())

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
                        //andrejko showNewCashDocDialog()

                    }
                    if (event is Invoice) {

                        Log.d("onShortClickListenerFrg", event.nai)
                        if(!event.uce.equals("31100")){
                            getTodoDialog(event)
                        }



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
        mSubscription?.add(mViewModel.getMyOrdersFromSqlServer("0")
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OrderFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setServerOrders(it) })

        mSubscription?.add(mViewModel.getObservableDocPdf()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OrderFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setUriPdf(it) })

        mSubscription?.add(mViewModel.getObservableException()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OrderFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setProxyException(it) })

        mSubscription?.add(mViewModel.getObservableDeleteOrder()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OrderFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setServerOrders(it) })

        mSubscription?.add(mViewModel.getObservableOrderToInv()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OrderFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setServerOrders(it) })

        ActivityCompat.invalidateOptionsMenu(activity)
    }

    private fun unBind() {

        mViewModel.clearObservableDeleteOrder()
        mViewModel.clearObservableOrderToInv()
        mSubscription?.unsubscribe()
        mSubscription?.clear()
        _disposables.dispose()
        if (mDisposable != null) {
            mDisposable?.dispose()
        }
        hideProgressBar()

    }

    private fun setUriPdf(uri: Uri) {

        mViewModel.clearObservableDocPDF()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        //activity.finish()

    }

    private fun setServerOrders(invoices: InvoiceList) {

        mAdapter?.setAbsserver(invoices.getInvoice())
        balance?.setText(invoices.getBalance())
        hideProgressBar()
    }



    class ClickFobEvent


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

    fun getTodoDialog(invoice: Invoice) {

        val inflater = LayoutInflater.from(activity)
        val textenter = inflater.inflate(R.layout.invoice_edit_dialog, null)

        val valuex = textenter.findViewById<View>(R.id.valuex) as TextView
        valuex.text = invoice.hod

        val builder = AlertDialog.Builder(activity)
        builder.setView(textenter).setTitle(getString(R.string.order) + " " + invoice.dok)

        builder.setItems(arrayOf<CharSequence>(getString(R.string.pdfdoc), getString(R.string.deletewholedoc), getString(R.string.getinvoice))
        ) { dialog, which ->
            // The 'which' argument contains the index position
            // of the selected item
            when (which) {
                0 -> {
                    navigateToGetPdf(invoice)
                }
                1 -> {
                    showDeleteOrderDialog(invoice)
                }
                2 -> {
                    showGetInvoiceDialog(invoice)
                }

            }
        }
        val dialog = builder.create()
        builder.show()

    }

    fun navigateToGetPdf(order: Invoice){
        //showProgressBar()
        mViewModel.emitGetPdfOrder(order)

    }

    fun showDeleteOrderDialog(order: Invoice) {

        alert("", getString(R.string.deletewholedoc) + " " + order.dok) {
            yesButton { navigateToDeleteOrder(order) }
            noButton {}
        }.show()

    }

    fun navigateToDeleteOrder(order: Invoice){
        showProgressBar()
        mViewModel.emitDeleteOrder(order);

    }

    fun showGetInvoiceDialog(order: Invoice) {

        alert("", getString(R.string.getinvoicefrom) + " " + order.dok) {
            yesButton { navigateToGetInvoice(order) }
            noButton {}
        }.show()

    }

    fun navigateToGetInvoice(order: Invoice){
        showProgressBar()
        mViewModel.emitOrderToInv(order)

    }


}
