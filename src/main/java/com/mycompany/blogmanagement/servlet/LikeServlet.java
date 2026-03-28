package com.mycompany.blogmanagement.servlet;

import com.mycompany.blogmanagement.dao.PostDAO;
import com.mycompany.blogmanagement.dao.PostLikeDAO;
import com.mycompany.blogmanagement.entity.Post;
import com.mycompany.blogmanagement.entity.PostLike;
import com.mycompany.blogmanagement.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

// Handles liking and unliking a specific blog post
@WebServlet("/like")
public class LikeServlet extends HttpServlet {
    private PostLikeDAO likeDAO = new PostLikeDAO();
    private PostDAO postDAO = new PostDAO();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        int postId = Integer.parseInt(request.getParameter("postId"));
        
        PostLike existingLike = likeDAO.findByPostAndUser(postId, user.getUserId());
        Post post = postDAO.findById(postId);
        
        if (existingLike != null) {
            likeDAO.delete(existingLike);
            post.setLikeCount(post.getLikeCount() - 1);
        } else {
            PostLike like = new PostLike();
            like.setPost(post);
            like.setUser(user);
            likeDAO.save(like);
            post.setLikeCount(post.getLikeCount() + 1);
        }
        
        postDAO.update(post);
        response.sendRedirect(request.getHeader("Referer"));
    }
}
