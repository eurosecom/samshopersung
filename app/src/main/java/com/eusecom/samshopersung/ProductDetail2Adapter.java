package com.eusecom.samshopersung;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.eusecom.samshopersung.rxbus.RxBus;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ProductDetail2Adapter extends RecyclerView.Adapter<ProductDetail2Adapter.MyViewHolder> {
 
    private Context mContext;
    private List<ProductKt> productList;
    private RxBus mRxBus;
    private Picasso mPicasso;
 
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
 
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }
 
 
    public ProductDetail2Adapter(Context context, RxBus rxbus, Picasso picasso) {
        this.mContext = context;
        this.mRxBus = rxbus;
        this.mPicasso = picasso;
    }

    public void setProductItems(List<ProductKt> products) {
        productList = products;
        notifyDataSetChanged();
    }
 
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_prodetail2, parent, false);
 
        return new MyViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ProductKt prod = productList.get(position);
        holder.title.setText(prod.getCis() + " - " + prod.getNat());

        String imageurl = "https://picsum.photos/600/600?image=" + prod.getCis();
        mPicasso.load(imageurl).resize(120, 120).into(holder.thumbnail);

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        
    }

 
    @Override
    public int getItemCount() {
        return productList.size();
    }
}