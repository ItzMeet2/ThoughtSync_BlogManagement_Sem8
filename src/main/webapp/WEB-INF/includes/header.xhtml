<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ThoughtForge – Where Ideas Come Alive</title>
    <meta name="description" content="ThoughtForge is a modern blogging platform for sharing ideas, stories, and insights.">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&family=Playfair+Display:wght@700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=1.1">
</head>
<body>
<header>
    <nav class="container">
        <a href="${pageContext.request.contextPath}/" class="nav-brand">✍️ ThoughtForge</a>
        <ul>
            <li><a href="${pageContext.request.contextPath}/">Home</a></li>
            <li><a href="${pageContext.request.contextPath}/post/">Blog</a></li>
            <c:choose>
                <c:when test="${sessionScope.user != null}">
                    <li><a href="${pageContext.request.contextPath}/dashboard">Dashboard</a></li>
                    <c:if test="${sessionScope.role == 'ADMIN'}">
                        <li><a href="${pageContext.request.contextPath}/admin">Admin</a></li>
                    </c:if>
                    <li style="display: flex; align-items: center; margin-left: 10px;">
                        <a href="${pageContext.request.contextPath}/profile" class="nav-avatar" title="My Profile" style="padding: 0; display: inline-flex; align-items: center; justify-content: center; overflow: hidden;">
                            <c:choose>
                                <c:when test="${not empty sessionScope.user.profilePicture}">
                                    <img src="${pageContext.request.contextPath}/${sessionScope.user.profilePicture}" alt="Avatar" style="width: 100%; height: 100%; object-fit: cover; border-radius: 50%;">
                                </c:when>
                                <c:otherwise>
                                    ${fn:substring(sessionScope.user.fullName != null && not empty sessionScope.user.fullName ? sessionScope.user.fullName : sessionScope.user.username, 0, 1).toUpperCase()}
                                </c:otherwise>
                            </c:choose>
                        </a>
                    </li>
                    <li style="margin-left: 15px;">
                        <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline btn-sm" style="padding: 6px 14px; color: var(--text-color); border-color: rgba(255,255,255,0.2);">Logout</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li><a href="${pageContext.request.contextPath}/login">Login</a></li>
                    <li><a href="${pageContext.request.contextPath}/register" class="nav-register">Register</a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </nav>
</header>
<main class="container">
