package com.eusecom.samshopersung

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import dagger.android.AndroidInjection
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import javax.inject.Inject
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.basket_activity.*


class OrpListKtActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var prefs: SharedPreferences

    lateinit var mPagerAdapter: FragmentPagerAdapter
    lateinit var viewPager: ViewPager
    private var mProgressBar: ProgressBar? = null
    var saltype = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)

        val i = intent
        val extras = i.extras
        saltype = extras!!.getInt("saltype")

        setContentView(R.layout.orplist_activity)
        setSupportActionBar(toolbar)
        initCollapsingToolbar()

        Log.d("SharedPreferences", "instx " + prefs.toString())

        // Create the adapter that will return a fragment for each section
        mPagerAdapter = object : FragmentPagerAdapter(supportFragmentManager) {
            private val mFragments = arrayOf<Fragment>(OrpFragment())
            private val mFragmentNames = arrayOf(getString(R.string.orp))

            override fun getItem(position: Int): Fragment {
                return mFragments[position]
            }

            override fun getCount(): Int {
                return mFragments.size
            }

            override fun getPageTitle(position: Int): CharSequence {
                return mFragmentNames[position]
            }
        }

        // Set up the ViewPager with the sections adapter.
        viewPager = findViewById(R.id.viewPager)
        viewPager.setAdapter(mPagerAdapter)

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



    override fun onDestroy() {
        super.onDestroy()
        hideProgressBar()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentDispatchingAndroidInjector
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.orp_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_offer -> consume { navigateToOffer() }
        R.id.clear_basket -> consume { showClearBasketDialog() }
        R.id.action_pay -> consume { showOrderBasketDialog() }

        else -> super.onOptionsItemSelected(item)
    }

    fun navigateToOffer(){

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



    fun showClearBasketDialog() {

        alert("", getString(R.string.clear_basket)) {
            yesButton { navigateToClearBasket() }
            noButton {}
        }.show()

    }

    fun navigateToClearBasket(){
        showProgressBar()


    }

    fun showOrderBasketDialog() {

        alert("", getString(R.string.order_basket)) {
            yesButton { navigateToOrderBasket() }
            noButton {}
        }.show()

    }

    fun navigateToOrderBasket(){


    }

}
