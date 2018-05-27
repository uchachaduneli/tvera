<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  //    boolean isAdmin = ((Integer) session.getAttribute("typeId") != null && (Integer) session.getAttribute("typeId") == UserDTO.USER_ADMIN);
  boolean isAdmin = true;
%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <base href="${pageContext.request.contextPath}/"/>
  <title>TV Era</title>
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <link rel="stylesheet" href="resources/css/bootstrap.css">
  <link rel="stylesheet" href="resources/css/font-awesome.css">
  <link rel="stylesheet" href="resources/css/AdminLTE.css">
  <link rel="stylesheet" href="resources/css/dataTables.bootstrap.css">
  <link rel="stylesheet" href="resources/css/skin-blue-light.css">
  <link rel="stylesheet" href="resources/css/global.css">
  <link rel="stylesheet" href="resources/css/bootstrap-select.css">
  <link rel="stylesheet" href="resources/css/bootstrap-datepicker.css">
  <link rel="stylesheet" href="resources/css/bootstrap-datetimepicker.css">
  <link rel="stylesheet" href="resources/css/ionicons.min.css">
  <link rel="shortcut icon" type="image/png" href="resources/imgs/logo.png"/>

  <script src="resources/js/jquery.js"></script>
  <script src="resources/js/jquery-ui.js"></script>
  <script src="resources/js/bootstrap-select.js"></script>
  <script>
    $.widget.bridge('uibutton', $.ui.button);
  </script>
  <script src="resources/js/moment.js"></script>
  <script src="resources/js/bootstrap.js"></script>
  <script src="resources/js/bootstrap-datepicker.js"></script>
  <script src="resources/js/bootstrap-datetimepicker.js"></script>
  <script src="resources/js/jquery.bootstrap-growl.min.js"></script>
  <script src="resources/js/adminlte.js"></script>
  <script src="resources/js/angular.js"></script>
  <script src="resources/js/global_util.js"></script>
  <script src="resources/js/growlMessages.js"></script>
  <script src="resources/js/requireds.js"></script>
  <script src="resources/js/checklist-model.js"></script>
  <script>
    $(document).ready(function () {
      $(".datepicker").datepicker({language: 'ka'});
      var url = window.location;
      $('.menuItem').filter(function () {
        return this.href.indexOf(url.pathname) > -1;
      }).addClass('active');
      if (url.pathname.indexOf("requests") > -1) {
        $('#selected_item').text("Requests");
      } else if (url.pathname.indexOf("contacts") > -1) {
        $('#selected_item').text("Contacts Database");
      }

    });

    var app = angular.module("app", ["checklist-model"]);
    app.controller("profileCtrl", function ($scope, $http, $location) {
      var absUrl = $location.absUrl();
      $scope.uri = "";
      if (absUrl.split("?")[1]) {
        $scope.uri = absUrl.split("?")[1].split("=")[1];
      }

      $scope.changePassword = function () {
        function resFunc(res) {
          if (res.errorCode == 0) {
            successMsg('Operation Successfull');
            closeModal('dropdown');
          } else {
            errorMsg('Operation Failed');
          }
          $scope.newpass = {};
        }

        ajaxCall($http, "users/change-password?pass=" + $scope.newpass.password + "&newpass=" + $scope.newpass.newpassword, null, resFunc);
      };
    });
  </script>
</head>
<body ng-app="app" class="hold-transition skin-blue-light sidebar-mini">
<div class="wrapper">
  <header class="main-header">
    <a href="" class="logo">
      <span class="logo-lg"><b>TV ERA</b></span>
    </a>
    <nav class="navbar navbar-static-top">
      <a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
        <span class="sr-only">Collapse Menu</span>
      </a>

      <div class="navbar-custom-menu" ng-controller="profileCtrl">
        <ul class="nav navbar-nav">
          <li class="dropdown user user-menu">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
              <i class="fa fa-user"></i>
              <span class="hidden-xs"><%= session.getAttribute("userDesc") %></span>
            </a>
            <ul class="dropdown-menu">
              <li class="user-header">
                <p>
                  <%= session.getAttribute("userDesc") %>
                  <small><%= session.getAttribute("typeName") %>
                  </small>
                </p>
              </li>
              <li class="user-body text-center">
                <div class=" form-group has-feedback">
                  <input type="password" name="password" placeholder="Current Password"
                         ng-model="newpass.password"
                         class="form-control">
                  <span class="fa fa-key form-control-feedback"></span>
                </div>
                <div class="form-group has-feedback">
                  <input type="password" name="password" placeholder="New Password"
                         ng-model="newpass.newpassword"
                         class="form-control">
                  <span class="fa fa-key form-control-feedback"></span>
                </div>
              </li>
              <li class="user-footer">
                <div class="pull-left">
                  <a href="" ng-click="changePassword()" class="btn btn-default btn-flat">Save</a>
                </div>
                <div class="pull-right">
                  <a href="logout" class="btn btn-default btn-flat">Log Out</a>
                </div>
              </li>
            </ul>
          </li>
          <li title="გამოსვლა">
            <a href="logout"><i class="fa fa-sign-out"></i></a>
          </li>
        </ul>
      </div>
    </nav>
  </header>
  <div class="row" class="main-sidebar">
    <aside class="main-sidebar">
      <section class="sidebar">
        <img src="resources/imgs/logo.png" style="width: 100%; margin-top: 5%;"/>
        <hr/>
        <ul class="sidebar-menu" data-widget="tree">
          <li>
            <a href="packages">
              <i class="fa fa-tasks"></i>
              <span>პაკეტები</span>
            </a>
          </li>
          <li>
            <a href="streets">
              <i class="fa fa-map-marker"></i>
              <span>ქუჩა</span>
            </a>
          </li>
          <li>
            <a href="incasators">
              <i class="fa fa-male"></i>
              <span>ინკასატორი</span>
            </a>
          </li>
          <li>
            <a href="districts">
              <i class="fa fa-building"></i>
              <span>უბანი</span>
            </a>
          </li>
          <c:if test="<%= isAdmin %>">
            <li>
              <a href="users">
                <i class="fa fa-users"></i>
                <span>მომხმარებელი</span>
              </a>
            </li>
            <li>
              <a href="statistics">
                <i class="fa fa-bar-chart"></i>
                <span>სტატისტიკა</span>
              </a>
            </li>
          </c:if>
        </ul>
      </section>
    </aside>
  </div>

  <div class="content-wrapper" ng-controller="angController">
    <section class="content-header text-center">
      <h4 id="selected_item"></h4>
    </section>
    <section class="content">


