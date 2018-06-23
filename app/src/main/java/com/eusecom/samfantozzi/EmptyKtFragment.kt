package com.eusecom.samfantozzi

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.ctx


/**
 * Kotlin empty fragment with Anko DSL

 */

class EmptyKtFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return EmptyKtFragmentUI().createView(AnkoContext.create(ctx, this))
    }


}
