package com.eusecom.samshopersung;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.eusecom.samshopersung.rxbus.RxBus;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductDetailAdapter extends RecyclerView.Adapter<ProductDetailAdapter.MyViewHolder> {
 
    private Context mContext;
    private List<ProductKt> productList;
    private RxBus mRxBus;
    private Picasso mPicasso;
 
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count, count1, desc1;
        public ImageView thumbnail, overflow;
 
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            count1 = (TextView) view.findViewById(R.id.count1);
            desc1 = (TextView) view.findViewById(R.id.desc1);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }
 
 
    public ProductDetailAdapter(Context context, RxBus rxbus, Picasso picasso) {
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
                .inflate(R.layout.row_prodetail, parent, false);
 
        return new MyViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ProductKt prod = productList.get(position);
        holder.title.setText(prod.getCis() + " - " + prod.getNat());

        holder.count1.setVisibility(View.VISIBLE);
        holder.count.setPaintFlags(holder.count.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        if( prod.getCed1().equals("0")) {
            holder.count1.setVisibility(View.GONE);
            holder.count.setPaintFlags(holder.count.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }

        holder.count.setText(prod.getCed() + " €");
        holder.count1.setText(prod.getCed1() + " €");
        holder.desc1.setText(prod.getDesc());

        String imageurl = "https://picsum.photos/500/500?image=" + prod.getCis();
        // loading prod cover using Glide library
        //Glide.with(mContext).load(imageurl).into(holder.thumbnail);

        // loading album cover using Dagger2 and Picasso library
        //Picasso.with(mContext).load(imageurl).resize(120, 120).into(holder.thumbnail);
        mPicasso.load(imageurl).resize(120, 120).into(holder.thumbnail);

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, prod);
            }
        });

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, prod);
            }
        });
    }
 
    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, ProductKt product) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.prodetail_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(product));
        popup.show();
    }
 
    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private ProductKt mprod;
        public MyMenuItemClickListener(ProductKt product) {
            this.mprod=product;
        }
 
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.action_add_basket:
                    //Toast.makeText(mContext, "Add to basket " + mprod.getNat(), Toast.LENGTH_SHORT).show();
                    mprod.setPrm1("101");
                    mRxBus.send(mprod);
                    return true;

                case R.id.action_add_favourite:
                    //Toast.makeText(mContext, "Add to favourite " + mprod.getNat(), Toast.LENGTH_SHORT).show();
                    mprod.setPrm1("111");
                    mRxBus.send(mprod);
                    return true;

                case R.id.action_delfav:
                    //Toast.makeText(mContext, "Get detail " + mprod.getNat(), Toast.LENGTH_SHORT).show();
                    mprod.setPrm1("114");
                    mRxBus.send(mprod);
                    return true;

                default:
            }
            return false;
        }
    }
 
    @Override
    public int getItemCount() {
        return productList.size();
    }
}