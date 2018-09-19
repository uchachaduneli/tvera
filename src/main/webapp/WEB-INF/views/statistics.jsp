<%--
  Created by IntelliJ IDEA.
  User: ME
  Date: 10/23/2017
  Time: 3:38 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>

<style>
  #chartdiv {
    width: 100%;
    height: 350px;
    font-size: 11px;
  }
</style>

<!-- Resources -->
<script src="resources/js/amcharts.js"></script>
<script src="resources/js/serial.js"></script>
<script src="resources/js/light.js"></script>

<script>
  app.controller("angController", function ($scope, $http, $filter) {
    $scope.srchCase = {};

    $('#loadingModal').modal('show');

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
      $('#loadingModal').modal('hide');
    }

    ajaxCall($http, "misc/get-districts", null, getDistricts);
  });

</script>

<div class="col-md-12">
  <div id="filter-panel" class="filter-panel">
    <div class="panel panel-default">
      <div class="panel-body">
        <div class="form-group col-md-2">
          <select class="form-control" ng-model="srchCase.districtId"
                  ng-change="loadMainData()">
            <option value="" selected="selected">უბანი</option>
            <option ng-repeat="v in districts" ng-selected="v.id === srchCase.districtId"
                    value="{{v.id}}">{{v.name +' ('+ v.incasator.name +' '+ v.incasator.lastname +')'}}
            </option>
          </select>
        </div>
        <div class="form-group col-md-2">
          <select class="form-control" ng-model="srchCase.streetId"
                  ng-change="loadMainData()">
            <option value="" selected="selected">ქუჩა</option>
            <option ng-repeat="v in streets" ng-selected="v.id === srchCase.streetId"
                    value="{{v.id}}">{{v.name}}
            </option>
          </select>
        </div>
        <div class="form-group col-md-2">
          <select class="form-control" ng-model="srchCase.incasatorId"
                  ng-change="loadMainData()">
            <option value="" selected="selected">ინკასატორი</option>
            <option ng-repeat="v in incasators" ng-selected="v.id === srchCase.incasatorId"
                    value="{{v.id}}">{{v.name +' '+ v.lastname}}
            </option>
          </select>
        </div>
        <div class="form-group col-md-4">
          <div class="input-group">
            <div class="input-append">
              <input type="text" class="form-control srch dateInput"
                     placeholder="დან" ng-model="srchCase.billDateFrom">
            </div>
            <span class="input-group-addon">გადახდის თარიღი</span>
            <div class="input-append">
              <input type="text" class="form-control srch dateInput"
                     placeholder="მდე" ng-model="srchCase.billDateTo">
            </div>
          </div>
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
<div class="box-body row" style="padding: 1% !important;">

  <div class="col col-sm-12">
    <div class="small-box bg-aqua">
      <div class="inner">
        <h3>{{sum}}₾ </h3>
      </div>
      <div class="icon">
        <i class="ion ion-stats-bars"></i>
      </div>
    </div>
  </div>
</div>
<div id="chartdiv"></div>

<%@include file="footer.jsp" %>