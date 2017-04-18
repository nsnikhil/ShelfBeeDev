package com.nrs.shelfbeedev.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nrs.shelfbeedev.R;
import com.nrs.shelfbeedev.object.ObjectTransaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterTransaction extends RecyclerView.Adapter<AdapterTransaction.MyViewHolder>{

    ArrayList<ObjectTransaction> mList;
    Context mContext;

    public AdapterTransaction(Context context,ArrayList<ObjectTransaction> list){
        mContext = context;
        mList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.single_transaction_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ObjectTransaction objectTransaction = mList.get(position);
        holder.mName.setText(objectTransaction.getBookId());
        holder.mDate.setText(makeDate(objectTransaction.getBuytime()));
    }

    private String makeDate(String time){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(time));
        return formatter.format(calendar.getTime());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.transItemName) TextView mName;
        @BindView(R.id.transItemDate) TextView mDate;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
