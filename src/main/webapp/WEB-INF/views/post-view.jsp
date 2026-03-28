<%@ page import="com.mycompany.blogmanagement.dao.CommentDAO" %>
<%@ page import="com.mycompany.blogmanagement.entity.Comment" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${post.title} – ThoughtForge</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&family=Playfair+Display:wght@700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=1.1">
</head>
<body>
    <jsp:include page="../includes/header.jsp"/>

    <div class="container" style="padding-top: 40px; padding-bottom: 60px; max-width: 900px;">
        
        <!-- Article Header -->
        <div class="article-header">
            <c:if test="${post.category != null}">
                <a href="${pageContext.request.contextPath}/search?category=${post.category.categoryId}" class="article-category">
                    ${post.category.categoryName}
                </a>
            </c:if>
            
            <h1 class="article-title">${post.title}</h1>
            
            <div class="article-meta">
                <div style="display: flex; align-items: center; gap: 12px;">
                    <div style="width: 44px; height: 44px; border-radius: 50%; background: linear-gradient(135deg, var(--primary-color), var(--secondary-color)); display: flex; align-items: center; justify-content: center; color: white; font-weight: bold; font-size: 1.2rem;">
                        ${fn:substring(post.author.fullName != null ? post.author.fullName : post.author.username, 0, 1).toUpperCase()}
                    </div>
                    <div>
                        <div style="color: #fff; font-weight: 600; font-size: 1rem;">
                            ${post.author.fullName != null ? post.author.fullName : post.author.username}
                        </div>
                        <div style="font-size: 0.85rem;">
                            ${fn:substring(post.publishedAt != null ? post.publishedAt : post.createdAt, 0, 10)}
                            &nbsp;·&nbsp;
                            👁️ ${post.viewCount} views
                        </div>
                    </div>
                </div>
                
                <div style="margin-left: auto; display: flex; gap: 16px;">
                    <button class="btn btn-outline btn-sm" style="border-radius: 20px; padding: 6px 16px;">
                        ❤️ ${post.likeCount}
                    </button>
                    <button class="btn btn-outline btn-sm" onclick="copyShareLink()" style="border-radius: 20px; padding: 6px 16px;">
                        ↗️ Share
                    </button>
                </div>
            </div>
            
            <script>
            function copyShareLink() {
                const url = window.location.href;
                navigator.clipboard.writeText(url).then(() => {
                    alert('Post Link copied to clipboard! Share it anywhere!');
                });
            }
            </script>
        </div>

        <!-- Featured Image -->
        <c:if test="${not empty post.featuredImage}">
            <img src="${pageContext.request.contextPath}/${post.featuredImage}" alt="${post.title}" class="article-hero-img">
        </c:if>

        <!-- Article Content -->
        <article class="article-content">
            ${post.content}
        </article>

        <!-- Action / Like Section -->
        <c:if test="${sessionScope.user != null}">
            <div class="like-section" style="display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 15px;">
                <div style="display: flex; align-items: center; gap: 10px;">
                    <form method="post" action="${pageContext.request.contextPath}/like">
                        <input type="hidden" name="postId" value="${post.postId}">
                        <button type="submit" class="like-btn">❤️ Like</button>
                    </form>
                    
                    <button class="like-btn" onclick="document.getElementById('reportModal').style.display='flex'" style="background: rgba(255, 60, 60, 0.1); color: #ff5555; border-color: rgba(255, 60, 60, 0.3);">
                        🚩 Report Post
                    </button>
                </div>
                
                <div style="display: flex; align-items: center; gap: 10px;">
                    <span style="color: var(--text-muted); font-size: 0.9rem;">Rate:</span>
                    <div style="display: flex; gap: 5px;">
                        <c:forEach var="i" begin="1" end="5">
                            <a href="javascript:void(0)" onclick="ratePost('${post.postId}', '${i}')" style="text-decoration: none; font-size: 1.2rem; filter: grayscale(1); transition: 0.2s;">⭐</a>
                        </c:forEach>
                    </div>
                </div>
            </div>
            
            <!-- Report Modal overlay -->
            <div id="reportModal" style="display: none; position: fixed; inset: 0; background: rgba(0,0,0,0.8); z-index: 1000; align-items: center; justify-content: center;">
                <div class="card" style="width: 100%; max-width: 500px; padding: 30px;">
                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
                        <h3 style="color: white; margin: 0;">Report this Post</h3>
                        <button onclick="document.getElementById('reportModal').style.display='none'" style="background: none; border: none; color: white; cursor: pointer; font-size: 1.5rem;">&times;</button>
                    </div>
                    
                    <c:if test="${not empty sessionScope.error}">
                        <div style="background: rgba(255, 60, 60, 0.1); border-left: 4px solid #ff5555; padding: 15px; margin-bottom: 20px; color: #ffaaaa; border-radius: 4px;">
                            ${sessionScope.error}
                            <c:remove var="error" scope="session"/>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/report" method="POST">
                        <input type="hidden" name="action" value="submit">
                        <input type="hidden" name="postId" value="${post.postId}">
                        
                        <div class="form-group">
                            <label style="color: var(--text-muted); font-size: 0.9rem; margin-bottom: 8px; display: block;">Why are you reporting this post?</label>
                            <textarea name="reason" required 
                                      style="min-height: 120px; padding: 15px; width: 100%; resize: vertical;" 
                                      placeholder="Please provide details about what is inappropriate, offensive, or violates our guidelines..."></textarea>
                        </div>
                        
                        <div style="display: flex; gap: 10px; margin-top: 25px;">
                            <button type="submit" class="btn" style="flex: 1; background: #ff5555; border-color: #ff5555; color: white;">Submit Report</button>
                            <button type="button" class="btn btn-outline" style="flex: 1;" onclick="document.getElementById('reportModal').style.display='none'">Cancel</button>
                        </div>
                    </form>
                </div>
            </div>
            
            <script>
            function ratePost(postId, rating) {
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = '${pageContext.request.contextPath}/rate';
                form.innerHTML = `<input type="hidden" name="postId" value="\${postId}"><input type="hidden" name="rating" value="\${rating}">`;
                document.body.appendChild(form);
                form.submit();
            }
            </script>
        </c:if>

        <!-- Comments Section -->
        <div class="comment-section">
            <h3>Comments / Discussion</h3>
            
            <c:if test="${sessionScope.user != null}">
                <div class="comment-form">
                    <h4>Leave a reply</h4>
                    <form method="post" action="${pageContext.request.contextPath}/comment">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="postId" value="${post.postId}">
                        <div class="form-group">
                            <textarea name="content" placeholder="Share your thoughts..." required style="min-height: 100px; padding: 15px; border-radius: var(--radius-md);"></textarea>
                        </div>
                        <button type="submit" class="btn" style="margin-top: 10px;">Post Comment</button>
                    </form>
                </div>
            </c:if>
            <c:if test="${sessionScope.user == null}">
                <div style="padding: 20px; background: rgba(255,255,255,0.05); border-radius: var(--radius-md); text-align: center; margin-bottom: 30px;">
                    <p style="margin-bottom: 12px; color: var(--text-muted);">Join the discussion to comment on this article.</p>
                    <a href="${pageContext.request.contextPath}/login" class="btn" style="border-radius: 20px;">Sign In to Comment</a>
                </div>
            </c:if>

            <div style="margin-top: 40px;">
                <%
                    com.mycompany.blogmanagement.dao.CommentDAO commentDAO = new com.mycompany.blogmanagement.dao.CommentDAO();
                    com.mycompany.blogmanagement.entity.Post p = (com.mycompany.blogmanagement.entity.Post) request.getAttribute("post");
                    if (p != null) {
                        java.util.List<com.mycompany.blogmanagement.entity.Comment> comments = commentDAO.findByPost(p.getPostId());
                        request.setAttribute("comments", comments);
                    }
                %>
                
                <c:choose>
                    <c:when test="${not empty comments}">
                        <c:forEach var="comment" items="${comments}">
                            <div class="comment">
                                <div class="comment-header">
                                    <div class="comment-avatar">
                                        ${fn:substring(comment.user.fullName != null ? comment.user.fullName : comment.user.username, 0, 1).toUpperCase()}
                                    </div>
                                    <div>
                                        <div class="comment-author">${comment.user.fullName != null ? comment.user.fullName : comment.user.username}</div>
                                        <div class="comment-date">${fn:substring(comment.createdAt, 0, 16)}</div>
                                    </div>
                                </div>
                                <div class="comment-text">
                                    ${comment.content}
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p style="color: var(--text-muted); text-align: center; padding: 20px 0;">
                            No comments yet. Be the first to start the conversation!
                        </p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        
    </div>

    <jsp:include page="../includes/footer.jsp"/>
</body>
</html>
