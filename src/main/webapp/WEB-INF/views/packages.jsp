<%--
  Created by IntelliJ IDEA.
  User: ME
  Date: 10/23/2017
  Time: 3:38 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>
<script>

  app.controller("angController", function ($scope, $http, $filter) {
    $scope.start = 0;
    $scope.page = 1;
    $scope.limit = "10";
    $scope.request = {};
    $scope.srchCase = {};
//        $scope.request.docs = [];

    $scope.loadMainData = function () {
        $scope.page = 1;
      $('#loadingModal').modal('show');
      function getMainData(res) {
        $scope.list = res.data;
        $('#loadingModal').modal('hide');
      }

      ajaxCall($http, "package/get-packages?start=" + $scope.start + "&limit=" + $scope.limit, angular.toJson($scope.srchCase), getMainData);
    }

    $scope.loadMainData();

    $scope.remove = function (id) {
      if (confirm("Pleace confirm operation?")) {
        if (id != undefined) {
          function resFnc(res) {
            if (res.errorCode == 0) {
              successMsg('Operation Successfull');
              $scope.loadMainData();
            }
          }

          ajaxCall($http, "package/delete-package?id=" + id, null, resFnc);
        }
      }
    };

    $scope.edit = function (id) {
      if (id != undefined) {
        var selected = $filter('filter')($scope.list, {id: id}, true);
        $scope.slcted = selected[0];
        $scope.request = selected[0];
        $scope.request.typeId = selected[0].type.id;
        $scope.request.groupId = selected[0].group.id;
      }
    }

    $scope.showDetails = function (id) {
      if (id != undefined) {
        var selected = $filter('filter')($scope.list, {id: id}, true);
        $scope.slcted = selected[0];
      }
    };

    $scope.handleDoubleClick = function (id) {
      $scope.showDetails(id);
      $('#detailModal').modal('show');
    };

    $scope.init = function () {
      $scope.request = {groupId: 1};
    };

    $scope.save = function () {

      function resFunc(res) {
        if (res.errorCode == 0) {
          successMsg('Operation Successfull');
          $scope.loadMainData();
          closeModal('editModal');
        } else {
          errorMsg('Operation Failed');
        }
      }

      $scope.req = {};

      $scope.req.id = $scope.request.id;
      $scope.req.name = $scope.request.name;
      $scope.req.personalPrice = $scope.request.personalPrice;
      $scope.req.juridicalPrice = $scope.request.juridicalPrice;
      $scope.req.scheduler = $scope.request.scheduler;
      $scope.req.groupId = $scope.request.groupId;
      $scope.req.typeId = $scope.request.typeId;

      console.log(angular.toJson($scope.req));
      ajaxCall($http, "package/save-package", angular.toJson($scope.req), resFunc);
    };


    $scope.rowNumbersChange = function () {
      $scope.start = 0;
      $scope.loadMainData();
    }

    $scope.handlePage = function (h) {
      if (parseInt(h) >= 0) {
        $scope.start = $scope.page * parseInt($scope.limit);
        $scope.page += 1;
      } else {
        $scope.page -= 1;
        $scope.start = ($scope.page * parseInt($scope.limit)) - parseInt($scope.limit);
      }
      $scope.loadMainData();
    }

    function getGroups(res) {
      $scope.groups = res.data;
    }

    ajaxCall($http, "misc/get-package-groups", null, getGroups);

    function getTypes(res) {
      $scope.types = res.data;
    }

    ajaxCall($http, "misc/get-package-types", null, getTypes);
  });
</script>

<div class="modal fade bs-example-modal-lg" id="detailModal" tabindex="-1" role="dialog"
     aria-labelledby="editModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="detailModalLabel">დეტალები</h4>
      </div>
      <div class="modal-body">
        <div class="row" id="printable">
          <table class="table table-striped">
            <tr>
              <th class="col-md-4 text-right">ID</th>
              <td>{{slcted.id}}</td>
            </tr>
            <tr>
              <th class="text-right">დასახელება</th>
              <td>{{slcted.name}}</td>
            </tr>
            <tr>
              <th class="text-right">იურიდიულ პირზე</th>
              <td>{{slcted.juridicalPrice}}</td>
            </tr>
            <tr>
              <th class="text-right">ფიზიკურ პირზე</th>
              <td>{{slcted.personalPrice}}</td>
            </tr>
            <tr>
              <th class="text-right">ჯგუფი</th>
              <td>{{slcted.type.name}}</td>
            </tr>
            <tr>
              <th class="text-right">მეთოდი</th>
              <td>{{slcted.group.name}}</td>
            </tr>
          </table>
          <div class="form-group"><br/></div>
        </div>
      </div>
    </div>
    <div class="modal-footer">
    </div>
  </div>
</div>

