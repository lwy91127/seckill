<%--
  Created by IntelliJ IDEA.
  User: lwy
  Date: 2016/7/10
  Time: 14:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="tag.jsp" %>
<div id="sidebar" class="col-md-2">
    <div id="icon">
        <p>PPTV <c:out value="${param.l1}"/></p>
    </div>
    <div>
        <ul id="user-nav" class="nav nav-tabs nav-stacked">
            <li>
                <a href="#systemSetting" class="nav-header collapsed" data-toggle="collapse">
                    用户管理
                </a>
                <ul id="systemSetting" class="nav nav-list collapse <c:if test="${ param.l1 eq 'user'}">in</c:if>"
                    style="height: 0px;">
                    <li><a href="#" class="<c:if test="${param.l2 eq 'userquery'}">active</c:if>">用户查询</a>
                    <li><a href="#">成长值</a>
                </ul>
            </li>
            <li>
                <a href="#taskSetting" class="nav-header collapsed" data-toggle="collapse">
                    任务管理
                </a>
                <ul id="taskSetting" class="nav nav-list collapse <c:if test="${ param.l1 eq 'task'}">in</c:if>"
                    style="height: 0px;">
                    <li><a href="#" class="<c:if test="${param.l2 eq 'tasked'}">active</c:if>">任务列表</a>
                    <li><a href="#">任务管理</a>
                </ul>
            </li>
            </li>
        </ul>

    </div>
</div>