<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Dashboard – ThoughtForge</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&family=Playfair+Display:wght@700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=1.1">
</head>
<body>
    <jsp:include page="../includes/header.jsp"/>

    <div class="container" style="padding-top: 40px; padding-bottom: 60px;">
        <div class="page-header">
            <h1 class="page-title">My Dashboard</h1>
            <a href="${pageContext.request.contextPath}/post/new" class="btn">✍️ Write New Post</a>
        </div>

        <c:choose>
            <c:when test="${not empty posts}">
                <div class="post-grid" style="grid-template-columns: repeat(auto-fill, minmax(380px, 1fr));">
                    <c:forEach var="post" items="${posts}">
                        <div class="post-card" style="display: flex; flex-direction: column;">
                            <div class="post-card-body">
                                <h3><a href="${pageContext.request.contextPath}/post/${post.postId}">${post.title}</a></h3>
                                
                                <div class="post-meta">
                                    <span class="post-meta-item">🗓️ ${fn:substring(post.createdAt, 0, 10)}</span>
                                    <span class="post-meta-item">👁️ ${post.viewCount}</span>
                                    <span class="post-meta-item">❤️ ${post.likeCount}</span>
                                    <span class="post-meta-item" style="margin-left: auto;">
                                        <c:choose>
                                            <c:when test="${post.status == 'published'}">
                                                <span class="badge badge-published">Published</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-draft">Draft</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>
                                
                                <p class="post-excerpt" style="margin-bottom: 24px;">
                                    <c:out value="${fn:length(post.content) > 120 ? fn:substring(post.content, 0, 120) : post.content}"/>...
                                </p>
                                
                                <div style="display: flex; gap: 8px; flex-wrap: wrap; margin-top: auto; padding-top: 16px; border-top: 1px solid var(--border-color);">
                                    <a href="${pageContext.request.contextPath}/post/${post.postId}" class="btn btn-outline btn-sm">View</a>
                                    <a href="${pageContext.request.contextPath}/post/edit?id=${post.postId}" class="btn btn-sm">Edit</a>
                                    
                                    <form method="post" action="${pageContext.request.contextPath}/post/" style="display:inline;">
                                        <input type="hidden" name="action" value="${post.status == 'published' ? 'unpublish' : 'publish'}">
                                        <input type="hidden" name="postId" value="${post.postId}">
                                        <button type="submit" class="btn btn-sm ${post.status == 'published' ? 'btn-warning' : 'btn-success'}">
                                            ${post.status == 'published' ? 'Draft' : 'Publish'}
                                        </button>
                                    </form>
                                    
                                    <form method="post" action="${pageContext.request.contextPath}/post/" style="display:inline; margin-left: auto;" 
                                          onsubmit="return confirm('Are you sure you want to delete this post? This cannot be undone.');">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="postId" value="${post.postId}">
                                        <button type="submit" class="btn btn-danger btn-sm">🗑️</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-state" style="padding: 80px 20px; background: var(--card-bg); border-radius: var(--radius-md); border: 1px solid var(--border-color);">
                    <div class="emoji">✍️</div>
                    <h3>Start Your Writing Journey</h3>
                    <p>You haven't published any posts yet. Start sharing your ideas with the world.</p>
                    <a href="${pageContext.request.contextPath}/post/new" class="btn" style="margin-top: 20px;">Write Your First Post</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <jsp:include page="../includes/footer.jsp"/>
</body>
</html>
