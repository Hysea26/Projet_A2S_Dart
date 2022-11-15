package com.example.dart;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomBaseAdapter extends BaseAdapter {

    Context context;
    String parametres[];
    int Statistiques_Images[];
    LayoutInflater inflater;

    public CustomBaseAdapter(Context ctx, String [] parametres, int [] Statistiques_Images) {
        this.context = ctx;
        this.parametres = parametres;
        this.Statistiques_Images = Statistiques_Images;
        inflater = LayoutInflater.from(ctx);


    }


    @Override
    public int getCount() {
        return parametres.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.activity_custom_list_view, null);
        TextView txtView = (TextView) convertView.findViewById(R.id.text_Metres);
        ImageView Statistiques_Images = (ImageView) convertView.findViewById(R.id.Image_Icon_Metres)
        txtView.setText(parametres[position]);
        Statistiques_Images.setImageResource(Statistiques_Images[position]);
        return convertView;

        return null;
    }
}
