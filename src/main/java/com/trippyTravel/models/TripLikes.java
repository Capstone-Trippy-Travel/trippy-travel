package com.trippyTravel.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity
@Table(name = "trip_likes")
public class TripLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long likes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    @JsonManagedReference
    private Trip trip;

    public TripLikes() {
    }

    public TripLikes(long likes, User user, Trip trip) {
        this.likes = likes;
        this.user = user;
        this.trip = trip;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public long getLikes() { return likes; }

    public void setLikes(long likes) { this.likes = likes; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Trip getTrip() { return trip; }

    public void setTrip(Trip trip) { this.trip = trip; }
}
