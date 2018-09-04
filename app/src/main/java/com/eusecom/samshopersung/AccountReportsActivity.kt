package com.eusecom.samshopersung

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import javax.inject.Inject
import org.jetbrains.anko.*


/**
 * Kotlin activity with ANKO Recyclerview
 * by https://medium.com/@BhaskerShrestha/kotlin-and-anko-for-your-android-1c11054dd255
 * listener by https://choicetechlab.com/blog/add-recyclerview-using-kotlin/
 * github https://github.com/prajakta05/recyclerviewKotlin
 */

class AccountReportsActivity : AppCompatActivity() {

    var reports: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val i = intent
        val extras = i.extras
        //0 accounting, 1 vat, 2 income, 3 mixed
        reports = extras!!.getString("reports")


        AccountReportsActivityUI(reports, "0").setContentView(this)



    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroy() {
        super.onDestroy()


    }


}
