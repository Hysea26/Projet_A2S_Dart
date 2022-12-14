package com.example.dart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// My adapter parties en cours

public class MyAdapterPEC extends RecyclerView.Adapter<MyAdapterPEC.MyViewHolder> {

    private ArrayList<RowItemPEC> mExampleList;
    private MyAdapterPEC.OnItemClickListener mListener;

    public MyAdapterPEC(ArrayList<RowItemPEC> exampleList) {
        this.mExampleList = exampleList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(MyAdapterPEC.OnItemClickListener listener) {
        mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImgUserView;
        public TextView mPseudoView;
        public TextView mLegendeView;


        public MyViewHolder(View itemView, final MyAdapterPEC.OnItemClickListener listener) {
            super(itemView);
            mImgUserView = itemView.findViewById(R.id.id_ImgUser_PEC);
            mPseudoView = itemView.findViewById(R.id.id_pseudo_PEC);
            mLegendeView = itemView.findViewById(R.id.id_legende_PEC);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }



    @Override
    public MyAdapterPEC.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_row_item_pec, parent, false);
        MyAdapterPEC.MyViewHolder evh = new MyAdapterPEC.MyViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(MyAdapterPEC.MyViewHolder holder, int position) {
        RowItemPEC currentItem = mExampleList.get(position);
        holder.mImgUserView.setImageResource(currentItem.getImgUser());
        holder.mPseudoView.setText(currentItem.getPseudo());
        holder.mLegendeView.setText(currentItem.getLegende());

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
