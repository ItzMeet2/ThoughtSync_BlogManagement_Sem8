<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard – ThoughtForge</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&family=Playfair+Display:wght@700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=1.1">
</head>
<body>
    <jsp:include page="../includes/header.jsp"/>

    <div class="container" style="padding-top: 40px; padding-bottom: 60px;">
        <div class="page-header">
            <h1 class="page-title">Admin Dashboard</h1>
            <a href="${pageContext.request.contextPath}/post/new" class="btn">✍️ Write New Post</a>
        </div>

        <!-- Statistics -->
        <div class="stats-grid">
            <a href="javascript:void(0)" onclick="toggleSection('posts')" class="stat-card" style="text-decoration: none; color: inherit; transition: transform 0.2s;">
                <div class="stat-icon">📝</div>
                <div class="stat-number">${totalPosts != null ? totalPosts : '0'}</div>
                <div class="stat-label">Total Posts</div>
            </a>
            <a href="javascript:void(0)" onclick="toggleSection('posts')" class="stat-card" style="text-decoration: none; color: inherit; transition: transform 0.2s;">
                <div class="stat-icon">✅</div>
                <div class="stat-number">${publishedPosts != null ? publishedPosts : '0'}</div>
                <div class="stat-label">Published</div>
            </a>
            <a href="javascript:void(0)" onclick="toggleSection('users')" class="stat-card" style="text-decoration: none; color: inherit; transition: transform 0.2s;">
                <div class="stat-icon">👥</div>
                <div class="stat-number">${totalUsers != null ? totalUsers : '0'}</div>
                <div class="stat-label">Users</div>
            </a>
            <a href="javascript:void(0)" onclick="toggleSection('categories')" class="stat-card" style="text-decoration: none; color: inherit; transition: transform 0.2s;">
                <div class="stat-icon">📂</div>
                <div class="stat-number">${totalCategories != null ? totalCategories : '0'}</div>
                <div class="stat-label">Categories</div>
            </a>
        </div>
        
        <!-- Pending Reports Table -->
        <div class="card" style="padding: 0; overflow: hidden; margin-bottom: 30px; border-color: rgba(255, 100, 100, 0.3);">
            <div style="padding: 24px; border-bottom: 1px solid var(--border-color); display: flex; justify-content: space-between; align-items: center; background: rgba(255, 60, 60, 0.05);">
                <h2 style="font-size: 1.25rem; font-weight: 700; color: #ff8888; margin: 0; display: flex; align-items: center; gap: 10px;">
                    🚩 Pending Reports <span class="badge" style="background: #ff5555; color: white;">${pendingReportCount != null ? pendingReportCount : '0'}</span>
                </h2>
            </div>
            
            <div class="table-wrap" style="border: none; border-radius: 0;">
                <c:choose>
                    <c:when test="${not empty pendingReports}">
                        <table>
                            <thead>
                                <tr>
                                    <th>Reported Post</th>
                                    <th>Reported By</th>
                                    <th style="width: 40%;">Reason</th>
                                    <th style="text-align: right;">Action Overview</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="report" items="${pendingReports}">
                                    <tr>
                                        <td style="font-weight: 500; color: #fff;">
                                            <a href="${pageContext.request.contextPath}/post/${report.post.postId}" target="_blank" style="color: var(--primary-color);">
                                                ${report.post.title} ↗
                                            </a>
                                        </td>
                                        <td>${report.reporter.username}</td>
                                        <td>
                                            <div style="font-size: 0.9rem; color: var(--text-muted); padding: 8px; background: rgba(255,255,255,0.03); border-radius: 4px; max-height: 80px; overflow-y: auto;">
                                                ${report.reason}
                                            </div>
                                        </td>
                                        <td style="text-align: right;">
                                            <form action="${pageContext.request.contextPath}/report" method="POST" style="display:inline;">
                                                <input type="hidden" name="action" value="dismiss">
                                                <input type="hidden" name="reportId" value="${report.reportId}">
                                                <button type="submit" class="btn btn-outline btn-sm" style="color: var(--text-muted);">Ignore</button>
                                            </form>
                                            
                                            <form action="${pageContext.request.contextPath}/report" method="POST" style="display:inline; margin-left: 8px;" onsubmit="return confirm('Are you sure you want to permanently DELETE this blog post? This cannot be undone.');">
                                                <input type="hidden" name="action" value="deletePost">
                                                <input type="hidden" name="reportId" value="${report.reportId}">
                                                <button type="submit" class="btn btn-outline btn-sm" style="color: #ff5555; border-color: rgba(255, 60, 60, 0.3);">🗑️ Delete Post</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-state" style="padding: 30px 20px;">
                            <p style="color: #ff8888;">No pending reports! Your community is safe and happy.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Posts Table -->
        <div id="posts" class="card" style="padding: 0; overflow: hidden; margin-bottom: 30px;">
            <div style="padding: 24px; border-bottom: 1px solid var(--border-color); display: flex; justify-content: space-between; align-items: center;">
                <h2 style="font-size: 1.25rem; font-weight: 700; color: #fff;">Recent Posts Overview</h2>
            </div>
            
            <div class="table-wrap" style="border: none; border-radius: 0;">
                <c:choose>
                    <c:when test="${not empty recentPosts}">
                        <table>
                            <thead>
                                <tr>
                                    <th>Title</th>
                                    <th>Author</th>
                                    <th>Status</th>
                                    <th>Views</th>
                                    <th>Likes</th>
                                    <th style="text-align: right;">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="post" items="${recentPosts}">
                                    <tr>
                                        <td style="font-weight: 500; color: #fff;">${post.title}</td>
                                        <td>${post.author.fullName != null ? post.author.fullName : post.author.username}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${post.status == 'published'}">
                                                    <span class="badge badge-published">Published</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-draft">Draft</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${post.viewCount}</td>
                                        <td>${post.likeCount}</td>
                                        <td style="text-align: right;">
                                            <a href="${pageContext.request.contextPath}/post/edit?id=${post.postId}" class="btn btn-outline btn-sm">Edit</a>
                                            <a href="${pageContext.request.contextPath}/post/${post.postId}" class="btn btn-outline btn-sm" style="margin-left: 4px;">View</a>
                                            <form action="${pageContext.request.contextPath}/admin" method="POST" style="display:inline; margin-left: 4px;" onsubmit="return confirm('Delete this post permanently?');">
                                                <input type="hidden" name="action" value="deletePost">
                                                <input type="hidden" name="postId" value="${post.postId}">
                                                <button type="submit" class="btn btn-outline btn-sm" style="color: #ff5555; border-color: rgba(255, 60, 60, 0.3);">🗑️</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-state" style="padding: 60px 20px;">
                            <div class="emoji">📭</div>
                            <h3>No posts found</h3>
                            <p>There are no posts in the database yet.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        
        <!-- Users Table -->
        <div id="users" class="card tab-section" style="display: none; padding: 0; overflow: hidden; margin-bottom: 30px;">
            <div style="padding: 24px; border-bottom: 1px solid var(--border-color); display: flex; justify-content: space-between; align-items: center;">
                <h2 style="font-size: 1.25rem; font-weight: 700; color: #fff;">Registered Users</h2>
            </div>
            
            <div class="table-wrap" style="border: none; border-radius: 0;">
                <c:choose>
                    <c:when test="${not empty allUsers}">
                        <table>
                            <thead>
                                <tr>
                                    <th>Username</th>
                                    <th>Full Name</th>
                                    <th>Email</th>
                                    <th>Role</th>
                                    <th>Joined</th>
                                    <th style="text-align: right;">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="u" items="${allUsers}">
                                    <tr>
                                        <td style="font-weight: 500; color: #fff;">@${u.username}</td>
                                        <td>${u.fullName != null ? u.fullName : '-'}</td>
                                        <td>${u.email}</td>
                                        <td><span class="badge" style="background: var(--surface-light);">${u.role.roleName}</span></td>
                                        <td>${fn:substring(u.createdAt, 0, 10)}</td>
                                        <td style="text-align: right;">
                                            <form action="${pageContext.request.contextPath}/admin" method="POST" style="display:inline;" onsubmit="return confirm('Delete this user account permanently?');">
                                                <input type="hidden" name="action" value="deleteUser">
                                                <input type="hidden" name="userId" value="${u.userId}">
                                                <button type="submit" class="btn btn-outline btn-sm" style="color: #ff5555; border-color: rgba(255, 60, 60, 0.3);" ${u.userId == sessionScope.user.userId ? 'disabled' : ''}>🗑️</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-state" style="padding: 40px 20px;">
                            <p>No users found in the database.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Categories Table -->
        <div id="categories" class="card tab-section" style="display: none; padding: 0; overflow: hidden; margin-bottom: 30px;">
            <div style="padding: 24px; border-bottom: 1px solid var(--border-color); display: flex; justify-content: space-between; align-items: center;">
                <h2 style="font-size: 1.25rem; font-weight: 700; color: #fff;">Blog Categories</h2>
            </div>
            
            <div class="table-wrap" style="border: none; border-radius: 0;">
                <c:choose>
                    <c:when test="${not empty allCategories}">
                        <table>
                            <thead>
                                <tr>
                                    <th style="width: 30%;">Category Name</th>
                                    <th>Description</th>
                                    <th style="text-align: right;">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="cat" items="${allCategories}">
                                    <tr>
                                        <td style="font-weight: 500; color: #fff;">${cat.categoryName}</td>
                                        <td style="color: var(--text-muted);">${cat.description != null ? cat.description : '-'}</td>
                                        <td style="text-align: right;">
                                            <form action="${pageContext.request.contextPath}/admin" method="POST" style="display:inline;" onsubmit="return confirm('Delete this category permanently? This will also affect posts under this category.');">
                                                <input type="hidden" name="action" value="deleteCategory">
                                                <input type="hidden" name="categoryId" value="${cat.categoryId}">
                                                <button type="submit" class="btn btn-outline btn-sm" style="color: #ff5555; border-color: rgba(255, 60, 60, 0.3);">🗑️</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-state" style="padding: 40px 20px;">
                            <p>No categories found in the database.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
    
    <script>
        function toggleSection(sectionId) {
            // Hide all tab sections
            document.querySelectorAll('.tab-section').forEach(function(el) {
                el.style.display = 'none';
            });
            // Show the targeted tab
            const target = document.getElementById(sectionId);
            if (target) {
                target.style.display = 'block';
                // Scroll to the element smoothly
                target.scrollIntoView({ behavior: 'smooth', block: 'start' });
            }
        }
    </script>

    <jsp:include page="../includes/footer.jsp"/>
</body>
</html>
