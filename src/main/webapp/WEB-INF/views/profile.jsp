<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile – ThoughtForge</title>
    <jsp:include page="../includes/header.jsp" />
    <style>
        .profile-page-wrap {
            padding: 40px 20px;
            max-width: 1000px;
            margin: 0 auto;
        }
        .profile-header-card {
            background: var(--glass-bg);
            backdrop-filter: blur(16px);
            -webkit-backdrop-filter: blur(16px);
            border: 1px solid var(--glass-border);
            border-radius: 24px;
            padding: 40px;
            display: flex;
            gap: 40px;
            align-items: center;
            margin-bottom: 40px;
        }
        .profile-avatar-container {
            flex-shrink: 0;
        }
        .profile-custom-avatar {
            width: 150px;
            height: 150px;
            border-radius: 50%;
            object-fit: cover;
            border: 4px solid var(--primary-color);
            box-shadow: 0 8px 32px rgba(99, 102, 241, 0.3);
        }
        .profile-initial-avatar {
            width: 150px;
            height: 150px;
            border-radius: 50%;
            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 64px;
            font-weight: 700;
            box-shadow: 0 8px 32px rgba(99, 102, 241, 0.3);
        }
        .profile-info {
            flex-grow: 1;
        }
        .profile-info h1 {
            font-size: 2.5rem;
            margin-bottom: 5px;
            color: var(--text-color);
        }
        .profile-info h3 {
            font-size: 1.25rem;
            color: var(--text-muted);
            font-weight: 400;
            margin-bottom: 20px;
        }
        .profile-bio {
            font-size: 1rem;
            line-height: 1.6;
            color: var(--text-color);
            margin-bottom: 20px;
            max-width: 600px;
        }
        .profile-stats {
            display: flex;
            gap: 20px;
            margin-bottom: 20px;
        }
        .stat-item {
            background: rgba(255, 255, 255, 0.05);
            padding: 10px 20px;
            border-radius: 12px;
            text-align: center;
        }
        .stat-value {
            font-size: 1.5rem;
            font-weight: 700;
            color: var(--primary-color);
        }
        .stat-label {
            font-size: 0.85rem;
            color: var(--text-muted);
            text-transform: uppercase;
            letter-spacing: 1px;
        }
        
        /* Tabs System */
        .profile-tabs {
            display: flex;
            gap: 20px;
            border-bottom: 1px solid var(--glass-border);
            margin-bottom: 30px;
        }
        .tab-btn {
            background: none;
            border: none;
            padding: 15px 30px;
            font-size: 1.1rem;
            color: var(--text-muted);
            cursor: pointer;
            font-weight: 500;
            position: relative;
            transition: all 0.3s ease;
        }
        .tab-btn:hover {
            color: var(--text-color);
        }
        .tab-btn.active {
            color: var(--primary-color);
        }
        .tab-btn.active::after {
            content: '';
            position: absolute;
            bottom: -1px;
            left: 0;
            width: 100%;
            height: 3px;
            background: var(--primary-color);
            border-radius: 3px 3px 0 0;
        }
        .tab-pane {
            display: none;
            animation: fadeIn 0.4s ease;
        }
        .tab-pane.active {
            display: block;
        }
        
        /* Edit Modal */
        .modal-overlay {
            position: fixed;
            top: 0; left: 0; width: 100%; height: 100%;
            background: rgba(0, 0, 0, 0.6);
            backdrop-filter: blur(8px);
            display: none;
            align-items: center;
            justify-content: center;
            z-index: 1000;
            opacity: 0;
            transition: opacity 0.3s ease;
        }
        .modal-overlay.active {
            display: flex;
            opacity: 1;
        }
        .edit-profile-modal {
            background: var(--bg-color);
            border: 1px solid var(--glass-border);
            border-radius: 24px;
            padding: 40px;
            width: 100%;
            max-width: 500px;
            transform: translateY(20px);
            transition: transform 0.3s ease;
        }
        .modal-overlay.active .edit-profile-modal {
            transform: translateY(0);
        }
        .modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }
        .close-btn {
            background: none;
            border: none;
            font-size: 1.5rem;
            color: var(--text-muted);
            cursor: pointer;
        }
        
        .posts-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 24px;
        }
        
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }
        
        @media (max-width: 768px) {
            .profile-header-card {
                flex-direction: column;
                text-align: center;
            }
            .profile-stats {
                justify-content: center;
            }
            .profile-actions {
                justify-content: center;
                display: flex;
                gap: 10px;
            }
        }
    </style>
