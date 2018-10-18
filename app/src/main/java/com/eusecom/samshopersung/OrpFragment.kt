package com.eusecom.samshopersung

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.eusecom.samshopersung.rxbus.RxBus
import com.eusecom.samshopersung.soap.soapekassa.EkassaResponseEnvelope
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.noButton
import org.jetbrains.anko.okButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.yesButton
import rx.Observable
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Kotlin fragment Recyclerview with classic XML itemlayout without Anko DSL
 * template samfantozzi CashListKtFragment.kt
 */

class OrpFragment : BaseKtFragment() {

    private var mAdapter: OrpAdapter? = null
    private var mRecycler: RecyclerView? = null
    private var mManager: LinearLayoutManager? = null
    private var mSubscription: CompositeSubscription? = null
    private var _disposables = CompositeDisposable()
    private var mDisposable: Disposable? = null

    @Inject
    lateinit var mSharedPreferences: SharedPreferences

    @Inject
    lateinit var  _rxBus: RxBus

    private var paydocx = "0";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        Log.d("Fromfrg Order ", mViewModel.toString())

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater!!.inflate(R.layout.fragment_orp, container, false)

        mRecycler = rootView.findViewById<View>(R.id.list) as RecyclerView
        mRecycler?.setHasFixedSize(true)
        mProgressBar = rootView.findViewById<View>(R.id.progress_bar) as ProgressBar

        return rootView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mAdapter = OrpAdapter(_rxBus)
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
                    if (event is OrpFragment.ClickFobEvent) {
                        //andrejko showNewCashDocDialog()

                    }
                    if (event is Invoice) {

                        Log.d("onShortClickListenerFrg", event.nai)
                        getTodoDialog(event)


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
        mSubscription?.add(mViewModel.getMyInvoicesFromSqlServer("2")
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OrpFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setServerInvoices(it) })

        mSubscription?.add(mViewModel.getObservableEkasaPdf()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OrpFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setUriPdf(it) })

        mSubscription?.add(mViewModel.getObservableDeleteEkasaDoc()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OrpFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setServerInvoices(it) })

        mSubscription?.add(mViewModel.getObservableRegisterReceiptEkassaResponseXml()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OrpFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setEkassaRegisterReceiptResponseXml(it) })

        mSubscription?.add(mViewModel.getObservableEkasaDocPaid()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OrpFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setServerInvoices(it) })

        ActivityCompat.invalidateOptionsMenu(activity)
    }

    private fun unBind() {

        mViewModel.clearObservableDeleteEkasaDoc()
        mViewModel.clearObservableRegisterReceiptEkassaResponseXml()
        mViewModel.clearObservableEkasaDocPaid()
        mViewModel.clearObservableEkasaPDF()
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
            if (responseEnvelop.body.getRegisterReceiptResponse != null) {
                val processdate = responseEnvelop.body.getRegisterReceiptResponse.getHeader.getProcessDate.toString()
                val dokid = responseEnvelop.body.getRegisterReceiptResponse.getReceiptData.getId.toString()
                Log.d("Reg. receipt result", processdate + " " + dokid)
                mViewModel.emitEkasaDocPaid(paydocx);
                Toast.makeText(activity, processdate + " " + dokid, Toast.LENGTH_LONG).show()
            }else{
                val errcode = responseEnvelop.body.getRegisterReceiptFault.getEkasaErrorCode
                Toast.makeText(activity, "Error code " + errcode, Toast.LENGTH_LONG).show()
            }

        }

    }

    private fun setUriPdf(file: File) {

        file.absoluteFile.toString()
        Log.d("file.absoluteFile ", file.absoluteFile.toString())
        val external = FileProvider.getUriForFile(activity,
                BuildConfig.APPLICATION_ID + ".provider", file)

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(external, "application/pdf")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
        mViewModel.clearObservableEkasaPDF()

    }

    private fun setServerInvoices(invoices: List<Invoice>) {

        //var items: List<ProductItemKt> = invoices.get(0).items
        //Log.d("items.get(0).nat", items.get(0).nat)
        //Log.d("items.get(1).nat", items.get(1).nat)
        mAdapter?.setAbsserver(invoices)
        hideProgressBar()
    }



    class ClickFobEvent


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

    fun getTodoDialog(invoice: Invoice) {

        val inflater = LayoutInflater.from(activity)
        val textenter = inflater.inflate(R.layout.invoice_edit_dialog, null)

        val valuex = textenter.findViewById<View>(R.id.valuex) as TextView
        valuex.text = invoice.hod

        val builder = AlertDialog.Builder(activity)
        builder.setView(textenter).setTitle(getString(R.string.orpdoc) + " " + invoice.dok)

        builder.setItems(arrayOf<CharSequence>(getString(R.string.getekassa), getString(R.string.pdfdoc), getString(R.string.deletewholedoc))
        ) { dialog, which ->
            // The 'which' argument contains the index position
            // of the selected item
            when (which) {
                0 -> {
                    showGetEkassaDialog(invoice)
                }
                1 -> {
                    navigateToGetEkasaPdf(invoice)
                }
                2 -> {
                    showDeleteInvoiceDialog(invoice)
                }

            }
        }
        val dialog = builder.create()
        builder.show()

    }

    fun navigateToGetEkasaPdf(order: Invoice){
        showProgressBar()
        //order.uce="31100"
        //order.dok="730001"
        //order.drh="54"
        PDFBoxResourceLoader.init(context)
       mViewModel.emitEkasaPdf(order)

    }

    fun showDeleteInvoiceDialog(order: Invoice) {

        alert("", getString(R.string.deletewholedoc) + " " + order.dok) {
            yesButton { navigateToDeleteInvoice(order) }
            noButton {}
        }.show()

    }

    fun navigateToDeleteInvoice(order: Invoice){
        showProgressBar()
        mViewModel.emitDeleteEkasaDoc(order);

    }

    fun showGetEkassaDialog(order: Invoice) {

        if (order.ksy == "0") {
            alert("", getString(R.string.getekassafrom) + " " + order.dok) {
                yesButton { navigateToGetEkassa(order) }
                noButton {}
            }.show()
        }else{
            alert("", order.dok + " " + getString(R.string.docregistered)) {
                okButton {  }
            }.show()
        }


    }

    fun navigateToGetEkassa(order: Invoice){

        showProgressBar()
        paydocx=order.dok
        mViewModel.emitRegisterReceiptEkassaXml(order)

    }



}
