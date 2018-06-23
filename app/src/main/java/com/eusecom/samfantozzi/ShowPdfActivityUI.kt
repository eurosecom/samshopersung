package com.eusecom.samfantozzi

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView


class ShowPdfActivityUI (): AnkoComponent<ShowPdfActivity>{

    override fun createView(ui: AnkoContext<ShowPdfActivity>): View = with(ui){

        return relativeLayout{
            padding = dip(5)
            lparams (width = matchParent, height = wrapContent)




        }

    }

}