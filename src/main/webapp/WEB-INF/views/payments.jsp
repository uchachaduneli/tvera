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
    $scope.founded = {};
    $scope.abonent = {};
//        $scope.request.docs = [];

    $scope.loadMainData = function () {
      $('#loadingModal').modal('show');

      function getMainData(res) {
        $scope.list = res.data.list;
        $scope.rowCount = res.data.size;
        $scope.total = res.data.total;
        $('#loadingModal').modal('hide');
      }

      if ($scope.srchCase.createDateFrom != undefined && $scope.srchCase.createDateFrom.includes('/')) {
        $scope.srchCase.createDateFrom = $scope.srchCase.createDateFrom.split(/\//).reverse().join('-')
      }
      if ($scope.srchCase.createDateTo != undefined && $scope.srchCase.createDateTo.includes('/')) {
        $scope.srchCase.createDateTo = $scope.srchCase.createDateTo.split(/\//).reverse().join('-')
      }

      ajaxCall($http, "payment/get-payments?start=" + $scope.start + "&limit=" + $scope.limit, angular.toJson($scope.srchCase), getMainData);
    }

    $scope.loadMainData();

    function getIncasators(res) {
      $scope.incasators = res.data.list;
    }

    ajaxCall($http, "misc/get-incasators?start=0&limit=99999", {}, getIncasators);

    function getDistricts(res) {
      $scope.districts = res.data;
    }

    ajaxCall($http, "misc/get-districts", null, getDistricts);

    $scope.remove = function (id) {
      if (confirm("Pleace confirm operation?")) {
        if (id != undefined) {
          function resFnc(res) {
            if (res.errorCode == 0) {
              successMsg('Operation Successfull');
              $scope.loadMainData();
            }
          }

          ajaxCall($http, "payment/delete-payment?id=" + id, null, resFnc);
        }
      }
    };

    function getStreets(res) {
      $scope.streets = res.data;
    }

    ajaxCall($http, "street/get-all-streets", null, getStreets);

    $scope.edit = function (id) {
      if (id != undefined) {
        var selected = $filter('filter')($scope.list, {id: id}, true);
        $scope.slcted = selected[0];
        $scope.request = selected[0];
        $scope.request.streetId = selected[0].street.id;
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
      $scope.request = {};
      $scope.founded = {};
      $scope.abonent = {};
    };

    $scope.save = function () {

      if ($scope.founded == undefined || $scope.founded.id == undefined) {
        errorMsg('აბონენტის ID ვერ მოიძებნა');
      }
      $('#loadingModal').modal('show');

      function resFunc(res) {
        if (res.errorCode == 0) {
          successMsg('Operation Successfull');
          $scope.loadMainData();
          $('#loadingModal').modal('hide');
          closeModal('editModal');
        } else {
          errorMsg('Operation Failed');
        }
      }

      if ($scope.request.payDate != undefined && $scope.request.payDate.includes('/')) {
        $scope.request.payDate = $scope.request.payDate.split(/\//).reverse().join('-')
      }

      $scope.req = {};

      //      $scope.req.id = $scope.request.id;
      $scope.req.amount = $scope.request.amount;
      $scope.req.checkNumber = $scope.request.checkNumber;
      $scope.req.abonentId = $scope.founded.id;
      $scope.req.payDate = $scope.request.payDate;

      console.log(angular.toJson($scope.req));
      ajaxCall($http, "payment/save-payment", angular.toJson($scope.req), resFunc);
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

    $scope.loadAbonent = function () {

      if ($scope.abonent.streetId > 0 && ($scope.abonent.roomNumber == '' || $scope.abonent.roomNumber == undefined)) {
        errorMsg('ბინის ნომრის მითითება აუცილებელია');
        return;
      }

      function getAbonentData(res) {
        $('#detailModal').modal('hide');
        if (res.data.list.length > 1) {
          errorMsg('ნაპოვნია ერთზე მეტი ჩანაწერი!!! გთხოვთ მიუთითოთ უნიკალური მახასიათებელი');
          return;
        } else if (res.data.list == undefined || res.data.list.length == 0) {
          errorMsg('მითითებული მონაცემით აბონენტი ვერ მოიძებნა');
        }
        $scope.founded = res.data.list[0];
      }

      $('#detailModal').modal('show');
      ajaxCall($http, "abonent/get-abonents?start=0&limit=99999", angular.toJson($scope.abonent), getAbonentData);
    }
  })
  ;
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
              <th class="text-right">აბონენტი</th>
              <td>{{slcted.abonent.name}} &nbsp; {{slcted.abonent.lastname}}</td>
            </tr>
            <tr>
              <th class="text-right">აბონენტის N</th>
              <td>{{slcted.abonent.id}}</td>
            </tr>
            <tr>
              <th class="text-right">პირადი N</th>
              <td>{{slcted.abonent.personalNumber}}</td>
            </tr>
            <tr>
              <th class="text-right">თანხა</th>
              <td>{{slcted.amount}}</td>
            </tr>
            <tr>
              <th class="text-right">ქვითრის N</th>
              <td>{{slcted.checkNumber}}</td>
            </tr>
            <tr>
              <th class="text-right">გადახდის თარიღი</th>
              <td>{{slcted.payDate}}</td>
            </tr>
            <tr>
              <th class="text-right">რეგისტრ. დრო</th>
              <td>{{slcted.createDate}}</td>
            </tr>
            <tr>
              <th class="text-right">თანამშრომელი</th>
              <td>{{slcted.user.userDesc}}</td>
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
            <div class="form-group col-sm-12 ">
              <div class="col-sm-4 col-xs-offset-2">
                <input type="text" placeholder="აბონ. N" ng-model="abonent.id"
                       class="form-control input-sm"/>
              </div>
              <div class="col-sm-1">ან</div>
              <div class="col-sm-4">
                <input type="text" placeholder="პირადი N" ng-model="abonent.personalNumber"
                       class="form-control input-sm"/>
              </div>
              <div class="col-sm-2"></div>
              <div class="col-sm-4 ">
                <select class="form-control" ng-model="abonent.streetId">
                  <option value="" selected="selected">ქუჩა</option>
                  <option ng-repeat="v in streets" ng-selected="v.id === abonent.streetId"
                          value="{{v.id}}">{{v.name}}
                  </option>
                </select>
              </div>
              <div class="col-sm-1">და</div>
              <div class="col-sm-4">
                <input type="text" placeholder="ბინის N" ng-model="abonent.roomNumber"
                       class="form-control input-sm"/>
              </div>
              <div class="col-sm-3"></div>
              <div class="col-sm-12 text-center">
                <a class="btn btn-default col-sm-6 col-xs-offset-4" ng-click="loadAbonent()">
                  <i class="fa fa-search"></i> ძებნა
                </a>
              </div>
            </div>
            <table class="table table-striped">
              <tr>
                <th class="col-md-2 text-right">აბონენტის N</th>
                <td>{{founded.id}}</td>
              </tr>
              <tr>
                <th class="text-right">აბონენტი</th>
                <td>{{founded.name}} &nbsp; {{founded.lastname}}</td>
              </tr>
              <tr>
                <th class="text-right">პირადი N</th>
                <td>{{founded.personalNumber}}</td>
              </tr>
              <tr>
                <th class="text-right">პაკეტი</th>
                <td>{{founded.packageType.name}}</td>
              </tr>
              <tr>
                <th class="text-right">ბალანსი</th>
                <td>{{founded.balance}}</td>
              </tr>
              <tr>
                <th class="text-right">ინკასატორი</th>
                <td>{{founded.district.incasator.name + ' '+ founded.district.incasator.lastname}}</td>
              </tr>
            </table>
            <div class="form-group col-sm-10 ">
              <label class="control-label col-sm-3">თანხა</label>
              <div class="col-sm-9">
                <input type="text" ng-model="request.amount" name="name" ng-disabled="founded.id === undefined"
                       class="form-control input-sm"/>
              </div>
            </div>
            <div class="form-group col-sm-10 ">
              <label class="control-label col-sm-3">ჩეკის N</label>
              <div class="col-sm-9">
                <input type="text" ng-model="request.checkNumber" ng-disabled="founded.id === undefined"
                       class="form-control input-sm"/>
              </div>
            </div>
            <div class="form-group col-sm-10 ">
              <label class="control-label col-sm-3">გადახდის თარიღი</label>
              <div class="col-sm-9">
                <input type="text" ng-model="request.payDate" ng-disabled="founded.id === undefined"
                       class="form-control input-sm dateInput"/>
              </div>
            </div>

            <div class="form-group col-sm-10"></div>
            <div class="form-group col-sm-10"></div>
            <div class="form-group col-sm-12 text-center">
              <a class="btn btn-app" ng-click="save()" ng-disabled="founded.id === undefined">
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
          <%--<c:if test="<%= isAdmin %>">--%>
          <button type="button" class="btn btn-block btn-primary btn-md" ng-click="init()"
                  data-toggle="modal" data-target="#editModal">
            <i class="fa fa-plus" aria-hidden="true"></i> &nbsp;
            დამატება
          </button>
          <%--</c:if>--%>
        </div>
        <div class="col-md-3 col-xs-offset-7">
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
                <div class="form-group col-md-3">
                  <input type="text" class="form-control srch" ng-model="srchCase.id"
                         placeholder="ID">
                </div>
                <%--<div class="form-group col-md-2">--%>
                <%--<input type="text" class="form-control srch" ng-model="srchCase.abonent.id"--%>
                <%--placeholder="აბონენტის ID">--%>
                <%--</div>--%>
                <div class="form-group col-md-3">
                  <input type="text" class="form-control srch" ng-model="srchCase.abonentId"
                         placeholder="აბონენტის N">
                </div>
                <div class="form-group col-md-3">
                  <input type="text" class="form-control srch" ng-model="srchCase.personalNumber"
                         placeholder="პირადი N">
                </div>
                <div class="form-group col-md-3">
                  <input type="text" class="form-control srch" ng-model="srchCase.checkNumber"
                         placeholder="ქვითრის N">
                </div>
                <div class="form-group col-md-3">
                  <input type="text" class="form-control srch" ng-model="srchCase.name"
                         placeholder="სახელი">
                </div>
                <div class="form-group col-md-3">
                  <input type="text" class="form-control srch" ng-model="srchCase.lastname"
                         placeholder="გვარი">
                </div>
                <div class="form-group col-md-4">
                  <div class="input-group">
                    <div class="input-append">
                      <input type="text" class="form-control srch dateInput"
                             placeholder="დან" ng-model="srchCase.createDateFrom">
                    </div>
                    <span class="input-group-addon">გადახდის დრო</span>
                    <div class="input-append">
                      <input type="text" class="form-control srch dateInput"
                             placeholder="მდე" ng-model="srchCase.createDateTo">
                    </div>
                  </div>
                </div>
                <div class="form-group col-md-2">
                  <select class="form-control" ng-model="srchCase.districtId" ng-change="loadMainData()">
                    <option value="" selected="selected">უბანი</option>
                    <option ng-repeat="s in districts" ng-selected="s.id === srchCase.districtId" ng-value="s.id">
                      {{s.name +' ('+s.incasator.name+' '+s.incasator.lastname+')'}}
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
              <th>აბონენტი</th>
              <th>თანხა</th>
              <th>ქვითრის N</th>
              <th>გადახდის თარიღი</th>
              <th class="col-md-2 text-center">Action</th>
            </tr>
            </thead>
            <tbody title="Double Click For Detailed Information">
            <tr ng-repeat="r in list" ng-dblclick="handleDoubleClick(r.id)">
              <td>{{r.id}}</td>
              <td>{{r.abonent.id}}</td>
              <td>{{r.abonent.name}}&nbsp;{{r.abonent.lastname}}</td>
              <td>{{r.amount}}</td>
              <td>{{r.checkNumber}}</td>
              <td>{{r.payDate}}</td>
              <td class="text-center">
                <a ng-click="showDetails(r.id)" data-toggle="modal" title="Details"
                   data-target="#detailModal" class="btn btn-xs">
                  <i class="fa fa-sticky-note-o"></i>&nbsp; დეტალები
                </a>&nbsp;&nbsp;
                <%--<c:if test="<%= isAdmin %>">--%>
                <%--<a ng-click="edit(r.id)" data-toggle="modal" data-target="#editModal"--%>
                <%--class="btn btn-xs">--%>
                <%--<i class="fa fa-pencil"></i>&nbsp;რედაქტირება--%>
                <%--</a>&nbsp;&nbsp;--%>
                <a ng-click="remove(r.id)" class="btn btn-xs">
                  <i class="fa fa-trash-o"></i>&nbsp;წაშლა
                </a>
                <%--</c:if>--%>
              </td>
            </tr>
            </tbody>
            <tfoot>
            <th colspan="3"></th>
            <th>სულ: {{total}}</th>
            <th colspan="3"></th>
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