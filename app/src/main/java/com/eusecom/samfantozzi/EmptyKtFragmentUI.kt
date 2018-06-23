package com.eusecom.samfantozzi

import android.support.v4.app.Fragment
import android.view.View
import org.jetbrains.anko.*

class EmptyKtFragmentUI() : AnkoComponent<Fragment> {
    override fun createView(ui: AnkoContext<Fragment>): View {
        return with(ui) {

            relativeLayout {
                lparams {
                    width = matchParent
                    height = matchParent
                }


            }
        }

    }
}

