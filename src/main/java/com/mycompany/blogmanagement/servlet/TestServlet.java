package com.mycompany.blogmanagement.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/test")
public class TestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Blog CMS - Deployment Test</h1>");
        out.println("<p>Application is deployed successfully!</p>");
        out.println("<p><a href='" + request.getContextPath() + "/'>Go to Home</a></p>");
        out.println("<p><a href='" + request.getContextPath() + "/login'>Go to Login</a></p>");
        out.println("</body></html>");
    }
}
