package com.eusecom.samshopersung

import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.basket_item.view.*
import kotlinx.android.synthetic.main.item_choosecompany.view.*

class BasketKtAdapter(var myAndroidOSList: List<BasketKt>, val listener: (BasketKt) -> Unit) : RecyclerView.Adapter<BasketKtAdapter.ViewHolder>() {

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

    fun setAbsserver(listabsserver: List<BasketKt>) {
        myAndroidOSList = listabsserver
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(myAndroidOS: BasketKt, listener: (BasketKt) -> Unit) = with(itemView)  {

            itemView.title.text = myAndroidOS.xcis + " " + myAndroidOS.xnat

            itemView.count.text = myAndroidOS.xmno + " x "
            itemView.count1.text = myAndroidOS.xced + " €"

            val imageurl = "https://picsum.photos/500/500?image=" + myAndroidOS.xcis
            // loading prod cover using Glide library
            Glide.with(itemView.context).load(imageurl).into(itemView.thumbnail)

            itemView.setOnClickListener{listener(myAndroidOS)}

        }
    }


}