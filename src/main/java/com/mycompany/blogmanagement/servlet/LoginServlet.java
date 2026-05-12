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
        request.getRequestDispatcher("/WEB-INF/views/login.xhtml").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String identifier = request.getParameter("username");
            String password   = request.getParameter("password");

            User user = userDAO.findByUsernameOrEmail(identifier);

            if (user != null && user.getIsActive() && PasswordUtil.verifyPassword(password, user.getPassword())) {
                HttpSession session = request.getSession();
                session.setAttribute("user",     user);
                session.setAttribute("userId",   user.getUserId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("role",     user.getRole().getRoleName());

                if ("ADMIN".equals(user.getRole().getRoleName())) {
                    response.sendRedirect(request.getContextPath() + "/admin");
                } else {
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                }
            } else {
                request.setAttribute("error", "Invalid username or password");
                request.getRequestDispatcher("/WEB-INF/views/login.xhtml").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred during login. Please try again.");
            request.getRequestDispatcher("/WEB-INF/views/login.xhtml").forward(request, response);
        }
    }
}
