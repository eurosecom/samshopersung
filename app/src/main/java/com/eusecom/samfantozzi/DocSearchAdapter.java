package com.eusecom.samfantozzi;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eusecom.samfantozzi.models.BankItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DocSearchAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<BankItem> studentList;

    // The minimum amount of items to have below your current scroll position before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount, sizeStudents, displayedStudents;
    private boolean loading;
    private DocSearchOnLoadMoreListener onLoadMoreListener;


    public DocSearchAdapter(List<BankItem> students, RecyclerView recyclerView) {
        studentList = students;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                            try {
                                sizeStudents = Integer.parseInt(studentList.get(0).getBal());
                            }catch(IndexOutOfBoundsException e){
                                sizeStudents = 0;
                            }
                            displayedStudents = studentList.size();

                            //Log.d("DocSearchMvp ", "size " + sizeStudents);
                            //Log.d("DocSearchMvp ", "displayed " + displayedStudents);

                            if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold) && displayedStudents < sizeStudents ) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return studentList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.docsearch_item, parent, false);

            vh = new StudentViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.docsearch_progressbar, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StudentViewHolder) {

            BankItem singleStudent = (BankItem) studentList.get(position);

            Picasso.with(((StudentViewHolder) holder).mContext).load(R.drawable.ic_call_made_black_24dp)
                    .resize(120, 120).into(((StudentViewHolder) holder).invoice_photo);

            if( singleStudent.getDrh().equals("3")) {
                Picasso.with(((StudentViewHolder) holder).mContext).load(R.drawable.ic_local_atm_black_24dp)
                        .resize(120, 120).into(((StudentViewHolder) holder).invoice_photo);
            }
            if( singleStudent.getDrh().equals("4")) {
                Picasso.with(((StudentViewHolder) holder).mContext).load(R.drawable.ic_account_balance_black_24dp)
                        .resize(120, 120).into(((StudentViewHolder) holder).invoice_photo);
            }
            if( singleStudent.getDrh().equals("1")) {
                Picasso.with(((StudentViewHolder) holder).mContext).load(R.drawable.ic_call_made_black_24dp)
                        .resize(120, 120).into(((StudentViewHolder) holder).invoice_photo);
            }
            if( singleStudent.getDrh().equals("2")) {
                Picasso.with(((StudentViewHolder) holder).mContext).load(R.drawable.ic_call_received_black_24dp)
                        .resize(120, 120).into(((StudentViewHolder) holder).invoice_photo);
            }
            if( singleStudent.getDrh().equals("5")) {
                Picasso.with(((StudentViewHolder) holder).mContext).load(R.drawable.ic_insert_drive_file_black_24dp)
                        .resize(120, 120).into(((StudentViewHolder) holder).invoice_photo);
            }

            ((StudentViewHolder) holder).invoice_name.setText(singleStudent.getUcm() + " / "
                    + singleStudent.getUcd() + " "
                    + singleStudent.getNai() + " "
                    + singleStudent.getPop());

            //((StudentViewHolder) holder).docx.setText(position + ". " + singleStudent.getDok());
            ((StudentViewHolder) holder).docx.setText(singleStudent.getDok());

            ((StudentViewHolder) holder).datex.setText(singleStudent.getDat());

            ((StudentViewHolder) holder).invoicex.setText(singleStudent.getFak());

            ((StudentViewHolder) holder).valuex.setText(singleStudent.getHod());

            ((StudentViewHolder) holder).student = singleStudent;

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public void setOnLoadMoreListener(DocSearchOnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //student viewholder
    public static class StudentViewHolder extends RecyclerView.ViewHolder {

        public TextView invoice_name;
        public ImageView invoice_photo;
        public TextView datex;
        public TextView invoicex;
        public TextView valuex;
        public TextView docx;
        public BankItem student;
        Context mContext;

        public StudentViewHolder(View v) {
            super(v);
            invoice_name = (TextView) v.findViewById(R.id.invoice_name);
            invoice_photo = (ImageView) v.findViewById(R.id.invoice_photo);
            datex = (TextView) v.findViewById(R.id.datex);
            invoicex = (TextView) v.findViewById(R.id.invoicex);
            valuex = (TextView) v.findViewById(R.id.valuex);
            docx = (TextView) v.findViewById(R.id.docx);
            mContext = itemView.getContext();

            v.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),
                            "OnClick :" + student.getDok() + " \n " + student.getPop(),
                            Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    //progressbar viewholder
    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

}
