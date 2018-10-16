package com.eusecom.samshopersung;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.eusecom.samshopersung.models.EkassaRequestBackup;
import com.eusecom.samshopersung.rxbus.RxBus;
import com.squareup.picasso.Picasso;
import java.util.List;


public class OrpRequestsAdapter extends RecyclerView.Adapter<OrpRequestsAdapter.OrpRequestsViewHolder> {

    private List<EkassaRequestBackup> mList;
    private RxBus mRxBus;
    private Picasso mPicasso;

    public OrpRequestsAdapter(RxBus bus, Picasso picasso){

        mRxBus = bus;
        mPicasso = picasso;
    }

    @Override
    public OrpRequestsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_request,parent,false);

        return new OrpRequestsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrpRequestsViewHolder holder, int position) {

        holder.idx.setText(mList.get(position).getId() + " ");
        holder.reqUuid.setText("reqUuid " + mList.get(position).getRequestUuid());
        holder.reqDate.setText("reqDate " + mList.get(position).getRequestDate());
        holder.reqCount.setText("reqCount " + mList.get(position).getSendingCount());
        holder.reqReceipt.setText("reqReceipt " + mList.get(position).getReceiptNumber());
        holder.reqStr.setText("reqString " + mList.get(position).getRequestStr());
        holder.resUuid.setText("resUuid " + mList.get(position).getResponseUuid());
        holder.resId.setText("resId " + mList.get(position).getReceiptDataId());

        Picasso.with(holder.mContext).load(R.drawable.ic_call_made_black_24dp).resize(120, 120).into(holder.reqimage);

        if(mList.get(position).getResponseUuid().equals("")){
            Picasso.with(holder.mContext).load(R.drawable.ic_call_made_red_24dp).resize(120, 120).into(holder.reqimage);
        }
        if(mList.get(position).getResponseUuid().equals("Error")){
            Picasso.with(holder.mContext).load(R.drawable.baseline_highlight_off_black_18dp).resize(120, 120).into(holder.reqimage);
        }

        holder.setClickListener(new OrpRequestsAdapter.OrpRequestsViewHolder.ClickListener() {
            public void onClick(View v, int pos, boolean isLongClick) {
                if (isLongClick) {

                    Log.d("OrpRequestsActivityLog", mList.get(pos).getId() + "");

                } else {

                    Log.d("OrpRequestsActivityLog", mList.get(pos).getId() + "");
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

    public void setDataToAdapter(List<EkassaRequestBackup> listabsserver) {
        mList = listabsserver;
        notifyDataSetChanged();
    }


    public static class OrpRequestsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        public TextView idx;
        public TextView reqUuid;
        public TextView reqDate;
        public TextView reqCount;
        public TextView reqReceipt;
        public TextView reqStr;
        public ImageView reqimage;
        public TextView resUuid;
        public TextView resId;
        private ClickListener clickListener;
        Context mContext;

        public OrpRequestsViewHolder(View itemView) {
            super(itemView);

            idx = (TextView) itemView.findViewById(R.id.idx);
            reqUuid = (TextView) itemView.findViewById(R.id.reqUuid);
            reqDate = (TextView) itemView.findViewById(R.id.reqDate);
            reqCount = (TextView) itemView.findViewById(R.id.reqCount);
            reqReceipt = (TextView) itemView.findViewById(R.id.reqReceipt);
            reqStr = (TextView) itemView.findViewById(R.id.reqStr);
            reqimage = (ImageView) itemView.findViewById(R.id.reqimage);
            resUuid = (TextView) itemView.findViewById(R.id.resUuid);
            resId = (TextView) itemView.findViewById(R.id.resId);
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
