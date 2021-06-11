package com.nect.friendlymony.Model;

public class FilterModel {

    String radius;
    String age_min;
    String age_max;
    String show_me;
    String typeDistance;


    public String getTypeDistance() {
        return typeDistance;
    }

    public void setTypeDistance(String typeDistance) {
        this.typeDistance = typeDistance;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getAge_min() {
        return age_min;
    }

    public void setAge_min(String age_min) {
        this.age_min = age_min;
    }

    public String getAge_max() {
        return age_max;
    }

    public void setAge_max(String age_max) {
        this.age_max = age_max;
    }

    public String getShow_me() {
        return show_me;
    }

    public void setShow_me(String show_me) {
        this.show_me = show_me;
    }
}
