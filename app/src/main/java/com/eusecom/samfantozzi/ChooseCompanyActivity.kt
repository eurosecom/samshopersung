package com.eusecom.samfantozzi

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.eusecom.samfantozzi.realm.RealmDomain
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import rx.Observable
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * Kotlin activity Recyclerview with classic XML itemlayout without Anko DSL
 * by https://choicetechlab.com/blog/add-recyclerview-using-kotlin/
 * github https://github.com/prajakta05/recyclerviewKotlin
 */

class ChooseCompanyActivity : AppCompatActivity() {

    @Inject
    lateinit var prefs: SharedPreferences

    @Inject
    lateinit var mViewModel: DgAllEmpsAbsMvvmViewModel

    private lateinit var recyclerView: RecyclerView

    var mSubscription: CompositeSubscription = CompositeSubscription()
    private var mProgressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as SamfantozziApp).dgaeacomponent().inject(this)

        setContentView(R.layout.activity_choosecompany)

        supportActionBar!!.setTitle(getString(R.string.clickchoosecompany))

        //Bind the recyclerview
        recyclerView = findViewById<RecyclerView>(R.id.rvAndroidVersions)
        mProgressBar = findViewById<View>(R.id.progress_bar) as ProgressBar

        //Add a LayoutManager
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recyclerView.addItemDecoration(LinearLayoutSpaceItemDecoration(10))

        bind();

    }

    private fun bind() {

        showProgressBar()
        mSubscription.add(mViewModel.myCompaniesFromServer
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("ChooseCompanyActivity", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext({ throwable -> Observable.empty() })
                .subscribe({ it -> setCompanies(it) }))

        mSubscription.add(mViewModel.myObservableSaveDomainToRealm
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("ChooseCompanyActivity", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext({ throwable -> Observable.empty() })
                .subscribe({ it -> savedDomain(it) }))


    }

    private fun savedDomain(domain: RealmDomain) {
        toast("Server " + domain.domain)
    }

    private fun setCompanies(companies: List<CompanyKt>) {

        recyclerView.adapter = ChooseCompanyAdapter(companies){
            toast("${it.xcf + " " + it.naz + " " + it.rok } Clicked")

            var domainx: RealmDomain = RealmDomain()
            domainx.setDomain(prefs.getString("servername", ""))
            mViewModel.emitSaveDomainToRealm(domainx)

            val editor = prefs.edit()
            editor.putString("fir", it.xcf).apply();
            editor.putString("usico", it.firico).apply();
            editor.putString("firnaz", it.naz).apply();
            editor.putString("firiban", it.iban).apply();
            editor.putString("rok", it.rok).apply();
            editor.putString("ume", "01." + it.rok).apply();
            editor.putString("firduct", it.duj).apply();

            editor.putString("firdph", it.firdph).apply();
            editor.putString("firdph+", it.firdph1).apply();
            editor.putString("firdphľ", it.firdph2).apply();

            editor.putString("pokluce", it.pokluce).apply();
            editor.putString("pokldok", it.pokldok).apply();
            editor.putString("pokldov", it.pokldov).apply();

            editor.putString("bankuce", it.bankuce).apply();
            editor.putString("bankdok", it.bankdok).apply();
            editor.putString("odbuce", it.odbuce).apply();
            editor.putString("odbdok", it.odbdok).apply();
            editor.putString("doduce", it.doduce).apply();
            editor.putString("doddok", it.doddok).apply();
            editor.putString("genuce", it.genuce).apply();
            editor.putString("gendok", it.gendok).apply();

            editor.commit();
            val i = intent
            setResult(101, i)
            finish()
        }
        hideProgressBar()
    }

    protected fun showProgressBar() {
        mProgressBar?.setVisibility(View.VISIBLE)
    }

    protected fun hideProgressBar() {
        mProgressBar?.setVisibility(View.GONE)
    }

    override fun onDestroy() {
        super.onDestroy()
        mSubscription?.unsubscribe()
        mSubscription?.clear()
        hideProgressBar()
        mViewModel.clearObservableSaveDomainToRealm()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> consume { navigateToSettings() }

        else -> super.onOptionsItemSelected(item)
    }

    fun navigateToSettings(){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    //consume oncreateoptionmenu
    inline fun consume(f: () -> Unit): Boolean {
        f()
        return true
    }


}
