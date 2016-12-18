package ch.epfl.sweng.project.models;

import java.util.List;

/**
 * Created by arnauddupeyrat on 08/11/16.
 */

public class Setting {

    private int ageMin;
    private int ageMax;
    private int radius;


    private List<String> musicTaste;

    public Setting(int ageMin, int ageMax, int radius, List<String> musicTaste) {
        this.ageMin = ageMin;
        this.ageMax = ageMax;
        this.radius = radius;
        this.musicTaste = musicTaste;
    }

    public Setting() {
    }

    public int getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(int ageMax) {
        this.ageMax = ageMax;
    }

    public int getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(int ageMin) {
        this.ageMin = ageMin;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public List<String> getMusicTaste() {
        return musicTaste;
    }

    public void setMusicTaste(List<String> musicTaste) {
        this.musicTaste = musicTaste;
    }

}
