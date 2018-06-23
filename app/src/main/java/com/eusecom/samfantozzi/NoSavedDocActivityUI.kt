package com.eusecom.samfantozzi

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView


class NoSavedDocActivityUI (val mAdapter: NoSavedDocAdapter): AnkoComponent<NoSavedDocActivity>{

    override fun createView(ui: AnkoContext<NoSavedDocActivity>): View = with(ui){

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