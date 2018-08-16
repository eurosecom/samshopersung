package com.eusecom.samshopersung

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment
import android.view.View
import android.widget.ProgressBar
import dagger.android.support.AndroidSupportInjection
import org.jetbrains.anko.alert
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.yesButton

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

    fun showDonotAdminAlert() {

        alert(getString(R.string.donotadmin), getString(R.string.didnotadmin)) {
            yesButton { navigateToLogin()
            }
            noButton {}
        }.show()
    }

    fun navigateToLogin(){
        val intent = Intent(activity, EmailPasswordActivity::class.java)
        startActivity(intent)
    }

    fun navigateToGetCompany(){
        val intent = Intent(activity, ChooseCompanyActivity::class.java)
        startActivity(intent)
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }




}
