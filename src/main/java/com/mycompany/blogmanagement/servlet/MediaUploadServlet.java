package com.mycompany.blogmanagement.servlet;

import com.mycompany.blogmanagement.dao.MediaDAO;
import com.mycompany.blogmanagement.entity.Media;
import com.mycompany.blogmanagement.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@WebServlet("/upload")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 10)
public class MediaUploadServlet extends HttpServlet {
    private MediaDAO mediaDAO = new MediaDAO();
    private static final String UPLOAD_DIR = "uploads";
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();
        
        Part filePart = request.getPart("file");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String filePath = UPLOAD_DIR + File.separator + System.currentTimeMillis() + "_" + fileName;
        
        filePart.write(uploadPath + File.separator + System.currentTimeMillis() + "_" + fileName);
        
        Media media = new Media();
        media.setFileName(fileName);
        media.setFilePath(filePath);
        media.setFileType(filePart.getContentType());
        media.setFileSize(filePart.getSize());
        media.setUploadedBy(user);
        
        mediaDAO.save(media);
        response.getWriter().write("{\"success\": true, \"path\": \"" + request.getContextPath() + "/" + filePath + "\"}");
    }
}
