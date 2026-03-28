<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<jsp:include page="../includes/header.jsp"/>

<!-- Hero -->
<section class="hero">
    <h1>Where Ideas Come Alive</h1>
    <p>Discover stories, insights, and ideas from writers across all topics</p>
    <form action="${pageContext.request.contextPath}/search" method="get" class="hero-search">
        <input type="text" name="q" placeholder="Search articles, topics, authors..." value="${param.q}">
        <button type="submit">🔍 Search</button>
    </form>
</section>

<!-- Popular Posts -->
<section style="margin-bottom: 60px;">
    <div class="section-header">
        <h2 class="section-title">🔥 Popular Posts</h2>
        <a href="${pageContext.request.contextPath}/post/" class="btn btn-outline" style="font-size:0.85rem; padding:8px 16px;">View All</a>
    </div>
    <c:choose>
        <c:when test="${not empty popularPosts}">
            <div class="post-grid">
                <c:forEach var="post" items="${popularPosts}">
                    <div class="post-card">
                        <c:choose>
                            <c:when test="${not empty post.featuredImage}">
                                <img src="${pageContext.request.contextPath}/${post.featuredImage}" alt="${post.title}" class="post-card-img">
                            </c:when>
                            <c:otherwise>
                                <div class="post-card-img-placeholder">📝</div>
                            </c:otherwise>
                        </c:choose>
                        <div class="post-card-body">
                            <c:if test="${post.category != null}">
                                <span class="post-card-category">${post.category.categoryName}</span>
                            </c:if>
                            <h3><a href="${pageContext.request.contextPath}/post/${post.postId}">${post.title}</a></h3>
                            <div class="post-meta">
                                <span class="post-meta-item">👤 ${not empty post.author.fullName ? post.author.fullName : post.author.username}</span>
                                <span class="post-meta-item">👁 ${post.viewCount}</span>
                                <span class="post-meta-item">❤️ ${post.likeCount}</span>
                            </div>
                            <p class="post-excerpt">
                                <c:out value="${fn:length(post.content) > 150 ? fn:substring(post.content, 0, 150) : post.content}"/>...
                            </p>
                            <a href="${pageContext.request.contextPath}/post/${post.postId}" class="read-more">Read More →</a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="empty-state">
                <div class="emoji">📭</div>
                <h3>No posts yet</h3>
                <p>Be the first to publish something amazing!</p>
                <c:if test="${sessionScope.user != null}">
                    <a href="${pageContext.request.contextPath}/post/new" class="btn" style="margin-top:16px;">✍️ Write a Post</a>
                </c:if>
                <c:if test="${sessionScope.user == null}">
                    <a href="${pageContext.request.contextPath}/register" class="btn" style="margin-top:16px;">Join ThoughtForge</a>
                </c:if>
            </div>
        </c:otherwise>
    </c:choose>
</section>

<!-- Recent Posts + Sidebar -->
<div class="home-layout">
    <!-- Recent Posts -->
    <section>
        <div class="section-header">
            <h2 class="section-title">🆕 Recent Posts</h2>
        </div>
        <c:choose>
            <c:when test="${not empty recentPosts}">
                <c:forEach var="post" items="${recentPosts}">
                    <div class="post-card" style="flex-direction:row; margin-bottom:20px; border-radius:14px;">
                        <c:if test="${not empty post.featuredImage}">
                            <img src="${pageContext.request.contextPath}/${post.featuredImage}" alt="${post.title}"
                                 style="width:160px; height:100%; object-fit:cover; border-radius:14px 0 0 14px; flex-shrink:0;">
                        </c:if>
                        <div class="post-card-body" style="padding:20px;">
                            <c:if test="${post.category != null}">
                                <span class="post-card-category">${post.category.categoryName}</span>
                            </c:if>
                            <h3><a href="${pageContext.request.contextPath}/post/${post.postId}">${post.title}</a></h3>
                            <div class="post-meta">
                                <span>👤 ${not empty post.author.fullName ? post.author.fullName : post.author.username}</span>
                                <span>👁 ${post.viewCount} views</span>
                            </div>
                            <p class="post-excerpt">
                                <c:out value="${fn:length(post.content) > 180 ? fn:substring(post.content, 0, 180) : post.content}"/>...
                            </p>
                            <a href="${pageContext.request.contextPath}/post/${post.postId}" class="read-more">Read More →</a>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <div class="emoji">✍️</div>
                    <h3>No published articles yet</h3>
                    <p>Check back soon for fresh content!</p>
                </div>
            </c:otherwise>
        </c:choose>
    </section>

    <!-- Sidebar -->
    <aside class="sidebar">
        <!-- Categories -->
        <div class="sidebar-widget">
            <h3>📂 Categories</h3>
            <c:choose>
                <c:when test="${not empty categories}">
                    <ul class="category-list">
                        <c:forEach var="category" items="${categories}">
                            <li>
                                <a href="${pageContext.request.contextPath}/search?category=${category.categoryId}">
                                    ${category.categoryName} <span>›</span>
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </c:when>
                <c:otherwise>
                    <p style="color:var(--text-muted); font-size:0.85rem;">No categories yet.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- CTA Widget -->
        <c:if test="${sessionScope.user == null}">
            <div class="sidebar-widget" style="text-align:center; background: linear-gradient(135deg, rgba(99,102,241,0.2), rgba(168,85,247,0.15)); border-color: rgba(99,102,241,0.3);">
                <div style="font-size:2.5rem; margin-bottom:12px;">✍️</div>
                <h3 style="border:none; color:#a5b4fc; font-size:1rem; text-transform:none; letter-spacing:normal; margin-bottom:8px;">Start Writing Today</h3>
                <p style="font-size:0.85rem; color:var(--text-muted); margin-bottom:16px;">Join our community of writers and share your ideas with the world.</p>
                <a href="${pageContext.request.contextPath}/register" class="btn" style="width:100%; font-size:0.85rem;">Create Account</a>
                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline" style="width:100%; font-size:0.85rem; margin-top:10px;">Sign In</a>
            </div>
        </c:if>

        <c:if test="${sessionScope.user != null}">
            <div class="sidebar-widget" style="text-align:center;">
                <div style="font-size:2rem; margin-bottom:10px;">👋</div>
                <h3 style="border:none; color:#fff; font-size:1rem; text-transform:none; letter-spacing:normal; margin-bottom:6px;">Hello, ${sessionScope.username}!</h3>
                <a href="${pageContext.request.contextPath}/post/new" class="btn" style="width:100%; font-size:0.85rem; margin-top:10px;">✍️ Write a Post</a>
                <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline" style="width:100%; font-size:0.85rem; margin-top:10px;">Dashboard</a>
            </div>
        </c:if>
    </aside>
</div>

<jsp:include page="../includes/footer.jsp"/>
