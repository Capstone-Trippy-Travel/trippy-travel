package com.trippyTravel.models;

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

    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;


    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Image> images;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<comment> comments;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trip")
    private List<Activity> activities;

    public Trip() {
    }

    public Trip(String location, String name, String status, String description, Date startDate, Date endDate, Group group, List<Image> images, List<comment> comments, List<Activity> activities) {
        this.location = location;
        this.name = name;
        this.status = status;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.group = group;
        this.images = images;
        this.comments = comments;
        this.activities = activities;
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

    public List<comment> getComments() { return comments; }

    public void setComments(List<comment> comments) { this.comments = comments; }

    public List<Activity> getActivities() { return activities; }

    public void setActivities(List<Activity> activities) { this.activities = activities; }
}
