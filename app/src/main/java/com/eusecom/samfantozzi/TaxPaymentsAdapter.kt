package com.eusecom.samfantozzi

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoContext


class TaxPaymentsAdapter(var mList: MutableList<Account>, val listener: (Account) -> Unit) : RecyclerView.Adapter<TaxPaymentsAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindItem(mList[position], listener)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(TaxPaymentsItemUI().createView(AnkoContext.create(parent!!.context)))
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        val accName = itemView?.findViewById<TextView>(R.id.accname)
        val accNumber = itemView?.findViewById<TextView>(R.id.accnumber)
        val accImage = itemView?.findViewById<ImageView>(R.id.accimg)

        fun bindItem(account: Account, listener: (Account) -> Unit) = with(itemView) {

            accName?.setText(account.accnumber + " " + account.accname)
            accNumber?.setText(account.accdoc)
            Picasso.with(itemView.context).load(R.drawable.ic_check_circle_black_24dp).resize(120, 120).into(accImage)
            itemView.setOnClickListener{listener(account)}

        }
    }

    fun setdata(list: List<Account>){
        mList = list as MutableList<Account>
        notifyDataSetChanged()
    }
}