</head>
<body>

<div class="profile-page-wrap">
    
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success" style="margin-bottom: 20px;">${successMessage}</div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger" style="margin-bottom: 20px;">${errorMessage}</div>
    </c:if>

    <!-- Profile Header -->
    <div class="profile-header-card">
        <div class="profile-avatar-container">
            <c:choose>
                <c:when test="${not empty sessionScope.user.profilePicture}">
                    <img src="${pageContext.request.contextPath}/${sessionScope.user.profilePicture}" alt="Avatar" class="profile-custom-avatar">
                </c:when>
                <c:otherwise>
                    <div class="profile-initial-avatar">
                        ${fn:substring(sessionScope.user.fullName != null && not empty sessionScope.user.fullName ? sessionScope.user.fullName : sessionScope.user.username, 0, 1).toUpperCase()}
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        
        <div class="profile-info">
            <h1>
                <c:choose>
                    <c:when test="${not empty sessionScope.user.fullName}">
                        ${sessionScope.user.fullName}
                    </c:when>
                    <c:otherwise>
                        ${sessionScope.user.username}
                    </c:otherwise>
                </c:choose>
            </h1>
            <h3>@${sessionScope.user.username} • <span class="badge ${sessionScope.user.role.roleName == 'ADMIN' ? 'badge-published' : 'badge-pending'}">${sessionScope.user.role.roleName}</span></h3>
            
            <p class="profile-bio">
                <c:choose>
                    <c:when test="${not empty sessionScope.user.bio}">
                        ${sessionScope.user.bio}
                    </c:when>
                    <c:otherwise>
                        <i style="color: var(--text-muted)">No bio provided yet.</i>
                    </c:otherwise>
                </c:choose>
            </p>
            
            <div class="profile-stats">
                <div class="stat-item">
                    <div class="stat-value">${fn:length(authoredPosts)}</div>
                    <div class="stat-label">Posts</div>
                </div>
                <div class="stat-item">
                    <div class="stat-value">${fn:length(likedPosts)}</div>
                    <div class="stat-label">Liked</div>
                </div>
                <div class="stat-item">
                    <div class="stat-value">${fn:substring(sessionScope.user.createdAt, 0, 4)}</div>
                    <div class="stat-label">Joined</div>
                </div>
            </div>
            
            <div class="profile-actions" style="margin-top: 20px;">
                <button onclick="toggleModal(true)" class="btn btn-primary">Edit Profile</button>
                <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline" style="margin-left: 10px;">Dashboard</a>
            </div>
        </div>
    </div>
    
    <!-- Content Tabs -->
    <div class="profile-tabs">
        <button class="tab-btn active" onclick="switchTab('authored')">My Posts</button>
        <button class="tab-btn" onclick="switchTab('liked')">Liked Posts</button>
    </div>
    
    <!-- Tab: Authored Posts -->
    <div id="tab-authored" class="tab-pane active">
        <c:choose>
            <c:when test="${empty authoredPosts}">
                <div style="text-align: center; padding: 50px; color: var(--text-muted);">
                    <p>You haven't written any posts yet.</p>
                    <a href="${pageContext.request.contextPath}/post?action=new" class="btn btn-primary" style="margin-top: 15px;">Create Your First Post</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="posts-grid">
                    <c:forEach var="post" items="${authoredPosts}">
                        <div class="admin-card">
                            <h3 style="margin-top: 0; font-size: 1.25rem; margin-bottom: 10px;">
                                <a href="${pageContext.request.contextPath}/post?id=${post.postId}" style="color: var(--text-color); text-decoration: none;">${post.title}</a>
                            </h3>
                            <div style="font-size: 0.9rem; color: var(--text-muted); margin-bottom: 15px;">
                                ${fn:substring(post.createdAt, 0, 10)} • 
                                <span class="badge badge-${post.status.toLowerCase()}">${post.status}</span>
                            </div>
                            <div style="display: flex; gap: 15px; color: var(--text-muted); font-size: 0.9rem;">
                                <span><i class="fas fa-eye"></i> ${post.viewCount}</span>
                                <span><i class="fas fa-heart"></i> ${post.likeCount}</span>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    
    <!-- Tab: Liked Posts -->
    <div id="tab-liked" class="tab-pane">
         <c:choose>
            <c:when test="${empty likedPosts}">
                <div style="text-align: center; padding: 50px; color: var(--text-muted);">
                    <p>You haven't liked any posts yet. Start exploring!</p>
                    <a href="${pageContext.request.contextPath}/home" class="btn btn-outline" style="margin-top: 15px;">Browse Home</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="posts-grid">
                    <c:forEach var="post" items="${likedPosts}">
                        <div class="admin-card">
                            <h3 style="margin-top: 0; font-size: 1.25rem; margin-bottom: 10px;">
                                <a href="${pageContext.request.contextPath}/post?id=${post.postId}" style="color: var(--text-color); text-decoration: none;">${post.title}</a>
                            </h3>
                            <div style="font-size: 0.9rem; color: var(--text-muted); margin-bottom: 15px;">
                                By @${post.author.username} • ${fn:substring(post.createdAt, 0, 10)}
                            </div>
                             <div style="display: flex; gap: 15px; color: var(--text-muted); font-size: 0.9rem;">
                                <span><i class="fas fa-heart" style="color: var(--danger-color)"></i> Liked</span>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

