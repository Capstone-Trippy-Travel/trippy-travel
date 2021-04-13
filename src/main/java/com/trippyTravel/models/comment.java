package com.trippyTravel.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity
@Table(name = "comment")
public class comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( name= "comment_text")
    private String comment_text;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    @JsonManagedReference
    private Trips trip;

    public comment(){}

    public comment(long id, String comment_text, User user, Trips trip) {
        this.id = id;
        this.comment_text = comment_text;
        this.user = user;
        this.trip = trip;
    }

    public comment(String comment_text, User user, Trips trip) {
        this.comment_text = comment_text;
        this.user = user;
        this.trip = trip;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Trips getTrip() {
        return trip;
    }

    public void setTrip(Trips trip) {
        this.trip = trip;
    }
}
