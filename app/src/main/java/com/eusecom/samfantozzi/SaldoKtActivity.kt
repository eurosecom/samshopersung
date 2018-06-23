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
import android.util.Log
import android.view.View
import com.eusecom.samfantozzi.rxbus.RxBus
import org.jetbrains.anko.*
import javax.inject.Inject

/**
 * Kotlin activity Recyclerview with classic XML itemlayout without Anko DSL

 */

class SaldoKtActivity : AppCompatActivity() {

    @Inject
    lateinit var prefs: SharedPreferences

    @Inject
    lateinit var mViewModel: DgAllEmpsAbsMvvmViewModel

    @Inject
    lateinit var _rxBus: RxBus

    private var mPagerAdapter: FragmentPagerAdapter? = null
    private var mViewPager: ViewPager? = null

    var saltype: Int = 0
    var salico: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as SamfantozziApp).dgaeacomponent().inject(this)

        val i = intent
        //0=mainfantozzi, 1=customers invoice, 2=supliers invoice, 3=cash document, 4=bank document, 5=internal document
        val extras = i.extras
        saltype = extras!!.getInt("saltype")
        salico = extras!!.getInt("salico")

        SaldoKtActivityUI(_rxBus, prefs, saltype, salico).setContentView(this)

        if (saltype == 0) {
            supportActionBar!!.setTitle(prefs.getString("odbuce", "") + " " + getString(R.string.saldocus))
        } else {
            supportActionBar!!.setTitle(prefs.getString("doduce", "") + " " + getString(R.string.saldosup))
        }

        if (saltype == 0) {

            mPagerAdapter = object : FragmentPagerAdapter(supportFragmentManager) {
                private val mFragments = arrayOf(SaldoCustomersFragment())
                private val mFragmentNames = arrayOf(getString(R.string.customers))
                private val mFragmentNamesIdc = arrayOf(getString(R.string.customer))

                override fun getItem(position: Int): Fragment {
                    return mFragments[position]
                }

                override fun getCount(): Int {
                    return mFragments.size
                }

                override fun getPageTitle(position: Int): CharSequence {
                    var titx: CharSequence = ""
                    if( salico == 0) {
                        titx = mFragmentNames[position]
                    }else{
                        titx = mFragmentNamesIdc[position] + " " + salico
                    }

                    return titx
                }
            }

        } else{

            mPagerAdapter = object : FragmentPagerAdapter(supportFragmentManager) {
                private val mFragments = arrayOf(SaldoSuppliersFragment())
                private val mFragmentNames = arrayOf(getString(R.string.suppliers))
                private val mFragmentNamesIdc = arrayOf(getString(R.string.supplier))

                override fun getItem(position: Int): Fragment {
                    return mFragments[position]
                }

                override fun getCount(): Int {
                    return mFragments.size
                }

                override fun getPageTitle(position: Int): CharSequence {
                    var titx: CharSequence = ""
                    if( salico == 0) {
                        titx = mFragmentNames[position]
                    }else{
                        titx = mFragmentNamesIdc[position] + " " + salico
                    }

                    return titx
                }
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
                        fab.visibility = View.INVISIBLE
                    }

                }
            })

        val tabLayout = findViewById<View>(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(mViewPager)

    }


    override fun onResume() {
        super.onResume()
        val editor = prefs.edit()
        editor.putString("edidok", salico.toString()).apply();
        editor.commit();
    }


    fun finishActivity(reports: String) {
        val `is` = Intent(this, SaldoKtActivity::class.java)
        val extras = Bundle()
        if (saltype == 0) { extras.putInt("saltype", 1) }
        if (saltype == 1) { extras.putInt("saltype", 0) }
        `is`.putExtras(extras)
        startActivity(`is`)
        finish()
    }

    fun showDonotadminAlert() {

        alert(getString(R.string.donotadmin), getString(R.string.action_loginadmin)) {
            yesButton {
                navigateToLogin()
            }
            noButton {}
        }.show()


    }

    fun showDonotloginAlert() {

        alert(getString(R.string.donotlogin), getString(R.string.action_login)) {
            yesButton {
                navigateToLogin()
            }
            noButton {}
        }.show()


    }

    fun showDonotcompanyAlert() {

        alert(getString(R.string.donotcompany), getString(R.string.getcompany)) {
            yesButton {
                navigateToGetCompany()
            }
            noButton {}
        }.show()

    }

    fun navigateToLogin(){
        val intent = Intent(this, EmailPasswordActivity::class.java)
        startActivity(intent)
    }

    fun navigateToGetCompany(){
        val usuid = prefs.getString("usuid", "")
        if (usuid == "0") {
            showDonotloginAlert()
        }else {
            startActivityForResult(intentFor<ChooseCompanyActivity>(), 101)
        }

    }


}
