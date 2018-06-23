package com.eusecom.samfantozzi

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.design.bottomNavigationView
import org.jetbrains.anko.sdk25.listeners.onClick


class AccountDoubleReportsActivityUI (val mReport: String, val prefs: SharedPreferences): AnkoComponent<AccountReportsActivity>{

    override fun createView(ui: AnkoContext<AccountReportsActivity>): View = with(ui){

        fun callCommandExecutorProxy(perm: String , dbType: AccountReportsHelperFacade.DBTypes
                                     , reportType :AccountReportsHelperFacade.ReportTypes
                                     , tableName : AccountReportsHelperFacade.ReportName
                                     , context: Context ) {
            val executor = CommandExecutorProxy(prefs.getString("usuid", "0")
                    , prefs.getString("fir", "0"), prefs.getString("usadmin", "0"))
            try {
                executor.runCommand(perm, dbType, reportType, tableName, context)
            } catch (e: Exception) {
                println("Exception Message::" + e.message)
                if(e.message.equals("adm")) { ui.owner.showDonotadminAlert() }
                if(e.message.equals("lgn")) { ui.owner.showDonotloginAlert() }
                if(e.message.equals("cmp")) { ui.owner.showDonotcompanyAlert() }
            }
        }

        return relativeLayout{
            padding = dip(10)
            lparams (width = matchParent, height = matchParent)

            button() {
                id = R.id.rep00
                textResource = R.string.action_setmonth
                onClick { startActivity<ChooseMonthActivity>() }
            }.lparams {
                width = matchParent
                height = wrapContent
                top
            }

            if( mReport.equals("0")) {

                button() {
                    id = R.id.rep01
                    textResource = R.string.popisbtnobrat
                    onClick {
                        AccountReportsHelperFacade.generateReport(AccountReportsHelperFacade.DBTypes.MYSQL
                                , AccountReportsHelperFacade.ReportTypes.PDF
                                , AccountReportsHelperFacade.ReportName.OBRATOV, context);
                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep00)
                }

                button() {
                    id = R.id.rep02
                    textResource = R.string.popisbtndenn
                    onClick {
                        AccountReportsHelperFacade.generateReport(AccountReportsHelperFacade.DBTypes.MYSQL
                                , AccountReportsHelperFacade.ReportTypes.PDF
                                , AccountReportsHelperFacade.ReportName.UDENNIK, context);
                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep01)
                }

                button() {
                    id = R.id.rep03
                    textResource = R.string.popisbtnsuv
                    onClick {
                        AccountReportsHelperFacade.generateReport(AccountReportsHelperFacade.DBTypes.MYSQL
                                , AccountReportsHelperFacade.ReportTypes.PDF
                                , AccountReportsHelperFacade.ReportName.SUVAHA, context);
                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep02)
                }

                button() {
                    id = R.id.rep04
                    textResource = R.string.popisbtnvys
                    onClick {
                        AccountReportsHelperFacade.generateReport(AccountReportsHelperFacade.DBTypes.MYSQL
                                , AccountReportsHelperFacade.ReportTypes.PDF
                                , AccountReportsHelperFacade.ReportName.VYSLED, context);
                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep03)
                }

                button() {
                    id = R.id.rep05
                    textResource = R.string.popisbtnkniodb
                    onClick {
                        AccountReportsHelperFacade.generateReport(AccountReportsHelperFacade.DBTypes.MYSQL
                                , AccountReportsHelperFacade.ReportTypes.PDF
                                , AccountReportsHelperFacade.ReportName.KNIODB, context);
                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep04)
                }

                button() {
                    id = R.id.rep06
                    textResource = R.string.popisbtnknidod
                    onClick {
                        AccountReportsHelperFacade.generateReport(AccountReportsHelperFacade.DBTypes.MYSQL
                                , AccountReportsHelperFacade.ReportTypes.PDF
                                , AccountReportsHelperFacade.ReportName.KNIDOD, context);
                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep05)
                }

            }//report 0


            if( mReport.equals("1")) {

                button() {
                    id = R.id.rep11
                    textResource = R.string.popisbtndph
                    onClick {
                        //generating MySql PDF report with using CommandExecutorProxy and Facade
                        callCommandExecutorProxy("lgn", AccountReportsHelperFacade.DBTypes.MYSQL
                                , AccountReportsHelperFacade.ReportTypes.PDF
                                , AccountReportsHelperFacade.ReportName.DPHPRZ, context)
                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep00)
                }

                button() {
                    id = R.id.rep12
                    textResource = R.string.popisbtnfinsta
                    onClick {
                        //generating MySql PDF report with using CommandExecutorProxy and Facade
                        callCommandExecutorProxy("lgn", AccountReportsHelperFacade.DBTypes.MYSQL
                                , AccountReportsHelperFacade.ReportTypes.PDF
                                , AccountReportsHelperFacade.ReportName.NEXTVERSION, context)

                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep11)
                }

                button() {
                    id = R.id.rep13
                    textResource = R.string.popisbtndppo
                    onClick {
                        //generating MySql PDF report with using CommandExecutorProxy and Facade
                        callCommandExecutorProxy("lgn", AccountReportsHelperFacade.DBTypes.MYSQL
                                , AccountReportsHelperFacade.ReportTypes.PDF
                                , AccountReportsHelperFacade.ReportName.NEXTVERSION, context)

                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep12)
                }


            }//report 1


            if( mReport.equals("2")) {

                button() {
                    id = R.id.rep21
                    textResource = R.string.popisbtnhlkn
                    onClick {
                        AccountReportsHelperFacade.generateReport(AccountReportsHelperFacade.DBTypes.MYSQL
                                , AccountReportsHelperFacade.ReportTypes.PDF
                                , AccountReportsHelperFacade.ReportName.HLKNIHA, context);
                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep00)
                }

                button() {
                    id = R.id.rep22
                    textResource = R.string.popisbtnhladok
                    onClick {
                        val `is` = Intent(context, DocSearchActivity::class.java)
                        context.startActivity(`is`)
                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep21)
                }

                button() {
                    id = R.id.rep23
                    textResource = R.string.customers
                    onClick {
                        val `is` = Intent(context, SaldoKtActivity::class.java)
                        val extras = Bundle()
                        extras.putInt("saltype", 0)
                        extras.putInt("salico", 0)
                        `is`.putExtras(extras)
                        context.startActivity(`is`)
                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep22)
                }

                button() {
                    id = R.id.rep24
                    textResource = R.string.suppliers
                    onClick {
                        val `is` = Intent(context, SaldoKtActivity::class.java)
                        val extras = Bundle()
                        extras.putInt("saltype", 1)
                        extras.putInt("salico", 0)
                        `is`.putExtras(extras)
                        context.startActivity(`is`)
                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep23)
                }


            }//report 2

            bottomNavigationView {
                id = R.id.botnav
                inflateMenu(R.menu.reports_menu)
                itemBackgroundResource = R.color.colorPrimaryLight
                if( mReport.equals("0")) {selectedItemId = R.id.action_accounts}
                if( mReport.equals("1")) {selectedItemId = R.id.action_tax}
                if( mReport.equals("2")) {selectedItemId = R.id.action_mixed}
            }.lparams {
                width = matchParent
                height = wrapContent
                //gravity = Gravity.BOTTOM
                //android:layout_gravity="bottom"
                alignParentBottom()
            }.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.action_accounts -> {
                        ui.owner.finishActivity("0")
                    }
                    R.id.action_tax -> {
                        ui.owner.finishActivity("1")
                    }
                    R.id.action_mixed -> {
                        ui.owner.finishActivity("2")
                    }
                }

                false
            }

        }

    }


}