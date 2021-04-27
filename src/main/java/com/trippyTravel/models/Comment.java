package com.trippyTravel.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column( name= "comment_text")
    private String comment_text;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    @JsonManagedReference
    private Trip trip;

    @ManyToOne(optional = true)
    @JoinColumn( name= "activity_id")
    @JsonManagedReference
    private Activity activity;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column()
    private Date createdAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentComment")
    @JsonBackReference
    private List<CommentReplies> replies;

    public Comment(){}

    public Comment(long id, String comment_text, User user, Trip trip, Activity activity, Date createdAt, List<CommentReplies> replies) {
        this.id = id;
        this.comment_text = comment_text;
        this.user = user;
        this.trip = trip;
        this.activity = activity;
        this.createdAt = createdAt;
        this.replies = replies;
    }

    public Comment(String comment_text, User user, Trip trip, Activity activity, Date createdAt, List<CommentReplies> replies) {
        this.comment_text = comment_text;
        this.user = user;
        this.trip = trip;
        this.activity = activity;
        this.createdAt = createdAt;
        this.replies = replies;
    }

    public Comment(String comment_text, User user, Trip trip, Activity activity) {
        this.comment_text = comment_text;
        this.user = user;
        this.trip = trip;
        this.activity = activity;
    }

    public Comment(String comment_text, User user, Trip trip) {
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

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Activity getActivity() { return activity; }

    public void setActivity(Activity activity) { this.activity = activity; }

    public Date getCreatedAt() { return createdAt; }

    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public List<CommentReplies> getReplies() { return replies; }

    public void setReplies(List<CommentReplies> replies) { this.replies = replies; }
}
