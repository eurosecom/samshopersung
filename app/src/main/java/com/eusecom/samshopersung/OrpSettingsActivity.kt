package com.eusecom.samshopersung

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
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
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit

class OrpSettingsActivity : AppCompatActivity() {

    @Inject
    lateinit var mViewModel: ShopperIMvvmViewModel
    @Inject
    lateinit var mRxBus: RxBus

    private val mDisposable = CompositeDisposable()
    private var mProgressBar: ProgressBar? = null

    var inputName: EditText? = null
    var inputHeadq: EditText? = null
    var inputIco: EditText? = null
    var inputDic: EditText? = null
    var inputIcd: EditText? = null
    var inputDkp: EditText? = null
    var inputShop: EditText? = null
    var inputOrsr: EditText? = null
    var inputPat1: EditText? = null
    var inputPat2: EditText? = null
    var btnSave: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orpset)
        setSupportActionBar(toolbar)

        mProgressBar = findViewById<View>(R.id.progress_bar) as? ProgressBar
        inputName = findViewById<View>(R.id.inputName) as? EditText
        inputHeadq = findViewById<View>(R.id.inputHeadq) as? EditText
        inputIco = findViewById<View>(R.id.inputIco) as? EditText
        inputDic = findViewById<View>(R.id.inputDic) as? EditText
        inputIcd = findViewById<View>(R.id.inputIcd) as? EditText
        inputDkp = findViewById<View>(R.id.inputDkp) as? EditText
        inputShop = findViewById<View>(R.id.inputShop) as? EditText
        inputOrsr = findViewById<View>(R.id.inputOrsr) as? EditText
        inputPat1 = findViewById<View>(R.id.inputPat1) as? EditText
        inputPat2 = findViewById<View>(R.id.inputPat2) as? EditText
        btnSave = findViewById<View>(R.id.btnSave) as? Button

        Log.d("OrpRequestsActivityLog", "mViewModel " + mViewModel.toString())

        btnSave?.setOnClickListener {
            _ -> navigateToSaveSettings()
        }
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

        inputName?.setText(sets.get(0).compname)
        inputHeadq?.setText(sets.get(0).headquarters)
        inputIco?.setText(sets.get(0).compidc.toString())
        inputDic?.setText(sets.get(0).compdic)
        inputIcd?.setText(sets.get(0).compicd)
        inputDkp?.setText(sets.get(0).dkp)
        inputShop?.setText(sets.get(0).shopadress)
        inputOrsr?.setText(sets.get(0).orsr)
        inputPat1?.setText(sets.get(0).pata1)
        inputPat2?.setText(sets.get(0).pata2)
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

        //, String headq, String dkp, String shop, String orsr, String pata1, String pata2

        showProgressBar()
        mDisposable.add(mViewModel.saveEkassaSettings("1", inputIco?.text.toString(), inputName?.text.toString()
                , inputDic?.text.toString(), inputIcd?.text.toString()
                , inputHeadq?.text.toString(), inputDkp?.text.toString()
                , inputShop?.text.toString(), inputOrsr?.text.toString()
                , inputPat1?.text.toString(), inputPat2?.text.toString())
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete({ ->
                    Log.d("OrpRequestsActivityLog", " completed")
                    toast("Saved settings of eCashBox")
                })
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
