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
    }

    ajaxCall($http, "misc/get-districts", null, getDistricts);

    $scope.loadMainData = function () {
      $('#loadingModal').modal('show');

      function getMainData(res) {
        $scope.activesCount = res.data.activesCount;
        $scope.inactivesCount = res.data.inactivesCount;
        $scope.wasInactivesCount = res.data.wasInactivesCount;// თუ  თარიღების ფილტრი ადევს
        $scope.receivedBankAmount = res.data.receivedBankAmount;
        $scope.receivedCashAmount = res.data.receivedCashAmount;
        $scope.incasatorSumAmount = res.data.incasatorSumAmount;
        $scope.abonentDavalSum = res.data.abonentDavalSum;
        $scope.abonentAvansSum = res.data.abonentAvansSum;
        $scope.abonentAvansCount = res.data.abonentAvansCount;
        $scope.abonentDavalCount = res.data.abonentDavalCount;
        $('#loadingModal').modal('hide');

      }

      if ($scope.srchCase.billDateFrom != undefined && $scope.srchCase.billDateFrom.includes('/')) {
        $scope.srchCase.billDateFrom = $scope.srchCase.billDateFrom.split(/\//).reverse().join('-')
      }
      if ($scope.srchCase.billDateTo != undefined && $scope.srchCase.billDateTo.includes('/')) {
        $scope.srchCase.billDateTo = $scope.srchCase.billDateTo.split(/\//).reverse().join('-')
      }

      ajaxCall($http, "statistics/get-count-data", angular.toJson($scope.srchCase), getMainData);
    }

    $scope.loadMainData();


  });

</script>


<!-- /.box-header -->
<div class="box-body row" style="padding: 1% !important;">

    <div class="row">
        <div class="col-xs-12">
            <div class="box">
                <div class="box-header">
                    <div class="col-xs-12 text-center">
                        <div id="filter-panel" class="filter-panel">
                            <div class="panel panel-default">
                                <div class="panel-body">
                                    <div class="form-group col-md-2">
                                        <select class="form-control" ng-model="srchCase.districtId"
                                                ng-change="loadMainData()">
                                            <option value="" selected="selected">უბანი</option>
                                            <option ng-repeat="v in districts"
                                                    ng-selected="v.id === srchCase.districtId"
                                                    value="{{v.id}}">{{v.name +' ('+ v.incasator.name +' '+
                                                v.incasator.lastname +')'}}
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
                                            <option ng-repeat="v in incasators"
                                                    ng-selected="v.id === srchCase.incasatorId"
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
                                            <span class="input-group-addon">თარიღი</span>
                                            <div class="input-append">
                                                <input type="text" class="form-control srch dateInput"
                                                       placeholder="მდე" ng-model="srchCase.billDateTo">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <button class="btn btn-default col-md-11" ng-click="loadMainData()"
                                                id="srchBtnId">
                                            <span class="fa fa-search"></span> &nbsp; &nbsp;ძებნა &nbsp; &nbsp;
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
                <div class="box-body">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <a class="btn btn-app">
                                <i class="fa fa-bar-chart"></i> სტატისტიკა
                            </a>
                            <table class="table table-striped">
                                <tr>
                                    <th class="col-md-3">აქტიური აბონენტები:</th>
                                    <td>{{activesCount}}</td>
                                </tr>
                                <tr>
                                    <th>არააქტიური აბონენტები:</th>
                                    <td>{{inactivesCount}}</td>
                                </tr>
                                <tr>
                                    <th>დროით შუალედში გათიშული იყო:</th>
                                    <td>{{wasInactivesCount != undefined ? wasInactivesCount :'მიუთითეთ თარიღი'}}
                                    </td>
                                </tr>
                                <tr>
                                    <th>ბანკით დარიცხული თანხა:</th>
                                    <td>{{receivedBankAmount}}
                                    </td>
                                </tr>
                                <tr>
                                    <th>სტანდარტულად დარიცხული თანხა:</th>
                                    <td>{{receivedCashAmount}}
                                    </td>
                                </tr>
                                <tr>
                                    <th>ინკასატორის ამოღებული თანხა:</th>
                                    <td>{{incasatorSumAmount != undefined ? incasatorSumAmount :'მიუთითეთ ინკასატორი'}}
                                    </td>
                                </tr>
                                <tr>
                                    <th>ავანსად შემოსული თანხა:</th>
                                    <td>{{abonentAvansSum}} &nbsp;|&nbsp; {{abonentAvansCount}} აბონენტი
                                    </td>
                                </tr>
                                <tr>
                                    <th>დავალიანება ჯამში:</th>
                                    <td>{{abonentDavalSum}} &nbsp;|&nbsp; {{abonentDavalCount}} აბონენტი
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="footer.jsp" %>