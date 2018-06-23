package com.eusecom.samfantozzi

import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

class CashListKtFragmentUI() : AnkoComponent<Fragment> {
    override fun createView(ui: AnkoContext<Fragment>): View {
        return with(ui) {
            relativeLayout {
                lparams {
                    width = matchParent
                    height = matchParent
                }

                editText {
                    id = R.id.query_edit_text
                    textSize = sp(10).toFloat()

                }.lparams {
                    width = matchParent
                    height = wrapContent
                }

                recyclerView{
                    lparams {
                        width = matchParent
                        height = wrapContent
                    }

                    layoutManager = LinearLayoutManager(ctx)
                    addItemDecoration(LinearLayoutSpaceItemDecoration(25))
                    //adapter = mAdapter
                    id = R.id.list

                }

                textView{
                    text = "xxxxxxxxx"
                    textSize = sp(10).toFloat()
                }.lparams {
                    width = wrapContent
                    height = wrapContent
                    below(R.id.query_edit_text)
                }
            }
        }
    }
}

