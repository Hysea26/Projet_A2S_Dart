package com.example.dart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapterJoueur extends RecyclerView.Adapter<MyAdapterJoueur.MyViewHolder> {

    private ArrayList<RowItemJoueur> mExampleList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImgUserView;
        public TextView mPseudoView;
        public TextView mNbPartiesView;
        public ImageView mSelectView;

        public MyViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImgUserView = itemView.findViewById(R.id.id_ImgUser_RIJ);
            mPseudoView = itemView.findViewById(R.id.id_pseudo_RIJ);
            mNbPartiesView = itemView.findViewById(R.id.id_NbParties_RIJ);
            mSelectView = itemView.findViewById(R.id.id_Img_select);

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

            mSelectView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public MyAdapterJoueur(ArrayList<RowItemJoueur> exampleList) {
        mExampleList = exampleList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_row_item_joueur, parent, false);
        MyViewHolder evh = new MyViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RowItemJoueur currentItem = mExampleList.get(position);

        holder.mImgUserView.setImageResource(currentItem.getImgRobotPart());
        holder.mPseudoView.setText(currentItem.getPseudo());
        holder.mNbPartiesView.setText(currentItem.getNbParties());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
