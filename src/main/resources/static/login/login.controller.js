(function () {
    'use strict';

    angular
        .module('app')
        .controller('LoginController', LoginController);

    LoginController.$inject = ['$location', '$rootScope', 'AuthenticationService', 'FlashService'];

    function LoginController($location, $rootScope, AuthenticationService, FlashService) {
        var vm = this;

        vm.login = login;

        function initController() {
            AuthenticationService.Logout();
        }

        function login() {
            const promise = AuthenticationService.Login(vm.username, vm.password)
            promise.then(handle).catch(console.error)
        }

        function handle(response) {
            if (response.data.token) {
                // store username and token in local storage to keep user logged in between page refreshes
                localStorage.setItem('currentUser', JSON.stringify({
                    token: response.data.token,
                    username: response.data.username
                }));
                document.location.href = "/";
            } else {
                FlashService.Error(response.message);
            }
        }

    }

})();
