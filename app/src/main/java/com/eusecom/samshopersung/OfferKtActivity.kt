package com.eusecom.samshopersung

import android.Manifest
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.*
import android.support.v7.widget.SearchView
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import co.zsmb.materialdrawerkt.builders.accountHeader
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import co.zsmb.materialdrawerkt.draweritems.sectionHeader
import com.bumptech.glide.Glide
import com.eusecom.samshopersung.di.ShopperScope
import com.eusecom.samshopersung.rxbus.RxBus
import com.google.zxing.integration.android.IntentIntegrator
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjection
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import kotlinx.android.synthetic.main.mainshopper_activity.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import rx.Observable
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.ArrayList
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Kotlin activity Recyclerview with classic XML itemlayout without Anko DSL
 * by https://choicetechlab.com/blog/add-recyclerview-using-kotlin/
 * github https://github.com/prajakta05/recyclerviewKotlin
 */

class OfferKtActivity : AppCompatActivity() {

    private lateinit var result: Drawer
    private lateinit var headerResult: AccountHeader
    private lateinit var headerProfil: ProfileDrawerItem

    private var recyclerView: RecyclerView? = null
    private var adapter: OfferProductAdapter? = null
    private var productList: MutableList<ProductKt>? = null
    private var offersubtitle: TextView? = null


    //searchview
    private var searchView: SearchView? = null
    private var menuItem: MenuItem? = null
    private var searchManager: SearchManager? = null
    private var querystring = ""
    private var mDisposable: Disposable? = null
    private var onQueryTextListener: SearchView.OnQueryTextListener? = null

    @Inject
    lateinit var prefs: SharedPreferences

    @Inject
    lateinit var  _rxBus: RxBus

    @Inject
    lateinit var  dgEventsStates: EventsStatesKt

    @ShopperScope
    @Inject
    lateinit var mViewModel: ShopperIMvvmViewModel

    @Inject
    lateinit var mPicasso: Picasso

    @Inject
    lateinit var mImageUrl: ImageUrl

    var mSubscription: CompositeSubscription = CompositeSubscription()
    private var _disposables = CompositeDisposable()
    private var mProgressBar: ProgressBar? = null
    private var mcount: String = "0"
    protected var isCollapsed: Boolean = true

