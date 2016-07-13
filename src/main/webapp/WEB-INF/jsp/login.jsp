<%--
  Created by IntelliJ IDEA.
  User: lwy
  Date: 2016/7/13
  Time: 13:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Login</title>
    <%@include file="common/bootsrtrap-head.jsp" %>
    <script src="/res/js/angular.min.js"></script>
    <style>
    </style>
</head>
<body ng-app="myApp">
<div id="loginbox" ng-controller="AppController as ctrl">
    <form id="loginform" class="form-vertical" name="myForm">
        <div class="control-group normal_text"><h3><img src="/res/img/logo.png" alt="Logo"/></h3></div>
        <div class="form-group" ng-class="{'has-danger':myForm.username.$invalid}">
            <div class="main_input_box">
                <span class="add-on bg_lg"><i class="icon-user"></i></span>
                <input  ng-model="username"  type="text" class="form-control" name="username" placeholder="Username" required ng-minlength="6">
            </div>
        </div>
        <div class="form-group">
            <div class="main_input_box">
                <span class="add-on bg_ly"><i class="icon-lock"></i></span><input type="password" class="form-control"
                                                                                  id="password" placeholder="Password">
            </div>
        </div>
        <div class="form-actions">
            <span class="pull-right"><button type="submit" href="index.html" ng-disabled="myForm.$invalid" class="btn btn-success"> Login</button></span>
        </div>
    </form>
</div>
<script>
    angular.module('myApp',[]).controller('AppController',[function ()
    {
        var self = this;
    }]);
</script>
</body>
</html>
