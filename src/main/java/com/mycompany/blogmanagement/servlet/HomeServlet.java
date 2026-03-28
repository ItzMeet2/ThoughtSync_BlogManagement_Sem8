package com.mycompany.blogmanagement.servlet;

import com.mycompany.blogmanagement.dao.CategoryDAO;
import com.mycompany.blogmanagement.dao.PostDAO;
import com.mycompany.blogmanagement.entity.Category;
import com.mycompany.blogmanagement.entity.Post;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("")
public class HomeServlet extends HttpServlet {
    private PostDAO postDAO = new PostDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Post> recentPosts = postDAO.findPublished();
        List<Post> popularPosts = postDAO.findMostViewed(5);
        List<Category> categories = categoryDAO.findAll();
        
        request.setAttribute("recentPosts", recentPosts);
        request.setAttribute("popularPosts", popularPosts);
        request.setAttribute("categories", categories);
        
        request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
    }
}
