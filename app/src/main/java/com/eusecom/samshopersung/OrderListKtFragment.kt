package com.eusecom.samshopersung

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.Toast
import dagger.android.support.AndroidSupportInjection
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.ctx
import javax.inject.Inject


/**
 * Kotlin empty fragment with Anko DSL

 */

class OrderListKtFragment : Fragment() {

    @Inject
    lateinit var mSharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return OrderListKtFragmentUI().createView(AnkoContext.create(ctx, this))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        var serverx = "From fragment " + mSharedPreferences?.getString("servername", "")
        Toast.makeText(activity, serverx, Toast.LENGTH_SHORT).show()


    }//end of onActivityCreated

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }



}
