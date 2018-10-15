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

  app.controller("angController", function ($scope, $http, $filter, $window) {
    $scope.start = 0;
    $scope.page = 1;
    $scope.limit = "10";
    $scope.srchCase = {abonent: {}};

    $scope.loadMainData = function () {
      $('#loadingModal').modal('show');

      function getMainData(res) {
        $scope.list = res.data.list;
        $scope.rowCount = res.data.size;
        $scope.total = res.data.total;
        $('#loadingModal').modal('hide');
      }

      if ($scope.tmpSrchOperDateTo != undefined && $scope.tmpSrchOperDateTo.length > 0) {
        $scope.srchCase.operDateTo = $scope.tmpSrchOperDateTo + '-16';
        $scope.srchCase.operDate = $scope.tmpSrchOperDateTo + '-01';
      } else {
        $scope.srchCase.operDate = undefined;
        $scope.srchCase.operDateTo = undefined;
      }
//      console.log($scope.srchCase);
      ajaxCall($http, "mobthlybills/get-mobthlybills?start=" + $scope.start + "&limit=" + $scope.limit, angular.toJson($scope.srchCase), getMainData);
    }

    $scope.loadMainData();

    $scope.downloadExcell = function () {
      $('#loadingModal').modal('show');

      if ($scope.tmpSrchOperDateTo != undefined && $scope.tmpSrchOperDateTo.length > 0) {
        $scope.srchCase.operDateTo = $scope.tmpSrchOperDateTo + '-16';
        $scope.srchCase.operDate = $scope.tmpSrchOperDateTo + '-01';
      } else {
        $scope.srchCase.operDate = undefined;
        $scope.srchCase.operDateTo = undefined;
      }

      function redirectToFile() {
        $('#loadingModal').modal('hide');
        $window.open("/excel.xls", "_blank");
      }

      ajaxCall($http, "mobthlybills/download-excell", angular.toJson($scope.srchCase), redirectToFile);
    }

    function getStreets(res) {
      $scope.streets = res.data;
    }

    ajaxCall($http, "street/get-all-streets", null, getStreets);

    function getIncasators(res) {
      $scope.incasators = res.data.list;
    }

    ajaxCall($http, "misc/get-incasators?start=0&limit=99999", {}, getIncasators);

    function getDistricts(res) {
      $scope.districts = res.data;
    }

    ajaxCall($http, "misc/get-districts", null, getDistricts);


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

  });
</script>

<div class="row not-printable">
  <div class="col-xs-12">
    <div class="box">
      <div class="box-header">
        <div class="col-md-2">

        </div>
        <div class="col-md-2 col-xs-offset-5 text-right">
          <div class="btn-group">
            <a ng-click="downloadExcell()" title="ექსელში ექსპორტი" class="btn btn-default pull-right">
              <i class="fa fa-file-excel-o"></i>
            </a>
          </div>
        </div>
        <div class="col-md-3">
          <select ng-change="rowNumbersChange()" class="pull-right form-control" ng-model="limit"
                  id="rowCountSelectId">
            <option value="10" selected>მაჩვენე 10</option>
            <option value="15">15</option>
            <option value="30">30</option>
            <option value="50">50</option>
            <option value="100">100</option>
          </select>
          <div class="col-xs-offset-2">
            (ნაპოვნია {{rowCount}} ჩანაწერი) &nbsp;
          </div>
        </div>
        <div class="row">
          <hr class="col-md-12"/>
        </div>
        <div class="col-md-12">
          <div id="filter-panel" class="filter-panel">
            <div class="panel panel-default">
              <div class="panel-body">
                <div class="form-group col-md-2">
                  <input type="text" class="form-control srch" ng-model="srchCase.abonent.id"
                         placeholder="აბ. N">
                </div>
                <div class="form-group col-md-2">
                  <input type="text" class="form-control srch"
                         ng-model="srchCase.abonent.personalNumber"
                         placeholder="პირ. N">
                </div>
                <div class="form-group col-md-2">
                  <input type="text" class="form-control srch" ng-model="srchCase.abonent.name"
                         placeholder="სახელი">
                </div>
                <div class="form-group col-md-3">
                  <input type="text" class="form-control srch" ng-model="srchCase.abonent.lastname"
                         placeholder="გვარი">
                </div>
                <div class="form-group col-md-2">
                  <select class="form-control" ng-model="srchCase.abonent.districtId"
                          ng-change="loadMainData()">
                    <option value="" selected="selected">უბანი</option>
                    <option ng-repeat="s in districts" ng-selected="s.id === srchCase.districtId"
                            ng-value="s.id">
                      {{s.name +' ('+s.incasator.name+' '+s.incasator.lastname+')'}}
                    </option>
                  </select>
                </div>
                <div class="form-group col-md-2">
                  <select class="form-control" ng-model="srchCase.abonent.incasatorId"
                          ng-change="loadMainData()">
                    <option value="" selected="selected">ინკასატორი</option>
                    <option ng-repeat="v in incasators" ng-selected="v.id === srchCase.incasatorId"
                            value="{{v.id}}">{{v.name +' '+ v.lastname}}
                    </option>
                  </select>
                </div>
                <div class="form-group col-md-2">
                  <input type="text" class="form-control srch monthDate"
                         placeholder="ოპერაციის თვე" ng-model="tmpSrchOperDateTo">
                </div>
                <div class="form-group col-md-3">
                  <select class="form-control" ng-model="srchCase.abonent.streetId"
                          ng-change="loadMainData()">
                    <option value="" selected="selected">ქუჩა</option>
                    <option ng-repeat="v in streets" ng-selected="v.id === srchCase.streetId"
                            value="{{v.id}}">{{v.name}}
                    </option>
                  </select>
                </div>
                <div class="form-group col-md-2">
                  <button class="btn btn-default col-md-11" ng-click="loadMainData()" id="srchBtnId">
                    <span class="fa fa-search"></span> &nbsp; &nbsp;ძებნა &nbsp; &nbsp;
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- /.box-header -->
        <div class="box-body">
          <table class="table table-bordered table-hover">
            <thead>
            <tr>
              <th>ID</th>
              <th>აბონ. N</th>
              <th>აბონენტი(ტარიფი)</th>
              <th>უბანი(ინკას.)</th>
              <th>ქუჩა</th>
              <th>თანხა</th>
              <th>ოპ. თარ.</th>
            </tr>
            </thead>
            <tbody title="Double Click For Detailed Information">
            <tr ng-repeat="r in list" ng-dblclick="handleDoubleClick(r.id)">
              <td>{{r.id}}</td>
              <td>{{r.abonent.id}}</td>
              <td>{{r.abonent.name}}&nbsp;{{r.abonent.lastname}} ({{r.abonent.bill}}₾)</td>
              <td>{{r.abonent.district.name}}&nbsp;({{r.abonent.district.incasator.name}}
                {{r.abonent.district.incasator.lastname}})
              </td>
              <td>{{r.abonent.street.name}}&nbsp;N{{r.abonent.streetNumber}}&nbsp;ბინა({{r.abonent.roomNumber}})</td>
              <td>{{r.amount | number: 2}}</td>
              <td>{{r.strOperDate}}</td>
            </tr>
            </tbody>
            <tfoot>
            <th colspan="5"></th>
            <th> {{total | number: 2}}</th>
            <th></th>
            </tfoot>
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
                    <a ng-click="handlePage(1)">»</a>
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