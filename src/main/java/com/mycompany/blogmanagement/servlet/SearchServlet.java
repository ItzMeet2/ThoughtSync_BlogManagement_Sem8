package com.mycompany.blogmanagement.servlet;

import com.mycompany.blogmanagement.dao.CategoryDAO;
import com.mycompany.blogmanagement.dao.PostDAO;
import com.mycompany.blogmanagement.dao.TagDAO;
import com.mycompany.blogmanagement.entity.Post;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    private final PostDAO postDAO = new PostDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final TagDAO tagDAO = new TagDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword  = request.getParameter("q");
        String catParam = request.getParameter("category");
        String tagParam = request.getParameter("tag");
        String sortBy   = request.getParameter("sort");

        Integer categoryId = (catParam != null && !catParam.isEmpty()) ? Integer.parseInt(catParam) : null;
        Integer tagId      = (tagParam != null && !tagParam.isEmpty())  ? Integer.parseInt(tagParam)  : null;

        List<Post> posts = postDAO.searchWithFilters(keyword, categoryId, tagId, sortBy);

        request.setAttribute("posts",      posts);
        request.setAttribute("keyword",    keyword);
        request.setAttribute("categoryId", categoryId);
        request.setAttribute("tagId",      tagId);
        request.setAttribute("sortBy",     sortBy);
        request.setAttribute("categories", categoryDAO.findAll());
        request.setAttribute("tags",       tagDAO.findAll());

        request.getRequestDispatcher("/faces/WEB-INF/views/blog-list.xhtml").forward(request, response);
    }
}
