package com.eusecom.samshopersung;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.eusecom.samshopersung.models.EkassaSettings;
import com.eusecom.samshopersung.rxbus.RxBus;
import com.squareup.picasso.Picasso;
import java.util.List;


public class OrpSettingsAdapter extends RecyclerView.Adapter<OrpSettingsAdapter.OrpSettingsViewHolder> {

    private List<EkassaSettings> mList;
    private RxBus mRxBus;
    private Picasso mPicasso;

    public OrpSettingsAdapter(RxBus bus, Picasso picasso){

        mRxBus = bus;
        mPicasso = picasso;
    }

    @Override
    public OrpSettingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_request,parent,false);

        return new OrpSettingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrpSettingsViewHolder holder, int position) {

        holder.idx.setText(mList.get(position).getId() + " ");
        holder.reqUuid.setText("id " + mList.get(position).getId());
        holder.reqDate.setText("ico " + mList.get(position).getCompidc());
        holder.reqCount.setText("name " + mList.get(position).getCompname());
        holder.reqReceipt.setText("dic " + mList.get(position).getCompdic());

        Picasso.with(holder.mContext).load(R.drawable.ic_call_made_black_24dp).resize(120, 120).into(holder.reqimage);

        holder.setClickListener(new OrpSettingsAdapter.OrpSettingsViewHolder.ClickListener() {
            public void onClick(View v, int pos, boolean isLongClick) {
                if (isLongClick) {

                    Log.d("OrpSettingsActivityLog", mList.get(pos).getId() + "");

                } else {

                    Log.d("OrpSettingsActivityLog", mList.get(pos).getId() + "");
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

    public void setDataToAdapter(List<EkassaSettings> listabsserver) {
        mList = listabsserver;
        notifyDataSetChanged();
    }


    public static class OrpSettingsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

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

        public OrpSettingsViewHolder(View itemView) {
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
