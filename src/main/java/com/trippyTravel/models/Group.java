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



    public Group(String name, User owner, List<Trip> trip) {
        this.name = name;
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


}
