package com.mycompany.blogmanagement.service;

import com.mycompany.blogmanagement.dao.UserDAO;
import com.mycompany.blogmanagement.entity.User;
import com.mycompany.blogmanagement.util.PasswordUtil;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public User authenticate(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user != null && PasswordUtil.verifyPassword(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public void registerUser(User user) {
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        userDAO.save(user);
    }

    public User getUserById(Integer id) {
        return userDAO.findById(id);
    }

    public User getUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public void updateUser(User user) {
        userDAO.update(user);
    }

    public boolean isAdmin(User user) {
        return user != null && "ADMIN".equals(user.getRole().getRoleName());
    }

    public boolean isAuthor(User user) {
        return user != null && ("AUTHOR".equals(user.getRole().getRoleName()) || "ADMIN".equals(user.getRole().getRoleName()));
    }
}
