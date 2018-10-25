<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Feitai Admin Welcome</title>
    <base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />
    <link href="${ctx}/static/css/main.css" rel="stylesheet" type="text/css" />
</head>
<body>
    <h2>Welcome Feitai Admin</h2>
    <div>JSP test</div>
    <div>${ctx}</div>
    <div>${value}</div>
</body>
</html>