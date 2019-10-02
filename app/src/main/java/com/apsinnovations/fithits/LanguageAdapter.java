package com.apsinnovations.fithits;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> implements Filterable {
    Context context;
    int resource;
    ArrayList<SelectMusicLanguage> objects;
    Activity mactivity;
    ArrayList<SelectMusicLanguage> objectsfull;
    private Filter FilterRecords;

    public LanguageAdapter(Context context, int resource, ArrayList<SelectMusicLanguage> objects){
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        objectsfull=new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public LanguageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resource, parent, false);
        LanguageAdapter.ViewHolder holder = new LanguageAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final LanguageAdapter.ViewHolder holder, final int position) {
        SelectMusicLanguage selectMusicLanguage=objects.get(position);
        holder.textLanguage.setText(""+selectMusicLanguage.Name);
        holder.imagelanguage.setImageResource(selectMusicLanguage.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectMusicLanguage selectMusicLanguage=objects.get(position);
                Intent intent=new Intent(holder.itemView.getContext(),PlayPunjabiActivity.class);
                intent.putExtra("keyImage",selectMusicLanguage.image);
                intent.putExtra("keyName",selectMusicLanguage.Name);
                holder.itemView.getContext().startActivity(intent);
                mactivity=(Activity)holder.itemView.getContext();
                mactivity.overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);

            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }
    @Override
    public Filter getFilter() {
        if(FilterRecords==null){
            FilterRecords=new LanguageAdapter.RecordFilter();
        }
        return FilterRecords;
    }

    private class RecordFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            ArrayList<SelectMusicLanguage> fRecords = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                //No need for filter
//                results.values = objects;
//                results.count = objects.size();
                fRecords.addAll(objectsfull);

            } else {
                //Need Filter
                // it matches the text  entered in the edittext and set the data in adapter list


                for (SelectMusicLanguage s : objectsfull) {
                    if (s.Name.toUpperCase().trim().contains(charSequence.toString().toUpperCase().trim())) {
                        fRecords.add(s);
                    }
                }

            }
            results.values = fRecords;
            results.count = fRecords.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            objects.clear();
            objects = (ArrayList<SelectMusicLanguage>) filterResults.values;
            notifyDataSetChanged();
        }
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imagelanguage;
        TextView textLanguage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           imagelanguage=itemView.findViewById(R.id.language_image);
           textLanguage=itemView.findViewById(R.id.language_name);
        }
    }
}
