package com.mycompany.blogmanagement.servlet;

import com.mycompany.blogmanagement.util.OAuthConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login/google")
public class GoogleLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String redirectUrl = OAuthConstants.GOOGLE_AUTH_URL + "?client_id=" + OAuthConstants.GOOGLE_CLIENT_ID +
                "&redirect_uri=" + OAuthConstants.GOOGLE_REDIRECT_URI +
                "&response_type=code" +
                "&scope=email%20profile";
        
        response.sendRedirect(redirectUrl);
    }
}
