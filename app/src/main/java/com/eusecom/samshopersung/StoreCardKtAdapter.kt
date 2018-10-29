package com.eusecom.samshopersung

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.storecard_item.view.*

class StoreCardKtAdapter(var mImageUrl: ImageUrl, var myAndroidOSList: MutableList<BasketKt>, val listener: (BasketKt, Int, Int) -> Unit) : RecyclerView.Adapter<StoreCardKtAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreCardKtAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.storecard_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: StoreCardKtAdapter.ViewHolder, position: Int) {
        holder.bindItems(mImageUrl, myAndroidOSList[position], position, listener)
    }

    override fun getItemCount(): Int {
        return myAndroidOSList.size
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(mImageUrl: ImageUrl, myAndroidOS: BasketKt, myPos: Int, listener: (BasketKt, Int, Int) -> Unit) = with(itemView)  {

            itemView.title.text = myAndroidOS.xcis + " " + myAndroidOS.xnat

            itemView.count.text = myAndroidOS.xmno + " x "
            itemView.count1.text = myAndroidOS.xced + " €"

            // loading prod cover using Glide library
            Glide.with(itemView.context).load(R.drawable.ic_local_atm_blue_24dp).into(itemView.thumbnail)

            itemView.pdfdoc.setOnClickListener{listener(myAndroidOS, myPos, 0)}


        }
    }


}
