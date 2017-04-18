package com.nrs.shelfbeedev.adapter;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nrs.shelfbeedev.R;
import com.nrs.shelfbeedev.object.ObjectUser;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterList extends RecyclerView.Adapter<AdapterList.MyViewHolder>{

    private static final String[] colorArray = {"#D32F2F", "#C2185B", "#7B1FA2", "#512DA8", "#303F9F", "#1976D2", "#0288D1",
            "#0097A7", "#00796B", "#388E3C", "#689F38", "#AFB42B", "#FBC02D", "#FFA000", "#F57C00", "#E64A19"};
    ArrayList<ObjectUser> mList;
    Context mContext;
    private Random r = new Random();

    public AdapterList(Context context,ArrayList<ObjectUser> list){
        mContext = context;
        mList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.single_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ObjectUser user = mList.get(position);
        holder.mName.setText(user.getName());
        holder.mHeading.setText(String.valueOf(mList.get(position).getName().toUpperCase().charAt(0)));
        holder.mConatiner.setBackgroundTintList(stateList());
    }

    @Override
    public int getItemCount() {
        return mList.size();
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
       @BindView(R.id.itemHeading)
        TextView mHeading;
        @BindView(R.id.itemName)
        TextView mName;
        @BindView(R.id.itemHeadingContainer)
        RelativeLayout mConatiner;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}