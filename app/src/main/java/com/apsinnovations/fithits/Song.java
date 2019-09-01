package com.apsinnovations.fithits;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


public class Song implements Serializable, Parcelable {
    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
     public String id;
     public String title;
     public String artist;
     public String Location;
    // public static ArrayList<Song> songs;
     public String albumpath;


     public Song() {
         id=null;
         title=null;
         artist=null;
         Location=null;
         albumpath=null;
    }

    public Song(String id,String alpath,String title, String artist,String location) {
        this.id=id;
         this.albumpath=alpath;
        this.title = title;
        this.artist = artist;
        this.Location=location;
    }

    protected Song(Parcel in) {
        id = in.readString();
        title = in.readString();
        artist = in.readString();
        Location = in.readString();
        albumpath = in.readString();
    }

    public void putSongData(String id,String title, String artist, String location){
        this.id=id;
        this.title = title;
        this.artist = artist;
        this.Location=location;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", Location='" + Location + '\'' +
                ", albumpath='" + albumpath + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(artist);
        parcel.writeString(Location);
        parcel.writeString(albumpath);
    }
}
