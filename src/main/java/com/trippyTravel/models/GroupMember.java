package com.trippyTravel.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity
@Table(name = "group_members")
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="admin")
    private boolean isAdmin;

//    @ManyToOne
//    @JoinColumn (name = "user_id")
//    private User member;
//
//    @OneToOne
//    private Group group;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn (name = "user_id")
    private User member;

    @ManyToOne
    @JoinColumn (name = "group_id")
    private Group group;

    public GroupMember() {
    }



    public GroupMember(boolean isAdmin, User member, Group group) {
        this.isAdmin = isAdmin;
        this.member = member;
        this.group = group;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public boolean isAdmin() { return isAdmin; }

    public void setAdmin(boolean admin) { isAdmin = admin; }

    public User getMember() { return member; }

    public void setMember(User member) { this.member = member; }

    public Group getGroup() { return group; }

    public void setGroup(Group group) { this.group = group; }
}
