package com.nrs.shelfbeedev.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nrs.shelfbeedev.R;
import com.nrs.shelfbeedev.object.ObjectBookTransaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterTransaction extends BaseAdapter {

    ArrayList<ObjectBookTransaction> mList;
    Context mContext;

    public AdapterTransaction(Context context, ArrayList<ObjectBookTransaction> list) {
        mContext = context;
        mList = list;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.single_transaction_item, parent, false);
            myViewHolder = new MyViewHolder(convertView);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }
        ObjectBookTransaction object = mList.get(position);
        myViewHolder.mName.setText(object.getName().substring(0,1).toUpperCase()+object.getName().substring(1));
        if(object.getTransStatus().equalsIgnoreCase("0")){
            myViewHolder.mDate.setText(makeDate(object.getBuytime()));
        }else {
            myViewHolder.mDate.setText(mContext.getResources().getString(R.string.detailDone));
        }
        return convertView;
    }

    private String makeDate(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(time));
        calendar.add(Calendar.DATE,7);
        return   "Due date : "+ formatter.format(calendar.getTime());
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.transItemName)
        TextView mName;
        @BindView(R.id.transItemDate)
        TextView mDate;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
