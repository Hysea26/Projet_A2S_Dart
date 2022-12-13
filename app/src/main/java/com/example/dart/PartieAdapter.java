package com.example.dart;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PartieAdapter extends RecyclerView.Adapter<PartieAdapter.MyViewHolder> {

    private ArrayList<RowItemPartie> mPartieList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgJoueur;
        public TextView nomJoueur;
        public TextView SetCompteur;
        public TextView LegCompteur;
        public TextView PointRestantRV;

        public MyViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            imgJoueur = itemView.findViewById(R.id.imgJoueur);
            nomJoueur = itemView.findViewById(R.id.nomJoueur);
            SetCompteur = itemView.findViewById(R.id.SetCompteur);
            LegCompteur = itemView.findViewById(R.id.LegCompteur);
            PointRestantRV = itemView.findViewById(R.id.PointRestantRV);

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

    public PartieAdapter(ArrayList<RowItemPartie> mPartieList) {
        this.mPartieList = mPartieList;
    }

    @NonNull
    @Override
    public PartieAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_row_item_partie, parent, false);
        MyViewHolder evh = new MyViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RowItemPartie currentItem = mPartieList.get(position);
        // holder.imgJoueur.setImageResource(currentItem.getImgUser());
        // holder.nomJoueur.setText(currentItem.getPseudo());
        holder.SetCompteur.setText(String.valueOf(3));
        holder.LegCompteur.setText(currentItem.getLeg());
        holder.PointRestantRV.setText(String.valueOf(mPartieList.get(position).getPoint()));
    }

    @Override
    public int getItemCount() {
        return mPartieList.size();
    }

}
