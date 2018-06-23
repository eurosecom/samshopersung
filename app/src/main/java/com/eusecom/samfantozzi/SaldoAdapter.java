/*
 * Copyright (c) 2016 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.eusecom.samfantozzi;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.eusecom.samfantozzi.models.Attendance;
import com.eusecom.samfantozzi.rxbus.RxBus;
import com.squareup.picasso.Picasso;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SaldoAdapter extends RecyclerView.Adapter<SaldoAdapter.SaldoViewHolder> {

    private List<Invoice> mListabsserver;
    private RxBus _rxBus;
    private int salico;

    SaldoAdapter(RxBus bus, int msalico){

        _rxBus = bus;
        salico = msalico;
    }

  @Override
  public SaldoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saldo,parent,false);

    return new SaldoViewHolder(view);
  }

  @Override
  public void onBindViewHolder(SaldoViewHolder holder, int position) {

      holder.invoice_name.setText(mListabsserver.get(position).getNai());
      Picasso.with(holder.mContext).load(R.drawable.ic_call_made_black_24dp).resize(120, 120).into(holder.invoice_photo);
      if( mListabsserver.get(position).getDrh().equals("1")) {
          Picasso.with(holder.mContext).load(R.drawable.ic_call_received_black_24dp).resize(120, 120).into(holder.invoice_photo);
      }

      holder.idcx.setText(mListabsserver.get(position).getIco());
      holder.dasx.setBackgroundColor(Color.WHITE);

      //holder.invoicex.setText(mListabsserver.get(position).getFak() + " " + mListabsserver.get(position).getPoh());
      holder.invoicex.setText(mListabsserver.get(position).getFak());
      holder.datx.setText(mListabsserver.get(position).getDat());
      holder.dasx.setText(mListabsserver.get(position).getDas());
      if( salico == 0 )
      {
          holder.invoicetype.setVisibility(View.GONE);
          //holder.invoice.setVisibility(View.GONE);
      }else {
          holder.invoicetype.setVisibility(View.VISIBLE);
          if( !mListabsserver.get(position).getPoh().equals("0")) {
              holder.dasx.setBackgroundColor(holder.mContext.getResources().getColor(R.color.material_red_100));
          }
          //holder.invoice.setVisibility(View.VISIBLE);
      }

      holder.hodx.setText(mListabsserver.get(position).getHod());

      holder.paidx.setText(mListabsserver.get(position).getZk0());

      holder.balx.setText(mListabsserver.get(position).getZk1());

      holder.setClickListener(new SaldoAdapter.SaldoViewHolder.ClickListener() {
          public void onClick(View v, int pos, boolean isLongClick) {
              if (isLongClick) {

                  // View v at position pos is long-clicked.
                  Log.d("onLongSaldoList", mListabsserver.get(pos).getNai());
                  //getDialog(mListabsserver.get(position).longi, mListabsserver.get(position), holder.mContext);


              } else {

                  Log.d("onShortSaldoList", mListabsserver.get(pos).getNai());
                  _rxBus.send(mListabsserver.get(pos));
              }
          }
      });

  }//end onbindviewholder

    @Override
    public int getItemCount() {
        return mListabsserver == null ? 0 : mListabsserver.size();
    }


    public void setAbsserver(List<Invoice> listabsserver) {
        mListabsserver = listabsserver;
        //Log.d("setAbsserver ", mListabsserver.get(0).dmna);
        notifyDataSetChanged();
    }


  public static class SaldoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

      public TextView invoice_name;
      public ImageView invoice_photo;
      public TextView invoice;
      public TextView invoicex;
      public RelativeLayout invoicetype;
      public TextView idcx;
      public TextView hodx;
      public TextView paidx;
      public TextView balx;
      public TextView dat, datx, das, dasx;
      private ClickListener clickListener;
      Context mContext;

    public SaldoViewHolder(View itemView) {
        super(itemView);

        invoice_name = (TextView) itemView.findViewById(R.id.invoice_name);
        invoice_photo = (ImageView) itemView.findViewById(R.id.invoice_photo);
        invoice = (TextView) itemView.findViewById(R.id.invoice);
        invoicex = (TextView) itemView.findViewById(R.id.invoicex);
        invoicetype = (RelativeLayout) itemView.findViewById(R.id.invoice_type);
        idcx = (TextView) itemView.findViewById(R.id.idcx);
        hodx = (TextView) itemView.findViewById(R.id.hodx);
        paidx = (TextView) itemView.findViewById(R.id.paidx);
        balx = (TextView) itemView.findViewById(R.id.balx);
        dat = (TextView) itemView.findViewById(R.id.dat);
        datx = (TextView) itemView.findViewById(R.id.datx);
        das = (TextView) itemView.findViewById(R.id.das);
        dasx = (TextView) itemView.findViewById(R.id.dasx);
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);


    }
      /* Interface for handling clicks - both normal and long ones. */
      public interface ClickListener {

          /**
           * Called when the view is clicked.
           *
           * @param v view that is clicked
           * @param position of the clicked item
           * @param isLongClick true if long click, false otherwise
           */
          public void onClick(View v, int position, boolean isLongClick);

      }

      /* Setter for listener. */
      public void setClickListener(ClickListener clickListener) {
          this.clickListener = clickListener;
      }

      @Override
      public void onClick(View v) {

          // If not long clicked, pass last variable as false.
          clickListener.onClick(v, getPosition(), false);
      }

      @Override
      public boolean onLongClick(View v) {

          // If long clicked, passed last variable as true.
          clickListener.onClick(v, getPosition(), true);
          return true;
      }


  }//end class viewholder

    private String getDateString(String date){

        try{
            String strCurrentDate = date;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
            Date newDate = format.parse(strCurrentDate);

            format = new SimpleDateFormat("dd.mm.yy");
            String datenew = format.format(newDate);

            return datenew;
        }
        catch(Exception ex){
            return "xx";
        }
    }







}//end class adapter
