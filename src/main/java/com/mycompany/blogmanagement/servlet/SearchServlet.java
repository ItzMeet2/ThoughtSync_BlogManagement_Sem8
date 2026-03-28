package com.mycompany.blogmanagement.servlet;

import com.mycompany.blogmanagement.dao.PostDAO;
import com.mycompany.blogmanagement.entity.Post;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    private PostDAO postDAO = new PostDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("q");
        String categoryId = request.getParameter("category");
        
        List<Post> posts;
        if (keyword != null && !keyword.trim().isEmpty()) {
            posts = postDAO.searchPosts(keyword);
            request.setAttribute("keyword", keyword);
        } else if (categoryId != null && !categoryId.isEmpty()) {
            posts = postDAO.findByCategory(Integer.parseInt(categoryId));
        } else {
            posts = postDAO.findPublished();
        }
        
        request.setAttribute("posts", posts);
        request.getRequestDispatcher("/WEB-INF/views/blog-list.jsp").forward(request, response);
    }
}
