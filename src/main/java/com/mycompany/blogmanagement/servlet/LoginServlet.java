package com.mycompany.blogmanagement.servlet;

import com.mycompany.blogmanagement.dao.UserDAO;
import com.mycompany.blogmanagement.entity.User;
import com.mycompany.blogmanagement.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading login page: " + e.getMessage());
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String identifier = request.getParameter("username");
            String password = request.getParameter("password");
            
            System.out.println("--- LOGIN ATTEMPT ---");
            System.out.println("Identifier provided: " + identifier);
            
            User user = userDAO.findByUsernameOrEmail(identifier);
            System.out.println("User found in DB: " + (user != null));
            
            if (user != null) {
                System.out.println("User is active: " + user.getIsActive());
                System.out.println("Password match: " + PasswordUtil.verifyPassword(password, user.getPassword()));
            }
            
            if (user != null && user.getIsActive() && PasswordUtil.verifyPassword(password, user.getPassword())) {
                System.out.println("Login successful, redirecting to dashboard");
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("role", user.getRole().getRoleName());
                
                if ("ADMIN".equals(user.getRole().getRoleName())) {
                    response.sendRedirect(request.getContextPath() + "/admin");
                } else {
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                }
            } else {
                System.out.println("Login failed - invalid credentials or inactive user");
                request.setAttribute("error", "Invalid username or password");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.out.println("EXCEPTION IN LOGIN POST:");
            e.printStackTrace();
            request.setAttribute("error", "An error occurred during login. Please try again.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}
