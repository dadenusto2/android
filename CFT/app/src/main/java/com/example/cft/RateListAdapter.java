package com.example.cft;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

class RateListAdapter extends BaseAdapter {//адаптер для элементов списка в активити
    private final ArrayList<Rate> Rates;//Список из ЦБ
    LayoutInflater Inflater;

    public RateListAdapter(ArrayList<Rate> arrayList, Context context) {
        Rates = arrayList;
        Inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() { return Rates.size(); }

    public Object getItem(int position){ return Rates.get(position); }

    public long getItemId(int position) {return position;}

    @SuppressLint("ViewHolder")
    // задаем элемент списка в активити
    public View getView(int position, View view, ViewGroup parent){
        view = Inflater.inflate(R.layout.rate, parent, false);
        if (Rates.isEmpty()) return view;
        ((TextView) view.findViewById(R.id.tvName)).setText(Rates.get(position).getName());
        ((TextView) view.findViewById(R.id.tvRate)).setText(Rates.get(position).getRate());
        ((TextView) view.findViewById(R.id.tvNominale)).setText(Rates.get(position).getNominale());
        return view;
    }

}
