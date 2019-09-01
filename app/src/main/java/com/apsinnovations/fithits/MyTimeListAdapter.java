package com.apsinnovations.fithits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class MyTimeListAdapter extends ArrayAdapter<String> {
    Context context;
    int resource;
    List<String> objects;
    public MyTimeListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view=LayoutInflater.from(context).inflate(resource,parent,false);
        TextView txtTime = view.findViewById(R.id.txtTime);
        txtTime.setText(objects.get(position));

        return view;
    }
}
