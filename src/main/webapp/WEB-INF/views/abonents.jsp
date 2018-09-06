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
    $scope.request = {};
    $scope.srchCase = {};
    $scope.packages = [];
    $scope.abonentPackages = [];
    $scope.abonentPackagesBeforeSave = [];
    $scope.package = {};

    $scope.loadMainData = function () {
      $('#loadingModal').modal('show');

      function getMainData(res) {
        $scope.list = res.data.list;
        $scope.rowCount = res.data.size;
        $('#loadingModal').modal('hide');
        // console.log($scope.list);
      }

      if ($scope.srchCase.billDateFrom != undefined && $scope.srchCase.billDateFrom.includes('/')) {
        $scope.srchCase.billDateFrom = $scope.srchCase.billDateFrom.split(/\//).reverse().join('-')
      }
      if ($scope.srchCase.billDateTo != undefined && $scope.srchCase.billDateTo.includes('/')) {
        $scope.srchCase.billDateTo = $scope.srchCase.billDateTo.split(/\//).reverse().join('-')
      }

      ajaxCall($http, "abonent/get-abonents?start=" + $scope.start + "&limit=" + $scope.limit, angular.toJson($scope.srchCase), getMainData);
    }

    $scope.loadMainData();

    $scope.downloadExcell = function () {

      if ($scope.srchCase.billDateFrom != undefined && $scope.srchCase.billDateFrom.includes('/')) {
        $scope.srchCase.billDateFrom = $scope.srchCase.billDateFrom.split(/\//).reverse().join('-')
      }
      if ($scope.srchCase.billDateTo != undefined && $scope.srchCase.billDateTo.includes('/')) {
        $scope.srchCase.billDateTo = $scope.srchCase.billDateTo.split(/\//).reverse().join('-')
      }

      function redirectToFile() {
        $window.open("resources/excell/excel.xls", "_blank");
      }

      ajaxCall($http, "abonent/download-excell", angular.toJson($scope.srchCase), redirectToFile);
    }

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

    $scope.changeStatus = function () {
      if ($scope.slcted != undefined) {

        if (confirm("დაადასტურეთ აბონენტის სერვისის " + ($scope.slcted.status.id == 2 ? 'გააქტიურება' : 'გათიშვა'))) {
          function resFnc(res) {
            if (res.errorCode == 0) {
              successMsg('Operation Successfull');
              closeModal('statusDialog');
              $scope.loadMainData();
            }
          }

          if ($scope.disableDate != undefined && $scope.disableDate.includes('/')) {
            $scope.disableDate = $scope.disableDate.split(/\//).reverse().join('-')
          }
          if ($scope.disableDate == undefined || $scope.disableDate.length < 1) {
            errorMsg('თარიღის მითითება სავალდებულოა');
            return;
          }

          ajaxCall($http, "abonent/change-abonent-status?id=" + $scope.slcted.id + "&date=" + $scope.disableDate, null, resFnc);
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
        $scope.loadObjectDetails($scope.slcted.id);

        function getAbPackages(res) {
          $scope.slcted.abonentPackages = res.data;
        }

        ajaxCall($http, "abonent/get-abonent-packages?id=" + $scope.slcted.id, null, getAbPackages);

      }
    };

    $scope.loadObjectDetails = function (id) {
      function getStatuHistory(res) {
        $scope.slcted.statusHistory = res.data;
      }

      ajaxCall($http, "abonent/get-status-history?id=" + id, null, getStatuHistory);
    }

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
      $scope.req.districtId = $scope.request.districtId;
      $scope.req.streetNumber = $scope.request.streetNumber;
      $scope.req.floor = $scope.request.floor;
      $scope.req.roomNumber = $scope.request.roomNumber;
      $scope.req.juridicalOrPhisical = $scope.request.juridicalOrPhisical;

      console.log(angular.toJson($scope.req));
      ajaxCall($http, "abonent/save-abonent", angular.toJson($scope.req), resFunc);
    };


    $scope.rowNumbersChange = function () {
      $scope.start = 0;
      $scope.loadMainData();
    };

    $scope.handlePage = function (h) {
      if (parseInt(h) >= 0) {
        $scope.start = $scope.page * parseInt($scope.limit);
        $scope.page += 1;
      } else {
        $scope.page -= 1;
        $scope.start = ($scope.page * parseInt($scope.limit)) - parseInt($scope.limit);
      }
      $scope.loadMainData();
    };

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

    $scope.getPackages = function () {
      function setPackages(res) {
        $scope.packages = res.data;
      }

      ajaxCall($http, "package/get-packages", null, setPackages);
    };

    $scope.getPackages();

    $scope.saveAbonentPackages = function () {

      function resFunc(res) {
        if (res.errorCode == 0) {
          successMsg('Operation Successfull');
//          $scope.loadMainData();
          closeModal('packages');

        } else {
          errorMsg('Operation Failed');
        }
      }

      $scope.req = {};

      $scope.req.packageTypeId = $scope.request.packageTypeId;
      $scope.req.abonendId = $scope.request.id;
      $scope.req.abonentPackages = $scope.abonentPackages;

      console.log(angular.toJson($scope.req));
      ajaxCall($http, "abonent/save-abonent-packages", angular.toJson($scope.req), resFunc);
    };

    $scope.loadAbonentPackages = function (id) {
//      $scope.getPackages();
      $scope.abonentPackages = [];
      if (id != undefined) {

        var selected = $filter('filter')($scope.list, {id: id}, true);
        $scope.request = selected[0];
        $scope.package.packageTypeId = $scope.request.packageTypeId;

        function getAbonentPackages(res) {
          $scope.abonentPackages = res.data;
          $scope.abonentPackagesBeforeSave = angular.copy($scope.abonentPackages);
          console.log($scope.abonentPackages);
          angular.forEach($scope.abonentPackages, function (v) {
            if (v.group.id == 6 || v.group.id == 7) {
              v.externalPointCount = $scope.request.servicePointsNumber;
              $filter('filter')($scope.packages, {id: v.id}, true)[0].externalPointCount = $scope.request.servicePointsNumber;
            }
          });
        }

        ajaxCall($http, "abonent/get-abonent-packages?id=" + $scope.request.id, null, getAbonentPackages);
      }
    };

    $scope.isChecked = function (value) {
      var idx = $filter('filter')($scope.abonentPackagesBeforeSave, {id: value.id}, true);
      if (idx[0] != undefined) {
        return true;
      } else {
        return false;
      }
    };

    $scope.printTickets = function () {
      if ($scope.srchCase == undefined || $scope.srchCase == null || $scope.srchCase.districtId == undefined || $scope.srchCase.districtId == 0) {
        errorMsg('გთხოვთ ფილტრში მიუთითოთ კონკრეტული უბანი');
      } else {
        var now = new Date();
        var currentDate = now.getDate() + '-' + (now.getMonth() + 1) + '-' + now.getFullYear() + ' ' + now.getHours() + ':' + now.getMinutes();
        $('#dateDivId').text(currentDate);

        var selected = $filter('filter')($scope.districts, {id: parseInt($scope.srchCase.districtId)}, true);
        $scope.receiptDistrict = selected[0];


        function getReceiptData(res) {
          $scope.receiptlist = res.data.list;
          $scope.receiptBalanceSum = 0.0;
          angular.forEach($scope.receiptlist, function (v) {
            $scope.receiptBalanceSum += v.balance;
          });

          $('#receiptModal').modal('show');
          $('#loadingModal').modal('hide');
        }

        if ($scope.srchCase.billDateFrom != undefined && $scope.srchCase.billDateFrom.includes('/')) {
          $scope.srchCase.billDateFrom = $scope.srchCase.billDateFrom.split(/\//).reverse().join('-')
        }
        if ($scope.srchCase.billDateTo != undefined && $scope.srchCase.billDateTo.includes('/')) {
          $scope.srchCase.billDateTo = $scope.srchCase.billDateTo.split(/\//).reverse().join('-')
        }

        ajaxCall($http, "abonent/get-abonents?start=0&limit=999999999", angular.toJson($scope.srchCase), getReceiptData);


      }
    };

    $scope.printHistory = function () {
      if ($scope.srchCase == undefined || $scope.srchCase == null || $scope.srchCase.districtId == undefined || $scope.srchCase.districtId == 0) {
        errorMsg('გთხოვთ ფილტრში მიუთითოთ კონკრეტული უბანი');
      } else {
        var now = new Date();
        var currentDate = now.getDate() + '-' + (now.getMonth() + 1) + '-' + now.getFullYear() + ' ' + now.getHours() + ':' + now.getMinutes();
        $('#dateDivId2').text(currentDate);

        var selected = $filter('filter')($scope.districts, {id: parseInt($scope.srchCase.districtId)}, true);
        $scope.receiptDistrict = selected[0];

        function getHistoryData(res) {
          $scope.historylist = res.data;
          $scope.uniqAbonentsHistory = [];
          angular.forEach($scope.historylist, function (v) {
            if ($scope.uniqAbonentsHistory.indexOf(v.abonent.id) === -1) {
              $scope.uniqAbonentsHistory.push(v.abonent.id);
            }
          });
          console.log($scope.receiptDistrict);
          $('#historyModal').modal('show');
          $('#loadingModal').modal('hide');
        }

        if ($scope.srchCase.billDateFrom != undefined && $scope.srchCase.billDateFrom.includes('/')) {
          $scope.srchCase.billDateFrom = $scope.srchCase.billDateFrom.split(/\//).reverse().join('-')
        }
        if ($scope.srchCase.billDateTo != undefined && $scope.srchCase.billDateTo.includes('/')) {
          $scope.srchCase.billDateTo = $scope.srchCase.billDateTo.split(/\//).reverse().join('-')
        }

        ajaxCall($http, "abonent/get-balance-history", angular.toJson($scope.srchCase), getHistoryData);
      }
    };
  });

  $(document).ready(function () {

    $('#packages').on('hidden.bs.modal', function () {
      // $(this).find('form')[0].reset();
//      window.location.reload();
      // angular.$scope.$apply();
      window.location.reload();
    });
  });

  function print(contentId) {
    var printContents = document.getElementById(contentId).innerHTML;
    var popupWin = window.open('', '_blank', 'width=1200,height=700');
    popupWin.document.open();
    popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" ' +
        'href="/tvera/resources/css/bootstrap.css" />' +
        '<link rel="stylesheet" type="text/css" </head>' +
        '<body onload="window.print()">' + printContents + '</html>');
    popupWin.document.close();
  }
</script>

<div class="modal fade bs-example-modal-lg" id="historyModal" tabindex="-1" role="dialog"
     aria-labelledby="historyModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-xl">
    <div class="modal-content">
      <div class="modal-body">
        <div class="row">
          <div class="col-md-12">
            <a onclick="print('historyDivId')" title="ბეჭდვა" class="btn btn-primary btn-lg pull-right">
              <i class="fa fa-print"></i>
            </a>
          </div>
          <div class="col-md-12" id="historyDivId">
            <table class="table table-striped table-bordered table-hover">
              <thead>
              <tr>
                <th class="text-center" colspan="8">
                  <h2>დავალიანების ისტორია</h2>
                </th>
              </tr>
              <tr>
                <th colspan="4">
                  ამობეჭდვის თარიღი: <span id="dateDivId2"></span>
                </th>
                <th class="text-center" colspan="4"> ინკასატორი {{receiptDistrict.incasator.name + ' ' +
                  receiptDistrict.incasator.lastname}}
                </th>
              </tr>
              <tr>
                <th class="text-center" colspan="8">{{receiptDistrict.name}}</th>
              </tr>
              </thead>
            </table>
            <table ng-repeat="v in uniqAbonentsHistory"
                   class="table table-striped table-bordered table-hover">
              <thead>
              <tr class="text-center">
                <th>#</th>
                <th>აბონ. N</th>
                <th>სახელი, გვარი</th>
                <th>პირ. N</th>
                <th>ქუჩა</th>
                <th>სართული</th>
                <th>ბინა</th>
                <th>ტარიფი</th>
              </tr>
              </thead>
              <tbody>
              <tr ng-repeat="r in historylist | filter:{abonent:{id:v}}:true " ng-if="$index == 0" class="text-center">
                <td></td>
                <td>{{r.abonent.id}}</td>
                <td>{{r.abonent.name}}{{r.abonent.lastname}}</td>
                <td>{{r.abonent.personalNumber}}</td>
                <td>{{r.abonent.street.name + ' #' + r.abonent.streetNumber}}</td>
                <td>{{r.abonent.floor}}</td>
                <td>{{r.abonent.roomNumber}}</td>
                <td>{{r.abonent.bill}}</td>
              </tr>
              <tr>
                <th colspan="2" style="background-color: #30bbbb;">თარიღი</th>
                <th colspan="2" style="background-color: #30bbbb;">დავალიანება</th>
                <th colspan="2" style="background-color: #30bbbb;">აღდგენის დავალ.</th>
                <th colspan="2" style="background-color: #30bbbb;">მონტაჟის დავალ.</th>
              </tr>
              <tr ng-repeat="a in historylist | filter:{abonent:{id:v}}: true" class="text-center">
                <td colspan="2">{{a.createDate}}</td>
                <td colspan="2">{{a.balance}}</td>
                <td colspan="2">{{a.restoreBalance}}</td>
                <td colspan="2">{{a.installationBalance}}</td>
              </tr>
              </tbody>
              <tfoot>
              <tr>
                <td colspan="8">
                  <hr/>
                </td>
              </tr>
              </tfoot>
            </table>
          </div>
          <div class="form-group"><br/></div>
        </div>
      </div>
    </div>
    <div class="modal-footer">
    </div>
  </div>
</div>


<div class="modal fade bs-example-modal-lg" id="receiptModal" tabindex="-1" role="dialog"
     aria-labelledby="receiptModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-xl">
    <div class="modal-content">
      <div class="modal-body">
        <div class="row">
          <div class="col-md-12">
            <a onclick="print('receiptDivId')" title="ბეჭდვა" class="btn btn-primary btn-lg pull-right">
              <i class="fa fa-print"></i>
            </a>
          </div>
          <div class="col-md-12" id="receiptDivId">
            <table class="table table-striped table-bordered table-hover">
              <thead>
              <tr>
                <th class="text-center" colspan="11">
                  <h2>საკონტროლო ფურცელი</h2>
                </th>
              </tr>
              <tr>
                <th class="text-center" colspan="11"> ინკასატორი {{receiptDistrict.incasator.name + ' ' +
                  receiptDistrict.incasator.lastname}}
                </th>
              </tr>
              <tr>
                <th class="text-center" colspan="11">{{receiptDistrict.name}}</th>
              </tr>
              <tr class="text-center">
                <th>#</th>
                <th>აბონ. N</th>
                <th>სახელი, გვარი</th>
                <th>პირადი N</th>
                <th>ქუჩა</th>
                <th>სართული</th>
                <th>ბინა</th>
                <th>ტარიფი</th>
                <th>აღდგენა</th>
                <th>მონტაჟი</th>
                <th>გადასახდელი</th>
              </tr>
              </thead>
              <tbody>
              <tr ng-repeat="r in receiptlist" class="text-center">
                <td>{{$index+1}}</td>
                <td>{{r.id}}</td>
                <td>{{r.name}}, {{r.lastname}}</td>
                <td>{{r.personalNumber}}</td>
                <td>{{r.street.name + ' #' + r.streetNumber}}</td>
                <td>{{r.floor}}</td>
                <td>{{r.roomNumber}}</td>
                <td>{{r.bill}}</td>
                <td>{{r.restoreBill}}</td>
                <td>{{r.installationBill}}</td>
                <td>{{r.balance}}</td>
              </tr>
              <tr class="text-center">
                <th colspan="10">ამობეჭდვის თარიღი: <span id="dateDivId"></span></th>
                <th>სულ: {{receiptBalanceSum}}</th>
              </tr>
              </tbody>
            </table>
          </div>
          <div class="form-group"><br/></div>
        </div>
      </div>
    </div>
    <div class="modal-footer">
    </div>
  </div>
</div>

<div class="modal fade bs-example-modal-lg not-printable" id="packages" role="dialog"
     aria-labelledby="packagesModalLabel"
     aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">აბონენტის პაკეტების მენეჯმენტი</h4>
      </div>
      <div class="modal-body">
        <div class="row">
          <form class="form-horizontal">
            <div class="form-group col-sm-12 ">

              <label class="control-label col-sm-1"></label>
              <div class="col-sm-11 list-group">
                <label ng-repeat="t in packages" class="col-sm-12 list-group-item">
                  <input type="checkbox" id="typechecks{{t.id}}" ng-disabled="isChecked(t)"
                         checklist-model="abonentPackages" checklist-value="t">&nbsp; {{t.type.name}} -
                  {{t.name}}&nbsp;(იურ:{{t.juridicalPrice}}ლ. / ფიზ:{{t.personalPrice}}ლ.)
                  <input type="number" ng-model="t.externalPointCount" class="pull-right text-center"
                         placeholder="რაოდენობა"
                         ng-show="t.group.id == 7 || t.group.id == 6">
                </label>
                <hr class="col-sm-11" style="margin: 0 0 !important;">
              </div>
            </div>
            <div class="form-group col-sm-10"></div>
            <div class="form-group col-sm-10"></div>
            <div class="form-group col-sm-12 text-center">
              <a class="btn btn-app" ng-click="saveAbonentPackages()">
                <i class="fa fa-save"></i> შენახვა
              </a>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

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
              <th class="col-md-4 text-right">აბონენტის N</th>
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
              <th class="text-right">პირადი N</th>
              <td>{{slcted.personalNumber}}</td>
            </tr>
            <tr>
              <th class="text-right">იურიდიული ფორმა</th>
              <td>{{slcted.juridicalOrPhisical == 1 ? 'ფიზიკური პირი': 'იურიდიული პირი'}}</td>
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
              <td>{{slcted.balance*-1}}</td>
            </tr>
            <tr>
              <th class="text-right">გადახდის თარიღი</th>
              <td>{{slcted.billDate}}</td>
            </tr>
            <tr>
              <th class="text-right">უბანი</th>
              <td>{{slcted.district.name}}</td>
            </tr>
            <tr>
              <th class="text-right">ქუჩა</th>
              <td>{{slcted.street.name}}</td>
            </tr>
            <tr>
              <th class="text-right">ქუჩის N</th>
              <td>{{slcted.streetNumber}}</td>
            </tr>
            <tr>
              <th class="text-right">სართული</th>
              <td>{{slcted.floor}}</td>
            </tr>
            <tr>
              <th class="text-right">ბინის N</th>
              <td>{{slcted.roomNumber}}</td>
            </tr>
            <tr>
              <th class="text-right">ინკასატორი</th>
              <td>{{slcted.district.incasator.name}}&nbsp;{{slcted.district.incasator.lastname}}</td>
            </tr>
            <tr>
              <th class="text-right">რეგისტრაცია</th>
              <td>{{slcted.createDate}}</td>
            </tr>
            <tr>
              <th class="text-right">შენიშვნა</th>
              <td>{{slcted.comment}}</td>
            </tr>
            <tr>
              <th class="text-right">ამჟამინდელი სტატუსი</th>
              <td>{{slcted.status.name}}</td>
            </tr>
            <tr>
              <th class="text-right">სტატუსების ისტორია</th>
              <td>
                <ul>
                  <li ng-repeat="k in slcted.statusHistory">
                    {{k.status.name}} - {{k.disableDate}}
                  </li>
                </ul>
              </td>
            </tr>
            <tr>
              <th class="text-right">პაკეტები</th>
              <td>
                <ul>
                  <li ng-repeat="t in slcted.abonentPackages">
                    {{t.type.name}} - {{t.name}}&nbsp;(იურ:{{t.juridicalPrice}}ლ. / ფიზ:{{t.personalPrice}}ლ.)
                    <span ng-show="t.group.id == 7 || t.group.id == 6"> რაოდენობა - {{t.externalPointCount}}</span>
                  </li>
                </ul>
              </td>
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

<div class="modal fade bs-example-modal-lg not-printable" id="statusDialog" role="dialog"
     aria-labelledby="statusDialogLabel"
     aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="statusDialogLabel">სტატუსის ცვლილება</h4>
      </div>
      <div class="modal-body">
        <div class="row">
          <table class="table table-striped">
            <tr>
              <th class="col-md-4 text-right">აბონენტის N</th>
              <td>{{slcted.id}}</td>
            </tr>
            <tr>
              <th class="text-right">სახელი, გვარი</th>
              <td>{{slcted.name}}&nbsp; {{slcted.lastname}}</td>
            </tr>
            <tr>
              <th class="text-right">პირადი N</th>
              <td>{{slcted.personalNumber}}</td>
            </tr>
            <tr>
              <th class="text-right">სტატუსი</th>
              <td>{{slcted.status.name}}</td>
            </tr>
          </table>
          <form class="form-horizontal" name="ediFormName">
            <div class="form-group col-sm-10 ">
              <label class="control-label col-sm-3">ოპერაციის თარიღი</label>
              <div class="col-sm-9">
                <input type="text" ng-model="disableDate" ng-init=""
                       class="form-control input-sm dateInput"/>
              </div>
            </div>
            <div class="form-group col-sm-10"></div>
            <div class="form-group col-sm-10"></div>
            <div class="form-group col-sm-12 text-center">
              <a class="btn btn-app" ng-click="changeStatus()">
                <i class="fa fa-save"></i> შენახვა
              </a>
            </div>

          </form>
        </div>
      </div>
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
              <label class="control-label col-sm-3">პირადი N</label>
              <div class="col-sm-9">
                <input type="text" ng-model="request.personalNumber" name="name" required
                       class="form-control input-sm"/>
              </div>
            </div>
            <div class="form-group col-sm-10">
              <label class="control-label col-sm-3">იურიდიული ფორმა</label>
              <div class="col-xs-9 btn-group">
                <div class="radio col-xs-6">
                  <label><input type="radio" ng-model="request.juridicalOrPhisical" value="1"
                                class="input-sm">ფიზიკური პირი</label>&nbsp;
                  <label><input type="radio" ng-model="request.juridicalOrPhisical" value="2"
                                class="input-sm">იურიდიული პირი</label>
                </div>
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
              <label class="control-label col-sm-3">ქუჩის N</label>
              <div class="col-sm-9">
                <input type="text" ng-model="request.streetNumber" name="name" required
                       class="form-control input-sm"/>
              </div>
            </div>
            <div class="form-group col-sm-10 ">
              <label class="control-label col-sm-3">სართული</label>
              <div class="col-sm-9">
                <input type="text" ng-model="request.floor" name="name" required
                       class="form-control input-sm"/>
              </div>
            </div>
            <div class="form-group col-sm-10 ">
              <label class="control-label col-sm-3">ბინის N</label>
              <div class="col-sm-9">
                <input type="text" ng-model="request.roomNumber" name="name" required
                       class="form-control input-sm"/>
              </div>
            </div>
            <div class="form-group col-sm-10 ">
              <label class="control-label col-sm-3">უბანი</label>
              <div class="col-sm-9">
                <select class="form-control" ng-model="request.districtId">
                  <option ng-repeat="s in districts" ng-selected="s.id === request.districtId" ng-value="s.id">
                    {{s.name +' ('+s.incasator.name+' '+s.incasator.lastname+')'}}
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
        <%--<div class="col-md-2 col-xs-offset-6">--%>
        <%--<a ng-click="downloadExcell()" title="ექსელში ექსპორტი" class="btn btn-default pull-right">--%>
        <%--<i class="fa fa-file-excel-o"></i>--%>
        <%--</a>--%>
        <%--</div>--%>
        <div class="col-md-2 col-xs-offset-5">
          <div class="btn-group">
            <a ng-click="downloadExcell()" title="ექსელში ექსპორტი" class="btn btn-default pull-right">
              <i class="fa fa-file-excel-o"></i>
            </a>
            <a ng-click="printTickets()" title="ქვითრის ბეჭდვა"
               class="btn btn-default pull-right">
              <i class="fa fa-print"></i>
            </a>
            <a ng-click="printHistory()" title="დავალიანებების ისტორია"
               class="btn btn-default pull-right">
              <i class="fa fa-clock-o"></i>
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
          <div class="col-xs-offset-3">
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
                  <label class="col-sm-12" title="დავალიანების მქონე აბონენტები">
                    <input type="checkbox" class="srch" ng-true-value="-1" ng-change="loadMainData()"
                           ng-false-value="0" ng-init="0" ng-model="srchCase.hasBill">&nbsp; დავ.
                  </label>
                </div>
                <div class="form-group col-md-2">
                  <input type="text" class="form-control srch" ng-model="srchCase.id"
                         placeholder="აბ. N">
                </div>
                <div class="form-group col-md-3">
                  <input type="text" class="form-control srch" ng-model="srchCase.personalNumber"
                         placeholder="პირ. N">
                </div>
                <div class="form-group col-md-2">
                  <input type="text" class="form-control srch" ng-model="srchCase.name"
                         placeholder="სახელი">
                </div>
                <div class="form-group col-md-3">
                  <input type="text" class="form-control srch" ng-model="srchCase.lastname"
                         placeholder="გვარი">
                </div>
                <div class="form-group col-md-2">
                  <input type="text" class="form-control srch" ng-model="srchCase.streetNumber"
                         placeholder="ქუჩის N">
                </div>
                <div class="form-group col-md-2">
                  <input type="text" class="form-control srch" ng-model="srchCase.roomNumber"
                         placeholder="ბინის N">
                </div>
                <div class="form-group col-md-2">
                  <select class="form-control" ng-model="srchCase.statusId"
                          ng-change="loadMainData()">
                    <option value="" selected="selected">სტატუსი</option>
                    <option ng-selected="1 === srchCase.statusId" value="{{1}}">აქტიური</option>
                    <option ng-selected="2 === srchCase.statusId" value="{{2}}">გათიშული</option>
                  </select>
                </div>
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
                <div class="form-group col-md-3 col-xs-offset-5">
                  <button class="btn btn-default col-md-11" ng-click="loadMainData()" id="srchBtnId">
                    <span class="fa fa-search"></span> &nbsp; ძებნა
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
              <th>აბონ. N</th>
              <th>პაკეტი</th>
              <th>სახელი</th>
              <th>გვარი</th>
              <th>ქუჩა</th>
              <th>ინკასატორი</th>
              <th>ბალანსი</th>
              <th>სტატუსი</th>
              <th class="col-md-2 text-center">Action</th>
            </tr>
            </thead>
            <tbody title="Double Click For Detailed Information">
            <tr ng-repeat="r in list" ng-dblclick="handleDoubleClick(r.id)">
              <td>{{r.id}}</td>
              <td>{{r.packageType.name}}</td>
              <td>{{r.name}}</td>
              <td>{{r.lastname}}</td>
              <td>{{r.street.name}}</td>
              <td>{{r.district.incasator.name + ' ' + r.district.incasator.lastname}}</td>
              <td style="background-color: {{(r.balance < 0 ? '#c4e3f3':(r.balance > 0 ? '#ebcccc':''))}}">
                {{r.balance*-1}}
              </td>
              <td>{{r.status.name}}</td>
              <td class="text-center">
                <a ng-click="showDetails(r.id)" data-toggle="modal" title="დეტალები"
                   data-target="#detailModal" class="btn btn-xs">
                  <i class="fa fa-sticky-note-o"></i>
                </a>&nbsp;&nbsp;
                <%--<c:if test="<%= isAdmin %>">--%>
                <a ng-click="edit(r.id)" data-toggle="modal" title="რედაქტირება" data-target="#editModal"
                   class="btn btn-xs">
                  <i class="fa fa-pencil"></i>
                </a>&nbsp;&nbsp;
                <a ng-click="loadAbonentPackages(r.id)" title="პაკეტები" data-toggle="modal"
                   data-target="#packages" class="btn btn-xs">
                  <i class="fa fa-tasks"></i>
                </a>&nbsp;&nbsp;
                <a ng-click="showDetails(r.id)" data-toggle="modal"
                   data-target="#statusDialog" title="სტატუსის შეცვლა" class="btn btn-xs">
                  <i class="fa fa-cogs"></i>
                </a>&nbsp;&nbsp;
                <a ng-click="loadAbonentPackages(r.id)" data-toggle="modal"
                   data-target="#changePackageDialog" title="პაკეტის ცვლილება" class="btn btn-xs">
                  <i class="fa fa-wrench"></i>
                </a>&nbsp;&nbsp;
                <a ng-click="remove(r.id)" title="წაშლა" class="btn btn-xs">
                  <i class="fa fa-trash-o"></i>
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