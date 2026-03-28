package com.mycompany.blogmanagement.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer categoryId;
    
    @Column(name = "category_name", unique = true, nullable = false)
    private String categoryName;
    
    private String description;
    
    @Column(name = "created_at")
    private Timestamp createdAt;
    
    @OneToMany(mappedBy = "category")
    private Set<Post> posts;

    public Category() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public Set<Post> getPosts() { return posts; }
    public void setPosts(Set<Post> posts) { this.posts = posts; }
}
