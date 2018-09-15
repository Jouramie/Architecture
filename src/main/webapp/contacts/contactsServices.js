angular.module('telephonyApp.contactsServices',[]).factory('Contact',function($resource){
    return $resource('http://localhost:8080/api/telephony/contacts/:id',{id:'@id'},{
        update: {
            method: 'PUT'
        },
        save:{
            method: 'POST'
        },
        delete: {
            method: 'DELETE'
        }
    });
});