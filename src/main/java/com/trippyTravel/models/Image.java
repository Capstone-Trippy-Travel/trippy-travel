package com.trippyTravel.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn( name= "user_id")
    @JsonManagedReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    @JsonManagedReference
    private Trips trip;

    @ManyToOne
    @JoinColumn( name= "activity_id")
    @JsonManagedReference
    private Activity activity;

    @Column( name= "image_url")
    private String image_url;

public Image(){}

    public Image(long id, User user, Trips trip, Activity activity, String image_url) {
        this.id = id;
        this.user = user;
        this.trip = trip;
        this.activity = activity;
        this.image_url = image_url;
    }

    public Image(User user, Trips trip, Activity activity, String image_url) {
        this.user = user;
        this.trip = trip;
        this.activity = activity;
        this.image_url = image_url;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Trips getTrip() { return trip; }

    public void setTrip(Trips trip) {
        this.trip = trip;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
