package com.eusecom.samshopersung


import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk25.listeners.onClick


class AccountReportsActivityUI (val mReport: String, val prm2: String): AnkoComponent<AccountReportsActivity>{

    override fun createView(ui: AnkoContext<AccountReportsActivity>): View = with(ui){


        return relativeLayout{
            padding = dip(10)
            lparams (width = matchParent, height = matchParent)

            recyclerView() {
                id = R.id.recycler_view
                onClick {

                }
            }.lparams {
                width = matchParent
                height = wrapContent
                top
            }

            progressBar() {
                id = R.id.progress_bar
                visibility = View.GONE
            }.lparams {
                width = wrapContent
                height = wrapContent
                below(R.id.recycler_view)
            }

            button() {
                id = R.id.rep00
                textResource = R.string.choosestorageitem
                onClick {
                    ui.owner.chooseItem()
                }
            }.lparams {
                width = matchParent
                height = wrapContent
                below(R.id.progress_bar)
            }

            if( mReport.equals("0")) {

                button() {
                    id = R.id.rep01
                    textResource = R.string.storelist
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
                        if(ui.owner.chosenItem()){
                            ui.owner.chooseActivity(0)
                        }else{
                            ui.owner.chooseItem()
                        }

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
                        if(ui.owner.chosenItem()){
                            ui.owner.chooseActivity(1)
                        }else{
                            ui.owner.chooseItem()
                        }
                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep02)
                }

                button() {
                    id = R.id.rep04
                    textResource = R.string.newstorageitem
                    onClick {

                    }
                }.lparams {
                    width = matchParent
                    height = wrapContent
                    below(R.id.rep03)
                }


            }//report 0



        }

    }


}