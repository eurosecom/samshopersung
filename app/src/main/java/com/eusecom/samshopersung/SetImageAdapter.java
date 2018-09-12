package com.eusecom.samshopersung;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.eusecom.samshopersung.models.Product;
import com.eusecom.samshopersung.rxbus.RxBus;
import com.squareup.picasso.Picasso;
import java.util.List;

import javax.inject.Inject;


public class SetImageAdapter extends RecyclerView.Adapter<SetImageAdapter.SetImageViewHolder> {

    private List<ProductKt> mList;
    @Inject
    public RxBus mRxBus;
    @Inject
    public Picasso mPicasso;

    @Inject
    public SetImageAdapter(){

    }

    @Override
    public SetImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chosenitem,parent,false);

        return new SetImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SetImageViewHolder holder, int position) {

        holder.invoice_name.setText(mList.get(position).getNat());

        String imageurl = Constants.IMAGE_URL + mList.get(position).getCis();
        mPicasso.with(holder.mContext).load(imageurl).resize(250, 250).into(holder.invoice_photo);

        holder.docx.setText(mList.get(position).getCis() + " ");

        holder.setClickListener(new SetImageAdapter.SetImageViewHolder.ClickListener() {
            public void onClick(View v, int pos, boolean isLongClick) {
                if (isLongClick) {

                    Log.d("SetImageActivityLog", mList.get(pos).getNat());

                } else {

                    Log.d("SetImageActivityLog", mList.get(pos).getNat());
                    mRxBus.send(mList.get(pos));
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void remove(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void setDataToAdapter(List<ProductKt> listabsserver) {
        mList = listabsserver;
        notifyDataSetChanged();
    }


    public static class SetImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        public TextView invoice_name;
        public ImageView invoice_photo;
        public TextView docx;
        private ClickListener clickListener;
        Context mContext;

        public SetImageViewHolder(View itemView) {
            super(itemView);

            invoice_name = (TextView) itemView.findViewById(R.id.invoice_name);
            invoice_photo = (ImageView) itemView.findViewById(R.id.invoice_photo);
            docx = (TextView) itemView.findViewById(R.id.docx);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        public interface ClickListener {

            public void onClick(View v, int position, boolean isLongClick);

        }

        public void setClickListener(ClickListener clickListener) {
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View v) {

            clickListener.onClick(v, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {

            clickListener.onClick(v, getPosition(), true);
            return true;
        }


    }


}
