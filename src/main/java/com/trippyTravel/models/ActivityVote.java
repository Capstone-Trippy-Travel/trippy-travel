package com.trippyTravel.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity
@Table(name ="activity_votes")
public class ActivityVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    @JsonManagedReference
    private Activity activity;

    @Column(name = "vote")
    private boolean Vote;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ActivityVote(){}

    public ActivityVote(Long id, Activity activity, boolean vote, User user) {
        this.id = id;
        this.activity = activity;
        Vote = vote;
        this.user = user;
    }

    public ActivityVote(Activity activity, boolean vote, User user) {
        this.activity = activity;
        Vote = vote;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public boolean isVote() {
        return Vote;
    }

    public void setVote(boolean vote) {
        Vote = vote;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
