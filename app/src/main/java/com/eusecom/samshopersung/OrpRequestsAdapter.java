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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category,parent,false);

        return new OrpRequestsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrpRequestsViewHolder holder, int position) {

        holder.invoice_name.setText(mList.get(position).getRequestUuid());

        holder.docx.setText(mList.get(position).getId() + " ");

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

        public TextView invoice_name;
        public ImageView invoice_photo;
        public TextView docx;
        private ClickListener clickListener;
        Context mContext;

        public OrpRequestsViewHolder(View itemView) {
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