</div>

<!-- Edit Profile Modal -->
<div class="modal-overlay" id="editModal">
    <div class="edit-profile-modal">
        <div class="modal-header">
            <h2>Edit Profile</h2>
            <button class="close-btn" onclick="toggleModal(false)">&times;</button>
        </div>
        
        <form action="${pageContext.request.contextPath}/profile" method="post" enctype="multipart/form-data">
            <input type="hidden" name="action" value="updateProfile">
            
            <div class="form-group">
                <label for="profilePicture">Profile Picture Avatar (Max 5MB)</label>
                <input type="file" id="profilePicture" name="profilePicture" class="form-control" accept="image/png, image/jpeg, image/gif">
            </div>
            
            <div class="form-group">
                <label for="fullName">Full Name</label>
                <input type="text" id="fullName" name="fullName" class="form-control" value="${sessionScope.user.fullName}">
            </div>
            
            <div class="form-group">
                <label for="bio">About Me (Bio)</label>
                <textarea id="bio" name="bio" class="form-control" rows="4" placeholder="Tell the community about yourself...">${sessionScope.user.bio}</textarea>
            </div>
            
            <div class="form-group" style="margin-top: 30px; display: flex; gap: 10px; justify-content: flex-end;">
                <button type="button" class="btn btn-outline" onclick="toggleModal(false)">Cancel</button>
                <button type="submit" class="btn btn-primary">Save Changes</button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="../includes/footer.jsp" />

<script>
    function switchTab(tabId) {
        // Hide all tabs
        document.querySelectorAll('.tab-pane').forEach(el => el.classList.remove('active'));
        document.querySelectorAll('.tab-btn').forEach(el => el.classList.remove('active'));
        
        // Show selected tab
        document.getElementById('tab-' + tabId).classList.add('active');
        event.currentTarget.classList.add('active');
    }
    
    function toggleModal(show) {
        const modal = document.getElementById('editModal');
        if (show) {
            modal.classList.add('active');
            document.body.style.overflow = 'hidden';
        } else {
            modal.classList.remove('active');
            document.body.style.overflow = '';
        }
    }
    
    // Close modal if user clicks outside of it
    document.getElementById('editModal').addEventListener('click', function(e) {
        if (e.target === this) {
            toggleModal(false);
        }
    });
</script>
</body>
</html>
