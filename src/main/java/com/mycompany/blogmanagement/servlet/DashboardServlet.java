package com.mycompany.blogmanagement.servlet;

import com.mycompany.blogmanagement.dao.PostDAO;
import com.mycompany.blogmanagement.entity.Post;
import com.mycompany.blogmanagement.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private PostDAO postDAO = new PostDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        String role = (String) session.getAttribute("role");
        
        List<Post> posts;
        if ("ADMIN".equals(role)) {
            posts = postDAO.findAll();
        } else {
            posts = postDAO.findByAuthor(user.getUserId());
        }
        
        request.setAttribute("posts", posts);
        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}
