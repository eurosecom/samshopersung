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
import com.eusecom.samshopersung.models.EkassaSettings
import com.eusecom.samshopersung.rxbus.RxBus
import dagger.android.AndroidInjection
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class OrpSettingsActivity : AppCompatActivity() {

    @Inject
    lateinit var mViewModel: ShopperIMvvmViewModel
    @Inject
    lateinit var mRxBus: RxBus
    @Inject
    lateinit var mAdapter: OrpSettingsAdapter

    private var mRecycler: RecyclerView? = null
    private var mManager: LinearLayoutManager? = null

    private val mDisposable = CompositeDisposable()
    private var mProgressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orpset)
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

        mDisposable.add(loadEkasaSettings()
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    hideProgressBar()
                    Log.e("OrpSettingsActivityLog", "Error Throwable " + throwable.message)
                }
                .subscribe({ it -> setSettings(it) }))

        val tapEventEmitter = mRxBus.asFlowable().publish()

        mDisposable
                .add(tapEventEmitter.subscribe { event ->
                    if (event is EkassaRequestBackup) {

                        val usnamex = event.requestUuid

                        Log.d("OrpRequestsActivityLog ", "bus " + usnamex)
                        //showDeleteDialog(event.id)

                    }

                })

        mDisposable
                .add(tapEventEmitter.publish { stream -> stream.buffer(stream.debounce(1, TimeUnit.SECONDS)) }
                        .observeOn(AndroidSchedulers.mainThread()).subscribe { taps ->
                    ///_showTapCount(taps.size()); OK
                })

        mDisposable.add(tapEventEmitter.connect())


    }


    private fun setSettings(sets: List<EkassaSettings>) {

        Log.d("Set ", "Settings")
        Log.d("OrpSettingsActivityLog", "set0 " + sets.get(0).id)
        Log.d("OrpSettingsActivityLog", "set0 " + sets.get(0).compname)

        mAdapter?.setDataToAdapter(sets)
        mManager?.scrollToPositionWithOffset(0, 0);
        hideProgressBar()
    }


    protected fun loadEkasaSettings(): Flowable<List<EkassaSettings>> {
        return mViewModel.loadEkasaSettings()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_ekasaset, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_request -> consume { navigateToOrpKtreqs() }
        R.id.action_orpdocs -> consume { navigateToOrpKtdocs() }

        else -> super.onOptionsItemSelected(item)
    }


    fun navigateToSaveSettings() {

        showProgressBar()
        mDisposable.add(mViewModel.saveEkassaSettings("1", "87654321", "PUCTO 403", "9876543210", "Sk9876543210")
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete({ -> Log.d("OrpRequestsActivityLog", " completed") })
                .doOnError { throwable ->
                    Log.e("OrpSettingsActivityLog", "Error Throwable " + throwable.message)
                }
                .subscribe({ -> Unit }))


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

    fun navigateToOrpKtreqs() {

        val `is` = Intent(this, OrpRequestsActivity::class.java)
        val extras = Bundle()
        extras.putInt("saltype", 0)
        `is`.putExtras(extras)
        startActivity(`is`)
        finish()
    }

}
