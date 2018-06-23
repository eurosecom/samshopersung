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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.eusecom.samfantozzi.models.BankItem;
import com.eusecom.samfantozzi.rxbus.RxBus;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AccountItemAdapter extends RecyclerView.Adapter<AccountItemAdapter.AccountItemViewHolder> {

    private List<BankItem> mListabsserver;
    private KlikaciListener mKlikaciListener;

    interface KlikaciListener {
        void klikolSomItem(BankItem item);
    }

    AccountItemAdapter(KlikaciListener klikaciListener){

        this.mKlikaciListener = klikaciListener;
    }

  @Override
  public AccountItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_accountitem,parent,false);

    return new AccountItemViewHolder(view);
  }

  @Override
  public void onBindViewHolder(AccountItemViewHolder holder, int position) {

      holder.invoice_name.setText(mListabsserver.get(position).getUcm() + " / "
              + mListabsserver.get(position).getUcd() + " "
              + mListabsserver.get(position).getNai() + " "
              + mListabsserver.get(position).getPop());
      String ucm = mListabsserver.get(position).getUcm();
      String ucm3 = "";
      if( ucm.length() >= 3 ) {ucm3 = ucm.substring(0, 3);}

      Picasso.with(holder.mContext).load(R.drawable.ic_call_made_black_24dp).resize(120, 120).into(holder.invoice_photo);
      if( mListabsserver.get(position).getDrh().equals("4")) {
          Picasso.with(holder.mContext).load(R.drawable.ic_account_balance_black_24dp).resize(120, 120).into(holder.invoice_photo);

          String receipt = "1";
          if( ucm.length() < 3 ) { receipt = "0"; }
          if( ucm.length() >= 3 ) {
              if (!ucm3.equals("221")) {
                  receipt = "0";
              }
          }
          if( receipt.equals("1")) {
              Picasso.with(holder.mContext).load(R.drawable.ic_account_balance_blue_24dp).resize(120, 120).into(holder.invoice_photo);
          }else{
              Picasso.with(holder.mContext).load(R.drawable.ic_account_balance_red_24dp).resize(120, 120).into(holder.invoice_photo);
          }

      }


      holder.docx.setText(mListabsserver.get(position).getDok());

      //holder.datex.setText(getDateString(mListabsserver.get(position).getDat()));
      holder.datex.setText(mListabsserver.get(position).getDat());

      holder.invoicex.setText(mListabsserver.get(position).getFak());

      holder.valuex.setText(mListabsserver.get(position).getHod());

      holder.setClickListener(new AccountItemAdapter.AccountItemViewHolder.ClickListener() {
          public void onClick(View v, int pos, boolean isLongClick) {
              if (isLongClick) {

                  // View v at position pos is long-clicked.
                  Log.d("onLongClickListener", mListabsserver.get(pos).getHod());

              } else {

                  Log.d("onShortClickListener", mListabsserver.get(pos).getHod());
                  mKlikaciListener.klikolSomItem(mListabsserver.get(pos));
              }
          }
      });

  }//end onbindviewholder

    @Override
    public int getItemCount() {
        return mListabsserver == null ? 0 : mListabsserver.size();
    }


    public void setBankItems(List<BankItem> listabsserver) {
        mListabsserver = listabsserver;
        //Log.d("setAbsserver ", mListabsserver.get(0).dmna);
        notifyDataSetChanged();
    }


  public static class AccountItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

      public TextView invoice_name;
      public ImageView invoice_photo;
      public TextView datex;
      public TextView invoicex;
      public TextView valuex;
      public TextView docx;
      private ClickListener clickListener;
      Context mContext;

    public AccountItemViewHolder(View itemView) {
        super(itemView);

        invoice_name = (TextView) itemView.findViewById(R.id.invoice_name);
        invoice_photo = (ImageView) itemView.findViewById(R.id.invoice_photo);
        datex = (TextView) itemView.findViewById(R.id.datex);
        invoicex = (TextView) itemView.findViewById(R.id.invoicex);
        valuex = (TextView) itemView.findViewById(R.id.valuex);
        docx = (TextView) itemView.findViewById(R.id.docx);
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
