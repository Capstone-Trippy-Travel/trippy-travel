package com.trippyTravel.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 20, unique = true)
    @NotBlank(message = "Username can't be empty")
    private String username;

    @Column(name = "first_name", nullable = false, length = 250)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 250)
    private String lastName;

    @Column(nullable = false, unique = true)
    @Email(message = "Invalid email")
    @NotBlank(message = "Email can't be empty")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password can't be empty")
    @JsonIgnore
    private String password;

    @Column(columnDefinition = "TEXT")
    private String profile_image;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<FriendList> friends;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "members")
    private List<GroupMembers> groupMembers;

    public User() {
    }

    public User(@NotBlank(message = "Username can't be empty") String username, String firstName, String lastName, @Email(message = "Invalid email") @NotBlank(message = "Email can't be empty") String email, @NotBlank(message = "Password can't be empty") String password, String profile_image, List<FriendList> friends, List<GroupMembers> groupMembers) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.profile_image = profile_image;
        this.friends = friends;
        this.groupMembers = groupMembers;
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

    public List<GroupMembers> getGroupMembers() { return groupMembers; }

    public void setGroupMembers(List<GroupMembers> groupMembers) { this.groupMembers = groupMembers; }

    @Override
    public String toString() {
        return String.format("%s email is %s and its ID is %d", username, email, id);
    }

}