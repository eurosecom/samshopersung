package com.eusecom.samshopersung

import android.content.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import dagger.android.AndroidInjection
import android.net.ConnectivityManager
import android.util.Log
import co.zsmb.materialdrawerkt.builders.accountHeader
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.badgeable.secondaryItem
import co.zsmb.materialdrawerkt.draweritems.divider
import co.zsmb.materialdrawerkt.draweritems.profile.profile
import co.zsmb.materialdrawerkt.draweritems.profile.profileSetting
import co.zsmb.materialdrawerkt.draweritems.sectionHeader
import com.eusecom.samshopersung.di.ShopperScope
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import kotlinx.android.synthetic.main.mainshopper_activity.*
import kotlinx.android.synthetic.main.mainshopper_content.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.listeners.onClick
import javax.inject.Inject


/**
 * SamShopper Main activity
 * Kotlin and Java dagger 2.11
 */

class MainShopperActivity : AppCompatActivity() {

    private lateinit var result: Drawer
    private lateinit var headerResult: AccountHeader
    private lateinit var headerProfil: ProfileDrawerItem
    private lateinit var alert: AlertBuilder<DialogInterface>

    @Inject
    lateinit var prefs: SharedPreferences

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    @ShopperScope
    @Inject
    lateinit var mViewModel: ShopperMvvmViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainshopper_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(false)

        constlay.onClick { view ->

            Log.e("Trigger", " onclick")
            updateUI()
        }


        fab.setOnClickListener {

            //view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            //_ -> navigateToRoomDemo()
            //Log.d("ShopperMvvmViewModel", "instx " + mViewModel.toString())
            //Log.d("SharedPreferences", "instx " + prefs.toString())
        }

        button1.setOnClickListener {
            //_ -> navigateToOfferKt()
        }

        button2.setOnClickListener {
            _ -> navigateToOfferKt()
        }

        button3.setOnClickListener {
            _ -> //navigateToInvoiceListKt()
        }

        button4.setOnClickListener {
            _ -> //navigateToSupplierList()
        }

        button5.setOnClickListener {
            _ -> //navigateToGeneralList()
        }

        buttonFir.setOnClickListener {
            _ -> navigateToGetCompany()
        }

        imageView.setOnClickListener {
            //_ -> navigateToAccountReportsKtnocontrol()
            _ -> //navigateToAccountReportsKt()
        }

        //kotlin drawer by https://github.com/zsmb13/MaterialDrawerKt
        result = drawer {

            toolbar = this@MainShopperActivity.toolbar
            hasStableIds = true
            savedInstance = savedInstanceState
            showOnFirstLaunch = false


            headerResult = accountHeader {
                background = R.drawable.pozadie
                savedInstance = savedInstanceState
                translucentStatusBar = true

                val usermailx = prefs.getString("username", "")
                headerProfil = profile("EuroSecom", usermailx) {
                    iconUrl = "http://www.edcom.sk"
                    identifier = 100
                }
                //profile("EDcom", "andrejd@edcom.sk") {
                //    iconUrl = "http://www.edcom.sk"
                //    identifier = 101
                //}

                profileSetting(getString(R.string.addacount_title), getString(R.string.addacount_summary)) {
                    icon = R.drawable.ic_history_black_24dp
                    identifier = 100_000

                    onClick { _ ->
                        navigateToLogin()
                        false
                    }

                }
                profileSetting(getString(R.string.resetpass_title), getString(R.string.resetpass_summary)) {
                    //iicon = GoogleMaterial.Icon.gmd_settings
                    icon = R.drawable.ic_check_circle_black_24dp
                    identifier = 100_001
                }


            }

            sectionHeader(getString(R.string.app_desc)) {
                divider = false
            }

            divider {}
            primaryItem(getString(R.string.action_loginout)) {

                onClick { _ ->
                    //Log.d("DRAWER", "Click.")
                    navigateToLogin()
                    false
                }

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


            if (BuildConfig.DEBUG) {

                divider {}
                secondaryItem("Albums DEMO item") {

                    onClick { _ ->
                        navigateToAlbumsDemo()
                        false
                    }
                }
            }else {

            }

            if (BuildConfig.DEBUG) {

                divider {}
                secondaryItem("Room DEMO ") {

                    onClick { _ ->
                        navigateToRoomDemo()
                        false
                    }
                }
            }else {

            }


        }

        //headerResult.setBackground(getResources().getDrawable(R.drawable.cupcake))
        //headerProfil.withName(R.string.october)
        //headerProfil.withEmail(R.string.october)
    }

