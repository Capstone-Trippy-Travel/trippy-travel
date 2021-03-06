package com.trippyTravel.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 20, unique = true)
    @javax.validation.constraints.NotBlank(message = "Username can't be empty")
    private String username;

    @Column(name = "first_name", nullable = false, length = 250)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 250)
    private String lastName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, unique = true)
    @javax.validation.constraints.Email(message = "Invalid email")
    @javax.validation.constraints.NotBlank(message = "Email can't be empty")
    private String email;

    @Column(nullable = false)
    @javax.validation.constraints.NotBlank(message = "Password can't be empty")
    @JsonIgnore
    private String password;

    @Column(columnDefinition = "TEXT")
    private String profile_image;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
    @JsonBackReference
    private List<FriendList> friends;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "member")
    @JsonBackReference
    private List<GroupMember> groupMember;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonBackReference
    private List<Image> images;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonBackReference
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonBackReference
    private List<TripLikes> tripLikes;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonBackReference
    private List<CommentReplies> commentReplies;


    public User() {
    }

    public User(@NotBlank(message = "Username can't be empty") String username, String firstName, String lastName, String description, @Email(message = "Invalid email") @NotBlank(message = "Email can't be empty") String email, @NotBlank(message = "Password can't be empty") String password, String profile_image, List<FriendList> friends, List<GroupMember> groupMember, List<Image> images, List<Comment> comments, List<CommentReplies> commentReplies, List<TripLikes> tripLikes) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
        this.email = email;
        this.password = password;
        this.profile_image = profile_image;
        this.friends = friends;
        this.groupMember = groupMember;
        this.images = images;
        this.comments = comments;
        this.commentReplies = commentReplies;
        this.tripLikes =tripLikes;
    }

    // Copy constructor an alternative for clone
    public User(User user) {
        this.id = user.id;
        this.username = user.username;
        this.password = user.password;
        this.email = user.email;
        this.firstName =user.firstName;
        this.lastName = user.lastName;
    }


    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getProfile_image() { return profile_image; }

    public void setProfile_image(String profile_image) { this.profile_image = profile_image; }

    public List<FriendList> getFriends() { return friends; }

    public void setFriends(List<FriendList> friends) { this.friends = friends; }

    public List<GroupMember> getGroupMember() { return groupMember; }

    public void setGroupMember(List<GroupMember> groupMember) { this.groupMember = groupMember; }

    public List<Image> getImages() { return images; }

    public void setImages(List<Image> images) { this.images = images; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public List<Comment> getComments() { return comments; }

    public void setComments(List<Comment> Comments) { this.comments = Comments; }

    public List<CommentReplies> getCommentReplies() { return commentReplies; }

    public void setCommentReplies(List<CommentReplies> commentReplies) { this.commentReplies = commentReplies; }

    @Override
    public String toString() {
        return String.format("%s email is %s and its ID is %d", username, email, id);
    }

    public String getFriendStatus() {
        return friendStatus;
    }

    public void setFriendStatus(String friendStatus) {
        this.friendStatus = friendStatus;
    }

    public String friendStatus = "not friends";

    public List<TripLikes> getTripLikes() { return tripLikes; }

    public void setTripLikes(List<TripLikes> tripLikes) { this.tripLikes = tripLikes; }
}