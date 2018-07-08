package com.eusecom.samshopersung

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_choosecompany.view.*

class BasketKtAdapter(var myAndroidOSList: List<CompanyKt>, val listener: (CompanyKt) -> Unit) : RecyclerView.Adapter<BasketKtAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketKtAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.basket_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: BasketKtAdapter.ViewHolder, position: Int) {
        holder.bindItems(myAndroidOSList[position], listener)
    }

    override fun getItemCount(): Int {
        return myAndroidOSList.size
    }

    fun setAbsserver(listabsserver: List<CompanyKt>) {
        myAndroidOSList = listabsserver
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(myAndroidOS: CompanyKt, listener: (CompanyKt) -> Unit) = with(itemView)  {

            itemView.tvName.text = myAndroidOS.xcf + " " + myAndroidOS.naz
            itemView.tvVersion.text = myAndroidOS.rok
            //itemView.ivIcon.setImageResource(myAndroidOS.imageIcon)
            Picasso.with(itemView.context).load(R.drawable.ic_check_circle_black_24dp).resize(120, 120).into(itemView.ivIcon)
            itemView.setOnClickListener{listener(myAndroidOS)}

        }
    }


}
