package com.mycompany.blogmanagement.servlet;

import com.mycompany.blogmanagement.dao.PostDAO;
import com.mycompany.blogmanagement.dao.PostRatingDAO;
import com.mycompany.blogmanagement.entity.Post;
import com.mycompany.blogmanagement.entity.PostRating;
import com.mycompany.blogmanagement.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/rate")
public class RatingServlet extends HttpServlet {
    private PostRatingDAO ratingDAO = new PostRatingDAO();
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
        int rating = Integer.parseInt(request.getParameter("rating"));
        
        Post post = postDAO.findById(postId);
        PostRating existingRating = ratingDAO.findByPostAndUser(postId, user.getUserId());
        
        if (existingRating != null) {
            existingRating.setRating(rating);
            ratingDAO.update(existingRating);
        } else {
            PostRating newRating = new PostRating();
            newRating.setPost(post);
            newRating.setUser(user);
            newRating.setRating(rating);
            ratingDAO.save(newRating);
        }
        
        response.sendRedirect(request.getHeader("Referer"));
    }
}
