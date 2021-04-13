package com.trippyTravel.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class Groups {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true, length = 250)
    private String name;

    @OneToOne
    private User owner;

    @OneToMany(mappedBy = "group")
    private List<Trips> trip;

    public Groups() {
    }

    public Groups(String name, User owner, List<Trips> trip) {
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

    public List<Trips> getTrip() { return trip; }

    public void setTrip(List<Trips> trip) { this.trip = trip; }
}
