package com.mycompany.blogmanagement.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "post_likes", uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "user_id"}))
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Integer likeId;
    
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "created_at")
    private Timestamp createdAt;

    public PostLike() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    public Integer getLikeId() { return likeId; }
    public void setLikeId(Integer likeId) { this.likeId = likeId; }
    
    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
