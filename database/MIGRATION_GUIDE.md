# Database Migration Guide

## Option 1: Fresh Installation (Recommended)

### Step 1: Backup existing data (if any)
```bash
pg_dump -U postgres blogdb > backup.sql
```

### Step 2: Drop and recreate database
```bash
# Connect to PostgreSQL
psql -U postgres

# Drop existing database
DROP DATABASE IF EXISTS blogdb;

# Create new database
CREATE DATABASE blogdb;

# Exit
\q
```

### Step 3: Run the new schema
```bash
psql -U postgres -d blogdb -f database/schema.sql
```

### Done! ✅
Database is now updated with all new tables and features.

---

## Option 2: Incremental Migration (Keep existing data)

### Step 1: Connect to database
```bash
psql -U postgres -d blogdb
```

### Step 2: Add new columns to existing tables
```sql
-- Add new columns to posts table
ALTER TABLE posts ADD COLUMN IF NOT EXISTS featured_image VARCHAR(255);
ALTER TABLE posts ADD COLUMN IF NOT EXISTS view_count INTEGER DEFAULT 0;
ALTER TABLE posts ADD COLUMN IF NOT EXISTS like_count INTEGER DEFAULT 0;

-- Update existing posts to have default values
UPDATE posts SET view_count = 0 WHERE view_count IS NULL;
UPDATE posts SET like_count = 0 WHERE like_count IS NULL;

-- Add parent_comment_id to comments table
ALTER TABLE comments ADD COLUMN IF NOT EXISTS parent_comment_id INTEGER REFERENCES comments(comment_id) ON DELETE CASCADE;
```

### Step 3: Create new tables
```sql
-- Post Likes Table
CREATE TABLE IF NOT EXISTS post_likes (
    like_id SERIAL PRIMARY KEY,
    post_id INTEGER REFERENCES posts(post_id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(post_id, user_id)
);

-- Post Ratings Table
CREATE TABLE IF NOT EXISTS post_ratings (
    rating_id SERIAL PRIMARY KEY,
    post_id INTEGER REFERENCES posts(post_id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    rating INTEGER CHECK (rating >= 1 AND rating <= 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(post_id, user_id)
);

-- Media Table
CREATE TABLE IF NOT EXISTS media (
    media_id SERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_type VARCHAR(50),
    file_size BIGINT,
    uploaded_by INTEGER REFERENCES users(user_id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Step 4: Create new indexes
```sql
CREATE INDEX IF NOT EXISTS idx_posts_views ON posts(view_count DESC);
CREATE INDEX IF NOT EXISTS idx_posts_likes ON posts(like_count DESC);
CREATE INDEX IF NOT EXISTS idx_post_likes_user ON post_likes(user_id);
CREATE INDEX IF NOT EXISTS idx_post_ratings_post ON post_ratings(post_id);
CREATE INDEX IF NOT EXISTS idx_posts_title_search ON posts USING gin(to_tsvector('english', title));
CREATE INDEX IF NOT EXISTS idx_posts_content_search ON posts USING gin(to_tsvector('english', content));
```

### Step 5: Exit
```sql
\q
```

### Done! ✅
Database migrated with all existing data preserved.

---

## Option 3: Using SQL Script

### Create migration script
Save this as `database/migrate.sql`:

```sql
-- Add new columns
ALTER TABLE posts ADD COLUMN IF NOT EXISTS featured_image VARCHAR(255);
ALTER TABLE posts ADD COLUMN IF NOT EXISTS view_count INTEGER DEFAULT 0;
ALTER TABLE posts ADD COLUMN IF NOT EXISTS like_count INTEGER DEFAULT 0;
ALTER TABLE comments ADD COLUMN IF NOT EXISTS parent_comment_id INTEGER REFERENCES comments(comment_id) ON DELETE CASCADE;

-- Update existing data
UPDATE posts SET view_count = 0 WHERE view_count IS NULL;
UPDATE posts SET like_count = 0 WHERE like_count IS NULL;

-- Create new tables
CREATE TABLE IF NOT EXISTS post_likes (
    like_id SERIAL PRIMARY KEY,
    post_id INTEGER REFERENCES posts(post_id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(post_id, user_id)
);

CREATE TABLE IF NOT EXISTS post_ratings (
    rating_id SERIAL PRIMARY KEY,
    post_id INTEGER REFERENCES posts(post_id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    rating INTEGER CHECK (rating >= 1 AND rating <= 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(post_id, user_id)
);

CREATE TABLE IF NOT EXISTS media (
    media_id SERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_type VARCHAR(50),
    file_size BIGINT,
    uploaded_by INTEGER REFERENCES users(user_id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_posts_views ON posts(view_count DESC);
CREATE INDEX IF NOT EXISTS idx_posts_likes ON posts(like_count DESC);
CREATE INDEX IF NOT EXISTS idx_post_likes_user ON post_likes(user_id);
CREATE INDEX IF NOT EXISTS idx_post_ratings_post ON post_ratings(post_id);
CREATE INDEX IF NOT EXISTS idx_posts_title_search ON posts USING gin(to_tsvector('english', title));
CREATE INDEX IF NOT EXISTS idx_posts_content_search ON posts USING gin(to_tsvector('english', content));
```

### Run migration
```bash
psql -U postgres -d blogdb -f database/migrate.sql
```

---

## Verify Migration

```bash
psql -U postgres -d blogdb

# Check tables
\dt

# Check posts table structure
\d posts

# Check new tables exist
\d post_likes
\d post_ratings
\d media

# Exit
\q
```

---

## Quick Commands Reference

```bash
# Backup database
pg_dump -U postgres blogdb > backup_$(date +%Y%m%d).sql

# Restore from backup
psql -U postgres -d blogdb -f backup_20240101.sql

# Check PostgreSQL is running
pg_isready

# Connect to database
psql -U postgres -d blogdb

# List all databases
psql -U postgres -c "\l"

# List all tables in blogdb
psql -U postgres -d blogdb -c "\dt"
```

---

## Troubleshooting

### Error: "database blogdb does not exist"
```bash
createdb -U postgres blogdb
```

### Error: "role postgres does not exist"
Replace `postgres` with your PostgreSQL username

### Error: "permission denied"
```bash
# Grant permissions
psql -U postgres -d blogdb
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO your_username;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO your_username;
```

### Check if migration was successful
```sql
SELECT column_name, data_type 
FROM information_schema.columns 
WHERE table_name = 'posts';
```
