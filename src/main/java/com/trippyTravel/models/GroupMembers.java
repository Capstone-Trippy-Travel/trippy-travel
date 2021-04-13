package com.trippyTravel.models;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "group_members")
public class GroupMembers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="admin")
    private boolean isAdmin;

    @ManyToOne
    @JoinColumn (name = "user_id")
    private User members;

    @OneToOne
    private Groups group;

    public GroupMembers() {
    }

    public GroupMembers(boolean isAdmin, User members, Groups group) {
        this.isAdmin = isAdmin;
        this.members = members;
        this.group = group;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public boolean isAdmin() { return isAdmin; }

    public void setAdmin(boolean admin) { isAdmin = admin; }

    public User getMembers() { return members; }

    public void setMembers(User members) { this.members = members; }

    public Groups getGroup() { return group; }

    public void setGroup(Groups group) { this.group = group; }
}
