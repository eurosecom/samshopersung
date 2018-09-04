package com.eusecom.samshopersung

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.design.bottomNavigationView
import org.jetbrains.anko.sdk25.listeners.onClick


class AccountReportsActivityUI (val mReport: String, val prm2: String): AnkoComponent<AccountReportsActivity>{

    override fun createView(ui: AnkoContext<AccountReportsActivity>): View = with(ui){


        return relativeLayout{
            padding = dip(10)
            lparams (width = matchParent, height = matchParent)

            button() {
                id = R.id.rep00
                textResource = R.string.storelist
                onClick {
                    //startActivity<ChooseMonthActivity>()
                }
            }.lparams {
                width = matchParent
                height = wrapContent
                top
            }

            if( mReport.equals("0")) {

                button() {
                    id = R.id.rep01
                    textResource = R.string.newstorageitem
                    onClick {

                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep00)
                }

                button() {
                    id = R.id.rep02
                    textResource = R.string.setimage
                    onClick {

                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep01)
                }

                button() {
                    id = R.id.rep03
                    textResource = R.string.setean
                    onClick {

                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep02)
                }



            }//report 0



        }

    }


}