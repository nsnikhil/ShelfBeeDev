package com.nrs.shelfbeedev.adapter;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nrs.shelfbeedev.R;
import com.nrs.shelfbeedev.object.ObjectBook;
import com.nrs.shelfbeedev.object.ObjectRequest;
import com.nrs.shelfbeedev.object.ObjectUser;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterUserList extends BaseAdapter {

    private static final String[] colorArray = {"#D32F2F", "#C2185B", "#7B1FA2", "#512DA8", "#303F9F", "#1976D2", "#0288D1",
            "#0097A7", "#00796B", "#388E3C", "#689F38", "#AFB42B", "#FBC02D", "#FFA000", "#F57C00", "#E64A19"};
    ArrayList<ObjectUser> mList;
    ArrayList<ObjectRequest> mRList;
    ArrayList<ObjectBook> mBList;
    Context mContext;
    private int key = 0;
    private int type;
    private String akey;
    private Random r = new Random();

    public AdapterUserList(Context context, ArrayList<ObjectUser> list) {
        mContext = context;
        mList = list;
        type = 1;
    }

    public AdapterUserList(Context context, ArrayList<ObjectRequest> list,int k) {
        mContext = context;
        mRList = list;
        key = k;
        type = 2;
    }

    public AdapterUserList(Context context, ArrayList<ObjectBook> list,String k) {
        mContext = context;
        mBList = list;
        akey = k;
        type = 3;
    }

    private int getRandom() {
        int color = r.nextInt(colorArray.length);
        return Color.parseColor(colorArray[color]);
    }

    private ColorStateList stateList() {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_enabled},
                new int[]{-android.R.attr.state_enabled},
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_pressed}
        };
        int color = getRandom();
        int[] colors = new int[]{color, color, color, color};
        return new ColorStateList(states, colors);
    }

    @Override
    public int getCount() {
        if(type==1){
            return mList.size();
        }else if(type==2){
            return mRList.size();
        }else if(type==3){
            return mBList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(type==1){
            return mList.get(position);
        }else if(type==2){
            return mRList.get(position);
        }else if(type==3){
            return mBList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.single_item, parent, false);
            myViewHolder = new MyViewHolder(convertView);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }
        setView(myViewHolder,position);
        return convertView;
    }

    private void setView(MyViewHolder myViewHolder,int position){
        myViewHolder.mConatiner.setBackgroundTintList(stateList());
        if(type==1){
            ObjectUser user = mList.get(position);
            myViewHolder.mName.setText(user.getName());
            myViewHolder.mHeading.setText(String.valueOf(mList.get(position).getName().toUpperCase().charAt(0)));
        }else if(type==2){
            ObjectRequest request = mRList.get(position);
            myViewHolder.mName.setText(request.getName());
            myViewHolder.mHeading.setText(String.valueOf(mRList.get(position).getName().toUpperCase().charAt(0)));
        }else if(type==3){
            ObjectBook book = mBList.get(position);
            myViewHolder.mName.setText(book.getName());
            myViewHolder.mHeading.setText(String.valueOf(mBList.get(position).getName().toUpperCase().charAt(0)));
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemHeading) TextView mHeading;
        @BindView(R.id.itemName) TextView mName;
        @BindView(R.id.itemHeadingContainer)
        RelativeLayout mConatiner;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
