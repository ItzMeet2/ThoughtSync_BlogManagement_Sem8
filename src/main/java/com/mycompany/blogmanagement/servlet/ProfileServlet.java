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
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 20,      // 20MB
    maxRequestSize = 1024 * 1024 * 30    // 30MB
)
public class ProfileServlet extends HttpServlet {
    
    private final UserDAO userDAO = new UserDAO();
    private final PostDAO postDAO = new PostDAO();
    private final PostLikeDAO postLikeDAO = new PostLikeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("user");
        
        if (sessionUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Fetch fresh user data just in case it was updated
        User user = userDAO.findById(sessionUser.getUserId());
        session.setAttribute("user", user); // Refresh session data
        
        // Fetch Authored Posts
        List<Post> authoredPosts = postDAO.findByAuthor(user.getUserId());
        request.setAttribute("authoredPosts", authoredPosts);
        
        // Fetch Liked Posts
        List<Post> likedPosts = postLikeDAO.findLikedPostsByUser(user.getUserId());
        request.setAttribute("likedPosts", likedPosts);

        // Consume flash messages
        if (session.getAttribute("successMessage") != null) {
            request.setAttribute("successMessage", session.getAttribute("successMessage"));
            session.removeAttribute("successMessage");
        }
        if (session.getAttribute("errorMessage") != null) {
            request.setAttribute("errorMessage", session.getAttribute("errorMessage"));
            session.removeAttribute("errorMessage");
        }

        request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("user");
        
        if (sessionUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        User user = userDAO.findById(sessionUser.getUserId());
        
        System.out.println("--- PROFILE UPDATE ATTEMPT ---");
        System.out.println("Action: " + action);
        
        try {
            if ("updateProfile".equals(action)) {
                String fullName = request.getParameter("fullName");
                String bio = request.getParameter("bio");
                
                System.out.println("New FullName: " + fullName);
                System.out.println("New Bio: " + bio);
                
                user.setFullName(fullName);
                user.setBio(bio);
                
                Part filePart = request.getPart("profilePicture");
                if (filePart != null && filePart.getSize() > 0) {
                    System.out.println("Profile picture part exists. Size: " + filePart.getSize());
                    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                    String extension = fileName.substring(fileName.lastIndexOf("."));
                    String uniqueFileName = UUID.randomUUID().toString() + extension;
                    
                    // Use a safe, absolute upload directory outside the war to prevent Payara path collisions
                    String srcPath = "E:" + File.separator + "Sem_8" + File.separator + "Project-8" + File.separator + "BlogManagement" + File.separator + "src" + File.separator + "main" + File.separator + "webapp" + File.separator + "uploads" + File.separator + "avatars";
                    File srcDir = new File(srcPath);
                    if (!srcDir.exists()) srcDir.mkdirs();
                    
                    String targetPath = "E:" + File.separator + "Sem_8" + File.separator + "Project-8" + File.separator + "BlogManagement" + File.separator + "target" + File.separator + "BlogManagement-1.0-SNAPSHOT" + File.separator + "uploads" + File.separator + "avatars";
                    File targetDir = new File(targetPath);
                    if (!targetDir.exists()) targetDir.mkdirs();
                    
                    System.out.println("Saving picture to TARGET: " + targetPath + File.separator + uniqueFileName);
                    
                    // Use explicit byte stream copying to completely bypass Payara's Part.write() relative path resolution bug
                    try (java.io.InputStream fileContent = filePart.getInputStream()) {
                        java.nio.file.Files.copy(
                            fileContent, 
                            Paths.get(targetPath + File.separator + uniqueFileName), 
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING
                        );
                    }
                    
                    // Also copy it to the source code so it survives NetBeans Clean & Build
                    java.nio.file.Files.copy(
                        Paths.get(targetPath + File.separator + uniqueFileName), 
                        Paths.get(srcPath + File.separator + uniqueFileName), 
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING
                    );
                    
                    user.setProfilePicture("uploads/avatars/" + uniqueFileName);
                } else {
                    System.out.println("No new profile picture attached.");
                }
                
                userDAO.update(user);
                session.setAttribute("user", user); // Update cached session user
                
                System.out.println("Profile successfully updated in database!");
                session.setAttribute("successMessage", "Profile updated successfully!");
                
            }
        } catch (Exception e) {
            System.out.println("EXCEPTION IN PROFILE UPDATE:");
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while updating your profile: " + e.getMessage());
        }
        
        // Re-forward to GET via redirect to prevent form resubmission
        response.sendRedirect(request.getContextPath() + "/profile");
    }
}
