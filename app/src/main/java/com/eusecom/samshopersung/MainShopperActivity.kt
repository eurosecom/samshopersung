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
    lateinit var mViewModel: ShopperIMvvmViewModel

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

            _ -> navigateToOfferKt()

        }

        button1.setOnClickListener {
            _ -> navigateToBasketKt()
        }

        button2.setOnClickListener {
            _ -> navigateToOfferKt()
        }

        button3.setOnClickListener {
            _ -> navigateToOrder()
        }

        button4.setOnClickListener {
            _ -> navigateToInvoice()
        }

        button5.setOnClickListener {
            _ -> navigateToAccountReportsKt()
        }

        buttonFir.setOnClickListener {
            _ -> navigateToGetCompany()
        }

        imageView.setOnClickListener {
            _ -> navigateToBasketKt()
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
            primaryItem(getString(R.string.orpekasa)) {

                onClick { _ ->
                    //Log.d("DRAWER", "Click.")
                    navigateToOrpKtnew()
                    false
                }

            }


            divider {}
            secondaryItem(getString(R.string.orders)) {

                onClick { _ ->
                    navigateToOrder()
                    false
                }
            }

            if (BuildConfig.DEBUG) {

                divider {}
                secondaryItem("KeyStore DEMO ") {

                    onClick { _ ->
                        navigateToKeyStoreDemo()
                        false
                    }
                }
            }else {

            }

            if (BuildConfig.DEBUG) {

                divider {}
                secondaryItem("Scan EAN DEMO ") {

                    onClick { _ ->
                        navigateToScanEanDemo()
                        false
                    }
                }
            }else {

            }

            if (BuildConfig.DEBUG) {

                divider {}
                secondaryItem("Flumborator dagger2 DEMO ") {

                    onClick { _ ->
                        navigateToFlumboratorDemo()
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

            if (BuildConfig.DEBUG) {

                divider {}
                secondaryItem("Maps NEW DEMO ") {

                    onClick { _ ->
                        navigateToMapsActivity()
                        false
                    }
                }
            }else {

            }

            if (BuildConfig.DEBUG) {

                divider {}
                secondaryItem("Map DEMO ") {

                    onClick { _ ->
                        navigateToMapActivity()
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
        R.id.action_setmfir -> consume { navigateToSetMyFir() }

        else -> super.onOptionsItemSelected(item)
    }

    fun navigateToPrivacyPolicy(){


    }

    fun navigateToOrder() {
        val usuid = prefs.getString("usuid", "")
        if (usuid == "" || usuid == "0") {
            showDonotloginAlert()
        }else {

            val usfir = prefs.getString("fir", "")
            if ( usfir == "" || usfir == "0" ) {
                showDonotcompanyAlert()
            }else {

                val `is` = Intent(this, OrderListActivity::class.java)
                val extras = Bundle()
                extras.putInt("saltype", 0)
                `is`.putExtras(extras)
                startActivity(`is`)
            }

        }
    }

    fun navigateToOrpKt() {
        val usuid = prefs.getString("usuid", "")
        if (usuid == "" || usuid == "0") {
            showDonotloginAlert()
        }else {

            val usfir = prefs.getString("fir", "")
            if ( usfir == "" || usfir == "0" ) {
                showDonotcompanyAlert()
            }else {

                val `is` = Intent(this, OrpListActivity::class.java)
                val extras = Bundle()
                extras.putInt("saltype", 0)
                `is`.putExtras(extras)
                startActivity(`is`)
            }

        }
    }

    fun navigateToOrpKtnew() {
        val usuid = prefs.getString("usuid", "")
        if (usuid == "" || usuid == "0") {
            showDonotloginAlert()
        }else {

            val usfir = prefs.getString("fir", "")
            if ( usfir == "" || usfir == "0" ) {
                showDonotcompanyAlert()
            }else {

                val `is` = Intent(this, OrpListKtActivity::class.java)
                val extras = Bundle()
                extras.putInt("saltype", 0)
                `is`.putExtras(extras)
                startActivity(`is`)
            }

        }
    }

    fun navigateToInvoice() {
        val usuid = prefs.getString("usuid", "")
        if (usuid == "" || usuid == "0") {
            showDonotloginAlert()
        }else {

            val usfir = prefs.getString("fir", "")
            if ( usfir == "" || usfir == "0" ) {
                showDonotcompanyAlert()
            }else {

                val `is` = Intent(this, OrderListActivity::class.java)
                val extras = Bundle()
                extras.putInt("saltype", 1)
                `is`.putExtras(extras)
                startActivity(`is`)
            }

        }
    }

    fun navigateToKeyStoreDemo() {
        val `is` = Intent(this, KeyStoreActivity::class.java)
        startActivity(`is`)
    }

    fun navigateToRoomDemo() {
        val servx = prefs.getString("servername", "")
        toast("servername " + servx)
        val `is` = Intent(this, RoomDemoActivity::class.java)
        startActivity(`is`)
    }

    fun navigateToScanEanDemo() {

        val `is` = Intent(this, ScanEanKtActivity::class.java)
        startActivity(`is`)
    }

    fun navigateToFlumboratorDemo() {

        val `is` = Intent(this, FlombulatorActivity::class.java)
        startActivity(`is`)
    }


    fun navigateToMapActivity() {

        val `is` = Intent(this, MapActivity::class.java)
        startActivity(`is`)
    }

    fun navigateToMapsActivity() {

        val `is` = Intent(this, MapsActivity::class.java)
        startActivity(`is`)
    }


    fun navigateToOfferKt() {

        val usuid = prefs.getString("usuid", "")
        if (usuid == "" || usuid == "0") {
            showDonotloginAlert()
        }else {

            val usfir = prefs.getString("fir", "")
            if ( usfir == "" || usfir == "0" ) {
                showDonotcompanyAlert()
            }else {

                val editor = prefs.edit()
                editor.putString("edidok", "0").apply();
                editor.commit();

                val `is` = Intent(this, OfferKtActivity::class.java)
                startActivity(`is`)
            }

        }

    }

    fun navigateToBasketKt() {

        val usuid = prefs.getString("usuid", "")
        if (usuid == "" || usuid == "0") {
            showDonotloginAlert()
        }else {

            val usfir = prefs.getString("fir", "")
            if ( usfir == "" || usfir == "0" ) {
                showDonotcompanyAlert()
            }else {

                val `is` = Intent(this, BasketKtActivity::class.java)
                startActivity(`is`)
            }

        }

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

    fun navigateToSetMyFir() {

        val usuid = prefs.getString("usuid", "")
        if (usuid == "" || usuid == "0") {
            showDonotloginAlert()
        }else {

            val usfir = prefs.getString("fir", "")
            if ( usfir == "" || usfir == "0" ) {
                showDonotcompanyAlert()
            }else {

                val `is` = Intent(this, NewIdcActivity::class.java)
                startActivity(`is`)
            }

        }

    }

    fun navigateToAccountReportsKt(){

        val usuid = prefs.getString("usuid", "")
        if (usuid == "" || usuid == "0") {
            showDonotcompanyAlert()
        }else {

            val usfir = prefs.getString("fir", "")
            if ( usfir == "" || usfir == "0" ) {
                showDonotcompanyAlert()
            }else {

                val editor = prefs.edit()
                editor.putString("edidok", "FINDITEM").apply();
                editor.commit();
                val `is` = Intent(this, AccountReportsActivity::class.java)
                val extras = Bundle()
                extras.putString("reports", "0")
                `is`.putExtras(extras)
                startActivity(`is`)
                //startActivity<AccountReportsActivity>()
            }

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
