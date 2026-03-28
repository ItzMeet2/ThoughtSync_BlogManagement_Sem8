package com.mycompany.blogmanagement.servlet;

import com.mycompany.blogmanagement.dao.*;
import com.mycompany.blogmanagement.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private PostDAO postDAO = new PostDAO();
    private UserDAO userDAO = new UserDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"ADMIN".equals(user.getRole().getRoleName())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        request.setAttribute("totalPosts", postDAO.getTotalPosts());
        request.setAttribute("publishedPosts", postDAO.getPublishedCount());
        request.setAttribute("totalUsers", userDAO.getTotalUsers());
        request.setAttribute("totalCategories", categoryDAO.getTotalCategories());
        
        ReportDAO reportDAO = new ReportDAO();
        request.setAttribute("pendingReports", reportDAO.findPendingReports());
        request.setAttribute("pendingReportCount", reportDAO.getPendingReportCount());
        
        request.setAttribute("recentPosts", postDAO.findAll());
        request.setAttribute("allUsers", userDAO.findAll());
        request.setAttribute("allCategories", categoryDAO.findAll());
        
        request.getRequestDispatcher("/WEB-INF/views/admin-dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"ADMIN".equals(user.getRole().getRoleName())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        String action = request.getParameter("action");
        try {
            if ("deletePost".equals(action)) {
                int postId = Integer.parseInt(request.getParameter("postId"));
                postDAO.delete(postId);
                session.setAttribute("success", "Post deleted successfully.");
            } else if ("deleteUser".equals(action)) {
                int userId = Integer.parseInt(request.getParameter("userId"));
                if (userId == user.getUserId()) {
                    session.setAttribute("error", "You cannot delete your own admin account.");
                } else {
                    userDAO.delete(userId);
                    session.setAttribute("success", "User deleted successfully.");
                }
            } else if ("deleteCategory".equals(action)) {
                int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                categoryDAO.delete(categoryId);
                session.setAttribute("success", "Category deleted successfully.");
            }
        } catch (Exception e) {
            session.setAttribute("error", "Error performing deletion: " + e.getMessage());
            e.printStackTrace();
        }
        
        response.sendRedirect(request.getContextPath() + "/admin");
    }
}
