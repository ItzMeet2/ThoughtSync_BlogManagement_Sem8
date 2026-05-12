package com.mycompany.blogmanagement.servlet;

import com.mycompany.blogmanagement.dao.PostDAO;
import com.mycompany.blogmanagement.dao.PostLikeDAO;
import com.mycompany.blogmanagement.dao.UserDAO;
import com.mycompany.blogmanagement.entity.Post;
import com.mycompany.blogmanagement.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@WebServlet("/profile")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 20, maxRequestSize = 1024 * 1024 * 30)
public class ProfileServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final PostDAO postDAO = new PostDAO();
    private final PostLikeDAO postLikeDAO = new PostLikeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = userDAO.findById(sessionUser.getUserId());
        session.setAttribute("user", user);

        List<Post> authoredPosts = postDAO.findByAuthor(user.getUserId());
        List<Post> likedPosts    = postLikeDAO.findLikedPostsByUser(user.getUserId());
        request.setAttribute("authoredPosts", authoredPosts);
        request.setAttribute("likedPosts",    likedPosts);

        if (session.getAttribute("successMessage") != null) {
            request.setAttribute("successMessage", session.getAttribute("successMessage"));
            session.removeAttribute("successMessage");
        }
        if (session.getAttribute("errorMessage") != null) {
            request.setAttribute("errorMessage", session.getAttribute("errorMessage"));
            session.removeAttribute("errorMessage");
        }

        request.getRequestDispatcher("/faces/WEB-INF/views/profile.xhtml").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = userDAO.findById(sessionUser.getUserId());

        try {
            if ("updateProfile".equals(request.getParameter("action"))) {
                user.setFullName(request.getParameter("fullName"));
                user.setBio(request.getParameter("bio"));

                Part filePart = request.getPart("profilePicture");
                if (filePart != null && filePart.getSize() > 0) {
                    String ext = filePart.getSubmittedFileName();
                    ext = ext.substring(ext.lastIndexOf("."));
                    String uniqueFileName = UUID.randomUUID().toString() + ext;

                    String uploadDir = getServletContext().getRealPath("/uploads/avatars");
                    File dir = new File(uploadDir);
                    if (!dir.exists()) dir.mkdirs();

                    try (java.io.InputStream in = filePart.getInputStream()) {
                        java.nio.file.Files.copy(in,
                            Paths.get(uploadDir + File.separator + uniqueFileName),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    }
                    user.setProfilePicture("uploads/avatars/" + uniqueFileName);
                }

                userDAO.update(user);
                session.setAttribute("user", user);
                session.setAttribute("successMessage", "Profile updated successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/profile");
    }
}
