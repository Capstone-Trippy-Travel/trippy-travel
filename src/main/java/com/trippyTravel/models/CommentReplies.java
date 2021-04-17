package com.trippyTravel.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comment_replies")
public class CommentReplies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column( name= "comment_text")
    private String comment_text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    @JsonManagedReference
    private Comment parentComment;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column()
    private Date createdAt;

    public CommentReplies() {
    }

    public CommentReplies(String comment_text, User user, Comment parentComment, Date createdAt) {
        this.comment_text = comment_text;
        this.user = user;
        this.parentComment = parentComment;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getComment_text() { return comment_text; }

    public void setComment_text(String comment_text) { this.comment_text = comment_text; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Comment getParentComment() { return parentComment; }

    public void setParentComment(Comment parentComment) { this.parentComment = parentComment; }

    public Date getCreatedAt() { return createdAt; }

    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
