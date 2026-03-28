-- Migration Script
-- Run this if your database already has tables from an older schema version
-- Safe to run multiple times (uses IF NOT EXISTS / IF EXISTS)

-- ============================================================
-- posts table: add any missing columns
-- ============================================================
ALTER TABLE posts ADD COLUMN IF NOT EXISTS featured_image VARCHAR(255);
ALTER TABLE posts ADD COLUMN IF NOT EXISTS view_count INTEGER DEFAULT 0;
ALTER TABLE posts ADD COLUMN IF NOT EXISTS like_count INTEGER DEFAULT 0;
ALTER TABLE posts ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'draft';
ALTER TABLE posts ADD COLUMN IF NOT EXISTS published_at TIMESTAMP;
ALTER TABLE posts ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Set safe defaults for any NULL values in existing rows
UPDATE posts SET view_count  = 0       WHERE view_count  IS NULL;
UPDATE posts SET like_count  = 0       WHERE like_count  IS NULL;
UPDATE posts SET status      = 'draft' WHERE status      IS NULL;
UPDATE posts SET updated_at  = created_at WHERE updated_at IS NULL;

-- ============================================================
-- users table: add any missing columns
-- ============================================================
ALTER TABLE users ADD COLUMN IF NOT EXISTS profile_picture VARCHAR(500);
ALTER TABLE users ADD COLUMN IF NOT EXISTS bio TEXT;

-- ============================================================
-- comments table: add parent_comment_id if missing
-- ============================================================
ALTER TABLE comments ADD COLUMN IF NOT EXISTS parent_comment_id INTEGER REFERENCES comments(comment_id) ON DELETE CASCADE;

-- ============================================================
-- Create missing tables (safe if they already exist)
-- ============================================================

CREATE TABLE IF NOT EXISTS post_likes (
    like_id    SERIAL PRIMARY KEY,
    post_id    INTEGER REFERENCES posts(post_id)    ON DELETE CASCADE,
    user_id    INTEGER REFERENCES users(user_id)    ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(post_id, user_id)
);

CREATE TABLE IF NOT EXISTS post_ratings (
    rating_id  SERIAL PRIMARY KEY,
    post_id    INTEGER REFERENCES posts(post_id)    ON DELETE CASCADE,
    user_id    INTEGER REFERENCES users(user_id)    ON DELETE CASCADE,
    rating     INTEGER CHECK (rating >= 1 AND rating <= 5) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(post_id, user_id)
);

CREATE TABLE IF NOT EXISTS media (
    media_id   SERIAL PRIMARY KEY,
    file_name  VARCHAR(255) NOT NULL,
    file_path  VARCHAR(500) NOT NULL,
    file_type  VARCHAR(50),
    file_size  BIGINT,
    uploaded_by INTEGER REFERENCES users(user_id) ON DELETE SET NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS post_tags (
    post_id INTEGER REFERENCES posts(post_id) ON DELETE CASCADE,
    tag_id  INTEGER REFERENCES tags(tag_id)  ON DELETE CASCADE,
    PRIMARY KEY (post_id, tag_id)
);

-- ============================================================
-- post_reports table: track user flagged inappropriate posts
-- ============================================================
CREATE TABLE IF NOT EXISTS post_reports (
    report_id   SERIAL PRIMARY KEY,
    post_id     INTEGER REFERENCES posts(post_id) ON DELETE CASCADE,
    reporter_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    reason      TEXT NOT NULL,
    status      VARCHAR(50) DEFAULT 'PENDING',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- Indexes (safe - uses IF NOT EXISTS)
-- ============================================================
CREATE INDEX IF NOT EXISTS idx_posts_author   ON posts(author_id);
CREATE INDEX IF NOT EXISTS idx_posts_category ON posts(category_id);
CREATE INDEX IF NOT EXISTS idx_posts_status   ON posts(status);
CREATE INDEX IF NOT EXISTS idx_posts_views    ON posts(view_count DESC);
CREATE INDEX IF NOT EXISTS idx_posts_likes    ON posts(like_count DESC);
CREATE INDEX IF NOT EXISTS idx_comments_post  ON comments(post_id);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_post_likes_user     ON post_likes(user_id);
CREATE INDEX IF NOT EXISTS idx_post_ratings_post   ON post_ratings(post_id);
CREATE INDEX IF NOT EXISTS idx_posts_title_search   ON posts USING gin(to_tsvector('english', title));
CREATE INDEX IF NOT EXISTS idx_posts_content_search ON posts USING gin(to_tsvector('english', content));

SELECT 'Migration completed successfully!' AS status;
