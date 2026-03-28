-- Blog and Content Management System Database Schema
-- PostgreSQL Database

-- Create Database
CREATE DATABASE blogdb;

-- Connect to blogdb
\c blogdb;

-- Roles Table
CREATE TABLE roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE NOT NULL
);

-- Users Table
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    role_id INTEGER REFERENCES roles(role_id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

-- Categories Table
CREATE TABLE categories (
    category_id SERIAL PRIMARY KEY,
    category_name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Posts Table
CREATE TABLE posts (
    post_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    author_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    category_id INTEGER REFERENCES categories(category_id) ON DELETE SET NULL,
    featured_image VARCHAR(255),
    status VARCHAR(20) DEFAULT 'draft',
    view_count INTEGER DEFAULT 0,
    like_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    published_at TIMESTAMP
);

-- Tags Table
CREATE TABLE tags (
    tag_id SERIAL PRIMARY KEY,
    tag_name VARCHAR(50) UNIQUE NOT NULL
);

-- Post_Tags Junction Table
CREATE TABLE post_tags (
    post_id INTEGER REFERENCES posts(post_id) ON DELETE CASCADE,
    tag_id INTEGER REFERENCES tags(tag_id) ON DELETE CASCADE,
    PRIMARY KEY (post_id, tag_id)
);

-- Comments Table
CREATE TABLE comments (
    comment_id SERIAL PRIMARY KEY,
    post_id INTEGER REFERENCES posts(post_id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    parent_comment_id INTEGER REFERENCES comments(comment_id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Post Likes Table
CREATE TABLE post_likes (
    like_id SERIAL PRIMARY KEY,
    post_id INTEGER REFERENCES posts(post_id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(post_id, user_id)
);

-- Post Ratings Table
CREATE TABLE post_ratings (
    rating_id SERIAL PRIMARY KEY,
    post_id INTEGER REFERENCES posts(post_id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    rating INTEGER CHECK (rating >= 1 AND rating <= 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(post_id, user_id)
);

-- Media Table
CREATE TABLE media (
    media_id SERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_type VARCHAR(50),
    file_size BIGINT,
    uploaded_by INTEGER REFERENCES users(user_id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert Default Roles
INSERT INTO roles (role_name) VALUES ('ADMIN'), ('AUTHOR'), ('READER');

-- Insert Default Admin User (password: admin123)
INSERT INTO users (username, email, password, full_name, role_id) 
VALUES ('admin', 'admin@blog.com', '$2a$10$AueDeBcHIk7Mi1x0uosf9OA8I7SjZZU/aNW.XEbC2qQbI5vFvPon2', 'Administrator', 1);

-- Insert Sample Categories
INSERT INTO categories (category_name, description) VALUES 
('Technology', 'Tech related posts'),
('Lifestyle', 'Lifestyle and personal posts'),
('Business', 'Business and entrepreneurship');

-- Create Indexes for Performance
CREATE INDEX idx_posts_author ON posts(author_id);
CREATE INDEX idx_posts_category ON posts(category_id);
CREATE INDEX idx_posts_status ON posts(status);
CREATE INDEX idx_posts_views ON posts(view_count DESC);
CREATE INDEX idx_posts_likes ON posts(like_count DESC);
CREATE INDEX idx_comments_post ON comments(post_id);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_post_likes_user ON post_likes(user_id);
CREATE INDEX idx_post_ratings_post ON post_ratings(post_id);

-- Full Text Search Index
CREATE INDEX idx_posts_title_search ON posts USING gin(to_tsvector('english', title));
CREATE INDEX idx_posts_content_search ON posts USING gin(to_tsvector('english', content));
