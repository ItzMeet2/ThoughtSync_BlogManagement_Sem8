package com.mycompany.blogmanagement.servlet;

import com.mycompany.blogmanagement.dao.UserDAO;
import com.mycompany.blogmanagement.entity.User;
import com.mycompany.blogmanagement.util.EmailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.UUID;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/forgot-password.xhtml").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String email = request.getParameter("email");

            if (email == null || email.trim().isEmpty()) {
                request.setAttribute("error", "Email field is required.");
                request.getRequestDispatcher("/WEB-INF/views/forgot-password.xhtml").forward(request, response);
                return;
            }

            User user = userDAO.findByEmail(email.trim());

            if (user != null) {
                // Generate a secure random token
                String token = UUID.randomUUID().toString();
                user.setResetToken(token);
                // Expire in 1 hour
                long oneHourInMillis = 3600000;
                user.setTokenExpiry(new Timestamp(System.currentTimeMillis() + oneHourInMillis));
                userDAO.update(user);

                // Build the reset password link
                String resetLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/reset-password?token=" + token;

                // Send email
                try {
                    EmailUtil.sendResetEmail(user.getEmail(), resetLink);
                    request.setAttribute("success", "A password reset link has been sent to your email address.");
                } catch (Exception emailEx) {
                    emailEx.printStackTrace();
                    // Fallback to printing the link to the console for testing
                    System.out.println("==========================================================================");
                    System.out.println("SMTP EMAIL SENDING FAILED! (SMTP credentials may not be configured)");
                    System.out.println("Copy and paste this link in your browser to complete the password reset:");
                    System.out.println(resetLink);
                    System.out.println("==========================================================================");
                    
                    request.setAttribute("success", "A password reset link has been generated. Since email sending failed, the link has been printed to the server console: " + resetLink);
                }
            } else {
                // To prevent email enumeration, we show the same success message
                request.setAttribute("success", "If that email address exists in our database, a reset link has been sent.");
            }

            request.getRequestDispatcher("/WEB-INF/views/forgot-password.xhtml").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred. Please try again.");
            request.getRequestDispatcher("/WEB-INF/views/forgot-password.xhtml").forward(request, response);
        }
    }
}
