package com.eusecom.samfantozzi

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import com.mikepenz.materialdrawer.model.DividerDrawerItem




class TaxPaymentsActivityUI (val mAdapter: TaxPaymentsAdapter): AnkoComponent<TaxPaymentsActivity>{

    override fun createView(ui: AnkoContext<TaxPaymentsActivity>): View = with(ui){

        return relativeLayout{
            padding = dip(5)
            lparams (width = matchParent, height = wrapContent)

            recyclerView{
                lparams (width = matchParent, height = wrapContent)
                layoutManager = LinearLayoutManager(ctx)
                addItemDecoration(LinearLayoutSpaceItemDecoration(25))
                adapter = mAdapter


            }



        }

    }

}