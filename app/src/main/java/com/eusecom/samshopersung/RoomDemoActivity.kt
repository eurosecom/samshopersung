package com.eusecom.samshopersung

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
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

class RoomDemoActivity : AppCompatActivity() {

    @Inject
    lateinit var mViewModel: ShopperIMvvmViewModel
    @Inject
    lateinit var mRxBus: RxBus
    @Inject
    lateinit var mAdapter: RoomDemoAdapter

    private var mRecycler: RecyclerView? = null
    private var mManager: LinearLayoutManager? = null

    private val mDisposable = CompositeDisposable()
    private var mProgressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)
        setSupportActionBar(toolbar)

        mProgressBar = findViewById<View>(R.id.progress_bar) as? ProgressBar

        mRecycler = findViewById<View>(R.id.list) as RecyclerView
        mRecycler?.setHasFixedSize(true)
        mManager = LinearLayoutManager(this)
        mManager?.setReverseLayout(false)
        mManager?.setStackFromEnd(true)
        mRecycler?.setLayoutManager(mManager)
        mRecycler?.setAdapter(mAdapter)

        Log.d("RoomDemoActivityLog", "mViewModel " + mViewModel.toString())

        fab.setOnClickListener { view ->

        }

        setDisposable()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposable.clear();
    }

    private fun setDisposable() {

        showProgressBar()
        mDisposable.add(loadProducts()
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { throwable ->
                    hideProgressBar()
                    Log.e("RoomDemoActivityLog", "Error Throwable " + throwable.message)
                }
                .subscribe({ it -> setProducts(it) }))

        val tapEventEmitter = mRxBus.asFlowable().publish()

        mDisposable
                .add(tapEventEmitter.subscribe { event ->
                    if (event is Product) {

                        val usnamex = event.name

                        Log.d("RoomDemoActivityLog ", "bus " + usnamex)
                        showDeleteDialog(event.uid)

                    }

                })

        mDisposable
                .add(tapEventEmitter.publish { stream -> stream.buffer(stream.debounce(1, TimeUnit.SECONDS)) }
                        .observeOn(AndroidSchedulers.mainThread()).subscribe { taps ->
                    ///_showTapCount(taps.size()); OK
                })

        mDisposable.add(tapEventEmitter.connect())


    }

    private fun setProducts(prods: List<Product>) {
        //Log.d("RoomDemoActivityLog", "cat0 " + prods.get(0).name)
        mAdapter?.setDataToAdapter(prods)
        //Scroll item 2 to 20 pixels from the top
        mManager?.scrollToPositionWithOffset(0, 0);
        hideProgressBar()
    }

    protected fun loadProducts(): Flowable<List<Product>> {
        return mViewModel.loadProducts();
    }

    fun showDeleteDialog(prodId: Int) {

        alert("", getString(R.string.action_delroomitem) + " " + prodId) {
            yesButton { navigateToRxDeleteById(prodId)  }
            noButton {}
        }.show()

    }

    fun navigateToRxDeleteById(prodId: Int){

        showProgressBar()
        mDisposable.add(mViewModel.deleteRxProductById(prodId)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete({  -> Log.d("RoomDemoActivityLog", " completed") })
                .doOnError { throwable ->
                    Log.e("RoomDemoActivityLog", "Error Throwable " + throwable.message)
                }
                .subscribe({  -> Unit }))

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_room, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> consume {   }
        R.id.action_addroomitem -> consume { navigateToUpdateRoomItem() }

        else -> super.onOptionsItemSelected(item)
    }

    fun navigateToUpdateRoomItem(){

        showProgressBar()
        mDisposable.add(mViewModel.updateProductName("namename")
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete({  -> Log.d("RoomDemoActivityLog", " completed") })
                .doOnError { throwable ->
                    Log.e("RoomDemoActivityLog", "Error Throwable " + throwable.message)
                }
                .subscribe({  -> Unit }))


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

}
