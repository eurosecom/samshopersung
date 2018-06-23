package com.eusecom.samfantozzi

import android.content.Context
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import org.jetbrains.anko.*


class ChooseMonthItemUI : AnkoComponent<Context> {

    override fun createView(ui: AnkoContext<Context>): View = with(ui){

        return relativeLayout {
            padding = dip(5)
            isClickable = true
            //background = context.obtainStyledAttributes(arrayOf(R.attr.selectableItemBackground).toIntArray()).getDrawable(0)
            background = ResourcesCompat.getDrawable(resources, R.drawable.selector_medium_high, null)

            // i do not use onclick from AnkoComponent, i use Adapter listener
            //onClick{
            //    toast("Clicked onItem")
            //}

            imageView{
                id = R.id.mnimg

            }.lparams{
                width = dip(35)
                height = dip(35)
                setMargins(0, 0, dip(15), 0)
            }

            textView{

                textSize = sp(9).toFloat()
                id = R.id.mnname

            }.lparams{
                width = matchParent
                height = wrapContent
                rightOf(R.id.mnimg)
            }

            textView{

                    textSize = sp(7).toFloat()
                    id = R.id.mnnumber
                }.lparams {
                width = wrapContent
                height = wrapContent
                rightOf(R.id.mnimg)
                below(R.id.mnname)
            }

        }


    }
}