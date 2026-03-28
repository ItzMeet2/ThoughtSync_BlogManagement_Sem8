package com.mycompany.blogmanagement.service;

import com.mycompany.blogmanagement.dao.PostDAO;
import com.mycompany.blogmanagement.entity.Post;
import java.sql.Timestamp;
import java.util.List;

public class PostService {
    private final PostDAO postDAO = new PostDAO();

    public void createPost(Post post) {
        postDAO.save(post);
    }

    public void updatePost(Post post) {
        post.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        postDAO.update(post);
    }

    public void publishPost(Integer postId) {
        Post post = postDAO.findById(postId);
        if (post != null) {
            post.setStatus("published");
            post.setPublishedAt(new Timestamp(System.currentTimeMillis()));
            postDAO.update(post);
        }
    }

    public Post getPostById(Integer id) {
        return postDAO.findById(id);
    }

    public List<Post> getAllPosts() {
        return postDAO.findAll();
    }

    public List<Post> getPublishedPosts() {
        return postDAO.findPublished();
    }

    public List<Post> getPostsByAuthor(Integer authorId) {
        return postDAO.findByAuthor(authorId);
    }

    public List<Post> getPostsByCategory(Integer categoryId) {
        return postDAO.findByCategory(categoryId);
    }

    public List<Post> searchPosts(String keyword) {
        return postDAO.searchPosts(keyword);
    }

    public List<Post> getMostViewedPosts(int limit) {
        return postDAO.findMostViewed(limit);
    }

    public List<Post> getMostLikedPosts(int limit) {
        return postDAO.findMostLiked(limit);
    }

    public void incrementViewCount(Integer postId) {
        postDAO.incrementViewCount(postId);
    }

    public void deletePost(Integer id) {
        postDAO.delete(id);
    }
}
