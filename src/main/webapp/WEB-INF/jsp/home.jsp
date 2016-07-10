<%--
  Created by IntelliJ IDEA.
  User: lwy
  Date: 2016/7/10
  Time: 13:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="common/tag.jsp" %>
<html>
<head>
    <title>Title</title>
    <%@include file="common/bootsrtrap-head.jsp" %>
</head>
<body>
<div class="container-fluid">
    <div class="row-fluid">
        <c:import url="common/left-menu.jsp" charEncoding="utf-8">
            <c:param name="l1" value="user"/>
            <c:param name="l2" value="userquery"/>
        </c:import>
        <div class="col-md-10">
            <ul class="breadcrumb" style="margin-bottom: 10px;">
                <li class="active"><a href="/home">VGS后台</a> <span
                        class="divider">/ </span></li>
                <li>用户查询</li>
            </ul>
        </div>
    </div>
</div>
</body>
</html>
