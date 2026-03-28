package com.mycompany.blogmanagement.servlet;

import com.mycompany.blogmanagement.dao.*;
import com.mycompany.blogmanagement.entity.*;
import com.mycompany.blogmanagement.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@WebServlet("/post/*")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 15    // 15MB
)
public class PostServlet extends HttpServlet {
    private PostDAO postDAO = new PostDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            listPosts(request, response);
        } else if (pathInfo.equals("/new")) {
            showNewForm(request, response);
        } else if (pathInfo.equals("/edit")) {
            showEditForm(request, response);
        } else {
            viewPost(request, response, pathInfo);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("create".equals(action)) {
            createPost(request, response);
        } else if ("update".equals(action)) {
            updatePost(request, response);
        } else if ("delete".equals(action)) {
            deletePost(request, response);
        } else if ("publish".equals(action)) {
            publishPost(request, response);
        }
    }
    
    private void listPosts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Post> posts = postDAO.findPublished();
        request.setAttribute("posts", posts);
        request.getRequestDispatcher("/WEB-INF/views/blog-list.jsp").forward(request, response);
    }
    
    private void viewPost(HttpServletRequest request, HttpServletResponse response, String pathInfo) throws ServletException, IOException {
        String postIdStr = pathInfo.substring(1);
        if (!ValidationUtil.isValidInteger(postIdStr)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid post ID");
            return;
        }
        
        try {
            int postId = Integer.parseInt(postIdStr);
            Post post = postDAO.findById(postId);
            if (post != null) {
                postDAO.incrementViewCount(postId);
                post = postDAO.findById(postId);
                request.setAttribute("post", post);
                request.getRequestDispatcher("/WEB-INF/views/post-view.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println("EXCEPTION IN viewPost:");
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading post: " + e.getMessage());
        }
    }
    
    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        List<Category> categories = categoryDAO.findAll();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/WEB-INF/views/post-form.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String postIdStr = request.getParameter("id");
        if (!ValidationUtil.isValidInteger(postIdStr)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid post ID");
            return;
        }
        
        int postId = Integer.parseInt(postIdStr);
        Post post = postDAO.findById(postId);
        List<Category> categories = categoryDAO.findAll();
        
        request.setAttribute("post", post);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/WEB-INF/views/post-form.jsp").forward(request, response);
    }
    
    private String processImageUpload(HttpServletRequest request) throws IOException, ServletException {
        Part filePart = request.getPart("featuredImageFile");
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String extension = fileName.substring(fileName.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID().toString() + extension;
            
            String srcPath = "E:" + File.separator + "Sem_8" + File.separator + "Project-8" + File.separator + "BlogManagement" + File.separator + "src" + File.separator + "main" + File.separator + "webapp" + File.separator + "uploads" + File.separator + "posts";
            File srcDir = new File(srcPath);
            if (!srcDir.exists()) srcDir.mkdirs();
            
            String targetPath = "E:" + File.separator + "Sem_8" + File.separator + "Project-8" + File.separator + "BlogManagement" + File.separator + "target" + File.separator + "BlogManagement-1.0-SNAPSHOT" + File.separator + "uploads" + File.separator + "posts";
            File targetDir = new File(targetPath);
            if (!targetDir.exists()) targetDir.mkdirs();
            
            try (InputStream fileContent = filePart.getInputStream()) {
                Files.copy(fileContent, Paths.get(targetPath + File.separator + uniqueFileName), StandardCopyOption.REPLACE_EXISTING);
            }
            Files.copy(Paths.get(targetPath + File.separator + uniqueFileName), Paths.get(srcPath + File.separator + uniqueFileName), StandardCopyOption.REPLACE_EXISTING);
            
            return "uploads/posts/" + uniqueFileName;
        }
        return null; // Return null if no new file was uploaded
    }
    
    private void createPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        
        Post post = new Post();
        post.setTitle(request.getParameter("title"));
        post.setContent(request.getParameter("content"));
        post.setAuthor(user);
        
        String categoryId = request.getParameter("categoryId");
        if (categoryId != null && !categoryId.isEmpty()) {
            Category category = categoryDAO.findById(Integer.parseInt(categoryId));
            post.setCategory(category);
        }
        
        // Handle image upload
        String uploadedImagePath = processImageUpload(request);
        if (uploadedImagePath != null) {
            post.setFeaturedImage(uploadedImagePath);
        } else {
             // Fallback to text url if supported, although we removed it from UI
             String textUrl = request.getParameter("featuredImage");
             if (textUrl != null && !textUrl.trim().isEmpty()) {
                 post.setFeaturedImage(textUrl);
             }
        }
        
        postDAO.save(post);
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
    
    private void updatePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int postId = Integer.parseInt(request.getParameter("postId"));
        Post post = postDAO.findById(postId);
        
        post.setTitle(request.getParameter("title"));
        post.setContent(request.getParameter("content"));
        post.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        
        String categoryId = request.getParameter("categoryId");
        if (categoryId != null && !categoryId.isEmpty()) {
            Category category = categoryDAO.findById(Integer.parseInt(categoryId));
            post.setCategory(category);
        }
        
        // Handle image upload update
        String uploadedImagePath = processImageUpload(request);
        if (uploadedImagePath != null) {
            post.setFeaturedImage(uploadedImagePath);
        }
        
        postDAO.update(post);
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
    
    private void deletePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int postId = Integer.parseInt(request.getParameter("postId"));
        postDAO.delete(postId);
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
    
    private void publishPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int postId = Integer.parseInt(request.getParameter("postId"));
        Post post = postDAO.findById(postId);
        
        if ("draft".equals(post.getStatus())) {
            post.setStatus("published");
            post.setPublishedAt(new Timestamp(System.currentTimeMillis()));
        } else {
            post.setStatus("draft");
            post.setPublishedAt(null);
        }
        
        postDAO.update(post);
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
}
