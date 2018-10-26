package com.eusecom.samshopersung

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import dagger.android.AndroidInjection
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import rx.Observable
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject
import android.widget.TextView
import com.eusecom.samshopersung.models.IShopperModelsFactory
import com.eusecom.samshopersung.realm.RealmInvoice
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.basket_activity.*



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

    @Inject
    lateinit var mModelsFactory: IShopperModelsFactory

    @Inject
    lateinit var mImageUrl: ImageUrl

    private lateinit var recyclerView: RecyclerView

    var mSubscription: CompositeSubscription = CompositeSubscription()
    private var mProgressBar: ProgressBar? = null
    private lateinit var mybasket: MutableList<BasketKt>
    var totmno: String = "0"
    var tothdd: String = "0.00"
    var totalbasket: TextView? = null
    var totalbasketeur: TextView? = null
    var myfir: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)

        setContentView(R.layout.basket_activity)
        //val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        initCollapsingToolbar()

        totalbasket = findViewById<View>(R.id.totalbasket) as TextView
        totalbasket?.text = getString(R.string.totalbasket, totmno )
        totalbasketeur = findViewById<View>(R.id.totalbasketeur) as TextView
        totalbasketeur?.text = getString(R.string.totalbasketeur, tothdd)
        myfir = findViewById<View>(R.id.myfir) as TextView

        //Bind the recyclerview
        recyclerView = findViewById<RecyclerView>(R.id.rvAndroidVersions)
        mProgressBar = findViewById<View>(R.id.progress_bar) as ProgressBar

        //Add a LayoutManager
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recyclerView.addItemDecoration(LinearLayoutSpaceItemDecoration(10))

        bind();

        Log.d("ShopperMvvmViewModel", "instx " + mViewModel.toString())
        Log.d("SharedPreferences", "instx " + prefs.toString())

        fab.setOnClickListener {
            val editor = prefs.edit()
            editor.putString("edidok", "0").apply();
            editor.commit();

            val `is` = Intent(this, OfferKtActivity::class.java)
            startActivity(`is`)
            finish()
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setTransitionName("buttontofab")
        }

    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private fun initCollapsingToolbar() {
        val collapsingToolbar = findViewById<View>(R.id.collapsing_toolbar) as CollapsingToolbarLayout
        collapsingToolbar.title = " "
        val appBarLayout = findViewById<View>(R.id.appbar) as AppBarLayout
        appBarLayout.setExpanded(true)

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            internal var isShow = false
            internal var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.title = getString(R.string.mybasket)
                    isShow = true
                } else if (isShow) {
                    collapsingToolbar.title = " "
                    isShow = false
                }
            }
        })
    }

    private fun bind() {

        showProgressBar()
        mSubscription.add(mViewModel.getMySumBasketFromSqlServer()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("BasketKtActivity", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext({ throwable -> Observable.empty() })
                .subscribe({ it -> setSumBasket(it) }))

        mSubscription.add(mViewModel.getMyObservableSaveSumBasketToServer()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("BasketKtActivity", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext({ throwable -> Observable.empty() })
                .subscribe({ it -> setDeletedBasket(it) }))

        mSubscription.add(mViewModel.getMyIdcData("3")
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("BasketKtActivity", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext({ throwable -> Observable.empty() })
                .subscribe({ it -> setNoSavedDocs(it) }))

    }

    private fun setNoSavedDocs(idc: List<RealmInvoice>) {

        //Toast.makeText(this, "Getting ID " + idc.get(0).getIco(), Toast.LENGTH_SHORT).show();

        if (idc.size > 0) {

            val myfirx: String = idc[0].ico + " - " + idc[0].kto
            myfir?.text = getString(R.string.myidc, myfirx )


        }

    }

    private fun setDeletedBasket(sumbasket: SumBasketKt) {

        totmno = sumbasket.smno
        tothdd = sumbasket.shdd
        totalbasket?.text = getString(R.string.totalbasket, totmno )
        totalbasketeur?.text = getString(R.string.totalbasketeur, tothdd)

        var basket: List<BasketKt> = sumbasket.basketitems

        if (basket.get(0).xid == "4") {
            toast(basket.get(0).xnat + " " + getString(R.string.deletedfrombasket))
        }
        if (basket.get(0).xid == "5") {
            toast(getString(R.string.allitems) + " " + getString(R.string.deletedfrombasket))
        }
        if (basket.get(0).xid == "6") {
            toast(basket.get(0).xnat + " " + getString(R.string.itemsordered))
        }
        if (basket.get(0).xid == "7") {
            toast(basket.get(0).xnat + " " + getString(R.string.movedtofav))
        }
        //Log.d("savedBasket ", basket.get(0).xnat);
        hideProgressBar()

        var posd: Int = Integer.parseInt(basket.get(0).xdph);

        if (basket.get(0).xid != "5" && basket.get(0).xid != "6") {
            mybasket.removeAt(posd)
            recyclerView?.adapter?.notifyItemRemoved(posd)
        }else {
            mybasket.clear()
        }

        recyclerView?.adapter?.notifyDataSetChanged()

    }

    private fun setBasket(basket: List<BasketKt>) {


    }

    private fun setSumBasket(sumbasket: SumBasketKt) {

        //Log.d("SumBasket0 ", sumbasket.basketitems.get(0).xnat);
        totmno = sumbasket.smno
        tothdd = sumbasket.shdd
        totalbasket?.text = getString(R.string.totalbasket, totmno )
        totalbasketeur?.text = getString(R.string.totalbasketeur, tothdd)

        mybasket = sumbasket.basketitems.toMutableList()
        recyclerView.adapter = BasketKtAdapter(mImageUrl, mybasket){it: BasketKt, posx: Int, type: Int ->

            //classic instance of factory
            // var shoppermodelsfactory: IShopperModelsFactory = ShopperModelsFactory()
            //dagger2 instance
            var mprod: ProductKt = mModelsFactory.productKt

            mprod.cis = it.xcis
            mprod.nat = it.xnat
            mprod.dph = posx.toString()
            mprod.zas = it.xcpl



            if( type == 0 ){
                mprod.prm1 = "4"
                showDeleteFromBasketDialog(mprod)
            }else{
                mprod.prm1 = "7"
                showMoveToFavtDialog(mprod)
            }



        }
        hideProgressBar()

    }

    override fun onDestroy() {
        super.onDestroy()
        mSubscription?.unsubscribe()
        mSubscription?.clear()
        hideProgressBar()
        mViewModel.clearMyObservableSaveSumBasketToServer()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.basket_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_offer -> consume { navigateToOffer() }
        R.id.clear_basket -> consume { showClearBasketDialog() }
        R.id.action_pay -> consume { showOrderBasketDialog() }

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

    fun showDeleteFromBasketDialog(product: ProductKt) {

        alert("", getString(R.string.action_delete_item) + " " + product.nat) {
            yesButton { navigateToDeleteFromBasket(product) }
            noButton {}
        }.show()

    }

    fun navigateToDeleteFromBasket(product: ProductKt){
        showProgressBar()
        mViewModel.emitMyObservableSaveSumBasketToServer(product)

    }

    fun showMoveToFavtDialog(product: ProductKt) {

        alert("", getString(R.string.movetofav) + " " + product.nat) {
            yesButton { navigateToMoveToFav(product) }
            noButton {}
        }.show()

    }

    fun navigateToMoveToFav(product: ProductKt){
        showProgressBar()
        mViewModel.emitMyObservableSaveSumBasketToServer(product)

    }

    fun showClearBasketDialog() {

        alert("", getString(R.string.clear_basket)) {
            yesButton { navigateToClearBasket() }
            noButton {}
        }.show()

    }

    fun navigateToClearBasket(){
        showProgressBar()

        //classic instance of factory
        // var shoppermodelsfactory: IShopperModelsFactory = ShopperModelsFactory()
        //dagger2 instance
        var mprod: ProductKt = mModelsFactory.productKt
        mprod.prm1 = "5"
        mprod.nat = getString(R.string.allitems)
        mViewModel.emitMyObservableSaveSumBasketToServer(mprod)

    }

    fun showOrderBasketDialog() {

        alert("", getString(R.string.order_basket)) {
            yesButton { navigateToOrderBasket() }
            noButton {}
        }.show()

    }

    fun navigateToOrderBasket(){
        //showProgressBar()

        var mprod: ProductKt = mModelsFactory.productKt
        mprod.prm1 = "6"
        mprod.nat = getString(R.string.allitems)
        mprod.desc = myfir?.text.toString()
        mprod.prm2 = prefs.getString("mfir", "");
        mViewModel.emitMyObservableSaveSumBasketToServer(mprod)

    }

}
