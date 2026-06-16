package com.mycompany.blogmanagement.servlet;

import com.mycompany.blogmanagement.dao.PostDAO;
import com.mycompany.blogmanagement.entity.Post;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

@WebServlet("/search/autocomplete")
public class SearchAutocompleteServlet extends HttpServlet {
    private final PostDAO postDAO = new PostDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("q");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (keyword == null || keyword.trim().isEmpty()) {
            response.getWriter().write("[]");
            return;
        }

        List<Post> posts = postDAO.searchTitles(keyword.trim(), 6);
        JSONArray jsonArray = new JSONArray();

        for (Post post : posts) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", post.getPostId());
            jsonObject.put("title", post.getTitle());
            if (post.getCategory() != null) {
                jsonObject.put("category", post.getCategory().getCategoryName());
            }
            jsonArray.put(jsonObject);
        }

        response.getWriter().write(jsonArray.toString());
    }
}
