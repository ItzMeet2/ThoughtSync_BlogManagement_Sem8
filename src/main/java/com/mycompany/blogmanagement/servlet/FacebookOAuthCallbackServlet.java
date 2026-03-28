package com.mycompany.blogmanagement.servlet;

import com.mycompany.blogmanagement.dao.RoleDAO;
import com.mycompany.blogmanagement.dao.UserDAO;
import com.mycompany.blogmanagement.entity.Role;
import com.mycompany.blogmanagement.entity.User;
import com.mycompany.blogmanagement.util.OAuthConstants;
import com.mycompany.blogmanagement.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@WebServlet("/login/facebook/callback")
public class FacebookOAuthCallbackServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();
    private RoleDAO roleDAO = new RoleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        String error = request.getParameter("error");

        if (error != null || code == null) {
            request.setAttribute("error", "Facebook login failed or was cancelled.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        try {
            // 1. Exchange Code for Access Token
            String tokenResponse = getAccessToken(code);
            JSONObject tokenJson = new JSONObject(tokenResponse);
            
            if (!tokenJson.has("access_token")) {
                throw new Exception("Failed to retrieve access token");
            }

            String accessToken = tokenJson.getString("access_token");

            // 2. Fetch User Info
            String userInfoResponse = getUserInfo(accessToken);
            JSONObject userInfo = new JSONObject(userInfoResponse);

            System.out.println("Facebook User Info: " + userInfo.toString());

            if (!userInfo.has("email")) {
                request.setAttribute("error", "Facebook login requires email permission.");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                return;
            }

            String email = userInfo.getString("email");
            String defaultName = userInfo.has("name") ? userInfo.getString("name") : "Facebook User";

            // 3. Process Login / Registration
            User user = userDAO.findByEmail(email);

            if (user == null) {
                user = new User();
                user.setUsername(email.split("@")[0] + "_" + UUID.randomUUID().toString().substring(0, 5));
                user.setEmail(email);
                user.setFullName(defaultName);
                String randomPass = UUID.randomUUID().toString();
                user.setPassword(PasswordUtil.hashPassword(randomPass));
                user.setIsActive(true);

                Role readerRole = roleDAO.findByName("Reader");
                if (readerRole == null) {
                    readerRole = roleDAO.findById(3); // Fallback to ID 3
                }
                user.setRole(readerRole);
                userDAO.save(user);
            }

            // 4. Create Session
            if (user.getIsActive()) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("role", user.getRole().getRoleName());

                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                request.setAttribute("error", "Account is disabled.");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred during Facebook authentication.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }

    private String getAccessToken(String code) throws Exception {
        String tokenUrl = OAuthConstants.FB_TOKEN_URL +
                "?client_id=" + URLEncoder.encode(OAuthConstants.FB_CLIENT_ID, StandardCharsets.UTF_8) +
                "&redirect_uri=" + URLEncoder.encode(OAuthConstants.FB_REDIRECT_URI, StandardCharsets.UTF_8) +
                "&client_secret=" + URLEncoder.encode(OAuthConstants.FB_CLIENT_SECRET, StandardCharsets.UTF_8) +
                "&code=" + URLEncoder.encode(code, StandardCharsets.UTF_8);

        URL url = new URL(tokenUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) response.append(line);
            return response.toString();
        }
    }

    private String getUserInfo(String accessToken) throws Exception {
        String userInfoUrl = OAuthConstants.FB_USER_INFO_URL + "&access_token=" + accessToken;
        URL url = new URL(userInfoUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) response.append(line);
            return response.toString();
        }
    }
}
