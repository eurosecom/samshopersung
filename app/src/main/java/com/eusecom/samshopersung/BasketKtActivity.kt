package com.eusecom.samshopersung

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.eusecom.samshopersung.realm.RealmDomain
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.mainshopper_activity.*
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

class BasketKtActivity : AppCompatActivity() {

    @Inject
    lateinit var prefs: SharedPreferences

    @Inject
    lateinit var mViewModel: ShopperIMvvmViewModel

    private lateinit var recyclerView: RecyclerView

    var mSubscription: CompositeSubscription = CompositeSubscription()
    private var mProgressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)

        setContentView(R.layout.basket_activity)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle(getString(R.string.mybasket))

        //Bind the recyclerview
        recyclerView = findViewById<RecyclerView>(R.id.rvAndroidVersions)
        mProgressBar = findViewById<View>(R.id.progress_bar) as ProgressBar

        //Add a LayoutManager
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recyclerView.addItemDecoration(LinearLayoutSpaceItemDecoration(10))

        bind();

        Log.d("ShopperMvvmViewModel", "instx " + mViewModel.toString())
        Log.d("SharedPreferences", "instx " + prefs.toString())

    }

    private fun bind() {

        showProgressBar()
        mSubscription.add(mViewModel.getMyBasketFromSqlServer()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("BasketKtActivity", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext({ throwable -> Observable.empty() })
                .subscribe({ it -> setBasket(it) }))

        mSubscription.add(mViewModel.myObservableSaveDomainToRealm
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("BasketKtActivity", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext({ throwable -> Observable.empty() })
                .subscribe({ it -> savedDomain(it) }))

    }

    private fun savedDomain(domain: RealmDomain) {
        toast("Server " + domain.domain)
    }

    private fun setBasket(basket: List<BasketKt>) {

        recyclerView.adapter = BasketKtAdapter(basket){
            toast("${it.xdok + " " + it.xcpl + " " + it.xnat } Clicked")


        }
        hideProgressBar()
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
        menuInflater.inflate(R.menu.basket_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_offer -> consume { navigateToOffer() }

        else -> super.onOptionsItemSelected(item)
    }

    fun navigateToOffer(){
        val intent = Intent(this, OfferKtActivity::class.java)
        startActivity(intent)
        finish()
    }

    //consume oncreateoptionmenu
    inline fun consume(f: () -> Unit): Boolean {
        f()
        return true
    }

    protected fun showProgressBar() {
        mProgressBar?.setVisibility(View.VISIBLE)
    }

    protected fun hideProgressBar() {
        mProgressBar?.setVisibility(View.GONE)
    }


}
