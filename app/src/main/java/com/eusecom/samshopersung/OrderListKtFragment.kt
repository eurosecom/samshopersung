package com.eusecom.samshopersung

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.ctx
import dagger.android.AndroidInjection
import android.app.Activity




/**
 * Kotlin empty fragment with Anko DSL

 */

class OrderListKtFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return OrderListKtFragmentUI().createView(AnkoContext.create(ctx, this))
    }





}
