<%@ include file="/WEB-INF/includes/header.jsp" %>

<h2>Blog Posts</h2>

<c:forEach var="post" items="${posts}">
    <div class="card post-card">
        <h2><a href="${pageContext.request.contextPath}/post/${post.postId}" style="color: #2c3e50; text-decoration: none;">${post.title}</a></h2>
        <div class="post-meta">
            <span>By ${post.author.fullName}</span> | 
            <span>${post.publishedAt}</span>
            <c:if test="${post.category != null}">
                | <span>${post.category.categoryName}</span>
            </c:if>
        </div>
        <div class="post-content">
            ${post.content.length() > 200 ? post.content.substring(0, 200).concat('...') : post.content}
        </div>
        <a href="${pageContext.request.contextPath}/post/${post.postId}" class="btn">Read More</a>
    </div>
</c:forEach>

<c:if test="${empty posts}">
    <div class="card">
        <p>No published posts yet.</p>
    </div>
</c:if>

<%@ include file="/WEB-INF/includes/footer.jsp" %>
