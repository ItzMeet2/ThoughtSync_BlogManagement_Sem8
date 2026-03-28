package com.mycompany.blogmanagement.servlet;

import com.mycompany.blogmanagement.dao.*;
import com.mycompany.blogmanagement.entity.*;
import com.mycompany.blogmanagement.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/comment")
public class CommentServlet extends HttpServlet {
    private CommentDAO commentDAO = new CommentDAO();
    private PostDAO postDAO = new PostDAO();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("add".equals(action)) {
            addComment(request, response);
        } else if ("approve".equals(action)) {
            approveComment(request, response);
        } else if ("delete".equals(action)) {
            deleteComment(request, response);
        }
    }
    
    private void addComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String postIdStr = request.getParameter("postId");
        String content = request.getParameter("content");
        
        if (!ValidationUtil.isValidInteger(postIdStr)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid post ID");
            return;
        }
        
        if (!ValidationUtil.isValidString(content, 1, 1000)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Comment must be between 1 and 1000 characters");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        int postId = Integer.parseInt(postIdStr);
        
        Comment comment = new Comment();
        comment.setPost(postDAO.findById(postId));
        comment.setUser(user);
        comment.setContent(ValidationUtil.sanitizeHtml(content.trim()));
        
        commentDAO.save(comment);
        response.sendRedirect(request.getContextPath() + "/post/" + postId);
    }
    
    private void approveComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int commentId = Integer.parseInt(request.getParameter("commentId"));
        Comment comment = commentDAO.findById(commentId);
        comment.setStatus("approved");
        commentDAO.update(comment);
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
    
    private void deleteComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int commentId = Integer.parseInt(request.getParameter("commentId"));
        commentDAO.delete(commentId);
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
}
