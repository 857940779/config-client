<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <base href="<%=basePath%>"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!--页面标题-->
    <title>Title</title>
    <!--引入bootstrap样式-->
    <link href="./bootstrap-3.3.7/css/bootstrap.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<div class="container">
    <h1>Welcome, this is SpringMVC Bootstrap WebApp</h1>
</div>

<!--引入jquery脚本-->
<%--<script src="<%=basePath%>bootstrap-3.3.5/bootstrap/js/jquery.js" type="text/javascript"></script>--%>
<script src="./bootstrap-3.3.7/js/jquery-3.3.1.js" type="text/javascript"></script>

<!--引入bootstrap脚本-->
<!--这里这么写是因为index与bootstrap是同级别的路径-->
<script src="./bootstrap-3.3.7/js/bootstrap.js" type="text/javascript"></script>

</body>
</html>