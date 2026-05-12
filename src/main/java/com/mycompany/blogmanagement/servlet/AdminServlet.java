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

        ReportDAO reportDAO = new ReportDAO();
        request.setAttribute("totalPosts",        postDAO.getTotalPosts());
        request.setAttribute("publishedPosts",     postDAO.getPublishedCount());
        request.setAttribute("totalUsers",         userDAO.getTotalUsers());
        request.setAttribute("totalCategories",    categoryDAO.getTotalCategories());
        request.setAttribute("pendingReports",     reportDAO.findPendingReports());
        request.setAttribute("pendingReportCount", reportDAO.getPendingReportCount());
        request.setAttribute("recentPosts",        postDAO.findAll());
        request.setAttribute("allUsers",           userDAO.findAll());
        request.setAttribute("allCategories",      categoryDAO.findAll());

        request.getRequestDispatcher("/faces/WEB-INF/views/admin-dashboard.xhtml").forward(request, response);
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
                postDAO.delete(Integer.parseInt(request.getParameter("postId")));
            } else if ("deleteUser".equals(action)) {
                int userId = Integer.parseInt(request.getParameter("userId"));
                if (userId == user.getUserId()) {
                    session.setAttribute("error", "You cannot delete your own admin account.");
                } else {
                    userDAO.delete(userId);
                }
            } else if ("deleteCategory".equals(action)) {
                categoryDAO.delete(Integer.parseInt(request.getParameter("categoryId")));
            }
        } catch (Exception e) {
            session.setAttribute("error", "Error performing deletion: " + e.getMessage());
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/admin");
    }
}
