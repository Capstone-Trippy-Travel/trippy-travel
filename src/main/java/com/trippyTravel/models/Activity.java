package com.trippyTravel.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "activities")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "place")
    private String place;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    @JsonManagedReference
    private Trip trip;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "activity")
    private List<Image> images;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "activity")
    private List<comment> comments;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "activity")
    private List<ActivityVote> activityVotes;

    public Activity(){}


    public Activity(String place, String description, Trip trip, List<Image> images, List<comment> comments, List<ActivityVote> activityVotes) {
        this.place = place;
        this.description = description;
        this.trip = trip;
        this.images = images;
        this.comments = comments;
        this.activityVotes = activityVotes;
    }

    public Activity(Long id, String place, String description, Trip trip, List<Image> images, List<comment> comments, List<ActivityVote> activityVotes) {
        this.id = id;
        this.place = place;
        this.description = description;
        this.trip = trip;
        this.images = images;
        this.comments = comments;
        this.activityVotes = activityVotes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public List<Image> getImages() { return images; }

    public void setImages(List<Image> images) { this.images = images; }

    public List<comment> getComments() { return comments; }

    public void setComments(List<comment> comments) { this.comments = comments; }

    public List<ActivityVote> getActivityVotes() { return activityVotes; }

    public void setActivityVotes(List<ActivityVote> activityVotes) {this.activityVotes = activityVotes; }
}
