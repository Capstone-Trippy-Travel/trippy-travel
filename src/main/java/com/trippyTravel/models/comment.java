package com.trippyTravel.models;

import javax.persistence.*;

@Entity
@Table(name = "comment")
public class comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

}
