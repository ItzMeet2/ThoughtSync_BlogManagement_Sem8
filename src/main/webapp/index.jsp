<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Blog Management System</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<header>
    <nav class="container">
        <h1>Blog CMS</h1>
        <ul>
            <li><a href="<%= request.getContextPath() %>/">Home</a></li>
            <li><a href="<%= request.getContextPath() %>/post/">Blog</a></li>
            <li><a href="<%= request.getContextPath() %>/login">Login</a></li>
        </ul>
    </nav>
</header>
<main class="container">
    <div class="card" style="text-align: center; padding: 60px 20px;">
        <h1 style="font-size: 3rem; margin-bottom: 20px;">Welcome to Blog CMS</h1>
        <p style="font-size: 1.2rem; color: #7f8c8d; margin-bottom: 30px;">
            A modern content management system for bloggers and content creators
        </p>
        <div style="display: flex; gap: 20px; justify-content: center;">
            <a href="<%= request.getContextPath() %>/post/" class="btn" style="font-size: 1.1rem;">View Blog Posts</a>
            <a href="<%= request.getContextPath() %>/login" class="btn btn-success" style="font-size: 1.1rem;">Get Started</a>
        </div>
    </div>
    
    <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 20px; margin-top: 40px;">
        <div class="card">
            <h3>Easy to Use</h3>
            <p>Simple and intuitive interface for managing your blog content</p>
        </div>
        <div class="card">
            <h3>Role-Based Access</h3>
            <p>Admin, Author, and Reader roles with appropriate permissions</p>
        </div>
        <div class="card">
            <h3>Comment System</h3>
            <p>Engage with your readers through built-in comment functionality</p>
        </div>
    </div>
</main>
<footer>
    <div class="container">
        <p>&copy; 2024 Blog Management System. All rights reserved.</p>
    </div>
</footer>
</body>
</html>
