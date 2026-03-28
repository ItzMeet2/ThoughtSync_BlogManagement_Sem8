package com.mycompany.blogmanagement.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "post_ratings", uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "user_id"}))
public class PostRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id")
    private Integer ratingId;
    
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(nullable = false)
    private Integer rating;
    
    @Column(name = "created_at")
    private Timestamp createdAt;

    public PostRating() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    public Integer getRatingId() { return ratingId; }
    public void setRatingId(Integer ratingId) { this.ratingId = ratingId; }
    
    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
