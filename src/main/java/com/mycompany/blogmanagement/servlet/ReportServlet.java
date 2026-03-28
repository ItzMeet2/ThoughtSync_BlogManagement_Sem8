package com.mycompany.blogmanagement.servlet;

import com.mycompany.blogmanagement.dao.*;
import com.mycompany.blogmanagement.entity.*;
import com.mycompany.blogmanagement.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/report")
public class ReportServlet extends HttpServlet {
    private ReportDAO reportDAO = new ReportDAO();
    private PostDAO postDAO = new PostDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You must be logged in to access this feature.");
            return;
        }

        String action = request.getParameter("action");
        User user = (User) session.getAttribute("user");

        try {
            if ("submit".equals(action)) {
                submitReport(request, response, user);
            } else if ("dismiss".equals(action)) {
                dismissReport(request, response, user);
            } else if ("deletePost".equals(action)) {
                deleteReportedPost(request, response, user);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "An error occurred while processing the report: " + e.getMessage());
            response.sendRedirect(request.getHeader("Referer"));
        }
    }

    private void submitReport(HttpServletRequest request, HttpServletResponse response, User reporter) throws IOException {
        String postIdStr = request.getParameter("postId");
        String reason = request.getParameter("reason");

        if (!ValidationUtil.isValidInteger(postIdStr) || reason == null || reason.trim().isEmpty()) {
            request.getSession().setAttribute("error", "Invalid report submission. Reason is required.");
            response.sendRedirect(request.getHeader("Referer"));
            return;
        }

        int postId = Integer.parseInt(postIdStr);
        Post post = postDAO.findById(postId);

        if (post == null) {
            request.getSession().setAttribute("error", "The post you are trying to report does not exist.");
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        Report report = new Report();
        report.setPost(post);
        report.setReporter(reporter);
        report.setReason(reason.trim());
        report.setStatus("PENDING");

        reportDAO.save(report);

        request.getSession().setAttribute("success", "Thank you. Your report has been submitted to the administrators for review.");
        response.sendRedirect(request.getContextPath() + "/post/" + postId);
    }

    private void dismissReport(HttpServletRequest request, HttpServletResponse response, User admin) throws IOException {
        if (!"ADMIN".equals(admin.getRole().getRoleName())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String reportIdStr = request.getParameter("reportId");
        if (ValidationUtil.isValidInteger(reportIdStr)) {
            Report report = reportDAO.findById(Integer.parseInt(reportIdStr));
            if (report != null) {
                report.setStatus("DISMISSED");
                reportDAO.update(report);
                request.getSession().setAttribute("success", "Report #" + report.getReportId() + " was successfully dismissed.");
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin");
    }

    private void deleteReportedPost(HttpServletRequest request, HttpServletResponse response, User admin) throws IOException {
        if (!"ADMIN".equals(admin.getRole().getRoleName())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String reportIdStr = request.getParameter("reportId");
        if (ValidationUtil.isValidInteger(reportIdStr)) {
            Report report = reportDAO.findById(Integer.parseInt(reportIdStr));
            if (report != null) {
                Post post = report.getPost();
                
                // Mark report as resolved
                report.setStatus("RESOLVED");
                reportDAO.update(report);
                
                // Delete the offending post
                if (post != null) {
                    postDAO.delete(post.getPostId());
                    request.getSession().setAttribute("success", "The reported post '" + post.getTitle() + "' was successfully deleted. The report is marked as resolved.");
                } else {
                    request.getSession().setAttribute("error", "The reported post no longer exists.");
                }
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin");
    }
}
