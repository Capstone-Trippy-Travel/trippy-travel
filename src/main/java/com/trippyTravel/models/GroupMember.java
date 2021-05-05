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

    public boolean isUnreadComment() {
        return unreadComment;
    }

    public void setUnreadComment(boolean unreadComment) {
        this.unreadComment = unreadComment;
    }

    public Trip getUnreadCommentTrip() {
        return unreadCommentTrip;
    }

    public void setUnreadCommentTrip(Trip unreadCommentTrip) {
        this.unreadCommentTrip = unreadCommentTrip;
    }

    @Column(name="unreadComment")
    private boolean unreadComment = false;

    @ManyToOne (optional = true)
    @JsonManagedReference
    @JoinColumn (name = "trip_id")
    private Trip unreadCommentTrip;

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
    @JsonManagedReference
    @JoinColumn (name = "group_id")
    private Group group;

    public GroupMember() {
    }



    public GroupMember(boolean isAdmin, User member, Group group) {
        this.isAdmin = isAdmin;
        this.member = member;
        this.group = group;
        this.unreadComment=false;
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