    public override fun onResume() {
        super.onResume()
        updateUI()

    }

    public override fun onPause() {
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        result.saveInstanceState(outState)
        headerResult.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (result.isDrawerOpen)
            result.closeDrawer()
        else
            super.onBackPressed()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> consume { navigateToSettings() }
        R.id.action_setdomain -> consume { navigateToSetDomain() }

        else -> super.onOptionsItemSelected(item)
    }

    fun navigateToPrivacyPolicy(){


    }

    fun navigateToRoomDemo() {
        val servx = prefs.getString("servername", "")
        toast("servername " + servx)
        val `is` = Intent(this, RoomDemoActivity::class.java)
        startActivity(`is`)
    }

    fun navigateToAlbumsDemo() {

        val `is` = Intent(this, AlbumsActivity::class.java)
        startActivity(`is`)
    }


    fun navigateToOfferKt() {

        val `is` = Intent(this, OfferKtActivity::class.java)
        startActivity(`is`)
    }

    fun navigateToSettings(){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    fun navigateToLogin(){
        val intent = Intent(this, EmailPasswordActivity::class.java)
        startActivity(intent)
    }

    fun navigateToGetCompany(){
        val usuid = prefs.getString("usuid", "")
        if (usuid == "" || usuid == "0") {
            showDonotloginAlert()
        }else {
            startActivityForResult(intentFor<ChooseCompanyActivity>(), 101)
        }

    }

    fun navigateToSetDomain(){
        val usuid = prefs.getString("usuid", "")
        if (usuid == "" || usuid == "0") {
            val `is` = Intent(this, DomainsViewModelActivity::class.java)
            startActivity(`is`)
        }else {
            showLoginAlert()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        //choosecompany
        if (resultCode == 101) {

            //toast("Returned 101 ChooseCompany " + resultCode)
        }
        //idc company
        if (resultCode == 201) {

            //toast("Returned 201 IdCompany " + akeico)
        }
    }


    fun showLoginAlert() {

        alert(getString(R.string.nologin), getString(R.string.action_nologin)) {
            yesButton { navigateToLogin()
                 }
            noButton {}
        }.show()


    }

    fun showDonotloginAlert() {

        alert(getString(R.string.donotlogin), getString(R.string.action_login)) {
            yesButton { navigateToLogin()
                 }
            noButton {}
        }.show()


    }

    fun showDonotcompanyAlert() {

        alert(getString(R.string.donotcompany), getString(R.string.getcompany)) {
            yesButton { navigateToGetCompany()
                 }
            noButton {}
        }.show()

    }

    fun updateUI(){

        if( isConnectedServer() ) {
            imageView2.setImageResource(R.drawable.web2);
            Log.e("Trigger", " web")
        }else{
            imageView2.setImageResource(R.drawable.sdkarta);
            Log.e("Trigger", " sdcard")
        }

        Log.d("updateUI ", "is going.")
        val usfir = prefs.getString("fir", "")
        if ( usfir == "" || usfir == "0" ) {
            buttonFir.setText(R.string.choosecompany)
        }else {
            buttonFir.setText(prefs.getString("fir", "") +
                    " " + prefs.getString("rok", "") +
                    " " + prefs.getString("firnaz", ""))
        }

        val usermailx = prefs.getString("username", "")
        val usernamex = prefs.getString("usname", "")
        val usuid = prefs.getString("usuid", "")

        if (usuid == "" || usuid == "0") {
            textView.setText(R.string.signed_out)
            headerProfil.withEmail(R.string.signed_out)
        } else {
            textView.setText(getString(R.string.username_status_fmt, usernamex))
            headerProfil.withEmail(usermailx)
        }

    }

    //consume oncreateoptionmenu
    inline fun consume(f: () -> Unit): Boolean {
        f()
        return true
    }


    fun isConnectedServer(): Boolean {

        //ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        val netInfo = connectivityManager.getActiveNetworkInfo()
        return netInfo != null && netInfo!!.isConnectedOrConnecting()

    }


}
