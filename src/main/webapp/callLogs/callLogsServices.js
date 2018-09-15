angular.module('telephonyApp.callLogsServices',[]).factory('CallLog',function($resource){
    return $resource('http://localhost:8080/api/telephony/calllogs/:id',{id:'@id'},{
        update: {
            method: 'PUT'
        },
        delete: {
            method: 'DELETE',
            params: {
              id: "@id"
            }
        }
    });
});