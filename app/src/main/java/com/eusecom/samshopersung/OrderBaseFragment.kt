package com.eusecom.samshopersung

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.eusecom.samshopersung.models.InvoiceList
import com.eusecom.samshopersung.rxbus.RxBus
import com.eusecom.samshopersung.soap.soapekassa.EkassaRegisterReceiptResponseEnvelope
import com.eusecom.samshopersung.soap.soapekassa.EkassaResponseEnvelope
import com.eusecom.samshopersung.soap.soaphello.HelloResponseEnvelope
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

abstract class OrderBaseFragment : BaseKtFragment() {

    private var mAdapter: OrderAdapter? = null
    private var mRecycler: RecyclerView? = null
    private var mManager: LinearLayoutManager? = null
    private var balance: TextView? = null
    var mSubscription: CompositeSubscription? = null
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

    abstract fun bindOrders()

    private fun bind() {

        _disposables = CompositeDisposable()

        val tapEventEmitter = _rxBus.asFlowable().publish()

        _disposables
                .add(tapEventEmitter.subscribe { event ->
                    if (event is OrderBaseFragment.ClickFobEvent) {
                        //andrejko showNewCashDocDialog()

                    }
                    if (event is Invoice) {

                        Log.d("onShortClickListenerFrg", event.nai)
                        if(!event.uce.equals(mSharedPreferences.getString("odbuce", ""))){
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
        bindOrders()

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
                .subscribe { it -> setServerOrderToInv(it) })

        mSubscription?.add(mViewModel.getObservableSoapEkassaResponse()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OrderFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setSoapResponse(it) })

        mSubscription?.add(mViewModel.getObservableRegisterReceiptEkassaResponse()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OrderFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setEkassaRegisterReceiptResponse(it) })

        mSubscription?.add(mViewModel.getObservableRegisterReceiptEkassaResponseXml()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OrderFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setEkassaRegisterReceiptResponseXml(it) })

        ActivityCompat.invalidateOptionsMenu(activity)
    }

    private fun unBind() {

        mViewModel.clearObservableDeleteOrder()
        mViewModel.clearObservableOrderToInv()
        mViewModel.clearObservableSoapEkassaResponse()
        mViewModel.clearObservableRegisterReceiptEkassaResponse()
        mViewModel.clearObservableRegisterReceiptEkassaResponseXml()
        mSubscription?.unsubscribe()
        mSubscription?.clear()
        _disposables.dispose()
        if (mDisposable != null) {
            mDisposable?.dispose()
        }
        hideProgressBar()

    }

    private fun setEkassaRegisterReceiptResponseXml(responseEnvelop: EkassaResponseEnvelope) {

        hideProgressBar()
        if (responseEnvelop != null) {
            //val helloresult = responseEnvelop.body.setHeader.setProcessDate
            //val helloresult = responseEnvelop.body.setReceiptData.setId
            //val helloresult = responseEnvelop.body.setReceiptData.toString()
            val helloresult = responseEnvelop.body.toString()
            Log.d("Reg. receipt result", helloresult)
            Toast.makeText(activity, helloresult, Toast.LENGTH_LONG).show()
        }

    }

    private fun setEkassaRegisterReceiptResponse(responseEnvelop: EkassaRegisterReceiptResponseEnvelope) {

        hideProgressBar()
        if (responseEnvelop != null) {
            val helloresult = responseEnvelop.body.getHelloResponse.result
            Log.d("Reg. receipt result", helloresult)
            Toast.makeText(activity, helloresult, Toast.LENGTH_LONG).show()
        }

    }

    private fun setSoapResponse(responseEnvelop: HelloResponseEnvelope) {

        hideProgressBar()
        if (responseEnvelop != null) {
            val helloresult = responseEnvelop.body.getHelloResponse.result
            Log.d("Soap Hello result", helloresult)
            Toast.makeText(activity, helloresult, Toast.LENGTH_LONG).show()
        }

    }

    private fun setUriPdf(uri: Uri) {

        mViewModel.clearObservableDocPDF()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        //activity.finish()

    }

    fun setServerOrders(invoices: InvoiceList) {

        mAdapter?.setAbsserver(invoices.getInvoice())
        balance?.setText(invoices.getBalance())
        hideProgressBar()
    }

    private fun setServerOrderToInv(invoices: InvoiceList) {

        mAdapter?.setAbsserver(invoices.getInvoice())
        balance?.setText(invoices.getBalance())
        hideProgressBar()

        val `is` = Intent(activity, OrderListActivity::class.java)
        val extras = Bundle()
        extras.putInt("saltype", 1)
        `is`.putExtras(extras)
        startActivity(`is`)
        activity.finish()
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

    abstract fun getTodoDialog(invoice: Invoice)

    fun navigateToGetPdf(order: Invoice){
        //showProgressBar()
        mViewModel.emitGetPdfOrder(order)

    }

    fun navigateOrderDetail(order: Invoice){

        val `is` = Intent(activity, OrderDetailActivity::class.java)
        val extras = Bundle()
        extras.putString("order", order.dok)
        `is`.putExtras(extras)
        startActivity(`is`)

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

    fun showGetEkassaDialog(order: Invoice) {

        alert("", getString(R.string.getekassafrom) + " " + order.dok) {
            yesButton { navigateToGetEkassa(order) }
            noButton {}
        }.show()

    }

    fun showGetEkassaHelloDialog(order: Invoice) {

        alert("", getString(R.string.getekassahellofrom) + " " + order.dok) {
            yesButton { navigateToGetEkassaHello(order) }
            noButton {}
        }.show()

    }


    fun navigateToGetInvoice(order: Invoice){
        showProgressBar()
        mViewModel.emitOrderToInv(order)

    }

    fun navigateToGetEkassa(order: Invoice){

        showProgressBar()
        //mViewModel.emitRegisterReceiptEkassa(order)
        mViewModel.emitRegisterReceiptEkassaXml(order)

    }

    fun navigateToGetEkassaHello(order: Invoice){

        showProgressBar()
        mViewModel.emitSoapEkassa(order)

    }


}
