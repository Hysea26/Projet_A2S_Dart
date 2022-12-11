package com.example.dart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PartieAdapter extends RecyclerView.Adapter<PartieAdapter.MyViewHolder> {

    private ArrayList<RowItemPartie> mPartieList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImgUserView;
        public TextView mPseudoView;
        public TextView SetCompteur;
        public TextView LegCompteur;
        public TextView PointRestant;

        public MyViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImgUserView = itemView.findViewById(R.id.id_ImgUser_RIJ);
            mPseudoView = itemView.findViewById(R.id.id_pseudo_RIJ);
            SetCompteur = itemView.findViewById(R.id.SetCompteur);
            LegCompteur = itemView.findViewById(R.id.LegCompteur);
            PointRestant = itemView.findViewById(R.id.PointRestant);

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

    public PartieAdapter(ArrayList<RowItemPartie> exampleList) {
        mPartieList = exampleList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_row_item_partie, parent, false);
        MyViewHolder evh = new MyViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RowItemPartie currentItem = mPartieList.get(position);

    //    holder.mImgUserView.setImageResource(currentItem.getImgUser());
     //   holder.mPseudoView.setText(currentItem.getPseudo());
    }

    @Override
    public int getItemCount() {
        return mPartieList.size();
    }
}