    private val PERMISSION_REQUEST_CODE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.offerproduct_activity)
        //val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        Log.d("OffAct lc", "onCreate " + dgEventsStates.querystring + dgEventsStates.collapsed.toString())
        setQueryString(dgEventsStates.querystring)
        isCollapsed=dgEventsStates.collapsed

        initCollapsingToolbar()

        recyclerView = findViewById<View>(R.id.recycler_view) as RecyclerView
        mProgressBar = findViewById<View>(R.id.progress_bar) as ProgressBar
        offersubtitle = findViewById<View>(R.id.offersubtitle) as TextView

        fab.setOnClickListener {
            val integrator = IntentIntegrator(this@OfferKtActivity)
            integrator.initiateScan()
        }

        _disposables = CompositeDisposable()

        val tapEventEmitter = _rxBus.asFlowable().publish()

        _disposables
                .add(tapEventEmitter.subscribe { event ->
                    if (event is ProductKt) {

                        val usnamex = event.nat + " " + event.prm1

                        Log.d("OfferKtActivityBus ", usnamex)
                        if(event.prm1.equals("1")){
                            showAddToBasketDialog(event)
                        }
                        if(event.prm1.equals("11")){
                            showAddToFavDialog(event)
                        }
                        if(event.prm1.equals("3")){
                            goToDetail(event.cis)
                        }
                        if(event.prm1.equals("14")){
                            showDelFromFavDialog(event)
                        }



                    }

                    if (event is SumBasketKt) {

                        mcount = event.smno;
                        //Log.d("ProductDetailFragment ", mcount)
                    }

                })

        _disposables
                .add(tapEventEmitter.publish { stream -> stream.buffer(stream.debounce(1, TimeUnit.SECONDS)) }
                        .observeOn(AndroidSchedulers.mainThread()).subscribe { taps ->
                    ///_showTapCount(taps.size()); OK
                })

        _disposables.add(tapEventEmitter.connect())

        productList = ArrayList<ProductKt>().toMutableList()
        adapter = OfferProductAdapter(this, productList, _rxBus, mPicasso, mImageUrl )

        val mLayoutManager = GridLayoutManager(this, 2)
        recyclerView?.setLayoutManager(mLayoutManager)
        recyclerView?.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(10), true))
        recyclerView?.setItemAnimator(DefaultItemAnimator())
        recyclerView?.setAdapter(adapter)

        //old version
        //prepareAlbums()
        //new Mvvm approach
        bind()

        try {
            Glide.with(this).load(R.drawable.pozadie).into(findViewById<View>(R.id.backdrop) as ImageView)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //kotlin drawer by https://github.com/zsmb13/MaterialDrawerKt
        result = drawer {

            toolbar = this@OfferKtActivity.toolbar
            hasStableIds = true
            savedInstance = savedInstanceState
            showOnFirstLaunch = false

            headerResult = accountHeader {
                background = R.drawable.pozadie
                savedInstance = savedInstanceState
                translucentStatusBar = true

            }

            sectionHeader(getString(R.string.choosecat)) {
                divider = false
            }

            divider {}
            primaryItem(getString(R.string.allcat)) {

                onClick { _ ->
                    navigateToCategory("0", "")
                    false
                }

            }

            divider {}
            primaryItem(getString(R.string.favitems)) {

                onClick { _ ->
                    //Log.d("DRAWER", "Click.")
                    navigateToCategory("99999", "")
                    false
                }
            }



        }

        if (querystring == "") {
            emitMyCatProductsFromSqlServer("0")
        } else {
            emitMyQueryProductsFromSqlServer(querystring)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){

            }else{
                requestPermission()
            }
        }else{

        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setTransitionName("buttontofab")
        }

    }

    private fun bind() {

        showProgressBar()

        mSubscription.add(getMyProductsFromSqlServer("0")
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OfferKtActivity", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext({ throwable -> Observable.empty() })
                .subscribe({ it -> setServerFirstProducts(it) }))

        mSubscription.add(getMyCatsFromSqlServer("1")
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OfferKtActivity", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext({ throwable -> Observable.empty() })
                .subscribe({ it -> setServerCategories(it) }))

        mSubscription.add(getMyObservableSaveSumBasketToServer()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OfferKtActivity", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext({ throwable -> Observable.empty() })
                .subscribe({ it -> setSavedBasket(it) }))

        mSubscription.add(getMyCatProductsFromSqlServer()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OfferKtActivity", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext({ throwable -> Observable.empty() })
                .subscribe({ it -> setServerProducts(it) }))

        mSubscription.add(getMyQueryProductsFromSqlServer()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OfferKtActivity", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext({ throwable -> Observable.empty() })
                .subscribe({ it -> setServerProducts(it) }))

    }

    private fun setSavedBasket(sumbasket: SumBasketKt) {

        if(sumbasket.sprm1.equals("0")) {
            toast(sumbasket.basketitems.get(0).xnat + " " + getString(R.string.savedtobasket))
            //Log.d("savedBasket ", basket.get(0).xnat);
            mcount = sumbasket.smno;
            showBasketItemsCount()
        }
        if(sumbasket.sprm1.equals("1") && sumbasket.sprm2.equals("1")){
            toast(sumbasket.basketitems.get(0).xnat + " " + getString(R.string.savedtofav))
        }
        hideProgressBar()
        if(sumbasket.sprm1.equals("1") && sumbasket.sprm2.equals("4")){
            toast(sumbasket.basketitems.get(0).xnat + " " + getString(R.string.deletedfromfav))
            navigateToCategory("99999", "")
        }

    }

    private fun setServerCategories(cats: List<CategoryKt>) {

        //Log.d("showCategory ", cats.get(0).nac);

        cats.forEach { i ->

            result.addItemsAtPosition(6, DividerDrawerItem())
            result.addItemsAtPosition(7, PrimaryDrawerItem().withName(i.cat + " " + i.nac)
                    .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                        override fun onItemClick(view: View, position: Int, drawerItem: IDrawerItem<out Any?, out RecyclerView.ViewHolder>?): Boolean {
                            Log.d("DRAWER", i.cat + " " + i.nac + " Clicked!")

                            navigateToCategory(i.cat, i.nac)
                            //return true //remain opened drawer
                            return false
                        }
                    })

            )
        }



    }

    private fun setServerFirstProducts(products: List<ProductKt>) {


        mcount = products.get(0).prm1;
        showBasketItemsCount()
        //setServerProducts(products)

    }

    private fun setServerProducts(products: List<ProductKt>) {

        //Log.d("showProduct ", products.get(0).nat);
        //Log.d("showProduct ", products.get(0).prm1);
        productList = products.toMutableList()
        adapter?.setProductItems(productList)

        if (querystring == "") {


        } else {
            Log.d("newText setServerProd", querystring)
            searchView?.setIconified(false)
            searchView?.setQuery(querystring, false)
            menuItem?.setVisible(true)

        }

        hideProgressBar()
    }

    private fun setQueryString(querystringx: String) {

        Log.d("newText querystringx", querystringx)
        if (querystringx == "") {

        } else {
            querystring = querystringx
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        mSubscription?.unsubscribe()
        mSubscription?.clear()
        _disposables.dispose()
        if (mDisposable != null) {
            mDisposable?.dispose()
        }
        hideProgressBar()
        mViewModel.clearMyObservableSaveSumBasketToServer()
        mViewModel.clearMyCatProductsFromSqlServe()
        mViewModel.clearMyQueryProductsFromSqlServe()

        Log.d("OffAct lc", "onDestroy")
    }

    override fun onResume() {
        super.onResume()
        ActivityCompat.invalidateOptionsMenu(this)
        Log.d("OffAct lc", "onResume")
    }

    override fun onPause() {
        super.onPause()
        dgEventsStates.querystring = querystring
        dgEventsStates.collapsed = isCollapsed
        Log.d("OffAct lc", "onPause")
    }

    protected fun getMyProductsFromSqlServer(category: String): Observable<List<ProductKt>>  {
        return mViewModel.getMyProductsFromSqlServer(category);
    }

    protected fun getMyCatsFromSqlServer(category: String): Observable<List<CategoryKt>>  {
        return mViewModel.getMyCatsFromSqlServer(category);
    }

    protected fun getMyObservableSaveSumBasketToServer(): Observable<SumBasketKt>  {
        return mViewModel.getMyObservableSaveSumBasketToServer()
    }

    protected fun emitMyCatProductsFromSqlServer(category: String)  {
        return mViewModel.emitMyCatProductsFromSqlServer(category);
    }

    protected fun getMyCatProductsFromSqlServer(): Observable<List<ProductKt>>   {
        return mViewModel.getMyCatProductsFromSqlServer();
    }

    protected fun getMyQueryProductsFromSqlServer(): Observable<List<ProductKt>>   {
        return mViewModel.getMyQueryProductsFromSqlServer();
    }

    protected fun emitMyQueryProductsFromSqlServer(query: String)  {
        return mViewModel.emitMyQueryProductsFromSqlServer(query);
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
                    isCollapsed = false
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.title = getString(R.string.offeritems)
                    isShow = true
                    isCollapsed = true
                } else if (isShow) {
                    collapsingToolbar.title = " "
                    isShow = false
                    isCollapsed = false
                }
            }
        })
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    inner class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing // item bottom
            } else {
                outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing // item top
                }
            }
        }
    }


    /**
     * Converting dp to pixel
     */
    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics))
    }

    //option menu
    var item: MenuItem? = null;
    var notifView: RelativeLayout? = null
    var notifCount: TextView? = null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.offeractivity_menu, menu)

        item = menu.findItem(R.id.action_badge)
        MenuItemCompat.setActionView(item, R.layout.offeractivity_basket_menuitem)
        notifView = MenuItemCompat.getActionView(item) as RelativeLayout?
        notifCount = notifView?.findViewById<View>(R.id.actionbar_itemsamount_textview) as TextView?
        notifCount?.text = mcount

        searchView = MenuItemCompat.getActionView(menu!!.findItem(R.id.action_search)) as SearchView
        menuItem = menu.findItem(R.id.action_search)
        searchManager = this.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView?.setSearchableInfo(searchManager?.getSearchableInfo(this.componentName))
        searchView?.setQueryHint(getString(R.string.searchhint))
        getObservableSearchViewText()


        return true
    }

    fun goToDetail(cis: String){

        val edidokprev = prefs.getString("edidok", "0")

        val editor = prefs.edit()
        editor.putString("edidok", cis).apply();
        editor.commit();

        if(!edidokprev.equals("FINDITEM")){
            val intent = Intent(this, ProductDetailActivity::class.java)
            startActivity(intent)
        }else{
            finish()
        }

    }

    fun goToBasket(view: View){
        val intent = Intent(this, BasketKtActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun showBasketItemsCount() {

        invalidateOptionsMenu()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_badge -> consume { navigateToBasket() }

        else -> super.onOptionsItemSelected(item)
    }

    fun navigateToBasket(){
        val intent = Intent(this, BasketKtActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun navigateToCategory(cat: String, nac: String){
        offersubtitle?.text = getString(R.string.category) + " " + cat + " " + nac
        if(cat.equals("0")) { offersubtitle?.text = getString(R.string.allcat) }
        if(cat.equals("99999")) { offersubtitle?.text = getString(R.string.favitems) }
        //next 6 lines collapse searchview widget, i do not want yet
        //querystring = ""
        //searchView?.setQuery(querystring, true)
        //searchView?.setIconified(true)
        //searchView?.clearFocus()
        //searchView?.onActionViewCollapsed()
        //menuItem?.collapseActionView()
        showProgressBar()
        emitMyCatProductsFromSqlServer(cat)
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

    fun showAddToBasketDialog(product: ProductKt) {

        alert("", getString(R.string.action_add_tobasket) + " " + product.nat) {
            yesButton { navigateToAddToBasket(product) }
            noButton {}
        }.show()

    }

    fun navigateToAddToBasket(product: ProductKt){
        showProgressBar()
        mViewModel.emitMyObservableSaveSumBasketToServer(product)

    }

    fun showAddToFavDialog(product: ProductKt) {

        alert("", getString(R.string.action_add_favourite) + " " + product.nat) {
            yesButton { navigateToAddToFav(product)  }
            noButton {}
        }.show()

    }

    fun navigateToAddToFav(product: ProductKt){
        showProgressBar()
        mViewModel.emitMyObservableSaveSumBasketToServer(product)

    }

    fun showDelFromFavDialog(product: ProductKt) {

        alert("", getString(R.string.action_del_favourite) + " " + product.nat) {
            yesButton { navigateToDelFromFav(product)  }
            noButton {}
        }.show()

    }

    fun navigateToDelFromFav(product: ProductKt){
        showProgressBar()

        product.prm1 = "14"
        mViewModel.emitMyObservableSaveSumBasketToServer(product)

    }


    //listener to searchview
    private fun getObservableSearchViewText() {

        val searchViewChangeStream = createSearchViewTextChangeObservable()

        mDisposable = searchViewChangeStream
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { showProgressBar() }
                .observeOn(io.reactivex.schedulers.Schedulers.io())
                .map(Function<String, Unit> {
                    query -> emitMyQueryProductsFromSqlServer(query)
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    offersubtitle?.text = getString(R.string.allcat)
                    showProgressBar()
                }

    }


    private fun createSearchViewTextChangeObservable(): io.reactivex.Observable<String> {
        val searchViewTextChangeObservable = io.reactivex.Observable.create(ObservableOnSubscribe<String> { emitter ->
            onQueryTextListener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    // use this method when query submitted
                    querystring=query.toString()
                    emitter.onNext(query.toString())
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    // use this method for auto complete search process
                    Log.d("newText", newText.toString())
                    querystring=newText.toString()
                    //setQueryString(newText.toString())
                    emitter.onNext(newText.toString())

                    return false
                }


            }

            searchView?.setOnQueryTextListener(onQueryTextListener)

            emitter.setCancellable { searchView?.setOnQueryTextListener(null) }
        })

        return searchViewTextChangeObservable
                .filter { query -> query.length >= 3 || query.equals("")}.debounce(400, TimeUnit.MILLISECONDS)  // add this line
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        val scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (scanResult != null) {
            var re: String? = scanResult.contents
            //andrejko inputEan?.setText(re)

            re?.let {
                toast( getString(R.string.popisean) + " " + re )
                querystring = re
                emitMyQueryProductsFromSqlServer(querystring)
            }

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                //permission is OK

            }
        }
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
        val result1 = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
        val result2 = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        return result == PackageManager.PERMISSION_GRANTED
                && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA
                , Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                , PERMISSION_REQUEST_CODE)

    }

}
