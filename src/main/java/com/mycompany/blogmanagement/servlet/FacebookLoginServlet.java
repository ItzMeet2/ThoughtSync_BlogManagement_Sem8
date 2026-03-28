package com.mycompany.blogmanagement.servlet;

import com.mycompany.blogmanagement.util.OAuthConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login/facebook")
public class FacebookLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String redirectUrl = OAuthConstants.FB_AUTH_URL + "?client_id=" + OAuthConstants.FB_CLIENT_ID +
                "&redirect_uri=" + OAuthConstants.FB_REDIRECT_URI +
                "&scope=public_profile,email";
        
        response.sendRedirect(redirectUrl);
    }
}
