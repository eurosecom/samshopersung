package com.eusecom.samfantozzi

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import com.mikepenz.materialdrawer.model.DividerDrawerItem




class ChooseMonthActivityUI (val mAdapter: ChooseMonthAdapter): AnkoComponent<ChooseMonthActivity>{

    override fun createView(ui: AnkoContext<ChooseMonthActivity>): View = with(ui){

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