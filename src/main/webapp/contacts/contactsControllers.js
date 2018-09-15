angular.module('telephonyApp.contactsControllers', [])
.controller('ContactListController', function ($scope, $state, $window, Contact) {

    $scope.contacts = Contact.query();

}).controller('ContactViewController', function ($scope, $state, $stateParams, Contact) {
    console.log("viewContact");
    $scope.contact = Contact.get({ id: $stateParams.id });

    $scope.deleteContact = function () {
        console.log("deleteContact");
        $scope.contact.$delete(function () {
            $state.go('contacts');
        });
    };

}).controller('ContactCreateController', function ($scope, $state, $stateParams, Contact) {

    console.log("createContact");
    $scope.contact = new Contact();

    $scope.addContact = function () {
        $scope.contact.$save(function () {
            $state.go('contacts');
        });
    }

}).controller('ContactEditController', function ($scope, $state, $stateParams, Contact) {

    $scope.updateContact = function () {
        console.log("updateContact");
        $scope.contact.$update(function () {
            $state.go('contacts');
        });
    };

    $scope.loadContact = function () {
        $scope.contact = Contact.get({ id: $stateParams.id });
    };

    $scope.loadContact();

});