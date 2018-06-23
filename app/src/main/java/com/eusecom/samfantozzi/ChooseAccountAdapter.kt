package com.eusecom.samfantozzi

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoContext


class ChooseAccountAdapter(var mList: MutableList<Account>, val listener: (Account) -> Unit) : RecyclerView.Adapter<ChooseAccountAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindItem(mList[position], listener)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(ChooseAccountItemUI().createView(AnkoContext.create(parent!!.context)))
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        val accName = itemView?.findViewById<TextView>(R.id.accname)
        val accNumber = itemView?.findViewById<TextView>(R.id.accnumber)
        val accImage = itemView?.findViewById<ImageView>(R.id.accimg)

        fun bindItem(account: Account, listener: (Account) -> Unit) = with(itemView) {

            accName?.setText(account.accname)
            //if(account.logprx) {
            //    accName?.setText(account.accname + " true")
            //}else{
            //    accName?.setText(account.accname + " false")
            //}
            accNumber?.setText(account.accnumber)
            Picasso.with(itemView.context).load(R.drawable.ic_call_made_black_24dp).resize(120, 120).into(accImage)
            if( account.acctype.equals("2")){
                Picasso.with(itemView.context).load(R.drawable.ic_call_received_black_24dp).resize(120, 120).into(accImage)
            }
            if( account.acctype.equals("3")){
                Picasso.with(itemView.context).load(R.drawable.ic_local_atm_black_24dp).resize(120, 120).into(accImage)
            }
            if( account.acctype.equals("4")){
                Picasso.with(itemView.context).load(R.drawable.ic_account_balance_black_24dp).resize(120, 120).into(accImage)
            }
            if( account.acctype.equals("5")){
                Picasso.with(itemView.context).load(R.drawable.ic_insert_drive_file_black_24dp).resize(120, 120).into(accImage)
            }
            itemView.setOnClickListener{listener(account)}

        }
    }

    fun setdata(list: List<Account>){
        mList = list as MutableList<Account>
        notifyDataSetChanged()
    }
}