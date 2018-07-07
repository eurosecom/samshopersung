package com.eusecom.samshopersung

import android.app.SearchManager
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.*
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import co.zsmb.materialdrawerkt.builders.accountHeader
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.badgeable.secondaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import co.zsmb.materialdrawerkt.draweritems.sectionHeader
import com.bumptech.glide.Glide
import com.eusecom.samshopersung.di.ShopperScope
import com.eusecom.samshopersung.models.Album
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.mainshopper_activity.*
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import rx.Observable
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.ArrayList
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
    private var albumList: MutableList<Album>? = null
    private var productList: MutableList<ProductKt>? = null

    //searchview from DocSearchActivity
    private var searchView: SearchView? = null
    private var menuItem: MenuItem? = null
    private var searchManager: SearchManager? = null

    @Inject
    lateinit var prefs: SharedPreferences

    @ShopperScope
    @Inject
    lateinit var mViewModel: ShopperIMvvmViewModel

    var mSubscription: CompositeSubscription = CompositeSubscription()
    private var mProgressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.offerproduct_activity)
        //val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        initCollapsingToolbar()

        recyclerView = findViewById<View>(R.id.recycler_view) as RecyclerView
        mProgressBar = findViewById<View>(R.id.progress_bar) as ProgressBar

        productList = ArrayList<ProductKt>()
        adapter = OfferProductAdapter(this, productList)

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

            sectionHeader(getString(R.string.app_desc)) {
                divider = false
            }

            divider {}
            primaryItem("Primary item") {

                onClick { _ ->
                    //Log.d("DRAWER", "Click.")
                    //navigateToAccountReportsKt()
                    false
                }

            }


            divider {}
            secondaryItem("Secondary item") {

                onClick { _ ->
                    //Log.d("DRAWER", "Click.")
                    //navigateToSaldoCustomerKt()
                    false
                }
            }



        }

    }

    private fun bind() {

        showProgressBar()
        mSubscription.add(mViewModel.getMyProductsFromSqlServer("1")
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OfferKtActivity", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext({ throwable -> Observable.empty() })
                .subscribe({ it -> setServerProducts(it) }))

        mSubscription.add(mViewModel.getMyCatsFromSqlServer("1")
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("OfferKtActivity", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext({ throwable -> Observable.empty() })
                .subscribe({ it -> setServerCategories(it) }))

    }

    private fun setServerCategories(cats: List<CategoryKt>) {

        Log.d("showCategory ", cats.get(0).nac);

    }

    private fun setServerProducts(products: List<ProductKt>) {

        Log.d("showProduct ", products.get(0).nat);
        productList = products.toMutableList()
        adapter?.setProductItems(productList)
        hideProgressBar()
    }


    override fun onDestroy() {
        super.onDestroy()
        mSubscription?.unsubscribe()
        mSubscription?.clear()
        hideProgressBar()
        //mViewModel.clearObservableSaveDomainToRealm()
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
                    collapsingToolbar.title = getString(R.string.offeritems)
                    isShow = true
                } else if (isShow) {
                    collapsingToolbar.title = " "
                    isShow = false
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
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.offeractivity_menu, menu)
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

    protected fun showProgressBar() {
        mProgressBar?.setVisibility(View.VISIBLE)
    }

    protected fun hideProgressBar() {
        mProgressBar?.setVisibility(View.GONE)
    }


}
