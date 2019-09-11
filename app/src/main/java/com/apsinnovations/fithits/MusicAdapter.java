package com.apsinnovations.fithits;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
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

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> implements Filterable {

    Context context;
    int resource;
    ArrayList<Song> objects;
    Activity mactivity;
    ArrayList<Song> objectsfull;
private Filter FilterRecords;

    public MusicAdapter(Context context, int resource, ArrayList<Song> objects) {
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        objectsfull=new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public MusicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resource, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MusicAdapter.ViewHolder holder, final int position) {
        Song song = objects.get(position);
        //Log.i("Song",""+song);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Song song = objects.get(position);
                Intent intent=new Intent(holder.itemView.getContext(),PlayMusicActivity.class);
                intent.putExtra("keySong",song.title);
                intent.putExtra("keyLocation",song.Location);
                intent.putExtra("keyArtist",song.artist);
                intent.putExtra("keyAlbum",song.albumpath);
                intent.putExtra("keyId",song.id);
                intent.putExtra("Songs",objects);

                holder.itemView.getContext().startActivity(intent);
                mactivity=(Activity)holder.itemView.getContext();
                mactivity.overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });


    if(!(song.albumpath==null)){
        Drawable img=Drawable.createFromPath(song.albumpath);
        holder.image.setImageDrawable(img);
        holder.txtTitle.setText(song.title);
        holder.txtArtist.setText(song.artist);
    }else{
            holder.image.setBackgroundResource(R.mipmap.ic_launcher);
            holder.txtTitle.setText(song.title);
            holder.txtArtist.setText(song.artist);
        }

    }

    @Override
    public int getItemCount() {

        return objects.size();
    }

    @Override
    public Filter getFilter() {
    if(FilterRecords==null){
        FilterRecords=new RecordFilter();
}
        return FilterRecords;
    }

    private class RecordFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            ArrayList<Song> fRecords = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                //No need for filter
//                results.values = objects;
//                results.count = objects.size();
                fRecords.addAll(objectsfull);

            } else {
                //Need Filter
                // it matches the text  entered in the edittext and set the data in adapter list


                for (Song s : objectsfull) {
                    if (s.title.toUpperCase().trim().contains(charSequence.toString().toUpperCase().trim())) {
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
            objects = (ArrayList<Song>) filterResults.values;
            notifyDataSetChanged();
        }
    }

        class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView txtTitle;
        TextView txtArtist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.music_image);
            txtTitle = itemView.findViewById(R.id.songname);
            txtArtist = itemView.findViewById(R.id.song_artist);
        }
    }

}
