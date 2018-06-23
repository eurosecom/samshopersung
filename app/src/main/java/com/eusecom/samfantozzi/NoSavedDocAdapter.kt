package com.eusecom.samfantozzi

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.eusecom.samfantozzi.realm.RealmInvoice
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoContext


class NoSavedDocAdapter(var mList: MutableList<RealmInvoice>, val listener: (RealmInvoice) -> Unit) : RecyclerView.Adapter<NoSavedDocAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindItem(mList[position], listener)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(NoSavedDocItemUI().createView(AnkoContext.create(parent!!.context)))
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        val accName = itemView?.findViewById<TextView>(R.id.accname)
        val accNumber = itemView?.findViewById<TextView>(R.id.accnumber)
        val accImage = itemView?.findViewById<ImageView>(R.id.accimg)

        fun bindItem(invoice: RealmInvoice, listener: (RealmInvoice) -> Unit) = with(itemView) {

            if( invoice.drh.equals("99")) {
                accName?.setText("IDC " + invoice.dok + " Name " + invoice.dat)
                accNumber?.setText("City " + invoice.hod)
            }else{
                accName?.setText(invoice.uce + " Doc " + invoice.dok + " Date " + invoice.dat)
                accNumber?.setText("Value " + invoice.hod)
            }

            Picasso.with(itemView.context).load(R.drawable.ic_call_made_black_24dp).resize(120, 120).into(accImage)
            if( invoice.drh.equals("2")){
                Picasso.with(itemView.context).load(R.drawable.ic_call_received_black_24dp).resize(120, 120).into(accImage)
            }
            if( invoice.drh.equals("3")){
                Picasso.with(itemView.context).load(R.drawable.ic_local_atm_black_24dp).resize(120, 120).into(accImage)
            }
            if( invoice.drh.equals("31")){
                Picasso.with(itemView.context).load(R.drawable.ic_local_atm_blue_24dp).resize(120, 120).into(accImage)
            }
            if( invoice.drh.equals("32")){
                Picasso.with(itemView.context).load(R.drawable.ic_local_atm_red_24dp).resize(120, 120).into(accImage)
            }
            if( invoice.drh.equals("4")){
                Picasso.with(itemView.context).load(R.drawable.ic_account_balance_black_24dp).resize(120, 120).into(accImage)
            }
            if( invoice.drh.equals("5")){
                Picasso.with(itemView.context).load(R.drawable.ic_insert_drive_file_black_24dp).resize(120, 120).into(accImage)
            }
            if( invoice.drh.equals("99")){
                Picasso.with(itemView.context).load(R.drawable.ic_action_person).resize(120, 120).into(accImage)
            }
            itemView.setOnClickListener{listener(invoice)}

        }
    }

    fun setdata(list: List<RealmInvoice>){
        mList = list as MutableList<RealmInvoice>
        notifyDataSetChanged()
    }
}