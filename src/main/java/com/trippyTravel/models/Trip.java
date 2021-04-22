package com.trippyTravel.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table (name="trips")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 250)
    private String location;

    @Column(nullable = false, length = 250)
    private String name;

    @Column(nullable = false, length = 250)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String visibility;


    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trip")
    @JsonBackReference
    private List<Image> images;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trip")
    @JsonBackReference
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trip")
    @JsonBackReference
    private List<TripLikes> tripLikes;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trip")
    @JsonBackReference
    private List<Activity> activities;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "unreadCommentTrip")
    @JsonBackReference
    private List<GroupMember> groupMembers;


    public Trip() {
    }

    public Trip(String location, String name, String status, String description, String visibility, Date startDate, Date endDate, Group group, List<Image> images, List<Comment> comments, List<TripLikes> tripLikes, List<Activity> activities, List<GroupMember> groupMembers) {
        this.location = location;
        this.name = name;
        this.status = status;
        this.description = description;
        this.visibility = visibility;
        this.startDate = startDate;
        this.endDate = endDate;
        this.group = group;
        this.images = images;
        this.comments = comments;
        this.tripLikes = tripLikes;
        this.activities = activities;
        this.groupMembers = groupMembers;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Date getStartDate() { return startDate; }

    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }

    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public Group getGroup() { return group; }

    public void setGroup(Group group) { this.group = group; }

    public List<Image> getImages() { return images; }

    public void setImages(List<Image> images) { this.images = images; }

    public List<Comment> getComments() { return comments; }

    public void setComments(List<Comment> comments) { this.comments = comments; }

    public String getVisibility() { return visibility; }

    public void setVisibility(String visibility) { this.visibility = visibility; }


    public List<GroupMember> getGroupMembers() { return groupMembers; }

    public void setGroupMembers(List<GroupMember> groupMembers) { this.groupMembers = groupMembers; }

    public List<Activity> getActivities() { return activities; }

    public void setActivities(List<Activity> activities) { this.activities = activities; }

    public List<TripLikes> getTripLikes() { return tripLikes; }

    public void setTripLikes(List<TripLikes> tripLikes) { this.tripLikes = tripLikes; }
}
