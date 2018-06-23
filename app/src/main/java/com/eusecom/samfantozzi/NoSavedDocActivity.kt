package com.eusecom.samfantozzi

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.toast
import javax.inject.Inject
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.eusecom.samfantozzi.realm.RealmEmployee
import com.eusecom.samfantozzi.realm.RealmInvoice
import io.realm.annotations.PrimaryKey
import rx.Observable


/**
 * Kotlin activity with ANKO Recyclerview with listener in adapter
 * by https://medium.com/@BhaskerShrestha/kotlin-and-anko-for-your-android-1c11054dd255
 * listener by https://choicetechlab.com/blog/add-recyclerview-using-kotlin/
 * github https://github.com/prajakta05/recyclerviewKotlin
 */

class NoSavedDocActivity : BaseListActivity() {

    @Inject
    lateinit var prefs: SharedPreferences

    @Inject
    lateinit var mViewModel: DgAllEmpsAbsMvvmViewModel

    var mSubscription: CompositeSubscription = CompositeSubscription()

    var fromact: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as SamfantozziApp).dgaeacomponent().inject(this)

        val i = intent

        //1=customers invoice, 2=supliers invoice, 3=cash document, 4=bank document, 5=internal document
        val extras = i.extras
        fromact = extras!!.getString("fromact")
        //toast("fromact " + fromact)

        val adapter: NoSavedDocAdapter = NoSavedDocAdapter(ArrayList<RealmInvoice>()){
            //toast("${it.nai + " " + it.dok } set")

            getTodoNoSavedDocDialog(it)
            //finish()
        }
        NoSavedDocActivityUI(adapter).setContentView(this)

        supportActionBar!!.setTitle(getString(R.string.action_nosaveddoc))

        val editor = prefs.edit()
        editor.putString("edidok", "0").apply()
        editor.commit()

        bind(adapter)

    }

    private fun bind(adapter: NoSavedDocAdapter) {

            showProgressDialog()
            mSubscription.add(mViewModel.getNoSavedDocFromRealm(fromact)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                    .doOnError { throwable -> Log.e("NoSavedDocAktivity ", "Error Throwable " + throwable.message) }
                    .onErrorResumeNext({ throwable -> Observable.empty() })
                    .subscribe({ it -> setNoSavedDocs(it, adapter) }))

            mSubscription.add(mViewModel.deletedInvoiceFromRealm()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                    .doOnError { throwable -> Log.e("NoSavedDocAktivity ", "Error Throwable " + throwable.message) }
                    .onErrorResumeNext({ throwable -> Observable.empty() })
                    .subscribe({ it -> deletedInvoice(it, adapter) }))

            mSubscription.add(mViewModel.deletedAllInvoicesFromRealm()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                    .doOnError { throwable -> Log.e("NoSavedDocAktivity ", "Error Throwable " + throwable.message) }
                    .onErrorResumeNext({ throwable -> Observable.empty() })
                    .subscribe({ it -> deletedInvoice(it, adapter) }))


            mSubscription.add(mViewModel.myObservableInvoiceToServer
                    .subscribeOn(Schedulers.computation())
                    .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                    .doOnError { throwable -> Log.e("NoSavedDocAktivity ", "Error Throwable " + throwable.message) }
                    .onErrorResumeNext({ throwable -> Observable.empty() })
                    .subscribe({ it -> savedInvoice(it) }))


    }

    private fun setNoSavedDocs(nosaveds: List<RealmInvoice>, adapter: NoSavedDocAdapter) {

        //toast("${nosaveds.get(0).dok } realminvoicedoc0")
        adapter.setdata(nosaveds)
        hideProgressDialog()
    }

    private fun deletedInvoice(nosaveds: List<RealmInvoice>, adapter: NoSavedDocAdapter) {

        adapter.setdata(nosaveds)
        hideProgressDialog()

    }

    private fun savedInvoice(savedinv: List<Invoice>) {

        //System.out.println("savedinvoice " + saveds);
        toast("${savedinv.get(0).dok } saved ")
        val invoicex = RealmInvoice()
        invoicex.drh = savedinv.get(0).drh
        invoicex.uce = savedinv.get(0).uce
        invoicex.dok = savedinv.get(0).dok
        invoicex.ico = savedinv.get(0).ico
        invoicex.nai = savedinv.get(0).nai
        invoicex.fak = savedinv.get(0).fak
        invoicex.ksy = savedinv.get(0).ksy
        invoicex.ssy = savedinv.get(0).ssy
        invoicex.ume = savedinv.get(0).ume
        invoicex.dat = savedinv.get(0).dat
        invoicex.daz = savedinv.get(0).daz
        invoicex.das = savedinv.get(0).das
        invoicex.poz = savedinv.get(0).poz
        invoicex.hod = savedinv.get(0).hod
        invoicex.zk0 = savedinv.get(0).zk0
        invoicex.zk1 = savedinv.get(0).zk1
        invoicex.dn1 = savedinv.get(0).dn1
        invoicex.zk2 = savedinv.get(0).zk2
        invoicex.dn2 = savedinv.get(0).dn2
        invoicex.saved = "true"
        invoicex.datm = ""
        invoicex.uzid = savedinv.get(0).uzid
        invoicex.kto = savedinv.get(0).kto
        invoicex.poh = savedinv.get(0).poh
        mViewModel.emitDeleteInvoiceFromRealm(invoicex);

    }


    override fun onDestroy() {
        super.onDestroy()
        mViewModel.clearDeleteInvoiceRealm()
        mViewModel.clearDeleteAllInvoicesRealm()
        mViewModel.clearObservableInvoiceToServer()
        mSubscription.unsubscribe()
        mSubscription.clear()

    }

    fun getTodoNoSavedDocDialog(invoice: RealmInvoice) {

        val inflater = LayoutInflater.from(this)
        val textenter = inflater.inflate(R.layout.nosaveddoc_todo_dialog, null)

        val valuex = textenter.findViewById<View>(R.id.valuex) as TextView
        valuex.text = invoice.hod

        val builder = AlertDialog.Builder(this)
        builder.setView(textenter).setTitle(getString(R.string.document) + " " + invoice.dok)

        builder.setItems(arrayOf<CharSequence>(getString(R.string.savedocserver), getString(R.string.deletedoc), getString(R.string.deletealldoc))
        ) { dialog, which ->
            // The 'which' argument contains the index position
            // of the selected item
            when (which) {
                0 -> {

                    val invoicex = RealmInvoice()
                    invoicex.drh = invoice.drh
                    invoicex.uce = invoice.uce
                    invoicex.dok = invoice.dok

                    invoicex.ico = invoice.ico
                    invoicex.nai = invoice.nai
                    invoicex.fak = invoice.fak
                    invoicex.ksy = invoice.ksy
                    invoicex.ssy = invoice.ssy
                    invoicex.ume = invoice.ume
                    invoicex.dat = invoice.dat
                    invoicex.daz = invoice.daz
                    invoicex.das = invoice.das
                    invoicex.poz = invoice.poz
                    invoicex.hod = invoice.hod
                    invoicex.zk0 = invoice.zk0
                    invoicex.zk1 = invoice.zk1
                    invoicex.dn1 = invoice.dn1
                    invoicex.zk2 = invoice.zk2
                    invoicex.dn2 = invoice.dn2
                    invoicex.saved = invoice.saved
                    invoicex.datm = invoice.datm
                    invoicex.uzid = invoice.uzid
                    invoicex.kto = invoice.kto
                    invoicex.poh = invoice.poh

                    showProgressDialog()
                    mViewModel.emitMyObservableInvoiceToServer(invoicex)
                }
                1 -> {
                    showProgressDialog()
                    mViewModel.emitDeleteInvoiceFromRealm(invoice)
                }
                2 -> {
                    showProgressDialog()
                    mViewModel.emitDeleteAllInvoicesFromRealm(invoice)
                }

            }
        }
        val dialog = builder.create()
        builder.show()

    }

}
