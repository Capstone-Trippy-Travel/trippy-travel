package com.trippyTravel.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name="groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true, length = 250)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String visibility;

    @Column(columnDefinition = "TEXT")
    private String profile_image;

    @OneToOne
    private User owner;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "group")
    @JsonBackReference
    private List<Trip> trip;


    @OneToMany(cascade = CascadeType.ALL,mappedBy = "group")
    @JsonBackReference
    private List<GroupMember> groupMembers;

    public Group() {
    }
    public Group(String name, User owner){
        this.name=name;
        this.owner=owner;
    }

    public Group(String name, String description, String visibility, String profile_image, User owner, List<Trip> trip, List<GroupMember> groupMembers) {
        this.name = name;
        this.description = description;
        this.visibility = visibility;
        this.profile_image = profile_image;
        this.owner = owner;
        this.trip = trip;
        this.groupMembers = groupMembers;
    }

    public Group(String name, String description, String profile_image, User owner, List<Trip> trip) {
        this.name = name;
        this.description = description;
        this.profile_image = profile_image;
        this.owner = owner;
        this.trip = trip;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public User getOwner() { return owner; }

    public void setOwner(User owner) { this.owner = owner; }

    public List<Trip> getTrip() { return trip; }

    public void setTrip(List<Trip> trip) { this.trip = trip; }

    public List<GroupMember> getGroupMembers() { return groupMembers; }

    public void setGroupMembers(List<GroupMember> groupMembers) { this.groupMembers = groupMembers; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getVisibility() { return visibility; }

    public void setVisibility(String visibility) { this.visibility = visibility; }

    public String getProfile_image() { return profile_image; }

    public void setProfile_image(String profile_image) { this.profile_image = profile_image; }
}
