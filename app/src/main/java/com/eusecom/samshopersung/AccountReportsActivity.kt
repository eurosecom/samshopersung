package com.eusecom.samshopersung

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import com.eusecom.samshopersung.helper.TransitionHelper
import com.eusecom.samshopersung.proxy.CommandExecutorProxyImpl
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.mainshopper_content.*
import org.jetbrains.anko.*
import rx.Observable
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject


/**
 * Kotlin activity with ANKO Recyclerview
 * by https://medium.com/@BhaskerShrestha/kotlin-and-anko-for-your-android-1c11054dd255
 * listener by https://choicetechlab.com/blog/add-recyclerview-using-kotlin/
 * github https://github.com/prajakta05/recyclerviewKotlin
 */

class AccountReportsActivity : AppCompatActivity() {

    @Inject
    lateinit var prefs: SharedPreferences

    @Inject
    lateinit var mAdapter: SetImageAdapter

    @Inject
    lateinit var mViewModel: ShopperIMvvmViewModel

    var reports: String = "0"

    private var mRecycler: RecyclerView? = null
    private var mManager: LinearLayoutManager? = null
    private var mSubscription: CompositeSubscription? = null
    private var mProgressBar: ProgressBar? = null
    private lateinit var mRep00: Button
    private lateinit var mRep01: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)

        val i = intent
        val extras = i.extras
        //0 accounting, 1 vat, 2 income, 3 mixed
        reports = extras!!.getString("reports")

        AccountReportsActivityUI(reports, "0").setContentView(this)

        mProgressBar = findViewById<View>(R.id.progress_bar) as ProgressBar
        mManager = LinearLayoutManager(this)
        mManager?.setReverseLayout(true)
        mManager?.setStackFromEnd(true)
        mRecycler = findViewById<View>(R.id.recycler_view) as RecyclerView
        mRecycler?.setLayoutManager(mManager)
        mRecycler?.setAdapter(mAdapter)
        mRep00 = findViewById<View>(R.id.rep00) as Button
        mRep01 = findViewById<View>(R.id.rep01) as Button

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mRep00.setTransitionName("buttonFirtobutton00")
            mRep01.setTransitionName("button5tobutton01")

        }

    }

    override fun onResume() {
        super.onResume()
        bind()
        val edidok = prefs.getString("edidok", "0")
        Log.d("edidok", edidok)
        if (!edidok.equals("FINDITEM")) {


        } else {

        }
    }

    override fun onPause() {
        super.onPause()
        unBind()
    }

    override fun onDestroy() {
        super.onDestroy()
        unBind()
    }

    private fun bind() {

        mSubscription = CompositeSubscription()

        //showProgressBar()
        mSubscription?.add(getMyQueryProductsFromSqlServer()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("AccountReportsActivity", "Error Throwable " + throwable.message)
                    //hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setServerProducts(it) })

        mSubscription?.add(mViewModel.getObservableException()
                .subscribeOn(Schedulers.computation())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    Log.e("AccountReportsActivity", "Error Throwable " + throwable.message)
                    hideProgressBar()
                    toast("Server not connected")
                }
                .onErrorResumeNext { throwable -> Observable.empty() }
                .subscribe { it -> setProxyException(it) })

        val edidok = prefs.getString("edidok", "")
        Log.d("SetImageActivityLog ", edidok)
        if (edidok != "0" && edidok != "" && edidok != "FINDITEM") {
            showProgressBar()
            emitMyQueryProductsFromSqlServer("GetDetail" + prefs.getString("edidok", "")!!)
        }

    }

    fun setProxyException(excp: String) {
        //toast("Permission " + excp + " not alowed.")
        hideProgressBar()
        if (excp.equals("LGN")) {
            //showDonotloginAlert()
            toast(getString(R.string.didnotlogin))
            println("LGN not approved.")
        }
        if (excp.equals("ADM")) {
            //showDonotAdminAlert()
            toast(getString(R.string.didnotadmin))
            println("ADM not approved.")
        }
        if (excp.equals("CMP")) {
            //showDonotcompanyAlert()
            toast(getString(R.string.didnotcompany))
            println("CMP not approved.")
        }

    }

    private fun setServerProducts(products: List<ProductKt>) {

        mAdapter.setDataToAdapter(products)
        hideProgressBar()
    }

    private fun getMyQueryProductsFromSqlServer(): Observable<List<ProductKt>> {
        return mViewModel.myQueryProductsFromSqlServer
    }

    private fun emitMyQueryProductsFromSqlServer(query: String) {
        showProgressBar()
        mViewModel.emitMyQueryProductsFromSqlServer(query)
    }

    private fun unBind() {

        mViewModel.clearObservableException()
        mViewModel.clearMyQueryProductsFromSqlServe()
        mSubscription?.unsubscribe()
        mSubscription?.clear()
        hideProgressBar()

    }

    fun chooseItem() {

        val editor = prefs.edit()
        editor.putString("edidok", "FINDITEM").apply();
        editor.commit();
        startActivity<OfferKtActivity>()
    }

    fun chosenItem(): Boolean {

        val edidok = prefs.getString("edidok", "")
        return edidok != "0" && edidok != "" && edidok != "FINDITEM"

    }

    fun chooseActivity(whatactivity: Int) {

        when (whatactivity) {
            0 -> {

                if (mViewModel.callCommandExecutorProxy(CommandExecutorProxyImpl.PermType.ADM, CommandExecutorProxyImpl.ReportTypes.PDF, CommandExecutorProxyImpl.ReportName.SETITEM)) {
                    println("command approved.")
                    val `is` = Intent(this, SetImageActivity::class.java)
                    val extras = Bundle()
                    extras.putInt("whatactivity", whatactivity)
                    `is`.putExtras(extras)
                    startActivity(`is`)
                }

            }
            1 -> {

                if (mViewModel.callCommandExecutorProxy(CommandExecutorProxyImpl.PermType.ADM, CommandExecutorProxyImpl.ReportTypes.PDF, CommandExecutorProxyImpl.ReportName.SETITEM)) {
                    println("command approved.")
                    val `is` = Intent(this, SetImageActivity::class.java)
                    val extras = Bundle()
                    extras.putInt("whatactivity", whatactivity)
                    `is`.putExtras(extras)
                    startActivity(`is`)
                }

            }
            2 -> {

                if (mViewModel.callCommandExecutorProxy(CommandExecutorProxyImpl.PermType.ADM, CommandExecutorProxyImpl.ReportTypes.PDF, CommandExecutorProxyImpl.ReportName.SETITEM)) {
                    println("command approved.")
                    val `is` = Intent(this, SetProductActivity::class.java)
                    val extras = Bundle()
                    extras.putInt("whatactivity", whatactivity)
                    `is`.putExtras(extras)
                    startActivity(`is`)
                }

            }
            3 -> {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mRep01.setTransitionName("rep01tofab");

                    //Create the transition participants required during a activity transition while
                    //avoiding glitches with the system UI.
                    val animationBundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                            *TransitionHelper.createSafeTransitionParticipants(this,
                                    false,
                                    Pair(mRep01, "rep01tofab")))
                            .toBundle()

                    // Start the activity with the participants, animating from one to the other.
                    val intent = Intent(this@AccountReportsActivity, StoreCardKtActivity::class.java)
                    startActivity(intent, animationBundle)
                } else {
                    val `is` = Intent(this, StoreCardKtActivity::class.java)
                    startActivity(`is`)
                }


            }
            else -> {
            }
        }

    }


    private fun showProgressBar() {
        mProgressBar?.setVisibility(View.VISIBLE)
    }

    private fun hideProgressBar() {
        mProgressBar?.setVisibility(View.GONE)
    }


}
