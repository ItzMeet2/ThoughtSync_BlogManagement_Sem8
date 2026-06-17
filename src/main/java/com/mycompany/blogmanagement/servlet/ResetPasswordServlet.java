package com.mycompany.blogmanagement.servlet;

import com.mycompany.blogmanagement.dao.UserDAO;
import com.mycompany.blogmanagement.entity.User;
import com.mycompany.blogmanagement.util.PasswordUtil;
import com.mycompany.blogmanagement.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");

        if (token == null || token.trim().isEmpty()) {
            request.setAttribute("error", "Invalid or missing password reset token.");
            request.getRequestDispatcher("/WEB-INF/views/forgot-password.xhtml").forward(request, response);
            return;
        }

        User user = userDAO.findByResetToken(token.trim());

        if (user == null || user.getTokenExpiry() == null || user.getTokenExpiry().before(new Timestamp(System.currentTimeMillis()))) {
            request.setAttribute("error", "The password reset link is invalid or has expired.");
            request.getRequestDispatcher("/WEB-INF/views/forgot-password.xhtml").forward(request, response);
            return;
        }

        request.setAttribute("token", token.trim());
        request.getRequestDispatcher("/WEB-INF/views/reset-password.xhtml").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String token = request.getParameter("token");
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");

            if (token == null || token.trim().isEmpty()) {
                request.setAttribute("error", "Invalid or missing token.");
                request.getRequestDispatcher("/WEB-INF/views/forgot-password.xhtml").forward(request, response);
                return;
            }

            if (newPassword == null || confirmPassword == null || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                request.setAttribute("error", "Please fill in all fields.");
                request.setAttribute("token", token);
                request.getRequestDispatcher("/WEB-INF/views/reset-password.xhtml").forward(request, response);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                request.setAttribute("error", "Passwords do not match.");
                request.setAttribute("token", token);
                request.getRequestDispatcher("/WEB-INF/views/reset-password.xhtml").forward(request, response);
                return;
            }

            if (!ValidationUtil.isValidString(newPassword, 6, 100)) {
                request.setAttribute("error", "Password must be between 6 and 100 characters.");
                request.setAttribute("token", token);
                request.getRequestDispatcher("/WEB-INF/views/reset-password.xhtml").forward(request, response);
                return;
            }

            User user = userDAO.findByResetToken(token.trim());

            if (user == null || user.getTokenExpiry() == null || user.getTokenExpiry().before(new Timestamp(System.currentTimeMillis()))) {
                request.setAttribute("error", "The password reset link is invalid or has expired.");
                request.getRequestDispatcher("/WEB-INF/views/forgot-password.xhtml").forward(request, response);
                return;
            }

            // Update user password and clear token
            user.setPassword(PasswordUtil.hashPassword(newPassword));
            user.setResetToken(null);
            user.setTokenExpiry(null);
            userDAO.update(user);

            request.setAttribute("success", "Password reset successfully! You can now log in.");
            request.getRequestDispatcher("/WEB-INF/views/login.xhtml").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred. Please try again.");
            request.getRequestDispatcher("/WEB-INF/views/forgot-password.xhtml").forward(request, response);
        }
    }
}