<div class="modal fade bs-example-modal-lg not-printable" id="editModal" role="dialog" aria-labelledby="editModalLabel"
     aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="editModalLabel">შეავსეთ ინფორმაცია</h4>
      </div>
      <div class="modal-body">
        <div class="row">
          <form class="form-horizontal" name="ediFormName">
            <div class="form-group col-sm-10 ">
              <label class="control-label col-sm-3">დასახელება</label>
              <div class="col-sm-9">
                <input type="text" ng-model="request.name" name="name" required
                       class="form-control input-sm"/>
              </div>
            </div>
            <div class="form-group col-sm-10 ">
              <label class="control-label col-sm-3">ღირებულება იურ. პირზე</label>
              <div class="col-sm-9">
                <input type="text" ng-model="request.juridicalPrice" name="name" required
                       class="form-control input-sm"/>
              </div>
            </div>
            <div class="form-group col-sm-10 ">
              <label class="control-label col-sm-3">ღირებულება ფიზ. პირზე</label>
              <div class="col-sm-9">
                <input type="text" ng-model="request.personalPrice" name="name" required
                       class="form-control input-sm"/>
              </div>
            </div>
            <div class="form-group col-sm-10 ">
              <label class="control-label col-sm-3">ჯგუფი</label>
              <div class="col-sm-9">
                <select class="form-control" ng-model="request.typeId">
                  <option ng-repeat="s in types"
                          ng-selected="s.id === request.typeId"
                          ng-value="s.id">{{s.name}}
                  </option>
                </select>
              </div>
            </div>
            <div class="form-group col-sm-10">
              <label class="control-label col-sm-3">მეთოდი</label>
              <div class="col-xs-9 btn-group">
                <div class="radio col-xs-12">
                  <label ng-repeat="s in groups" class="col-xs-6">
                    <input type="radio" ng-model="request.groupId" ng-value="s.id"
                           class="input-sm">{{s.name}}&nbsp;&nbsp;
                  </label>
                </div>
              </div>
            </div>
            <div class="form-group col-sm-10">
              <label class="control-label col-sm-3">გადახდის მეთოდი</label>
              <div class="col-xs-9 btn-group">
                <div class="radio col-xs-12">
                  <label class="col-xs-6">
                    <input type="radio" ng-model="request.scheduler" ng-value="1"
                           class="input-sm">სისტემატიური&nbsp;&nbsp;
                  </label>
                  <label class="col-xs-6">
                    <input type="radio" ng-model="request.scheduler" ng-value="2"
                           class="input-sm">ერთჯერადი&nbsp;&nbsp;
                  </label>
                </div>
              </div>
            </div>
            <div class="form-group col-sm-10"></div>
            <div class="form-group col-sm-10"></div>
            <div class="form-group col-sm-12 text-center">
              <a class="btn btn-app" ng-click="save()">
                <i class="fa fa-save"></i> შენახვა
              </a>
            </div>

          </form>
        </div>
      </div>
    </div>
  </div>
</div>

<div class="row not-printable">
  <div class="col-xs-12">
    <div class="box">
      <div class="box-header">
        <div class="col-md-2">
          <c:if test="<%= isAdmin %>">
            <button type="button" class="btn btn-block btn-primary btn-md" ng-click="init()"
                    data-toggle="modal" data-target="#editModal">
              <i class="fa fa-plus" aria-hidden="true"></i> &nbsp;
              დამატება
            </button>
          </c:if>
        </div>
        <div class="col-md-2 col-xs-offset-8">
        </div>
        <div class="row">
          <hr class="col-md-12"/>
        </div>
        <!-- /.box-header -->
        <div class="box-body">
          <table class="table table-bordered table-hover">
            <thead>
            <tr>
              <th>ID</th>
              <th>დასახელება</th>
              <th>ღირ. იურ. პირზე</th>
              <th>ღირ. ფიზ. პირზე</th>
              <th>ჯგუფი</th>
              <th>მეთოდი</th>
              <th class="col-md-2 text-center">Action</th>
            </tr>
            </thead>
            <tbody title="Double Click For Detailed Information">
            <tr ng-repeat="r in list" ng-dblclick="handleDoubleClick(r.id)">
              <td>{{r.id}}</td>
              <td>{{r.name}}</td>
              <td>{{r.juridicalPrice}}</td>
              <td>{{r.personalPrice}}</td>
              <td>{{r.type.name}}</td>
              <td>{{r.group.name}}</td>
              <td class="text-center">
                <a ng-click="showDetails(r.id)" data-toggle="modal" title="Details"
                   data-target="#detailModal" class="btn btn-xs">
                  <i class="fa fa-sticky-note-o"></i>&nbsp; დეტალები
                </a>&nbsp;&nbsp;
                <%--<c:if test="<%= isAdmin %>">--%>
                <a ng-click="edit(r.id)" data-toggle="modal" data-target="#editModal"
                   class="btn btn-xs">
                  <i class="fa fa-pencil"></i>&nbsp;რედ.
                </a>&nbsp;&nbsp;
                <a ng-click="remove(r.id)" class="btn btn-xs">
                  <i class="fa fa-trash-o"></i>&nbsp;წაშლა
                </a>
                <%--</c:if>--%>
              </td>
            </tr>
            </tbody>
          </table>
          <div class="panel-footer">
            <div class="row">
              <div class="col col-md-12">
                <ul class="pagination pull-right">
                  <li>
                    <a>(გვერდი {{page}}) </a>
                  </li>
                  <li>
                    <a ng-click="handlePage(-1)">«</a>
                  </li>
                  <li>
                    <a ng-click="handlePage(1)" ng>»</a>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
<%@include file="footer.jsp" %>