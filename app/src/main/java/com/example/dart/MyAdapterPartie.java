package com.example.dart;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapterPartie extends RecyclerView.Adapter<MyAdapterPartie.MyViewHolder> {

    private ArrayList<RowItemPartie> mExampleList;
    private OnItemClickListener mListener;

    public MyAdapterPartie(ArrayList<RowItemPartie> exampleList) {
        this.mExampleList = exampleList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImgUserView;
        public TextView mPseudoView;
        public TextView mNbSetView;
        public TextView mNbLegView;
        public TextView mPointRestantView;

        public MyViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImgUserView = itemView.findViewById(R.id.imgJoueur);
            mPseudoView = itemView.findViewById(R.id.nomJoueur);
            mNbSetView = itemView.findViewById(R.id.SetCompteur);
            mNbLegView = itemView.findViewById(R.id.LegCompteur);
            mPointRestantView = itemView.findViewById(R.id.PointRestantRV);

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
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_row_item_partie, parent, false);
        MyViewHolder evh = new MyViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(MyAdapterPartie.MyViewHolder holder, int position) {
        RowItemPartie currentItem = mExampleList.get(position);

        holder.mImgUserView.setImageResource(currentItem.getImgUser());
        holder.mPseudoView.setText(currentItem.getPseudo());
        holder.mNbLegView.setText(String.valueOf(currentItem.getLeg()));
        holder.mNbSetView.setText(String.valueOf(currentItem.getSet()));
        holder.mPointRestantView.setText(String.valueOf(currentItem.getPoint()));
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
