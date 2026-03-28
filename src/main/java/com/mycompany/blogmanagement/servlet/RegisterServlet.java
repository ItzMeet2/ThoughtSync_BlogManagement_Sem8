package com.mycompany.blogmanagement.servlet;

import com.mycompany.blogmanagement.dao.RoleDAO;
import com.mycompany.blogmanagement.dao.UserDAO;
import com.mycompany.blogmanagement.entity.Role;
import com.mycompany.blogmanagement.entity.User;
import com.mycompany.blogmanagement.util.PasswordUtil;
import com.mycompany.blogmanagement.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();
    private RoleDAO roleDAO = new RoleDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        
        // Validate inputs
        if (!ValidationUtil.isValidString(username, 3, 50)) {
            request.setAttribute("error", "Username must be between 3 and 50 characters");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }
        
        if (!ValidationUtil.isValidEmail(email)) {
            request.setAttribute("error", "Please enter a valid email address");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }
        
        if (!ValidationUtil.isValidString(password, 6, 100)) {
            request.setAttribute("error", "Password must be between 6 and 100 characters");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }
        
        if (!ValidationUtil.isValidString(fullName, 2, 100)) {
            request.setAttribute("error", "Full name must be between 2 and 100 characters");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }
        
        if (userDAO.findByUsername(username) != null) {
            request.setAttribute("error", "Username already exists");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }
        
        if (userDAO.findByEmail(email) != null) {
            request.setAttribute("error", "Email already exists");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }
        
        User user = new User();
        user.setUsername(username.trim());
        user.setEmail(email.trim());
        user.setPassword(PasswordUtil.hashPassword(password));
        user.setFullName(fullName.trim());
        
        Role readerRole = roleDAO.findByName("READER");
        user.setRole(readerRole);
        
        userDAO.save(user);
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
