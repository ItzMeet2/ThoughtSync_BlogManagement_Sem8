package com.mycompany.blogmanagement.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "full_name")
    private String fullName;
    
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    
    @Column(name = "created_at")
    private Timestamp createdAt;
    
    @Column(name = "is_active")
    private Boolean isActive;
    
    @Column(name = "profile_picture")
    private String profilePicture;
    
    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;
    
    @OneToMany(mappedBy = "author")
    private Set<Post> posts;
    
    @OneToMany(mappedBy = "user")
    private Set<Comment> comments;

    public User() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.isActive = true;
    }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public Set<Post> getPosts() { return posts; }
    public void setPosts(Set<Post> posts) { this.posts = posts; }
    
    public Set<Comment> getComments() { return comments; }
    public void setComments(Set<Comment> comments) { this.comments = comments; }
    
    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }
    
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
}
