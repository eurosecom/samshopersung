package com.eusecom.samfantozzi

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import com.eusecom.samfantozzi.rxbus.RxBus
import org.jetbrains.anko.*
import org.jetbrains.anko.design.bottomNavigationView
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.sdk25.listeners.onClick
import org.jetbrains.anko.support.v4.viewPager

class SaldoKtActivityUI (val _rxBus: RxBus, val prefs: SharedPreferences
                         , val saltype: Int, val salico: Int): AnkoComponent<SaldoKtActivity>{

    override fun createView(ui: AnkoContext<SaldoKtActivity>): View = with(ui){

        return relativeLayout{
            padding = dip(5)
            lparams (width = matchParent, height = wrapContent)

            verticalLayout{

                tabLayout{
                    lparams {
                    width = matchParent
                    height = wrapContent
                    }
                    id = R.id.tabs

                }

                viewPager{
                    lparams {
                    width = matchParent
                    height = matchParent
                    }
                    id = R.id.container

                }

            }

            bottomNavigationView {
                id = R.id.botnav
                inflateMenu(R.menu.saldo_bottommenu)
                itemBackgroundResource = R.color.colorPrimaryLight
                selectedItemId = R.id.action_toothersaldo

            }.lparams {
                width = matchParent
                height = wrapContent
                //gravity = Gravity.BOTTOM
                //android:layout_gravity="bottom"
                alignParentBottom()
            }.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.action_toothersaldo -> {
                        ui.owner.finishActivity("0")
                    }
                    R.id.action_saldopdf1 -> {

                        if(saltype == 0 ) {
                            val `is` = Intent(context, ShowPdfActivity::class.java)
                            val extras = Bundle()
                            extras.putString("fromact", "71")
                            extras.putString("drhx", "71")
                            extras.putString("ucex", prefs.getString("odbuce", ""))
                            extras.putString("dokx", "0")
                            extras.putString("icox", salico.toString())
                            `is`.putExtras(extras)
                            context.startActivity(`is`)
                            `is`.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        }else{
                            val `is` = Intent(context, ShowPdfActivity::class.java)
                            val extras = Bundle()
                            extras.putString("fromact", "72")
                            extras.putString("drhx", "72")
                            extras.putString("ucex", prefs.getString("doduce", ""))
                            extras.putString("dokx", "0")
                            extras.putString("icox", salico.toString())
                            `is`.putExtras(extras)
                            context.startActivity(`is`)
                            `is`.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        }
                    }
                    R.id.action_saldopdf2 -> {
                        if(saltype == 0 ) {
                            //generating MySql PDF report with using CommandExecutorProxy and Facade
                            val `is` = Intent(context, ShowPdfActivity::class.java)
                            val extras = Bundle()
                            extras.putString("fromact", "73")
                            extras.putString("drhx", "73")
                            extras.putString("ucex", prefs.getString("odbuce", ""))
                            extras.putString("dokx", "0")
                            extras.putString("icox", salico.toString())
                            `is`.putExtras(extras)
                            context.startActivity(`is`)
                            `is`.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        }else{
                            val `is` = Intent(context, ShowPdfActivity::class.java)
                            val extras = Bundle()
                            extras.putString("fromact", "74")
                            extras.putString("drhx", "74")
                            extras.putString("ucex", prefs.getString("doduce", ""))
                            extras.putString("dokx", "0")
                            extras.putString("icox", salico.toString())
                            `is`.putExtras(extras)
                            context.startActivity(`is`)
                            `is`.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        }
                    }
                }

                false
            }

        }

    }



}