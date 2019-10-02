package com.apsinnovations.fithits;

public class SelectMusicLanguage {
    int image;
    String Name;
public SelectMusicLanguage(){

}
    public SelectMusicLanguage(int image, String name) {
        this.image = image;
        Name = name;
    }

    @Override
    public String toString() {
        return "SelectMusicLanguage{" +
                "image=" + image +
                ", Name='" + Name + '\'' +
                '}';
    }
}
