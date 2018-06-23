package com.eusecom.samfantozzi

import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import com.eusecom.samfantozzi.models.Attendance
import com.eusecom.samfantozzi.models.InvoiceList
import com.eusecom.samfantozzi.realm.RealmInvoice
import com.eusecom.samfantozzi.rxbus.RxBus
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import org.jetbrains.anko.AlertBuilder
import org.jetbrains.anko.AlertDialogBuilder
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

 */

class CashListKtFragment : Fragment() {

    private var mAdapter: CashListAdapter? = null
    private var mRecycler: RecyclerView? = null
    private var mManager: LinearLayoutManager? = null
    private var balance: TextView? = null

    private var mProgressBar: ProgressBar? = null

    private var mSubscription: CompositeSubscription? = null
    private var _disposables = CompositeDisposable()

    @Inject
    lateinit var mSharedPreferences: SharedPreferences

    @Inject
    lateinit var mViewModel: DgAllEmpsAbsMvvmViewModel

    @Inject
    lateinit var  _rxBus: RxBus

    private lateinit var alert: AlertBuilder<DialogInterface>

    //searchview
    private var searchView: SearchView? = null
    private var onQueryTextListener: SearchView.OnQueryTextListener? = null
    private var mDisposable: Disposable? = null
    protected var mSupplierSearchEngine: SupplierSearchEngine? = null
    var searchManager: SearchManager? = null
    private var invoiceszal: MutableList<Invoice>  = mutableListOf<Invoice>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity.application as SamfantozziApp).dgaeacomponent().inject(this)
        setHasOptionsMenu(true)

        Log.d("mViewModel frg ", mViewModel.toString())

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater!!.inflate(R.layout.fragment_cashlistkt, container, false)

        balance = rootView.findViewById<View>(R.id.balance) as TextView
        mRecycler = rootView.findViewById<View>(R.id.list) as RecyclerView
        mRecycler?.setHasFixedSize(true)
        mProgressBar = rootView.findViewById<View>(R.id.progress_bar) as ProgressBar

        return rootView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mAdapter = CashListAdapter(_rxBus)
        mAdapter?.setAbsserver(emptyList())
        mSupplierSearchEngine = SupplierSearchEngine(emptyList())
        // Set up Layout Manager, reverse layout
        mManager = LinearLayoutManager(context)
        mManager?.setReverseLayout(true)
        mManager?.setStackFromEnd(true)
        mRecycler?.setLayoutManager(mManager)
        mRecycler?.setAdapter(mAdapter)

    }//end of onActivityCreated

    private fun bind() {

        _disposables = CompositeDisposable()

        val tapEventEmitter = _rxBus.asFlowable().publish()

        _disposables
                .add(tapEventEmitter.subscribe { event ->
                    if (event is CashListKtFragment.ClickFobEvent) {
                        //Log.d("CashListKtActivity  ", " fobClick ")
                        showNewCashDocDialog()

                    }
                    if (event is Invoice) {

                        val usnamex = event.nai

                        //Log.d("CashListKtFragment ", usnamex)
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
        mSubscription?.add(mViewModel.getMyCashDocsFromSqlServer("3")
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("CashListKtFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setServerCashDocs(it) })

        mSubscription?.add(mViewModel.getMyInvoiceDelFromServer()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable -> Log.e("CashListKtFragment ", "Error Throwable " + throwable.message) }
                .onErrorResumeNext({ throwable -> Observable.empty() })
                .subscribe({ it -> deletedInvoice(it) }))

        mSubscription?.add(mViewModel.getMyObservableCashListQuery()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("CashListKtFragment", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setQueryString(it) })


        mSubscription?.add(mViewModel.getNoSavedDocFromRealm("3")
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable -> Log.e("CashListKtFragment ", "Error Throwable " + throwable.message) }
                .onErrorResumeNext({ throwable -> Observable.empty() })
                .subscribe({ it -> setNoSavedDocs(it) }))


        ActivityCompat.invalidateOptionsMenu(activity)
        (activity as AppCompatActivity).supportActionBar!!.setTitle(mSharedPreferences.getString("ume", "") + " "
                + mSharedPreferences.getString("pokluce", "") + " " + getString(R.string.cashdocuments))
    }

    private fun unBind() {

        mViewModel.clearObservableAbsencesFromFB()
        mViewModel.clearObservableCashListQuery()
        mViewModel.clearObservableInvoiceDelFromServer()
        mSubscription?.unsubscribe()
        mSubscription?.clear()
        _disposables.dispose()
        if (mDisposable != null) {
            mDisposable?.dispose()
        }
        hideProgressBar()

    }

    private fun deletedInvoice(saveds: List<Invoice>) {

        //System.out.println("savedinvoice " + saveds);
        toast("${saveds.get(0).dok } deleted pos. ${saveds.get(0).poh }")
        balance?.setText(saveds.get(0).hod)

        val pohx: String = saveds.get(0).poh
        //invoiceszal.removeAt(posx)
        //invoiceszal.remove(invoiceszal.get(posx))

        var invoiceszalnew: MutableList<Invoice>  = mutableListOf<Invoice>()
        val iterate = invoiceszal.listIterator()
        var ix: Int = 0
        while (iterate.hasNext()) {
            val oldValue = iterate.next()
            if (!oldValue.poh.equals(pohx))
            {
                System.out.println("iterate dok " + invoiceszal.get(ix).dok);
                invoiceszalnew.add(invoiceszal.get(ix))
            }
            ix = ix + 1

        }

        invoiceszal = invoiceszalnew
        mAdapter?.setAbsserver(invoiceszalnew)
        nastavResultAs(invoiceszalnew)
        hideProgressBar()

    }

    private fun setNoSavedDocs(nosaveds: List<RealmInvoice> ) {

        if( nosaveds.size > 0  ) {
            toast("${nosaveds.get(0).dok } to save the realminvoicedoc0")
        }


    }

    private fun setQueryString(querystring: String) {

        //toast(" querystring " + querystring)
        if( querystring.equals("")){

        }else {
            searchView?.setQuery(querystring, false)
        }

    }

    private fun setServerInvoices(invoices: List<Invoice>) {

        //Log.d("searchModel ", "in setServerInvoices")
        //toast(" nai0 " + invoices.get(0).nai)
        invoiceszal = invoices.toMutableList()
        mAdapter?.setAbsserver(invoices)
        nastavResultAs(invoices)
        hideProgressBar()
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
        mSupplierSearchEngine = SupplierSearchEngine(resultAs)
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
        Log.d("CashListKtFragment ", "onDestroy");
        unBind()

    }

    override fun onResume() {
        super.onResume()
        Log.d("CashListKtFragment ", "onResume");
        bind()
    }

    override fun onPause() {
        super.onPause()
        Log.d("CashListKtFragment ", "onPause");
        unBind()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {

        // Retrieve the SearchView and plug it into SearchManager
        inflater!!.inflate(R.menu.menu_listdoc, menu)
        searchView = MenuItemCompat.getActionView(menu!!.findItem(R.id.action_search)) as SearchView
        searchManager = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView?.setSearchableInfo(searchManager?.getSearchableInfo(activity.componentName))
        getObservableSearchViewText()
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

        if (id == R.id.action_setmonth) {

            val `is` = Intent(activity, ChooseMonthActivity::class.java)
            startActivity(`is`)
            return true
        }

        if (id == R.id.action_setaccount) {

            val `is` = Intent(activity, ChooseAccountActivity::class.java)
            val extras = Bundle()
            extras.putString("fromact", "3")
            `is`.putExtras(extras)
            startActivity(`is`)
            return true
        }

        if (id == R.id.action_nosaveddoc) {

            val `is` = Intent(activity, NoSavedDocActivity::class.java)
            val extras = Bundle()
            extras.putString("fromact", "3")
            `is`.putExtras(extras)
            startActivity(`is`)
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    //listener to searchview
    private fun getObservableSearchViewText() {

        val searchViewChangeStream = createSearchViewTextChangeObservable()

        mDisposable = searchViewChangeStream
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { showProgressBar() }
                .observeOn(io.reactivex.schedulers.Schedulers.io())
                .map(Function<String, List<Invoice>> {
                    query -> mSupplierSearchEngine?.searchModel(query)
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    hideProgressBar()
                    showResultAs(result)
                }

    }


    private fun createSearchViewTextChangeObservable(): io.reactivex.Observable<String> {
        val searchViewTextChangeObservable = io.reactivex.Observable.create(ObservableOnSubscribe<String> { emitter ->
            onQueryTextListener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    // use this method when query submitted
                    //Toast.makeText(getActivity(), "submit " + query, Toast.LENGTH_SHORT).show();
                    emitter.onNext(query.toString())
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    // use this method for auto complete search process
                    //Toast.makeText(getActivity(), "change " + newText, Toast.LENGTH_SHORT).show();
                    emitter.onNext(newText.toString())
                    mViewModel.emitMyObservableCashListQuery(newText.toString())
                    return false
                }


            }

            searchView?.setOnQueryTextListener(onQueryTextListener)

            emitter.setCancellable { searchView?.setOnQueryTextListener(null) }
        })

        return searchViewTextChangeObservable
                .filter { query -> query.length >= 3 || query == "" }.debounce(300, TimeUnit.MILLISECONDS)  // add this line
    }


    fun getTodoDialog(invoice: Invoice) {

        val inflater = LayoutInflater.from(activity)
        val textenter = inflater.inflate(R.layout.invoice_edit_dialog, null)

        val valuex = textenter.findViewById<View>(R.id.valuex) as TextView
        valuex.text = invoice.hod

        val builder = AlertDialog.Builder(activity)
        builder.setView(textenter).setTitle(getString(R.string.document) + " " + invoice.dok)

        builder.setItems(arrayOf<CharSequence>(getString(R.string.pdf), getString(R.string.edit), getString(R.string.delete))
        ) { dialog, which ->
            // The 'which' argument contains the index position
            // of the selected item
            when (which) {
                0 -> {
                    //mViewModel.emitDocumentPdfUri(invoice)
                    val `is` = Intent(activity, ShowPdfActivity::class.java)
                    val extras = Bundle()
                    extras.putString("fromact", "3")
                    extras.putString("drhx", invoice.drh)
                    extras.putString("ucex", invoice.uce)
                    extras.putString("dokx", invoice.dok)
                    extras.putString("icox", invoice.ico)
                    `is`.putExtras(extras)
                    startActivity(`is`)
                }
                1 -> {
                    navigateToEditDoc(invoice)
                    //editDialog(invoice).show()
                }
                2 -> {
                    showDeleteDialog(invoice)
                }
            }
        }
        val dialog = builder.create()
        builder.show()

    }

    fun showNewCashDocDialog() {

        alert(getString(R.string.drupoh_summary), getString(R.string.createdoc)) {
            //yesButton { navigateToNewCashDoc(1) }
            //noButton { navigateToNewCashDoc(2) }
            positiveButton(getString(R.string.expense), { _ -> navigateToNewCashDoc(2) } )
            negativeButton(getString(R.string.receipt), { _ -> navigateToNewCashDoc(1) } )
        }.show()

    }

    fun navigateToNewCashDoc(drupoh: Int){

        val drupohx: String = drupoh.toString()
        val `is` = Intent(context, NewCashDocKtActivity::class.java)
        val extras = Bundle()
        extras.putString("drupoh", drupohx)
        extras.putString("newdok", "1")
        extras.putString("edidok", "0")
        `is`.putExtras(extras)
        startActivity(`is`)

        //getActivity().startActivity<NewCashDocKtActivity>()

    }


    fun showEditDialog(invoice: Invoice) {

        alert("", getString(R.string.createdoc) + " " + invoice.dok) {
            yesButton { navigateToEditDoc(invoice) }
            noButton {}
        }.show()

    }

    fun navigateToEditDoc(invoice: Invoice){

        var drupohx: String = "1"
        if (invoice.drh.equals("32")){ drupohx = "2" }

        val `is` = Intent(context, NewCashDocKtActivity::class.java)
        val extras = Bundle()
        extras.putString("drupoh", drupohx)
        extras.putString("newdok", "0")
        extras.putString("edidok", invoice.dok)
        `is`.putExtras(extras)
        startActivity(`is`)
        //getActivity().startActivity<InvoiceListKtActivity>()

    }

    fun showDeleteDialog(invoice: Invoice) {

        alert("", getString(R.string.deletedoc) + " " + invoice.dok) {
            yesButton { navigateToDeleteDoc(invoice)  }
            noButton {}
        }.show()

    }

    fun navigateToDeleteDoc(invoice: Invoice){
        //getActivity().startActivity<InvoiceListKtActivity>()
        showProgressBar()
        mViewModel.emitDelInvFromServer(invoice)

    }


}
