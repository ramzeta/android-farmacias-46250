package com.example.farmaciasalcudia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FarmaciaAdapter extends ArrayAdapter<Farmacia> {
    private Context context;
    private List<Farmacia> farmacias;

    public FarmaciaAdapter(Context context, List<Farmacia> farmacias) {
        super(context, 0, farmacias);
        this.context = context;
        this.farmacias = farmacias;
    }


    private static class ViewHolder {
        TextView dateTextView;
        TextView guardiaTextView;
        TextView reforcTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_farmacia, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.dateTextView = convertView.findViewById(R.id.dateTextView);
            viewHolder.guardiaTextView = convertView.findViewById(R.id.guardiaTextView);
            viewHolder.reforcTextView = convertView.findViewById(R.id.reforcTextView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Farmacia farmacia = farmacias.get(position);
        viewHolder.dateTextView.setText(farmacia.getDate());
        viewHolder.guardiaTextView.setText(farmacia.getGuardia());
        viewHolder.reforcTextView.setText(farmacia.getReforc());

        return convertView;
    }
}
