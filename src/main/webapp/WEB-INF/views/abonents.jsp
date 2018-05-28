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


  $(document).ready(function () {

    $('.dateInput').datepicker({
      format: "dd/mm/yyyy",
      autoclose: true,
    })

    $('.srch').keypress(function (e) {
      var key = e.which;
      if (key == 13) {
        $('#srchBtnId').click();
        return false;
      }
    });

  });

  app.controller("angController", function ($scope, $http, $filter) {
    $scope.start = 0;
    $scope.page = 1;
    $scope.limit = "10";
    $scope.request = {};
    $scope.srchCase = {};
//        $scope.request.docs = [];

    $scope.loadMainData = function () {
      function getMainData(res) {
        $scope.list = res.data;
      }

      if ($scope.srchCase.billDate != undefined && $scope.srchCase.billDate.includes('/')) {
        $scope.srchCase.billDate = $scope.srchCase.billDate.split(/\//).reverse().join('-')
      }
      if ($scope.srchCase.billDateTo != undefined && $scope.srchCase.billDateTo.includes('/')) {
        $scope.srchCase.billDateTo = $scope.srchCase.billDateTo.split(/\//).reverse().join('-')
      }

      ajaxCall($http, "abonent/get-abonents?start=" + $scope.start + "&limit=" + $scope.limit, angular.toJson($scope.srchCase), getMainData);
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

          ajaxCall($http, "abonent/delete-abonent?id=" + id, null, resFnc);
        }
      }
    };

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
        console.log($scope.slcted);
      }
    };

    $scope.handleDoubleClick = function (id) {
      $scope.showDetails(id);
      $('#detailModal').modal('show');
    };

    $scope.init = function () {
      $scope.request = {};
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

      if ($scope.request.billDate != undefined && $scope.request.billDate.includes('/')) {
        $scope.request.billDate = $scope.request.billDate.split(/\//).reverse().join('-')
      }

      $scope.req = {};

      $scope.req.id = $scope.request.id;
      $scope.req.name = $scope.request.name;
      $scope.req.lastname = $scope.request.lastname;
      $scope.req.abonentNumber = $scope.request.abonentNumber;
      $scope.req.personalNumber = $scope.request.personalNumber;
      $scope.req.deviceNumber = $scope.request.deviceNumber;
      $scope.req.comment = $scope.request.comment;
      $scope.req.billDate = $scope.request.billDate;
      $scope.req.streetId = $scope.request.streetId;

      console.log(angular.toJson($scope.req));
      ajaxCall($http, "abonent/save-abonent", angular.toJson($scope.req), resFunc);
    };


    $scope.rowNumbersChange = function () {
      $scope.start = 0;
      $scope.loadMainData();
    }

    $scope.handlePage = function (h) {
      if (parseInt(h) >= 0) {
        $scope.page += 1;
        $scope.start = $scope.page * $scope.limit;
      } else {
        $scope.page -= 1;
        $scope.start = ($scope.page * $scope.limit) < 0 ? 0 : ($scope.page * $scope.limit);
      }
      $scope.loadMainData();
    }

    function getStreets(res) {
      $scope.streets = res.data;
    }

    ajaxCall($http, "street/get-streets", null, getStreets);

    function getIncasators(res) {
      $scope.incasators = res.data;
    }

    ajaxCall($http, "misc/get-incasators", null, getIncasators);

    function getDistricts(res) {
      $scope.districts = res.data;
    }

    ajaxCall($http, "misc/get-districts", null, getDistricts);
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
              <th class="text-right">სახელი</th>
              <td>{{slcted.name}}</td>
            </tr>
            <tr>
              <th class="text-right">გვარი</th>
              <td>{{slcted.lastname}}</td>
            </tr>
            <tr>
              <th class="text-right">აბონენტის N</th>
              <td>{{slcted.abonentNumber}}</td>
            </tr>
            <tr>
              <th class="text-right">პირადი N</th>
              <td>{{slcted.personalNumber}}</td>
            </tr>
            <tr>
              <th class="text-right">მოწყობილობის N</th>
              <td>{{slcted.deviceNumber}}</td>
            </tr>
            <tr>
              <th class="text-right">გადასახადი</th>
              <td>{{slcted.bill}}</td>
            </tr>
            <tr>
              <th class="text-right">ბალანსი</th>
              <td>{{slcted.balance}}</td>
            </tr>
            <tr>
              <th class="text-right">გადახდის თარიღი</th>
              <td>{{slcted.billDate}}</td>
            </tr>
            <tr>
              <th class="text-right">უბანი</th>
              <td>{{slcted.street.district.name}}</td>
            </tr>
            <tr>
              <th class="text-right">ქუჩა</th>
              <td>{{slcted.street.name}}</td>
            </tr>
            <tr>
              <th class="text-right">ინკასატორი</th>
              <td>{{slcted.street.incasator.name}}&nbsp;{{slcted.street.incasator.lastname}}</td>
            </tr>
            <tr>
              <th class="text-right">რეგისტრაცია</th>
              <td>{{slcted.createDate}}</td>
            </tr>
            <tr>
              <th class="text-right">შენიშვნა</th>
              <td>{{slcted.comment}}</td>
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
              <label class="control-label col-sm-3">სახელი</label>
              <div class="col-sm-9">
                <input type="text" ng-model="request.name" name="name" required
                       class="form-control input-sm"/>
              </div>
            </div>
            <div class="form-group col-sm-10 ">
              <label class="control-label col-sm-3">გვარი</label>
              <div class="col-sm-9">
                <input type="text" ng-model="request.lastname" name="name" required
                       class="form-control input-sm"/>
              </div>
            </div>
            <div class="form-group col-sm-10 ">
              <label class="control-label col-sm-3">აბონენტის N</label>
              <div class="col-sm-9">
                <input type="text" ng-model="request.abonentNumber" name="name" required
                       class="form-control input-sm"/>
              </div>
            </div>
            <div class="form-group col-sm-10 ">
              <label class="control-label col-sm-3">პირადი N</label>
              <div class="col-sm-9">
                <input type="text" ng-model="request.personalNumber" name="name" required
                       class="form-control input-sm"/>
              </div>
            </div>
            <div class="form-group col-sm-10 ">
              <label class="control-label col-sm-3">მოწყობილობის N</label>
              <div class="col-sm-9">
                <input type="text" ng-model="request.deviceNumber" name="name" required
                       class="form-control input-sm"/>
              </div>
            </div>
            <div class="form-group col-sm-10 ">
              <label class="control-label col-sm-3">ქუჩა</label>
              <div class="col-sm-9">
                <select class="form-control" ng-model="request.streetId">
                  <option ng-repeat="s in streets" ng-selected="s.id === request.streetId" ng-value="s.id">
                    {{s.name}}
                  </option>
                </select>
              </div>
            </div>
            <div class="form-group col-sm-10 ">
              <label class="control-label col-sm-3">გადახდის თარიღი</label>
              <div class="col-sm-9">
                <input type="text" ng-model="request.billDate" required
                       class="form-control input-sm dateInput">
              </div>
            </div>
            <div class="form-group col-sm-10 ">
              <label class="control-label col-sm-3">შენიშვნა</label>
              <div class="col-sm-9">
                <textarea cols="5" rows="5" type="text" ng-model="request.comment" required
                          class="form-control input-sm"> </textarea>
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
          <%--<c:if test="<%= isAdmin %>">--%>
          <button type="button" class="btn btn-block btn-primary btn-md" ng-click="init()"
                  data-toggle="modal" data-target="#editModal">
            <i class="fa fa-plus" aria-hidden="true"></i> &nbsp;
            დამატება
          </button>
          <%--</c:if>--%>
        </div>
        <div class="col-md-2 col-xs-offset-8">
          <select ng-change="rowNumbersChange()" class="pull-right form-control" ng-model="limit"
                  id="rowCountSelectId">
            <option value="10" selected>მაჩვენე 10</option>
            <option value="15">15</option>
            <option value="30">30</option>
            <option value="50">50</option>
            <option value="100">100</option>
          </select>
        </div>
        <div class="row">
          <hr class="col-md-12"/>
        </div>
        <div class="col-md-12">
          <div id="filter-panel" class="filter-panel">
            <div class="panel panel-default">
              <div class="panel-body">
                <div class="form-group col-md-2">
                  <input type="text" class="form-control srch" ng-model="srchCase.id"
                         placeholder="ID">
                </div>
                <div class="form-group col-md-2">
                  <input type="text" class="form-control srch" ng-model="srchCase.abonentNumber"
                         placeholder="აბონენტის N">
                </div>
                <div class="form-group col-md-2">
                  <input type="text" class="form-control srch" ng-model="srchCase.personalNumber"
                         placeholder="პირადი N">
                </div>
                <div class="form-group col-md-2">
                  <input type="text" class="form-control srch" ng-model="srchCase.deviceNumber"
                         placeholder="მოწყობილობის N">
                </div>
                <div class="form-group col-md-2">
                  <input type="text" class="form-control srch" ng-model="srchCase.name"
                         placeholder="სახელი">
                </div>
                <div class="form-group col-md-2">
                  <input type="text" class="form-control srch" ng-model="srchCase.lastname"
                         placeholder="გვარი">
                </div>
                <div class="form-group col-md-2">
                  <select class="form-control" ng-model="srchCase.districtId"
                          ng-change="loadMainData()">
                    <option value="" selected="selected">უბანი</option>
                    <option ng-repeat="v in districts" ng-selected="v.id === srchCase.districtId"
                            value="{{v.id}}">{{v.name}}
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
                  <select class="form-control" ng-model="srchCase.streetId"
                          ng-change="loadMainData()">
                    <option value="" selected="selected">ინკასატორი</option>
                    <option ng-repeat="v in incasators" ng-selected="v.id === srchCase.incasatorId"
                            value="{{v.id}}">{{v.name}}
                    </option>
                  </select>
                </div>
                <div class="form-group col-md-4">
                  <div class="input-group">
                    <div class="input-append">
                      <input type="text" class="form-control srch dateInput"
                             placeholder="დან" ng-model="srchCase.billDate">
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
        <div class="box-body">
          <table class="table table-bordered table-hover">
            <thead>
            <tr>
              <th>ID</th>
              <th>აბონ. N</th>
              <th>პირადი N</th>
              <th>სახელი</th>
              <th>გვარი</th>
              <th>ქუჩა</th>
              <th>ინკასატორი</th>
              <th>ბალანსი</th>
              <th class="col-md-2 text-center">Action</th>
            </tr>
            </thead>
            <tbody title="Double Click For Detailed Information">
            <tr ng-repeat="r in list" ng-dblclick="handleDoubleClick(r.id)">
              <td>{{r.id}}</td>
              <td>{{r.abonentNumber}}</td>
              <td>{{r.personalNumber}}</td>
              <td>{{r.name}}</td>
              <td>{{r.lastname}}</td>
              <td>{{r.street.name}}</td>
              <td>{{r.street.incasator.name}}</td>
              <td>{{r.balance}}</td>
              <td class="text-center">
                <a ng-click="showDetails(r.id)" data-toggle="modal" title="Details"
                   data-target="#detailModal" class="btn btn-xs">
                  <i class="fa fa-sticky-note-o"></i>&nbsp; დეტალები
                </a>&nbsp;&nbsp;
                <%--<c:if test="<%= isAdmin %>">--%>
                <a ng-click="edit(r.id)" data-toggle="modal" data-target="#editModal"
                   class="btn btn-xs">
                  <i class="fa fa-pencil"></i>&nbsp;რედაქტ.
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