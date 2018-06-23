package com.eusecom.samfantozzi

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import com.eusecom.samfantozzi.rxbus.RxBus
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.toast
import javax.inject.Inject

/**
 * Kotlin activity Recyclerview with classic XML itemlayout without Anko DSL

 */

class NewCashDocKtActivity : AppCompatActivity() {

    @Inject
    lateinit var prefs: SharedPreferences

    @Inject
    lateinit var mViewModel: DgAllEmpsAbsMvvmViewModel

    @Inject
    lateinit var _rxBus: RxBus

    private var mPagerAdapter: FragmentPagerAdapter? = null
    private var mViewPager: ViewPager? = null

    var drupoh: String = "1"
    var newdok: String = "1"
    var edidok: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as SamfantozziApp).dgaeacomponent().inject(this)

        val i = intent
        //drupoh 1=receipt, 2=expense
        val extras = i.extras
        drupoh = extras!!.getString("drupoh")
        newdok = extras!!.getString("newdok")
        edidok = extras!!.getString("edidok")

        //setContentView(R.layout.activity_cashlist)
        NewCashDocKtActivityUI(_rxBus).setContentView(this)

        if( newdok == "1" ) {
            if (drupoh == "1") {
                supportActionBar!!.setTitle(prefs.getString("pokluce", "") + " " + getString(R.string.newreceipt))
            } else {
                supportActionBar!!.setTitle(prefs.getString("pokluce", "") + " " + getString(R.string.newexpense))
            }
        }else{
            if (drupoh == "1") {
                supportActionBar!!.setTitle(prefs.getString("pokluce", "") + " " + getString(R.string.editreceipt))
            } else {
                supportActionBar!!.setTitle(prefs.getString("pokluce", "") + " " + getString(R.string.editexpense))
            }
        }

        val editor = prefs.edit()
        editor.putString("drupoh", drupoh).apply()
        editor.putString("newdok", newdok).apply()
        editor.putString("edidok", edidok).apply()
        editor.commit()

        // Create the adapter that will return a fragment for each section
        mPagerAdapter = object : FragmentPagerAdapter(supportFragmentManager) {
            private val mFragments = arrayOf(NewCashDocFragment(), EmptyKtFragment())
            private val mFragmentNames = arrayOf(getString(R.string.newdoc), getString(R.string.empty))

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
        mViewPager = findViewById<View>(R.id.container) as ViewPager
        //mViewPager.setAdapter(mPagerAdapter) kotlin smart cast to ViewPager is impossible mPagerAdapter = null from other thread
        mViewPager?.setAdapter(mPagerAdapter)

            mViewPager?.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

                override fun onPageSelected(position: Int) {
                    // Check if this is the page you want.
                    if (position == 0) {
                        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
                        fab.visibility = View.GONE
                        if( newdok == "1" ) {
                            if (drupoh == "1") {
                                supportActionBar!!.setTitle(prefs.getString("pokluce", "") + " " + getString(R.string.newreceipt))
                            } else {
                                supportActionBar!!.setTitle(prefs.getString("pokluce", "") + " " + getString(R.string.newexpense))
                            }
                        }else{
                            if (drupoh == "1") {
                                supportActionBar!!.setTitle(prefs.getString("pokluce", "") + " " + getString(R.string.editreceipt))
                            } else {
                                supportActionBar!!.setTitle(prefs.getString("pokluce", "") + " " + getString(R.string.editexpense))
                            }
                        }
                    }
                    if (position == 1) {
                        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
                        fab.setVisibility(View.GONE);
                        //fab.visibility = View.VISIBLE
                        supportActionBar!!.setTitle(getString(R.string.empty))
                    }

                }
            })

        val tabLayout = findViewById<View>(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(mViewPager)
        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setVisibility(View.GONE);

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        //idc company
        if (resultCode == 201) {

            val extras = data?.extras
            val akeico: String = extras!!.getString("akeico")
            toast("Returned 201 IdCompany in act ico " + akeico)

            val akeicomodel = IdcChoosenKt(akeico, "", true)

            _rxBus.send(akeicomodel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}
