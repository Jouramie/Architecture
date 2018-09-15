angular.module('telephonyApp.callLogsControllers', [])
.controller('CallLogsController', function ($scope, $state, $stateParams, CallLog) {

    $scope.callLogs = CallLog.query();

    $scope.deleteCallLog = function (id) {
        CallLog.delete(id).$promise.then(
            function( value ){
              $scope.callLogs = CallLog.query();
            },

            function( error ){
              alert(error);
            }
        )
    };
});