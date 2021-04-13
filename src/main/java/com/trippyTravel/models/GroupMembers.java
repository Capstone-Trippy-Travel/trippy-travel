package com.trippyTravel.models;


import javax.persistence.*;

@Entity
@Table(name = "group_members")
public class GroupMembers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private boolean isAdmin;
}
