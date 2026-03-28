<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${post != null ? 'Edit Post' : 'Write a Story'} – ThoughtForge</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&family=Playfair+Display:wght@700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=1.1">
</head>
<body>
    <jsp:include page="../includes/header.jsp"/>

    <div class="container" style="padding-top: 40px; padding-bottom: 60px;">
        <div class="page-header" style="border-bottom: none; margin-bottom: 20px;">
            <a href="${pageContext.request.contextPath}/dashboard" style="color: var(--text-muted); text-decoration: none; font-size: 0.9rem; display: inline-flex; align-items: center; gap: 5px;">
                ← Back to Dashboard
            </a>
        </div>

        <div class="post-form-grid">
            <!-- Main Content Area -->
            <div class="card" style="padding: 40px;">
                <h1 style="font-family: 'Playfair Display', serif; font-size: 2.2rem; margin-bottom: 30px; color: #fff;">
                    ${post != null ? 'Edit Your Story' : 'Write a New Story'}
                </h1>

                <form method="post" action="${pageContext.request.contextPath}/post/" id="postForm" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="${post != null ? 'update' : 'create'}">
                    <c:if test="${post != null}">
                        <input type="hidden" name="postId" value="${post.postId}">
                    </c:if>
                    
                    <div class="form-group">
                        <label for="title" style="font-size: 1.1rem; color: #fff;">Story Title</label>
                        <input type="text" id="title" name="title" value="${post != null ? post.title : ''}" 
                               style="font-size: 1.5rem; padding: 16px 20px; font-weight: 600; font-family: 'Playfair Display', serif;" 
                               placeholder="Enter a captivating title..." required>
                    </div>
                    
                    <div class="form-group" style="margin-top: 30px;">
                        <label for="content" style="font-size: 1.1rem; color: #fff;">Story Content</label>
                        <textarea id="content" name="content" required
                                  style="min-height: 400px; font-size: 1.1rem; line-height: 1.8; padding: 20px;"
                                  placeholder="Tell your story... Use markdown or plain text.">${post != null ? post.content : ''}</textarea>
                    </div>
                </form>
            </div>

            <!-- Settings Sidebar -->
            <div class="sidebar">
                <div class="card" style="padding: 24px;">
                    <h3 style="font-size: 1.1rem; color: #fff; margin-bottom: 20px; padding-bottom: 12px; border-bottom: 1px solid var(--border-color);">
                        Post Settings
                    </h3>
                    
                    <div class="form-group">
                        <label for="categoryId">Category</label>
                        <select id="categoryId" name="categoryId" form="postForm" required>
                            <option value="">Select a category...</option>
                            <c:forEach var="category" items="${categories}">
                                <option value="${category.categoryId}" 
                                        ${post != null && post.category.categoryId == category.categoryId ? 'selected' : ''}>
                                    ${category.categoryName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group" style="margin-top: 30px;">
                        <label>Featured Image Upload</label>
                        <c:if test="${not empty post.featuredImage}">
                            <div style="margin-bottom: 10px;">
                                <img src="${pageContext.request.contextPath}/${post.featuredImage}" alt="Current Image" style="width: 100%; border-radius: 8px; border: 1px solid var(--border-color);">
                            </div>
                        </c:if>
                        <input type="file" name="featuredImageFile" form="postForm" accept="image/png, image/jpeg, image/gif"
                               style="background: transparent; border: 1px dashed var(--border-color); padding: 12px; cursor: pointer;">
                        <small style="color: var(--text-muted); display: block; margin-top: 6px; font-size: 0.8rem;">Upload a new image from your PC (Max 10MB).</small>
                    </div>

                    <div style="margin-top: 40px; padding-top: 20px; border-top: 1px solid var(--border-color);">
                        <button type="submit" form="postForm" class="btn" style="width: 100%; margin-bottom: 12px;">
                            ${post != null ? '💾 Save Changes' : '🚀 Publish Story'}
                        </button>
                        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline" style="width: 100%; display: block; text-align: center;">
                            Cancel
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="../includes/footer.jsp"/>
</body>
</html>
