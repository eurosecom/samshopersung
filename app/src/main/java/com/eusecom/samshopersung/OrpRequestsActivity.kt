package com.eusecom.samshopersung

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import com.eusecom.samshopersung.models.EkassaRequestBackup
import com.eusecom.samshopersung.models.Product
import com.eusecom.samshopersung.rxbus.RxBus
import dagger.android.AndroidInjection
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import java.util.concurrent.TimeUnit

class OrpRequestsActivity : AppCompatActivity() {

    @Inject
    lateinit var mViewModel: ShopperIMvvmViewModel
    @Inject
    lateinit var mRxBus: RxBus
    @Inject
    lateinit var mAdapter: OrpRequestsAdapter

    private var mRecycler: RecyclerView? = null
    private var mManager: LinearLayoutManager? = null

    private val mDisposable = CompositeDisposable()
    private var mProgressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requests)
        setSupportActionBar(toolbar)

        mProgressBar = findViewById<View>(R.id.progress_bar) as? ProgressBar

        mRecycler = findViewById<View>(R.id.list) as RecyclerView
        mRecycler?.setHasFixedSize(true)
        mManager = LinearLayoutManager(this)
        mManager?.setReverseLayout(false)
        mManager?.setStackFromEnd(true)
        mRecycler?.setLayoutManager(mManager)
        mRecycler?.setAdapter(mAdapter)

        Log.d("OrpRequestsActivityLog", "mViewModel " + mViewModel.toString())

        setDisposable()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposable.clear();
    }

    private fun setDisposable() {

        showProgressBar()
        mDisposable.add(loadRequests()
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    hideProgressBar()
                    Log.e("OrpRequestsActivityLog", "Error Throwable " + throwable.message)
                }
                .subscribe({ it -> setProducts(it) }))

        val tapEventEmitter = mRxBus.asFlowable().publish()

        mDisposable
                .add(tapEventEmitter.subscribe { event ->
                    if (event is EkassaRequestBackup) {

                        val usnamex = event.requestUuid

                        Log.d("OrpRequestsActivityLog ", "bus " + usnamex)
                        showDeleteDialog(event.id)

                    }

                })

        mDisposable
                .add(tapEventEmitter.publish { stream -> stream.buffer(stream.debounce(1, TimeUnit.SECONDS)) }
                        .observeOn(AndroidSchedulers.mainThread()).subscribe { taps ->
                    ///_showTapCount(taps.size()); OK
                })

        mDisposable.add(tapEventEmitter.connect())


    }

    private fun setProducts(prods: List<EkassaRequestBackup>) {
        //Log.d("OrpRequestsActivityLog", "cat0 " + prods.get(0).name)
        mAdapter?.setDataToAdapter(prods)
        //Scroll item 2 to 20 pixels from the top
        mManager?.scrollToPositionWithOffset(0, 0);
        hideProgressBar()
    }

    protected fun loadRequests(): Flowable<List<EkassaRequestBackup>> {
        return mViewModel.loadEkasaRequests()
    }

    fun showDeleteDialog(reqId: Int) {

        alert("", getString(R.string.orpdelreq) + " " + reqId) {
            yesButton { navigateToRxDeleteById(reqId) }
            noButton {}
        }.show()

    }

    fun navigateToRxDeleteById(reqId: Int) {

        showProgressBar()
        mDisposable.add(mViewModel.deleteRxEkassaReqById(reqId)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete({ -> Log.d("OrpRequestsActivityLog", " completed") })
                .doOnError { throwable ->
                    Log.e("OrpRequestsActivityLog", "Error Throwable " + throwable.message)
                }
                .subscribe({ -> Unit }))

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_requests, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> consume { navigateToSettings() }
        R.id.action_orpdocs -> consume { navigateToOrpKtdocs() }

        else -> super.onOptionsItemSelected(item)
    }

    fun navigateToSettings(){

        val `is` = Intent(this, OrpSettingsActivity::class.java)
        val extras = Bundle()
        extras.putInt("saltype", 0)
        `is`.putExtras(extras)
        startActivity(`is`)
        finish()
    }


    //consume oncreateoptionmenu
    inline fun consume(f: () -> Unit): Boolean {
        f()
        return true
    }

    private fun showProgressBar() {
        mProgressBar?.setVisibility(View.VISIBLE)
    }

    private fun hideProgressBar() {
        mProgressBar?.setVisibility(View.GONE)
    }

    fun navigateToOrpKtdocs() {

        val `is` = Intent(this, OrpListKtActivity::class.java)
        val extras = Bundle()
        extras.putInt("saltype", 0)
        `is`.putExtras(extras)
        startActivity(`is`)
        finish()

    }

}
