package com.eusecom.samshopersung

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment
import android.view.View
import android.widget.ProgressBar
import dagger.android.support.AndroidSupportInjection

/**
 * Kotlin Base fragment
 */

abstract class BaseKtFragment : Fragment() {

    protected  var mProgressBar: ProgressBar? = null

    protected fun showProgressBar() {
        mProgressBar?.setVisibility(View.VISIBLE)
    }

    protected fun hideProgressBar() {
        mProgressBar?.setVisibility(View.GONE)
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }




}
