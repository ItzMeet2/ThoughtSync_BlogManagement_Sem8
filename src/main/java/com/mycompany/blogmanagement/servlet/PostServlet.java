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

@WebServlet({"/post/*", "/post"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 15)
public class PostServlet extends HttpServlet {
    private PostDAO postDAO = new PostDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/"))  { listPosts(request, response); }
        else if (pathInfo.equals("/new"))              { showNewForm(request, response); }
        else if (pathInfo.equals("/edit"))             { showEditForm(request, response); }
        else                                           { viewPost(request, response, pathInfo); }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("create".equals(action))                                    createPost(request, response);
        else if ("update".equals(action))                               updatePost(request, response);
        else if ("delete".equals(action))                               deletePost(request, response);
        else if ("publish".equals(action) || "unpublish".equals(action)) publishPost(request, response);
    }

    private void listPosts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("posts", postDAO.findPublished());
        request.getRequestDispatcher("/WEB-INF/views/blog-list.xhtml").forward(request, response);
    }

    private void viewPost(HttpServletRequest request, HttpServletResponse response, String pathInfo) throws ServletException, IOException {
        String postIdStr = pathInfo.substring(1);
        if (!ValidationUtil.isValidInteger(postIdStr)) { response.sendError(HttpServletResponse.SC_BAD_REQUEST); return; }
        try {
            int postId = Integer.parseInt(postIdStr);
            Post post = postDAO.findById(postId);
            if (post != null) {
                postDAO.incrementViewCount(postId);
                request.setAttribute("post", postDAO.findById(postId));
                request.setAttribute("comments", new CommentDAO().findByPost(postId));
                
                PostRatingDAO ratingDAO = new PostRatingDAO();
                Double avgRating = ratingDAO.getAverageRating(postId);
                if (avgRating != null) {
                    avgRating = Math.round(avgRating * 10.0) / 10.0;
                }
                request.setAttribute("averageRating", avgRating != null ? avgRating : 0.0);

                HttpSession session = request.getSession(false);
                if (session != null && session.getAttribute("user") != null) {
                    User user = (User) session.getAttribute("user");
                    PostRating userRating = ratingDAO.findByPostAndUser(postId, user.getUserId());
                    if (userRating != null) {
                        request.setAttribute("userRating", userRating.getRating());
                    }
                }

                request.getRequestDispatcher("/WEB-INF/views/post-view.xhtml").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }
        request.setAttribute("categories", categoryDAO.findAll());
        request.getRequestDispatcher("/WEB-INF/views/post-form.xhtml").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }
        String postIdStr = request.getParameter("id");
        if (!ValidationUtil.isValidInteger(postIdStr)) { response.sendError(HttpServletResponse.SC_BAD_REQUEST); return; }
        request.setAttribute("post", postDAO.findById(Integer.parseInt(postIdStr)));
        request.setAttribute("categories", categoryDAO.findAll());
        request.getRequestDispatcher("/WEB-INF/views/post-form.xhtml").forward(request, response);
    }

    private String processImageUpload(HttpServletRequest request) throws IOException, ServletException {
        Part filePart = request.getPart("featuredImageFile");
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String ext = fileName.substring(fileName.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID().toString() + ext;
            String uploadDir = getServletContext().getRealPath("/uploads/posts");
            new File(uploadDir).mkdirs();
            try (InputStream in = filePart.getInputStream()) {
                Files.copy(in, Paths.get(uploadDir + File.separator + uniqueFileName), StandardCopyOption.REPLACE_EXISTING);
            }
            return "uploads/posts/" + uniqueFileName;
        }
        return null;
    }

    private void createPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = (User) request.getSession(false).getAttribute("user");
        Post post = new Post();
        post.setTitle(request.getParameter("title"));
        post.setContent(request.getParameter("content"));
        post.setAuthor(user);
        post.setStatus("published");
        post.setPublishedAt(new Timestamp(System.currentTimeMillis()));
        String catId = request.getParameter("categoryId");
        if (catId != null && !catId.isEmpty()) post.setCategory(categoryDAO.findById(Integer.parseInt(catId)));
        String img = processImageUpload(request);
        if (img != null) post.setFeaturedImage(img);
        postDAO.save(post);
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }

    private void updatePost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Post post = postDAO.findById(Integer.parseInt(request.getParameter("postId")));
        post.setTitle(request.getParameter("title"));
        post.setContent(request.getParameter("content"));
        post.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        String catId = request.getParameter("categoryId");
        if (catId != null && !catId.isEmpty()) post.setCategory(categoryDAO.findById(Integer.parseInt(catId)));
        String img = processImageUpload(request);
        if (img != null) post.setFeaturedImage(img);
        postDAO.update(post);
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }

    private void deletePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        postDAO.delete(Integer.parseInt(request.getParameter("postId")));
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }

    private void publishPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Post post = postDAO.findById(Integer.parseInt(request.getParameter("postId")));
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
