package com.trippyTravel.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "activities")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "activity")
    private List<ActivityVote> activityVotes;

    public Activity(){}


    public Activity(String place, String description, Trip trip, List<Image> images, List<Comment> Comments, List<ActivityVote> activityVotes) {
        this.place = place;
        this.description = description;
        this.trip = trip;
        this.images = images;
        this.comments = comments;
        this.activityVotes = activityVotes;
    }

    public Activity(Long id, String place, String description, Trip trip, List<Image> images, List<Comment> comments, List<ActivityVote> activityVotes) {
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

    public List<Comment> getComments() { return comments; }

    public void setComments(List<Comment> comments) { this.comments = comments; }

    public List<ActivityVote> getActivityVotes() { return activityVotes; }

    public void setActivityVotes(List<ActivityVote> activityVotes) {this.activityVotes = activityVotes; }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @Column(name = "address")
    private String address;

    @Column(name = "rating")
    private double rating;

    @Column(name = "reviews")
    private int reviews;

    @Column(name = "website")
    private String website;

    @Column(name = "phone")
    private String phone;

    @Column(name = "hours")
    private String hours;

    @Column(name = "placeId")
    private String placeId;
}
