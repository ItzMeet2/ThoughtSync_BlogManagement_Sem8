import os
import glob
import re

views_dir = r"e:\Sem_8\Project-8\BlogManagement\src\main\webapp\WEB-INF\views"
files_to_skip = ["login.xhtml", "register.xhtml", "blog-list.xhtml"]

wrapper_top = """<ui:composition template="/WEB-INF/includes/layout.xhtml"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:ui="jakarta.faces.facelets"
    xmlns:c="jakarta.tags.core"
    xmlns:f="jakarta.faces.core"
    xmlns:h="jakarta.faces.html"
    xmlns:fn="jakarta.tags.functions">
<ui:define name="content">
"""

wrapper_bottom = """
</ui:define>
</ui:composition>
"""

xhtml_files = glob.glob(os.path.join(views_dir, "*.xhtml"))

for filepath in xhtml_files:
    filename = os.path.basename(filepath)
    if filename in files_to_skip:
        continue
    
    with open(filepath, "r", encoding="utf-8") as f:
        content = f.read()

    # Remove JSP page directives
    content = re.sub(r'<%@\s*page[^>]*%>\s*', '', content)
    # Remove JSP taglib directives
    content = re.sub(r'<%@\s*taglib[^>]*%>\s*', '', content)
    
    # Check if header is included via any JSP include tag
    if re.search(r'<(jsp:|%@\s*)include\s+(page|file)="[^"]*header\.(jsp|xhtml)"\s*/?>', content) or '<ui:composition' in content:
        # replace header include
        content = re.sub(r'<(jsp:|%@\s*)include\s+(page|file)="[^"]*header\.(jsp|xhtml)"\s*/?>\s*', wrapper_top, content)
        # replace footer include
        content = re.sub(r'<(jsp:|%@\s*)include\s+(page|file)="[^"]*footer\.(jsp|xhtml)"\s*/?>\s*', wrapper_bottom, content)
        
        # Write back
        with open(filepath, "w", encoding="utf-8") as f:
            f.write(content)
        print(f"Processed {filename}")
