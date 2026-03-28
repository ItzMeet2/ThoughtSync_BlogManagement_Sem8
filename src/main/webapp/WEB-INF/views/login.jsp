<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login – ThoughtForge</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&family=Playfair+Display:wght@700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=1.1">
</head>
<body>
<header>
    <nav class="container">
        <a href="${pageContext.request.contextPath}/" class="nav-brand">✍️ ThoughtForge</a>
        <ul>
            <li><a href="${pageContext.request.contextPath}/">Home</a></li>
            <li><a href="${pageContext.request.contextPath}/register" class="nav-register">Register</a></li>
        </ul>
    </nav>
</header>

<div class="auth-page">
    <div class="auth-card">
        <h2>Welcome Back</h2>
        <p class="auth-sub">Sign in to continue to ThoughtForge</p>

        <c:if test="${not empty error}">
            <div class="alert alert-error">⚠️ ${error}</div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="alert alert-success">✅ ${success}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/login" autocomplete="on">
            <div class="form-group">
                <label for="username">Username or Email</label>
                <input type="text" id="username" name="username"
                       placeholder="Enter your username or email"
                       value="${param.username}"
                       autocomplete="username" required>
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <div class="password-wrapper">
                    <input type="password" id="password" name="password"
                           placeholder="Enter your password"
                           autocomplete="current-password" required>
                    <button type="button" class="password-toggle" onclick="togglePassword('password', this)">
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="eye-icon"><path d="M2.062 12.348a1 1 0 0 1 0-.696 10.75 10.75 0 0 1 19.876 0 1 1 0 0 1 0 .696 10.75 10.75 0 0 1-19.876 0"/><circle cx="12" cy="12" r="3"/></svg>
                    </button>
                </div>
            </div>
            <button type="submit" class="btn">Sign In →</button>
        </form>

        <script>
            function togglePassword(inputId, btn) {
                const input = document.getElementById(inputId);
                if (input.type === 'password') {
                    input.type = 'text';
                    btn.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="eye-off-icon"><path d="M9.88 9.88a3 3 0 1 0 4.24 4.24"/><path d="M10.73 5.08A10.43 10.43 0 0 1 12 5c7 0 10 7 10 7a13.16 13.16 0 0 1-1.67 2.68"/><path d="M6.61 6.61A13.526 13.526 0 0 0 2 12s3 7 10 7a9.74 9.74 0 0 0 5.39-1.61"/><line x1="2" x2="22" y1="2" y2="22"/></svg>';
                } else {
                    input.type = 'password';
                    btn.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="eye-icon"><path d="M2.062 12.348a1 1 0 0 1 0-.696 10.75 10.75 0 0 1 19.876 0 1 1 0 0 1 0 .696 10.75 10.75 0 0 1-19.876 0"/><circle cx="12" cy="12" r="3"/></svg>';
                }
            }
        </script>

        <div class="auth-divider">or continue with</div>

        <a href="${pageContext.request.contextPath}/login/google" class="btn btn-google">
            <img src="https://upload.wikimedia.org/wikipedia/commons/c/c1/Google_%22G%22_logo.svg" alt="Google">
            Continue with Google
        </a>

        <div class="auth-footer">
            Don't have an account? <a href="${pageContext.request.contextPath}/register">Create one free →</a>
        </div>
    </div>
</div>

<footer>
    <div class="container">
        <div class="footer-inner">
            <div class="footer-brand">✍️ ThoughtForge</div>
            <div class="footer-copy">&copy; 2026 ThoughtForge. All rights reserved.</div>
        </div>
    </div>
</footer>
</body>
</html>
