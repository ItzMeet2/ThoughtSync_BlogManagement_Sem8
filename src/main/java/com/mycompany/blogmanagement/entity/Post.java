package com.mycompany.blogmanagement.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    
    @Column(name = "featured_image")
    private String featuredImage;
    
    private String status;
    
    @Column(name = "view_count")
    private Integer viewCount;
    
    @Column(name = "like_count")
    private Integer likeCount;
    
    @Column(name = "created_at")
    private Timestamp createdAt;
    
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    
    @Column(name = "published_at")
    private Timestamp publishedAt;
    
    @ManyToMany
    @JoinTable(
        name = "post_tags",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Comment> comments;

    public Post() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = new Timestamp(System.currentTimeMillis());
        this.status = "draft";
        this.viewCount = 0;
        this.likeCount = 0;
    }

    public Integer getPostId() { return postId; }
    public void setPostId(Integer postId) { this.postId = postId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
    
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
    
    public Timestamp getPublishedAt() { return publishedAt; }
    public void setPublishedAt(Timestamp publishedAt) { this.publishedAt = publishedAt; }
    
    public Set<Tag> getTags() { return tags; }
    public void setTags(Set<Tag> tags) { this.tags = tags; }
    
    public Set<Comment> getComments() { return comments; }
    public void setComments(Set<Comment> comments) { this.comments = comments; }
    
    public String getFeaturedImage() { return featuredImage; }
    public void setFeaturedImage(String featuredImage) { this.featuredImage = featuredImage; }
    
    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }
    
    public Integer getLikeCount() { return likeCount; }
    public void setLikeCount(Integer likeCount) { this.likeCount = likeCount; }
}
