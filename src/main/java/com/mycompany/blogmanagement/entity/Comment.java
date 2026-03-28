package com.mycompany.blogmanagement.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer commentId;
    
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    private String status;
    
    @Column(name = "created_at")
    private Timestamp createdAt;

    public Comment() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.status = "pending";
    }

    public Integer getCommentId() { return commentId; }
    public void setCommentId(Integer commentId) { this.commentId = commentId; }
    
    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public Comment getParentComment() { return parentComment; }
    public void setParentComment(Comment parentComment) { this.parentComment = parentComment; }
}
