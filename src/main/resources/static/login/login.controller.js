(function () {
    'use strict';

    angular
        .module('app')
        .controller('LoginController', LoginController);

    LoginController.$inject = ['$location', '$rootScope', 'AuthenticationService', 'FlashService'];

    function LoginController($location, $rootScope, AuthenticationService, FlashService) {
        var vm = this;

        vm.login = login;

        function login() {
            const promise = AuthenticationService.login(vm.username, vm.password);
            promise.then(response => handle(response))
                .catch(error => FlashService.Error(error.data.error_description));
        }

        function handle(response) {
            // store username and token in local storage to keep user logged in between page refreshes
            localStorage.setItem('currentUser', JSON.stringify({
                token: response.data.token,
                username: response.data.username
            }));
            document.location.href = "/";
        }

    }

})();
