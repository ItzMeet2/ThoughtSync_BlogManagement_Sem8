package com.mycompany.blogmanagement.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Integer tagId;
    
    @Column(name = "tag_name", unique = true, nullable = false)
    private String tagName;
    
    @ManyToMany(mappedBy = "tags")
    private Set<Post> posts;

    public Tag() {}

    public Tag(String tagName) {
        this.tagName = tagName;
    }

    public Integer getTagId() { return tagId; }
    public void setTagId(Integer tagId) { this.tagId = tagId; }
    
    public String getTagName() { return tagName; }
    public void setTagName(String tagName) { this.tagName = tagName; }
    
    public Set<Post> getPosts() { return posts; }
    public void setPosts(Set<Post> posts) { this.posts = posts; }
}
