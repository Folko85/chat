(function () {
    'use strict';

    angular
        .module('app')
        .factory('AuthenticationService', AuthenticationService);

    AuthenticationService.$inject = ['$http'];

    function AuthenticationService($http) {
        var service = {};

        service.Login = Login;

        return service;

        function Login(username, password) {
            return $http.post('/api/users/login', {username: username, password: password});
        }


        function Logout() {
            // remove user from local storage and clear http auth header
            delete $localStorage.currentUser;
            $http.defaults.headers.common.Authorization = '';
        }

    }

})();