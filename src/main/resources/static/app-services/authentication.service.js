(function () {
    'use strict';

    angular
        .module('app')
        .factory('AuthenticationService', AuthenticationService);

    AuthenticationService.$inject = ['$http'];

    function AuthenticationService($http) {
        var service = {};

        service.login = login;
        service.logout = logout;

        return service;

        function login(username, password) {
            return $http.post('/api/users/login', {username: username, password: password});
        }


        function logout() {
            return $http.get('/api/users/logout');
        }

    }

})();