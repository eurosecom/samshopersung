package com.eusecom.samfantozzi

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.toast
import javax.inject.Inject
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import android.util.Log
import rx.Observable


/**
 * Kotlin activity with ANKO Recyclerview with listener in adapter
 * by https://medium.com/@BhaskerShrestha/kotlin-and-anko-for-your-android-1c11054dd255
 * listener by https://choicetechlab.com/blog/add-recyclerview-using-kotlin/
 * github https://github.com/prajakta05/recyclerviewKotlin
 */

class TaxPaymentsActivity : BaseListActivity() {

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

        val adapter: TaxPaymentsAdapter = TaxPaymentsAdapter(ArrayList<Account>()){
            toast("${it.accname + " " + it.accnumber } set")

        }
        TaxPaymentsActivityUI(adapter).setContentView(this)

        supportActionBar!!.setTitle(getString(R.string.taxpay))

        bind(adapter)

    }

    private fun bind(adapter: TaxPaymentsAdapter) {

            showProgressDialog()
            mSubscription.add(mViewModel.getMyTaxPaymentsFromSqlServer(fromact)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                    .doOnError {
                        throwable ->
                        Log.e("TaxPaymentsAktivity ", "Error Throwable " + throwable.message)
                        hideProgressDialog()
                    }
                    .onErrorResumeNext({ throwable -> Observable.empty() })
                    .subscribe({ it -> setAccountsFromServer(it, adapter) }))

    }



    private fun setAccountsFromServer(accounts: List<Account>, adapter: TaxPaymentsAdapter) {

        //toast("${accounts.get(0).accname } invoice0")
        adapter.setdata(accounts)
        hideProgressDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        mSubscription.unsubscribe()
        mSubscription.clear()
    }


}
